package group24.planning;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.ai.interfaces.IPlanning;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;
import com.unimelb.swen30006.partc.core.MapReader;
import com.unimelb.swen30006.partc.core.infrastructure.TrafficLight;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Intersection.Direction;
import com.unimelb.swen30006.partc.roads.Road;

public class Group24Planner implements IPlanning {

	private static final String FILE = "C:\\Users\\Kevin\\Documents\\Professional\\Academic\\UoM\\SWD\\Part C - Driving It Home\\SWEN30006 Part C - Driving It Home\\desktop\\bin\\test_course.xml";
	private static final float MAX_DIST = 50f;
	public static final float BRAKE_DIST = 25;
	private Road[] roads;
	private Intersection[] intersections;
	private Car car;
	ArrayList<Road> routeList;
	PriorityComparator comparator;
	private boolean inIntersection;
	private ArrayList<Point2D.Double> intersectionWayPoints;
	private Point2D.Double target;
	private TrafficLightHandler tLHandler;
	private SignHandler sHandler;

	public Group24Planner(Car car) {
		// We create another map reader here rather than creating a new
		// reading object as MapReader has the methods we require
		this.car = car;
		MapReader reader = new MapReader(FILE);
		routeList = new ArrayList<Road>();
		this.roads = reader.processRoads();
		this.intersections = reader.processIntersections();
		comparator = new PriorityComparator();
		inIntersection = false;
		intersectionWayPoints = new ArrayList<Point2D.Double>();
		target = new Point2D.Double();
		tLHandler = new TrafficLightHandler(car);
		sHandler = new SignHandler(car);
		//planRoute(new Point2D.Double(180, 140));
	}

	@Override
	public boolean planRoute(Point2D.Double destination) {
		// Initialize variables
		Road startRoad;
		boolean end = false;

		// Get the first road starting from the destination end
		if ((startRoad = closestRoad(destination)) == null) {
			return false;
		}

		// Add to route list
		routeList.add(startRoad);
		// Get the two intersection ends of this road
		Intersection[] ends = getTwoEnds(startRoad);
		// Calculate the length of each intersection to the car
		double lengthA = ends[0].pos.distance(car.getPosition());
		double lengthB = ends[1].pos.distance(car.getPosition());
		Intersection currentIntersection;
		// If intersection A is shorter
		if (lengthA <= lengthB) {
			// The first intersection is A
			currentIntersection = ends[0];
			// Else if intersection B is shorter
		} else {
			// The first intersection is B
			currentIntersection = ends[1];
		}

		// Loop till the end
		while (!end) {
			// Have to find the next intersection to traverse to
			Intersection nextIntersection = null;
			// Initialize best distance to infinite
			double bestDistance = Float.MAX_VALUE;
			// For all roads of this current intersection
			for (Road aRoad : currentIntersection.roads.values()) {
				// Find the potential next intersections
				Intersection potentialIntersection = getNextIntersection(
						currentIntersection, aRoad);
				// Calculate distance for each
				double distance = potentialIntersection.pos
						.distance(car.getPosition());
				// If the distance of this potential distance is shorter than
				// the best distance
				if (distance < bestDistance) {
					// Best distance is the new distance
					bestDistance = distance;
					// Best intersection so far is this potential intersection
					nextIntersection = potentialIntersection;
				}
			}
			// Since we got the next intersection to traverse to
			// Get the connecting road between them
			Road nextRoad = getConnectingRoad(currentIntersection,
					nextIntersection);
			// If the next road is where the car is currently at
			if (nextRoad.equals(closestRoad(car.getPosition()))) {
				float targetX = -1;
				float targetY = -1;
				// Need to set the first target X and target Y based on
				// direction
				if (nextIntersection.roads.get(Direction.North).equals(nextRoad)) {
					targetX = (float) nextRoad.getStartPos().getX() + (nextRoad.getWidth() / 4);
					targetY = (float) nextRoad.getStartPos().getY();
				} else if (nextIntersection.roads.get(Direction.South).equals(nextRoad)) {
					targetX = (float) nextRoad.getEndPos().getX() - (nextRoad.getWidth() / 4);
					targetY = (float) nextRoad.getEndPos().getY();
				} else if (nextIntersection.roads.get(Direction.West).equals(nextRoad)) {
					targetX = (float) nextRoad.getEndPos().getX();
					targetY = (float) nextRoad.getEndPos().getY() + (nextRoad.getWidth() / 4);
				} else if (nextIntersection.roads.get(Direction.East).equals(nextRoad)) {
					targetX = (float) nextRoad.getStartPos().getX();
					targetY = (float) nextRoad.getStartPos().getY()- (nextRoad.getWidth() / 4);
				}
				target.setLocation(targetX, targetY);
				// We have found the car, no need to plan further
				end = true;
				// Else if it just a regular path
			}
			// Add it to the route list.
			routeList.add(nextRoad);
			// So now we traverse the intersection and resume finding
			currentIntersection = nextIntersection;
		}
		// Now we have to reverse the route list we generated
		Collections.reverse(routeList);
		// Return true
		return true;
	}

	// North and East - Starting position for road
	// South and West - Ending position for road
	// Differences in update implementation to final design:
	// - Prune any perceptResponse object that is behind the car
	// - Check to see if car is at destination
	// - If car is in intersection, follow waypoints
	// - if car is not in intersection, follow targetX and targetY
	// - Do not have a lane handler, just goes to a calculated point that keeps
	// 	 it in the lane
	// - 
	@Override
	public void update(PerceptionResponse[] results, float delta) {
		//First, do not consider the perception response of objects behind the car
		ArrayList<PerceptionResponse> front = pruneBehind(results);
		//Then sort objects by distance, secondary sort by ttc
		front.sort(comparator);
		Intersection currentInter;
		
		
		/*if(theENd){
			
		}*/
		
		float futureX = (float) (car.getPosition().getX() + car.getVelocity().x);
		float futureY = (float) (car.getPosition().getY() + car.getVelocity().y);
		Point2D.Double futurePos = new Point2D.Double(futureX, futureY);
		
		if((currentInter = intersectionAtPoint(futurePos)) != null){
			if(!inIntersection){
				routeList.remove(0);
				setNextTarget(intersectionAtPoint(futurePos), routeList.get(0));
				if(getRoadDirection(currentInter, roadAtPoint(car.getPosition())).equals(Intersection.Direction.North)){
					if(getRoadDirection(currentInter, routeList.get(0)).equals(Intersection.Direction.East)){
						intersectionWayPoints = Calculations.generateWaypoints(futurePos, target, 8.0f, 3f, true, true);
					}
					else if(getRoadDirection(currentInter, routeList.get(0)).equals(Intersection.Direction.West)){
						intersectionWayPoints = Calculations.generateWaypoints(futurePos, target, 30.0f, 3f, true, false);
					}
				}
				else if (getRoadDirection(currentInter, roadAtPoint(car.getPosition())).equals(Intersection.Direction.East)){
					if(getRoadDirection(currentInter, routeList.get(0)).equals(Intersection.Direction.North)){
						intersectionWayPoints = Calculations.generateWaypoints(futurePos, target, 30.0f, 3f, true, false);
					}
					else if(getRoadDirection(currentInter, routeList.get(0)).equals(Intersection.Direction.South)){
						intersectionWayPoints = Calculations.generateWaypoints(futurePos, target, 8.0f, 3f, true, true);
					}
				}
				else if (getRoadDirection(currentInter, roadAtPoint(car.getPosition())).equals(Intersection.Direction.South)){
					if(getRoadDirection(currentInter, routeList.get(0)).equals(Intersection.Direction.East)){
						intersectionWayPoints = Calculations.generateWaypoints(futurePos, target, 30.0f, 3f, true, false);
					}
					else if(getRoadDirection(currentInter, routeList.get(0)).equals(Intersection.Direction.West)){
						intersectionWayPoints = Calculations.generateWaypoints(futurePos, target, 8.0f, 3f, true, true);
					}
				}
				else if (getRoadDirection(currentInter, roadAtPoint(car.getPosition())).equals(Intersection.Direction.West)){
					if(getRoadDirection(currentInter, routeList.get(0)).equals(Intersection.Direction.North)){
						intersectionWayPoints = Calculations.generateWaypoints(futurePos, target, 8.0f, 3f, true, true);
					}
					else if(getRoadDirection(currentInter, routeList.get(0)).equals(Intersection.Direction.South)){
						intersectionWayPoints = Calculations.generateWaypoints(futurePos, target, 30.0f, 3f, true, false);
					}
				}
				else {
					intersectionWayPoints.add(target);
				}
				inIntersection = true;
			}
			else {
				if(!intersectionWayPoints.isEmpty()){
					car.turn(Calculations.getTurningAngle(car, intersectionWayPoints.get(0)));
					if(futurePos.distance(intersectionWayPoints.get(0)) < 1){
						intersectionWayPoints.remove(0);
					}
				} else {
					setOtherEnd(currentInter, routeList.get(0));
					inIntersection = false;
				}
			}
		}
		
		else {
			car.turn(Calculations.getTurningAngle(car, target));
			
			PerceptionResponse trafficLight;
			PerceptionResponse sign;
			
			trafficLight = getFirstTrafficLight(front);
			sign = getFirstSign(front);
			tLHandler.handle(trafficLight);
			sHandler.handle(sign);

		}	
		//Sort by time to collide
		//If time to collide is below a certain threshold, that is highest priority item and tell car to brake
		//Check for time to collide 
		//Else we check distance to traffic lights and lane marking
		//if we have a road marking to the left of the car and a lane marking on the right, we are in the
		//correct lane. We steer if we're not correctly in the lane
		//If traffic light, we check the state, if red we brake, if green we accel, if yellow we check dist to decide
		//
		//At the same time we should be checking our route
		//If car's position ahead is an intersection, we start turning on the road that is in our route list

	}
	
	
	@Override
	public float eta() {
		// Get the total distance of the planned path
		float distance = Calculations.calculateDistance(routeList);
		// Get the current velocity of the car
		float velocity = car.getVelocity().len();
		// Calculate and return the ETA
		return Calculations.calculateETA(distance, velocity);
	}

	/**
	 * Finds the closest road to a point and returns this road. Does not
	 * consider actual travel distance to a road, purely the direct distance
	 * 
	 * @param pos
	 *            the position to check from
	 * @return the closest road to that position, of null if none are within
	 *         MAX_ROAD_DISTANCE
	 */
	public Road closestRoad(Point2D.Double pos) {
		float minDist = Float.MAX_VALUE;
		Road minRoad = null;
		for (Road r : this.roads) {
			float tmpDist = r.minDistanceTo(pos);
			if (tmpDist < minDist) {
				minDist = tmpDist;
				minRoad = r;
			}
		}
		return (minDist < MAX_DIST) ? minRoad : null;
	}

	/**
	 * Find the intersection that exists at a point, for cases where we are not
	 * on a road but need to navigate through the road structure
	 * 
	 * @param pos
	 *            the point we are searching from
	 * @return the intersection present at pos, or null if none is present
	 */
	public Intersection intersectionAtPoint(Point2D.Double pos) {
		for (Intersection i : this.intersections) {
			if (i.containsPoint(pos)) {
				return i;
			}
		}
		return null;
	}

	/**
	 * Find whether or not there is a road at a given point
	 * 
	 * @param pos
	 *            the point we want to check for
	 * @return the road that exists at that point, or null if none exists
	 */
	public Road roadAtPoint(Point2D.Double pos) {
		for (Road r : this.roads) {
			if (r.containsPoint(pos)) {
				return r;
			}
		}
		return null;
	}


	private ArrayList<PerceptionResponse> pruneBehind(
			PerceptionResponse[] prList) {
		// First make an empty list
		ArrayList<PerceptionResponse> front = new ArrayList<PerceptionResponse>();

		// For all perception response objects on the list
		for (PerceptionResponse aPR : prList) {
			// If the object's angle relative to the car is less than or equals
			// to 90, it is infront of us
			if (Math.abs(car.getVelocity().angle(aPR.direction)) <= 90) {
				// Add it into the new list
				front.add(aPR);
			}
		}
		// Return the pruned list
		return front;
	}

	private PerceptionResponse getFirstTrafficLight(
			ArrayList<PerceptionResponse> pr) {
		for (PerceptionResponse aPR : pr) {
			if (aPR.objectType == Classification.TrafficLight) {
				return aPR;
			}
		}
		return null;
	}
	
	private PerceptionResponse getFirstSign(ArrayList<PerceptionResponse> pr){
		for (PerceptionResponse aPR : pr) {
			if (aPR.objectType == Classification.Sign) {
				return aPR;
			}
		}
		return null;
	}

	private Intersection[] getTwoEnds(Road road) {
		int count = 0;
		Intersection[] ends = new Intersection[2];
		for (Intersection aIntersection : intersections) {
			if (aIntersection.roads.containsValue(road)) {
				ends[count] = aIntersection;
				count += 1;
			}
		}
		return ends;
	}

	private Road getConnectingRoad(Intersection a, Intersection b) {
		for (Road aRoad : a.roads.values()) {
			if (b.roads.containsValue(aRoad)) {
				return aRoad;
			}
		}
		return null;
	}

	private Intersection getNextIntersection(Intersection currentIntersection,
			Road path) {
		for (Intersection aIntersection : intersections) {
			if (aIntersection.roads.containsValue(path)
					&& !aIntersection.equals(currentIntersection)) {
				return aIntersection;
			}
		}
		for (Intersection aIntersection : intersections) {
			if (aIntersection.roads.containsValue(path)) {
				return aIntersection;
			}
		}
		return null;
	}

	private void setNextTarget(Intersection intersection, Road road) {
		float targetX = -1;
		float targetY = -1;
		if (intersection.roads.get(Direction.North).equals(road)) {
			// Moving North
			targetY = (float) road.getStartPos().getY();
			targetX = (float) road.getStartPos().getX() - (road.getWidth() / 4);

		} else if (intersection.roads.get(Direction.South).equals(road)) {
			// Moving South
			targetY = (float) road.getEndPos().getY();
			targetX = (float) road.getEndPos().getX() + (road.getWidth() / 4);

		} else if (intersection.roads.get(Direction.West).equals(road)) {
			// Moving West
			targetX = (float) road.getEndPos().getX();
			targetY = (float) road.getEndPos().getY() - (road.getWidth() / 4);

		} else if (intersection.roads.get(Direction.East).equals(road)) {
			// Moving East
			targetX = (float) road.getStartPos().getX();
			targetY = (float) road.getStartPos().getY() + (road.getWidth() / 4);
		}
		target.setLocation(targetX, targetY);
	}
	
	private void setOtherEnd(Intersection intersection, Road road) {
		float targetX = -1;
		float targetY = -1;
		if (intersection.roads.get(Direction.North).equals(road)) {
			// Moving North
			targetY = (float) road.getEndPos().getY();
			targetX = (float) road.getEndPos().getX() - (road.getWidth() / 4);

		} else if (intersection.roads.get(Direction.South).equals(road)) {
			// Moving South
			targetY = (float) road.getStartPos().getY();
			targetX = (float) road.getStartPos().getX() + (road.getWidth() / 4);

		} else if (intersection.roads.get(Direction.West).equals(road)) {
			// Moving West
			targetX = (float) road.getStartPos().getX();
			targetY = (float) road.getStartPos().getY() - (road.getWidth() / 4);

		} else if (intersection.roads.get(Direction.East).equals(road)) {
			// Moving East
			targetX = (float) road.getEndPos().getX();
			targetY = (float) road.getEndPos().getY() + (road.getWidth() / 4);
		}
		target.setLocation(targetX, targetY);
	}
		
	private Intersection.Direction getRoadDirection(Intersection inter, Road road) {
		for (Intersection.Direction direction : inter.roads.keySet()) {
			if (inter.roads.get(direction).equals(road)) {
				return direction;
			}
		};
		
		return null;
		
	}
	
	/* Returns the road point at the other end of the point being passed in */
	private Point2D.Double otherRoadPoint(Point2D.Double position){
		
		Road currentRoad = roadAtPoint(position);
		
		// Road Start and End positions
		Point2D.Double roadStartPoint = currentRoad.getStartPos();
		Point2D.Double roadEndPoint = currentRoad.getEndPos();
		
		// Distance from start and end positions
		float distanceStart = (float) roadStartPoint.distance(position);
		float distanceEnd = (float)roadEndPoint.distance(position);
		
		
		if(distanceStart > distanceEnd){
			return roadStartPoint;
		}
		
		return roadEndPoint;
	}
	
	
}

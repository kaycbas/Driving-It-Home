package group32.planning;
import com.unimelb.swen30006.partc.roads.*;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.objects.*;


import java.awt.geom.Point2D;
import java.util.*;

/**
 * The router class is responsible for using the roadgraph class to get the shortest path from the current position of
 * the car to a destination point. It also is responsible for providing the eta to the destination.
 * @author jordanlopresti
 *
 */

public class Router {
	
	//the index in the route ArrayList of the road we are on 
	private int currentRoadIndex;
	
	//The classes required to build the graph for routing
	private RoadGraph rg;
	private Graph g;
	
	//List of roads on the route
	private ArrayList<Road> route;
	
	//List of intersections in the world
	private ArrayList<Intersection> intersections;
	
	//The destination point
	private Point2D.Double destination;
	
	//Attributed required for eta
	private float avgSpeed;
	private Vector2 prevVel;
	
	//The car we are routing
	public final Car car;
	
	private float eta;

	/**
	 * Constructor method for creating a new route
	 * @param c1 The car we want to route
	 */	
	public Router(Car c1) {
		this.car = c1;
		this.rg = new RoadGraph("C:\\Users\\Kevin\\Documents\\Professional\\Academic\\UoM\\SWD\\Part C - Driving It Home\\SWEN30006 Part C - Driving It Home\\desktop\\bin\\test_course.xml");
		rg.readCourse();
		rg.buildIntersections();
		this.intersections = rg.getIntersections();
		this.g = new Graph(rg.createGraph());
		this.prevVel = c1.getVelocity();
		this.avgSpeed = 0; // Initially at rest
		this.currentRoadIndex = 0;
	}
	
	//Get method for the route
	public ArrayList<Road> getRoute() {
		return this.route;
	}
	
	//Get method for the destination
	public Point2D.Double getDestination() {
		return this.destination;
	}
	
	//Setter method for the destination
	public void setDestination(Point2D.Double d) {
		this.destination = d;
	}
	
	//Getter method for the next road on the route
	public Road getNextRoad() {
		return this.route.get(this.currentRoadIndex + 1);
	}
	
	/**
	 * Calculates the remaining distance based on the roads on the route and the current
	 * position of the car
	 * @param pos The position of the car
	 * @param dest The destination position
	 * @return
	 */
  	public double calculateRemainingDist(Point2D.Double pos, Point2D.Double dest) {
		float remainDist = 0;
		
		boolean start = false; // Flag to start calculating distance

		// For every Road r
		for (Road r : this.route) {
			// If our position and destination is on the same road calculate the remaining distance
			if (r.containsPoint(pos) && r.containsPoint(dest)) {
		    	double xdiff = dest.x - pos.x;
		    	double ydiff = dest.y - pos.y;
				remainDist += Math.sqrt(xdiff*xdiff + ydiff*ydiff);
			} else if (r.containsPoint(pos) && !r.containsPoint(dest)) {
				// If our position is on the this road, start calculating distance
				start = true;  // Start flag becomes active
				// add the remaining distance to the next road
				remainDist += this.route.get(this.route.indexOf(r) + 1).minDistanceTo(pos);
				continue;
			}
			
			// If start flag has started add the lengths of the remaining road.
			if (start) {
				remainDist += r.getLength();
			}	
		}
		

		System.out.println("Current pos: " + pos + " remDist: " + remainDist);
		return remainDist;
	}
	
	
	// Returns magnitude of velocity
	public float getMagVelocity(Vector2 vel) {
		float velx = vel.x;
		float vely = vel.y;
		return (float) Math.sqrt(velx*velx + vely*vely);
	}
	
	// Returns the avgSpeed based on the current velocity of car and the previous velocity
	public float calcAvgSpeed(float delta) {
		float changeInVel = this.getMagVelocity(this.car.getVelocity()) - this.getMagVelocity(prevVel);
		float avgSpeed = changeInVel/delta;
		this.prevVel = this.car.getVelocity(); // Previous velocity is updated;
		return avgSpeed;
	}
	
	public void calculateEta(float delta) {

		double dist = this.calculateRemainingDist(this.car.getPosition(), this.getDestination());
		this.avgSpeed = calcAvgSpeed(delta);
		float eta = (float) (dist/avgSpeed);
		this.setEta(eta);
	}
	
	public boolean calculateRoute() {
		g.dijkstra(rg.roadAtPoint(car.getPosition()));
		this.route = g.getPath(rg.closestRoad(this.destination));
		if(this.route == null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public Intersection intersectionAtPoint(Point2D.Double pos){
		for(Intersection i: this.intersections){
			if(i.containsPoint(pos)){
				System.out.println("Cunt");
				return i;
			}
		}
		return null;
	}
	
	public void printRoute() {
		
		for(Road r : this.route) {
			System.out.println(r);
		}
	}

	// Added getters and setters to access eta and set eta
	public float getEta() {
		return eta;
	}

	public void setEta(float eta) {
		this.eta = eta;
	}
}

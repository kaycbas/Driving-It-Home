/**
 * Group 70
 * Kaven Peng 696573
 * Liep Lim 686458
 * William Song 641305
 * */
package group70.sensing;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import group70.sensing.ObjectToArray.Tuple;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;
import com.unimelb.swen30006.partc.roads.RoadMarking;

/**
 * a new processor class different to the original design. we needed a class
 * that handles all of the objects and pass them into generators
 */
public class Processor {
	private HashMap<WorldObject, Point2D.Double[]> objects = new HashMap<WorldObject, Point2D.Double[]>();
	private HashMap<Road, Point2D.Double[]> roads = new HashMap<Road, Point2D.Double[]>();
	private HashMap<Intersection, Point2D.Double[]> intersections = new HashMap<Intersection, Point2D.Double[]>();
	private HashMap<RoadMarking, Point2D.Double[]> markings = new HashMap<RoadMarking, Point2D.Double[]>();
	private HashMap<Car, Point2D.Double[]> cars = new HashMap<Car, Point2D.Double[]>();
	private HashMap<Intersection, Point2D.Double[]> intersectionEdges = new HashMap<Intersection, Point2D.Double[]>();
	private Color worldColor;
	private float delta;
	private Point2D.Double pos;
	private int vis;
	private Vector2 carVelocity;
	private ObjectToArray calc;

	public Processor(int vis) {
		this.vis = vis;
	}

	public void update(Point2D.Double pos, float delta, Color worldColor, Car car, WorldObject[] scannedObjects,
			Road[] scannedRoads, Intersection[] scannedIntersections) {
		calc = new ObjectToArray(pos, vis);
		processObjects(scannedObjects, calc);
		processRoads(scannedRoads, calc);
		processIntersections(scannedIntersections, calc);
		processCar(car, calc);
		this.worldColor = worldColor;
		this.carVelocity = car.getVelocity();
	}

	public void processCar(Car car, ObjectToArray calc) {
		cars.put(car, calc.getCarCoords(car));
	}

	public void processObjects(WorldObject[] scannedObjects, ObjectToArray calc) {
		for (WorldObject object : scannedObjects) {
			objects.put(object, calc.getObjectCoords(object));
		}
	}

	// ive made some changes
	public void processRoads(Road[] scannedRoads, ObjectToArray calc) {
		for (Road road : scannedRoads) {
			Tuple roadTuple = calc.getRoadCoords(road);
			roads.put(road, roadTuple.getObjectCoords());
			// will get boolean from Tuple returned from
			// calc.getRoadCoords(road)
			boolean horizontal = roadTuple.isHorizontal();
			for (RoadMarking marking : road.getMarkers()) {
				markings.put(marking, calc.getMarkingCoords(marking, horizontal));
			}
		}
	}

	public void processIntersections(Intersection[] scannedIntersections, ObjectToArray calc) {
		for (Intersection intersection : scannedIntersections) {
			intersections.put(intersection, calc.getIntersectionCoords(intersection));
			intersectionEdges.put(intersection, calc.getIntersectionEdges(intersection));
		}
	}

	public HashMap<WorldObject, Point2D.Double[]> getObjects() {
		return objects;
	}

	public HashMap<Road, Point2D.Double[]> getRoads() {
		return roads;
	}

	public HashMap<Intersection, Point2D.Double[]> getIntersections() {
		return intersections;
	}

	public HashMap<RoadMarking, Point2D.Double[]> getMarkings() {
		return markings;
	}

	public Color getWorldColor() {
		return worldColor;
	}

	public float getDelta() {
		return delta;
	}

	public Point2D.Double getPos() {
		return pos;
	}

	public Vector2 getCarVelocity() {
		return carVelocity;
	}

	public HashMap<Intersection, Double[]> getIntersectionEdges() {
		return intersectionEdges;
	}

	public HashMap<Car, Double[]> getCars() {
		return cars;
	}

}

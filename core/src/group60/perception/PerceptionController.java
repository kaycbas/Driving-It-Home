/* PerceptionController.java
 * 
 * SWEN30006 Part C - Driving It Home
 * Semester 1, 2016
 * 
 * by Group 60
 * Adrian Cheung 636330
 * Shi Chang Zhang 695434
 * Kevin Bastoul 802847
 * 
 * Description: This is the controller class for our perception 
 * subsystem which implements IPerception.
 * 
 */

package group60.perception;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.ai.interfaces.IPerception;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.objects.Car;

import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;

public class PerceptionController implements IPerception {

	/*
	 * TODO Add some comments here.
	 */
	private float delta;
	private Car car;
	private MultiMap map;
	private Classifier classifier;
	private ArrayList<Blob> detectedBlobs;
	private BlobDetector detector;
	private KnownObjectHandler knownObjectsInfo;


	public PerceptionController(Car car, float delta) {
		this.car = car;
		this.delta = delta;
		this.knownObjectsInfo = new KnownObjectHandler();
		detector = new BlobDetector(knownObjectsInfo);
		this.classifier = new Classifier(knownObjectsInfo);
	}

	public PerceptionResponse[] analyseSurroundings(boolean[][] spaceMap,
			Color[][] colorMap, Vector2[][] velMap) {
		
		this.map = combineMaps(spaceMap,colorMap,velMap);
		this.detectedBlobs = detectBlobs(this.map);

		return createPerceptionResponses(classifyBlobs(detectedBlobs));
	}

	/*
	 * Add some comments
	 */
	public MultiMap combineMaps(boolean[][] spaceMap, Color[][] colourMap, Vector2[][] velMap) {
		// first, convert all values in velMap to absolute velocities
		int dim = velMap.length;
		Vector2[][] abVelMap = new Vector2[dim][dim];
		Vector2 currentVel = car.getVelocity();
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				abVelMap[row][col] = velMap[row][col].add(currentVel);
			}
		}
		Point2D.Double[][] posMap = generatePositionMap(dim, "center");
		// now, chuck everything into a multimap
		return new MultiMap(spaceMap, colourMap, abVelMap, posMap);
	}

	private Point2D.Double[][] generatePositionMap(int dim, String origin) {
		Point2D.Double currentPos = car.getPosition();
		Point2D.Double[][] posMap = new Point2D.Double[dim][dim];

		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				posMap[row][col] = new Point2D.Double(currentPos.getX() + (col - dim/2),
						currentPos.getY() + (row - dim/2));
			}
		}
		return posMap;
	}

	/*
	 * Add some comments
	 */

	public ArrayList<Blob> detectBlobs(MultiMap map){
		ArrayList<Blob> detectedBlobs = detector.generateBlobs(map);
		System.out.println("num detected blobs: " + detectedBlobs.size());
		return detectedBlobs;
	}

	/*
	 * Add some comments
	 */

	public ArrayList<ClassifiedObject> classifyBlobs(ArrayList<Blob> b){
		return this.classifier.classify(b);
	}

	/**
	 * For each ClassifiedObject in c, create a PerceptionResponse.
	 * 
	 * @param c Array of ClassifiedObjects.
	 * @return A PerceptionResponse Array.
	 */
	public PerceptionResponse[] createPerceptionResponses(ArrayList<ClassifiedObject> c) {
		int n = c.size();
		PerceptionResponse[] response = new PerceptionResponse[n];
		
		float dist;
		float time;
		Vector2 vel;
		Vector2 dir;
		PerceptionResponse.Classification objType;
		HashMap<String, Object> info;
		
		for (int i = 0; i < n; i++) {
			
			dir = calcDistance(c.get(i));
			dist = (float)Math.sqrt(Math.pow(dir.x,2)+Math.pow(dir.y,2));
			time = calcTimeToCollision(c.get(i));
			vel = c.get(i).getAbVel();
			objType = c.get(i).classification;
			info = c.get(i).information;
			response[i] = new PerceptionResponse(dist,vel,dir,time,objType,info);
		}

		return response;
	}


	private Vector2 calcDistance(ClassifiedObject detectedObject) {

		Point2D.Double objectPosition = detectedObject.getPosition();
		Point2D.Double carPosition = this.car.getPosition();

		double x, y;
		double x1 = objectPosition.getX();
		double y1 = objectPosition.getY();
		double x2 = carPosition.getX();
		double y2 = carPosition.getY();

		double objectLength = detectedObject.length;
		double objectWidth = detectedObject.width;
		double carLength = this.car.getLength();
		double carWidth = this.car.getWidth();

		if (x1 < x2) {
			if ((x1 + objectWidth / 2) < (x2 - carWidth / 2)) {
				x = (x1 + objectWidth / 2) - (x2 - carWidth / 2);
			} else {
				x = 0;
			}
		} else {
			if ((x1 - objectWidth / 2) > (x2 + carWidth / 2)) {
				x = (x1 - objectWidth / 2) - (x2 + carWidth / 2);
			} else {
				x = 0;
			}
		}

		if (y1 < y2) {
			if ((y1 + objectLength / 2) < (y2 - carLength / 2)) {
				y = (y1 + objectLength / 2) - (y2 - carLength / 2);
			} else {
				y = 0;
			}
		} else {
			if ((y1 - objectLength / 2) > (y2 + carLength / 2)) {
				y = (y1 - objectLength / 2) - (y2 + carLength / 2);
			} else {
				y = 0;
			}
		}

		return new Vector2((float)x, (float)y);
	}

	private float calcTimeToCollision(ClassifiedObject detectedObject) {
		// Disregard acceleration because it updates so many times.
		Vector2 displacement = calcDistance(detectedObject);
		Vector2 velocity = (this.car.getVelocity())
				.sub(detectedObject.getAbVel());

		if (velocity
				.isCollinear(
						displacement,
						Math.max((float)detectedObject.length,
								(float)detectedObject.width))) {
			if (velocity.x == 0 && velocity.y != 0) {
				return displacement.y / velocity.y;
			} else if (velocity.x != 0 && velocity.y == 0) {
				return displacement.x / velocity.x;
			} else {
				return Math.min(displacement.x / velocity.x, displacement.y
						/ velocity.y);
			}
		} else {
			return -1;
		}
	}
}

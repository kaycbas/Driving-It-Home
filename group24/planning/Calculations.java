package group24.planning;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.roads.Road;

/**
 * Collection of calculations used in the Planning interface.
 */
public final class Calculations {
	
	private final static float acceleration = 25.0f;
	
	/**
	 * Calculates the total distance of the roads on the route to 
	 * to
	 */
	public static float calculateDistance(ArrayList<Road> routeList) {
		float totalDistance = 0;
		for (Road aRoad : routeList) {
			totalDistance += aRoad.getLength();
		}
		return totalDistance;
	}

	public static float calculateETA(float distance, float velocity) {

		float time = quadraticEquation(acceleration/ 2, velocity, -distance);

		return time;
	}

	public static int getMin(double a, double b) {
		if (a < b) {
			return -1;
		} else if (a > b) {
			return 1;
		}
		return 0;
	}

	// Returns the angle that we should turn the car
	public static float getTurningAngle(Car car, Point2D.Double targetPosition) {

		// Get the current velocity
		Vector2 carVelocity = car.getVelocity();

		// Get the current position
		Point2D.Double currentPosition = car.getPosition();

		// Create a vector in our desired direction
		Vector2 desiredVelocity = createVector(currentPosition, targetPosition);

		// Get the angle of turningAngle relative to desiredVelocity
		float turningAngle = carVelocity.angle(desiredVelocity);

		return turningAngle;
	}

	private static float quadraticEquation(float a, float b, float c) {

		float discriminant = (float) (Math.pow(b, 2) - 4 * a * c);

		if (discriminant < 0) {
			// In our case, discriminant should always be positive
			// Stuff here
		}

		float time = (float) ((Math.sqrt(discriminant) - b) / (2 * a));

		return time;
	}

	/*
	 * Returns the normalized vector in the direction we want the car to move in
	 */
	private static Vector2 createVector(Point2D.Double currentPosition, Point2D.Double targetPosition) {

		Vector2 vector = new Vector2();

		// Difference in the components of the target and current position
		float diffPositionX = (float) (targetPosition.getX() - currentPosition.getX());
		float diffPositionY = (float) (targetPosition.getY() - currentPosition.getY());

		// Set the Vector to be in our desired direction and also normalized
		vector.set(diffPositionX, diffPositionY).nor();

		return vector;
	}

	/* Creates an ArrayList of points between two positions */
	public static ArrayList<Point2D.Double> generateWaypoints(Point2D.Double currentPosition,
			Point2D.Double finalPosition, float radius, float pointSteps, boolean shortest, boolean side) {
		ArrayList<Point2D.Double> pointArray = new ArrayList<Point2D.Double>();

		// Calculate the middle of the two given points.

		double sumOfX = currentPosition.x + finalPosition.x;
		double sumOfY = currentPosition.y + finalPosition.y;

		Point2D.Double middlePoint = new Point2D.Double(sumOfX, sumOfY);
		middlePoint.x /= 2.0f;
		middlePoint.y /= 2.0f;

		// Calculate the distance between the two points
		double differenceX = finalPosition.x - currentPosition.x;
		double differenceY = finalPosition.y - currentPosition.y;
		double distance = Math.sqrt(differenceX * differenceX + differenceY * differenceY);

		if (radius * 2.0f < distance) {
			System.out.println("Error: Distance is too small for radius!");
		}

		// Calculate the middle of the expected curve.
		double factor = Math
				.sqrt((radius * radius) / ((differenceX * differenceX) + (differenceY * differenceY)) - 0.25f);

		Point2D.Double circleMiddlePoint = new Point2D.Double(0, 0);

		if (side) {
			circleMiddlePoint.x = 0.5f * (sumOfX) + factor * (differenceY);
			circleMiddlePoint.y = 0.5f * (sumOfY) + factor * (currentPosition.x - finalPosition.x);
		} else {
			circleMiddlePoint.x = 0.5f * (sumOfX) - factor * (differenceY);
			circleMiddlePoint.y = 0.5f * (sumOfY) - factor * (currentPosition.x - finalPosition.x);
		}

		// Calculate the two reference angles
		float angle1 = (float) Math.atan2(currentPosition.y - circleMiddlePoint.y,
				currentPosition.x - circleMiddlePoint.x);
		float angle2 = (float) Math.atan2(finalPosition.y - circleMiddlePoint.y, finalPosition.x - circleMiddlePoint.x);

		// Calculate the step.
		float step = pointSteps / radius;

		// Swap them if needed
		if (angle1 > angle2) {
			float temp = angle1;
			angle1 = angle2;
			angle2 = temp;
		}

		boolean flipped = false;
		if (!shortest) {
			if (angle2 - angle1 < Math.PI) {
				float temp = angle1;
				angle1 = angle2;
				angle2 = temp;
				angle2 += Math.PI * 2.0f;
				flipped = true;
			}
		}
		for (float f = angle1; f < angle2; f += step) {
			Point2D.Double p = new Point2D.Double(Math.cos(f) * (double) radius + circleMiddlePoint.x,
					Math.sin(f) * (double) radius + circleMiddlePoint.y);
			pointArray.add(p);
		}
		if (flipped ^ side) {
			pointArray.add(currentPosition);
		} else {
			pointArray.add(finalPosition);
		}
		return pointArray;
	}
}
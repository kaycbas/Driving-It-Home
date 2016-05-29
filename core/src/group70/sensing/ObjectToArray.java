/**
 * Group 70
 * Kaven Peng 696573
 * Liep Lim 686458
 * William Song 641305
 * */
package group70.sensing;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;
import com.unimelb.swen30006.partc.roads.RoadMarking;

/**
 * This class translate the absolute coordinations of objects to
 * ones which reflecting their relative position in visibility 
 * region and can be used to access 2D array(Maps)
 */
public class ObjectToArray {
	private Point2D.Double center;
	private int visRange;
	private PositionTranslator translator;

	// Tuple class for Road and Road Markings
	public class Tuple {
		private boolean horizontal;
		private Point2D.Double[] objectCoords;

		public Tuple(boolean horizontal, Point2D.Double[] objectCoords) {
			this.horizontal = horizontal;
			this.objectCoords = objectCoords;
		}

		public boolean isHorizontal() {
			return horizontal;
		}

		public Point2D.Double[] getObjectCoords() {
			return objectCoords;
		}
	}

	public ObjectToArray(Point2D.Double center, int visRange) {
		this.center = center;
		this.translator = new PositionTranslator(center, visRange / 2);
		this.visRange = visRange / 2;

	}

	public Point2D.Double[] getObjectCoords(WorldObject object) {
		// System.out.printf("Object: %s", object.toString());
		// System.out.printf("Center coordinate: (%d,%d)\n",
		ArrayList<Point2D.Double> objectCoords = new ArrayList<Point2D.Double>();
		int left = (int) (object.getPosition().getX() - object.getLength() / 2);
		// System.out.printf("Left : %d \n", left);
		int right = (int) (object.getPosition().getX() + object.getLength() / 2);
		// System.out.printf("Right: %d\n", right);
		int top = (int) (object.getPosition().getY() + object.getLength() / 2);
		// System.out.printf("Top: %d\n", top);
		int bot = (int) (object.getPosition().getY() - object.getLength() / 2);
		// System.out.printf("Bot: %d\n", bot);
		for (int i = left; i < right; i++) {
			// clamping coordinates to be within 100x100 array bounds
			if (i >= center.x + visRange || i <= center.x - visRange) {
				// System.out.printf("Ignored.\n");
				continue;
			} else {
				for (int j = bot; j < top; j++) {
					if (j >= center.y + visRange || j <= center.y - visRange) {
						continue;
					}
					Point2D.Double coord = translator.translate(new Point2D.Double(i, j));
					objectCoords.add(coord);
				}
			}
		}

		return objectCoords.toArray(new Point2D.Double[objectCoords.size()]);
	}

	public Tuple getRoadCoords(Road road) {
		boolean horizontal = false;
		int left, right, top, bot;
		if (road.getStartPos().getY() == road.getEndPos().getY()) {
			horizontal = true;
		}
		ArrayList<Point2D.Double> objectCoords = new ArrayList<Point2D.Double>();
		if (!horizontal) {
			// vertical road
			left = (int) (road.getStartPos().getX() - road.getWidth() / 2);
			right = (int) (road.getStartPos().getX() + road.getWidth() / 2);
			bot = (int) (road.getStartPos().getY());
			top = (int) (road.getEndPos().getY());
		} else {
			left = (int) (road.getStartPos().getX());
			right = (int) (road.getEndPos().getX());
			bot = (int) (road.getStartPos().getY() - road.getWidth() / 2);
			top = (int) (road.getStartPos().getY() + road.getWidth() / 2);
		}
		for (int i = left; i < right; i++) {
			// clamping coordinates to be within 100x100 array bounds
			if (i >= center.x + visRange || i <= center.x - visRange) {
				// System.out.printf("Ignored.\n");
				continue;
			} else {
				for (int j = bot; j < top; j++) {
					if (j >= center.y + visRange || j <= center.y - visRange) {
						continue;
					}
					Point2D.Double coord = translator.translate(new Point2D.Double(i, j));
					objectCoords.add(coord);
				}
			}
		}
		// change this to return a Tuple(horizontal,objectCoords)
		return new Tuple(horizontal, objectCoords.toArray(new Point2D.Double[objectCoords.size()]));
	}

	// Use the boolean horizontal to distinguish marking direction
	public Point2D.Double[] getMarkingCoords(RoadMarking marking, boolean horizontal) {
		ArrayList<Point2D.Double> objectCoords = new ArrayList<Point2D.Double>();
		int xUnits = 0;
		int yUnits = 0;
		if (horizontal) {
			xUnits = 1;
		} else {
			yUnits = 1;
		}

		// one unit width
		int left = (int) (marking.position.x - xUnits);
		int right = (int) marking.position.y + xUnits;
		// 2 unit length
		int top = (int) (marking.position.y + yUnits);
		int bot = (int) (marking.position.y - yUnits);
		for (int i = left; i < right; i++) {
			// clamping coordinates to be within 100x100 array bounds
			if (i >= center.x + visRange || i <= center.x - visRange) {
				continue;
			} else {
				for (int j = bot; j < top; j++) {
					if (j >= center.y + visRange || j <= center.y - visRange) {
						continue;
					}
					Point2D.Double coord = translator.translate(new Point2D.Double(i, j));
					objectCoords.add(coord);
				}
			}
		}
		return objectCoords.toArray(new Point2D.Double[objectCoords.size()]);
	}

	public Point2D.Double[] getIntersectionCoords(Intersection intersection) {
		ArrayList<Point2D.Double> objectCoords = new ArrayList<Point2D.Double>();
		int left = (int) (intersection.pos.x);
		int right = (int) (intersection.pos.x + intersection.width);
		int top = (int) (intersection.pos.y);
		int bot = (int) (intersection.pos.y - intersection.length);
		for (int i = left; i < right; i++) {
			// clamping coordinates to be within 100x100 array bounds
			if (i >= center.x + visRange || i <= center.x - visRange) {
				continue;
			} else {
				for (int j = bot; j < top; j++) {
					if (j >= center.y + visRange || j <= center.y - visRange) {
						continue;
					}
					Point2D.Double coord = translator.translate(new Point2D.Double(i, j));
					objectCoords.add(coord);
				}
			}
		}
		return objectCoords.toArray(new Point2D.Double[objectCoords.size()]);
	}

	public Point2D.Double[] getCarCoords(Car car) {

		ArrayList<Point2D.Double> objectCoords = new ArrayList<Point2D.Double>();
		int left = (int) (car.getPosition().getX() - car.getWidth() / 2);
		int right = (int) (car.getPosition().getX() + car.getWidth() / 2);
		int top = (int) (car.getPosition().getY() + car.getLength() / 2);
		int bot = (int) (car.getPosition().getY() - car.getLength() / 2);
		for (int i = left; i < right; i++) {
			// clamping coordinates to be within 100x100 array bounds
			if (i >= center.x + visRange || i <= center.x - visRange) {
				continue;
			} else {
				for (int j = bot; j < top; j++) {
					if (j >= center.y + visRange || j <= center.y - visRange) {
						continue;
					}
					Point2D.Double coord = translator.translate(new Point2D.Double(i, j));
					objectCoords.add(coord);
				}
			}
		}

		return objectCoords.toArray(new Point2D.Double[objectCoords.size()]);
	}

	// don't think this only gets the edges, i may be wrong but it looks the
	// same as above car coords function
	public Point2D.Double[] getIntersectionEdges(Intersection intersection) {
		ArrayList<Point2D.Double> objectCoords = new ArrayList<Point2D.Double>();
		int left = (int) (intersection.pos.x - 1);
		int right = (int) (intersection.pos.x + intersection.width + 1);
		int top = (int) (intersection.pos.y + 1);
		int bot = (int) (intersection.pos.y - intersection.length - 1);
		for (int i = left; i < right; i++) {
			// clamping coordinates to be within 100x100 array bounds
			if (i >= center.x + visRange || i <= center.x - visRange) {
				continue;
			} else {
				for (int j = bot; j < top; j++) {
					if (j >= center.y + visRange || j <= center.y - visRange) {
						continue;
					}
					Point2D.Double coord = translator.translate(new Point2D.Double(i, j));
					objectCoords.add(coord);
				}
			}
		}

		return objectCoords.toArray(new Point2D.Double[objectCoords.size()]);
	}

}

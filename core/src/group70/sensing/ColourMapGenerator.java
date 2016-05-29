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
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;
import com.unimelb.swen30006.partc.roads.RoadMarking;

/* 
 * Generator which is in charge of color map, getting initial
 * colour from environment of the world
 * 
 */
public class ColourMapGenerator implements MapGenerator {
	private Color[][] cMap;
	private HashMap<WorldObject, Point2D.Double[]> objects;
	private HashMap<Road, Point2D.Double[]> roads;
	private HashMap<Intersection, Point2D.Double[]> intersections;
	private HashMap<RoadMarking, Point2D.Double[]> markings;
	private HashMap<Intersection, Point2D.Double[]> intersectionEdges;
	private HashMap<Car, Point2D.Double[]> cars;
	private Color worldColor;

	public Color[][] getCMap() {
		return cMap;
	}

	@Override
	public void initEmptyMap(int vis) {

		this.cMap = new Color[vis][vis];

		int i, j;
		for (i = 0; i < vis; i++) {
			for (j = 0; j < vis; j++) {
				this.cMap[i][j] = worldColor;
			}
		}
		return;
	}

	// Process different kinds of objects one by one, basically same approach
	@Override
	public void generate(Double pos, float delta, int vis) {
		for (Entry<Road, Point2D.Double[]> roadEntry : roads.entrySet()) {
			// no need to get road, the color is all the same
			// also no way to access color so I just set it by hard coding
			Point2D.Double[] roadCoords = roadEntry.getValue();
			for (Point2D.Double coords : roadCoords) {
				cMap[(int) coords.getX()][(int) coords.getY()] = Color.DARK_GRAY;
			}
		}
		for (Entry<Intersection, Point2D.Double[]> sectionEntry : intersections.entrySet()) {
			Point2D.Double[] sectionCoords = sectionEntry.getValue();
			for (Point2D.Double coords : sectionCoords) {
				cMap[(int) coords.getX()][(int) coords.getY()] = Color.DARK_GRAY;
			}
		}
		for (Entry<Intersection, Point2D.Double[]> sectionEntry : intersectionEdges.entrySet()) {
			Point2D.Double[] edgeCoords = sectionEntry.getValue();
			for (Point2D.Double coords : edgeCoords) {
				cMap[(int) coords.getX()][(int) coords.getY()] = Color.LIGHT_GRAY;
			}
		}
		for (Entry<RoadMarking, Point2D.Double[]> markingEntry : markings.entrySet()) {
			Point2D.Double[] markingCoords = markingEntry.getValue();
			for (Point2D.Double coords : markingCoords) {
				cMap[(int) coords.getX()][(int) coords.getY()] = Color.LIGHT_GRAY;
			}
		}
		for (Entry<WorldObject, Point2D.Double[]> objectEntry : objects.entrySet()) {
			WorldObject object = objectEntry.getKey();
			Point2D.Double[] objectCoords = objectEntry.getValue();
			Color objectColor = object.getColour();
			for (Point2D.Double coords : objectCoords) {
				cMap[(int) coords.getX()][(int) coords.getY()] = objectColor;
			}
		}
		for (Entry<Car, Point2D.Double[]> carEntry : cars.entrySet()) {
			Car car = carEntry.getKey();
			Point2D.Double[] carCoords = carEntry.getValue();
			Color carColor = car.getColour();
			for (Point2D.Double coords : carCoords) {
				cMap[(int) coords.getX()][(int) coords.getY()] = carColor;
			}
		}
	}

	// Getting objects from the world by using processor
	@Override
	public void getProcessedData(Processor processor) {
		objects = processor.getObjects();
		roads = processor.getRoads();
		cars = processor.getCars();
		intersections = processor.getIntersections();
		intersectionEdges = processor.getIntersectionEdges();
		markings = processor.getMarkings();
		worldColor = processor.getWorldColor();
	}

}

/**
 * Group 70
 * Kaven Peng 696573
 * Liep Lim 686458
 * William Song 641305
 * */
package group70.sensing;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;

/** scanner class that gets all objects from the world */
public class Scanner {
	private WorldObject[] scannedObjects;
	private Road[] scannedRoads;
	private Intersection[] intersections;
	private Intersection[] scannedIntersections;
	private Color worldColor;

	public Scanner(World world) {
		this.intersections = world.getIntersections();
	}

	public void Scan(World world, Point2D.Double pos) {
		intersectionScan(pos);
		this.scannedRoads = world.roadsAroundPoint(pos);
		this.scannedObjects = world.objectsAtPoint(pos);
		this.worldColor = world.getEnvironmentColour();
	}

	public void intersectionScan(Point2D.Double carPos) {
		ArrayList<Intersection> scanned = new ArrayList<Intersection>();
		float xmin = (float) carPos.x - World.VISIBILITY_RADIUS;
		float xmax = (float) carPos.x + World.VISIBILITY_RADIUS;
		float ymin = (float) carPos.y - World.VISIBILITY_RADIUS;
		float ymax = (float) carPos.y + World.VISIBILITY_RADIUS;
		for (Intersection i : intersections) {
			float interX = (float) i.pos.getX();
			float interY = (float) i.pos.getY();
			if ((interX < xmax) && (interX > xmin) && (interY < ymax) && (interY > ymin)) {
				scanned.add(i);
			}
		}
		this.scannedIntersections = scanned.toArray(new Intersection[scanned.size()]);
	}

	public WorldObject[] getScannedObjects() {
		return scannedObjects;
	}

	public Intersection[] getScannedIntersections() {
		return scannedIntersections;
	}

	public Road[] getScannedRoads() {
		return scannedRoads;
	}

	public Color getWorldColor() {
		return worldColor;
	}

}

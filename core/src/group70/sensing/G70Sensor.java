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
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;
import com.unimelb.swen30006.partc.ai.interfaces.*;

/*
 * Group 70's implementation of ISensing interface. this is
 * the controller class, 
 */
public class G70Sensor implements ISensing {

	private Vector2[][] velocityMap;
	private boolean[][] spaceMap;
	private Color[][] colourMap;
	private ArrayList<MapGenerator> generators = new ArrayList<MapGenerator>();
	private Scanner scanner;
	private Point2D.Double pos;
	private float delta;
	private WorldObject[] scannedObjects;
	private Road[] scannedRoads;
	private Intersection[] scannedIntersections;
	private Color scannedWorldColor;
	private World world;
	private Car car;
	private Processor processor;

	public G70Sensor(World world, Car car) {
		this.scanner = new Scanner(world);
		this.car = car;
		this.world = world;

		initGenerators();
	}

	/** initializes the generators we needed */
	public void initGenerators() {
		generators.add(new VelocityMapGenerator());
		generators.add(new SpaceMapGenerator());
		generators.add(new ColourMapGenerator());
	}

	/**
	 * scans the world
	 */
	public void getScan() {
		scanner.Scan(world, pos);
		scannedObjects = scanner.getScannedObjects();
		scannedRoads = scanner.getScannedRoads();
		scannedIntersections = scanner.getScannedIntersections();
		scannedWorldColor = scanner.getWorldColor();
	}

	/**
	 * calls the generators which processes the data first then generates the
	 * map
	 */
	public void callGenerate(MapGenerator generator, int vis) {
		generator.getProcessedData(processor);
		generator.initEmptyMap(vis);
		generator.generate(pos, delta, vis);
	}

	@Override
	public boolean update(Point2D.Double pos, float delta, int visibility) {
		processor = new Processor(visibility);
		// scans world and updates everything
		this.pos = pos;
		this.delta = delta;
		getScan();
		// processes scanned info into data more easily used by generators
		processor.update(pos, delta, scannedWorldColor, car, scannedObjects, scannedRoads, scannedIntersections);
		for (MapGenerator generator : generators) {
			callGenerate(generator, visibility);
			if (generator instanceof ColourMapGenerator) {
				this.colourMap = ((ColourMapGenerator) generator).getCMap();
			} else if (generator instanceof VelocityMapGenerator) {
				this.velocityMap = ((VelocityMapGenerator) generator).getRelativeVMap();
			} else if (generator instanceof SpaceMapGenerator) {
				this.spaceMap = ((SpaceMapGenerator) generator).getSMap();
			}
		}
		//toggle for print
		//printMaps();
		return true;
	}

	@Override
	public Vector2[][] getVelocityMap() {
		return velocityMap;
	}

	@Override
	public boolean[][] getSpaceMap() {
		return spaceMap;
	}

	@Override
	public Color[][] getColourMap() {
		return colourMap;
	}

	/**
	 * for debugging, print 3 maps
	 */
	public void printMaps() {

		System.out.println("SPACE MAP:");
		for (int i = 0; i < World.VISIBILITY_RADIUS; i++) {

			for (int j = 0; j < World.VISIBILITY_RADIUS; j++) {
				System.out.print(" [" + spaceMap[i][j] + "] ");
			}
			System.out.print('\n');
		}

		System.out.println("COLOUR MAP:");
		for (int i = 0; i < World.VISIBILITY_RADIUS; i++) {

			for (int j = 0; j < World.VISIBILITY_RADIUS; j++) {
				System.out.print(" [" + colourMap[i][j].toIntBits() + "] ");
			}
			System.out.print('\n');
		}

		System.out.println("VELOCITY MAP:");
		for (int i = 0; i < World.VISIBILITY_RADIUS; i++) {

			for (int j = 0; j < World.VISIBILITY_RADIUS; j++) {
				System.out.print(" [" + velocityMap[i][j].toString() + "] ");
			}
			System.out.print('\n');
		}
	}

}

/**
 * Group 70
 * Kaven Peng 696573
 * Liep Lim 686458
 * William Song 641305
 * */
package group70.sensing;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map.Entry;

import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.WorldObject;

/**
 * Generator which is in charge of space map, the initial
 * value is false.
 */
public class SpaceMapGenerator implements MapGenerator {
	private boolean[][] sMap;
	private HashMap<WorldObject, Point2D.Double[]> objects;
	private HashMap<Car, Point2D.Double[]> cars;

	@Override
	public void initEmptyMap(int vis) {

		this.sMap = new boolean[vis][vis];

		int i, j;
		for (i = 0; i < vis; i++) {
			for (j = 0; j < vis; j++) {
				this.sMap[i][j] = false;
			}
		}
		return;
	}

	@Override
	public void generate(Point2D.Double pos, float delta, int vis) {

		// Processing collidable objects
		for (Entry<WorldObject, Point2D.Double[]> objectEntry : objects.entrySet()) {
			Point2D.Double[] objectCoords = objectEntry.getValue();
			for (Point2D.Double coords : objectCoords) {
				sMap[(int) coords.getX()][(int) coords.getY()] = true;
			}
		}

		// Processing car
		for (Entry<Car, Point2D.Double[]> carEntry : cars.entrySet()) {
			// Car car = carEntry.getKey();
			Point2D.Double[] carCoords = carEntry.getValue();
			// Color carColor = car.getColour();
			for (Point2D.Double coords : carCoords) {
				sMap[(int) coords.getX()][(int) coords.getY()] = true;
			}
		}

	}

	@Override
	public void getProcessedData(Processor processor) {
		objects = processor.getObjects();
		cars = processor.getCars();
	}

	public boolean[][] getSMap() {
		return sMap;
	}

}

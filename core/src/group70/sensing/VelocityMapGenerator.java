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
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.objects.WorldObject;

/**
 * Generator which is in charge of color map, getting initial
 * speed is 0. Every objects' velocity is calculated by the distance
 * they have moved(if any), and delta.
 * 
 */
public class VelocityMapGenerator implements MapGenerator {
	private Vector2[][] relativeVMap;
	private HashMap<WorldObject, Point2D.Double[]> prevObjects = null;
	private HashMap<WorldObject, Point2D.Double[]> objects;
	private Vector2 carVelocity;
	private HashMap<WorldObject, Vector2> objectRelativeVTable = null;

	/**
	 * fills all objectrelativeVtable based on its previous position and current
	 * position
	 */
	public void fillObjectRelativeVTable(HashMap<WorldObject, Point2D.Double[]> prevObjects,
			HashMap<WorldObject, Point2D.Double[]> objects, float delta) {
		// start state with no previous objects listed
		if (prevObjects == null) {
			for (WorldObject cur : objects.keySet()) {
				addObjectRelativeVel(cur, new Vector2(0, 0));
			}
		} else {
			for (WorldObject cur : objects.keySet()) {
				for (WorldObject prev : prevObjects.keySet()) {
					if (cur.equals(prev)) {
						float x = (float) (cur.getPosition().x - prev.getPosition().x);
						float y = (float) (cur.getPosition().y - prev.getPosition().y);
						Vector2 absVel = new Vector2(x, y).scl(1 / delta);
						addObjectRelativeVel(cur, absVel);
						// new object that wasn't in previous map
					} else {
						addObjectRelativeVel(cur, new Vector2(0, 0));
					}
				}
			}
		}
	}

	/**
	 * A relative to B is a - b hence absVel - carVel
	 */
	public Vector2 CalcRelativeVelocity(Vector2 absVel, Vector2 carVel) {
		return absVel.sub(carVel);
	}

	public void addObjectRelativeVel(WorldObject obj, Vector2 absVel) {
		Vector2 objectRelativeV = CalcRelativeVelocity(absVel, carVelocity);
		if (objectRelativeVTable == null) {
			objectRelativeVTable = new HashMap<WorldObject, Vector2>();
			objectRelativeVTable.put(obj, objectRelativeV);
		} else {
			objectRelativeVTable.put(obj, objectRelativeV);
		}
	}

	public Vector2[][] getRelativeVMap() {
		return relativeVMap;
	}

	@Override
	public void initEmptyMap(int vis) {
		this.relativeVMap = new Vector2[vis][vis];

		int i, j;
		for (i = 0; i < vis; i++) {
			for (j = 0; j < vis; j++) {
				this.relativeVMap[i][j] = new Vector2(0, 0);
			}
		}
		return;
	}

	@Override
	public void generate(Double pos, float delta, int vis) {
		fillObjectRelativeVTable(prevObjects, objects, delta);
		for (Entry<WorldObject, Point2D.Double[]> objectEntry : objects.entrySet()) {
			WorldObject object = objectEntry.getKey();
			Point2D.Double[] objectCoords = objectEntry.getValue();
			Vector2 objectV = objectRelativeVTable.get(object);
			for (Point2D.Double coords : objectCoords) {
				relativeVMap[(int) coords.getX()][(int) coords.getY()] = objectV;
			}
		}
		// set current object hashmap to be previous map
		prevObjects = objects;
	}

	@Override
	public void getProcessedData(Processor processor) {
		objects = processor.getObjects();
		carVelocity = processor.getCarVelocity();
	}

}

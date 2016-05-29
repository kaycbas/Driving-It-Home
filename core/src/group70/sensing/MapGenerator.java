/**
 * Group 70
 * Kaven Peng 696573
 * Liep Lim 686458
 * William Song 641305
 * */
package group70.sensing;

import java.awt.geom.Point2D;

/** Map generator interface, */
public interface MapGenerator {
	/** generates map */
	public void generate(Point2D.Double pos, float delta, int vis);

	/** initializes the map */
	public void initEmptyMap(int visibility);

	/** new function get processed data which is different to our original
	 * design, since some more objects such as colour needed to be passed into
	 * generators
	 */
	public void getProcessedData(Processor processor);
}

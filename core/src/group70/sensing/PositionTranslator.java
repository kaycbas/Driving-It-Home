/**
 * Group 70
 * Kaven Peng 696573
 * Liep Lim 686458
 * William Song 641305
 * */
package group70.sensing;

import java.awt.geom.Point2D;

/**
 * position translator class
 */
public class PositionTranslator {
	private Point2D.Double center;
	private int visRange;

	public PositionTranslator(Point2D.Double center, int vis) {
		this.center = center;
		this.visRange = vis;
	}

	/**
	 * Translates absolute coordinates to array coordinates relative to the
	 * carPos which is in the center of 100x100 array visRange = 100/2 right now
	 * i.e. if something were on car, relative position would be (0,0) but in
	 * the array since car is at (50,50) then the objects array position would
	 * also be (50,50)
	 */
	public Point2D.Double translate(Point2D.Double pos) {
		return new Point2D.Double(pos.x - center.x + visRange, pos.y - center.y + visRange);
	}
}

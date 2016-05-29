/* ClassifiedObject.java
 * 
 * SWEN30006 Part C - Driving It Home
 * Semester 1, 2016
 * 
 * by Group 60
 * Adrian Cheung 636330
 * Shi Chang Zhang 695434
 * Kevin Bastoul 802847
 * 
 * Description: This is a Blob which has been classified
 * by a KnownObject.
 */

package group60.perception;

import java.awt.geom.Point2D;

import com.badlogic.gdx.math.Vector2;

public class ClassifiedObject extends KnownObject {

	/*
	 * TODO Comments pls.
	 */
	private Point2D.Double position;
	private Vector2 abVel;
	
	public ClassifiedObject(KnownObject ko, Point2D.Double abPos, Vector2 abVel) {
		super(ko.classification, ko.colour, ko.length, ko.width, 
				ko.staticFlag, ko.collidableFlag, ko.information);
		this.position = abPos;
		this.abVel = abVel;
	}

	/*
	 * Getter and Setters
	 */
	public Point2D.Double getPosition() {
		return position;
	}

	public void setPosition(Point2D.Double position) {
		this.position = position;
	}

	public Vector2 getAbVel() {
		return abVel;
	}

	public void setAbVel(Vector2 abVel) {
		this.abVel = abVel;
	}

}

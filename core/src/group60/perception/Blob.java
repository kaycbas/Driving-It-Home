/* Blob.java
 * 
 * SWEN30006 Part C - Driving It Home
 * Semester 1, 2016
 * 
 * by Group 60
 * Adrian Cheung 636330
 * Shi Chang Zhang 695434
 * Kevin Bastoul 802847
 * 
 * Description: Blobs represent our candidates for a 
 * PerceptionResponse.
 * 
 */

package group60.perception;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Blob {

	/*
	 * comments
	 */
	private Point2D.Double position;
	private double width;
	private double length;
	private Color colour;
	private Vector2 abVel;
	private boolean isColliable;
	private boolean isCompleteX;
	private boolean isCompleteY;

	/**
	 * Comments
	 * @param position
	 * @param width
	 * @param length
	 * @param colour
	 * @param abVel
	 * @param isCollidable
	 * @param isCompleteX
	 * @param isCompleteY
	 */
	public Blob(Point2D.Double position, double width, double length, Color colour, Vector2 abVel,
			boolean isCollidable, boolean isCompleteX, boolean isCompleteY) {

		this.position = position;
		this.width=width;
		this.length=length;
		this.colour=colour;
		this.abVel=abVel;
		this.isColliable=isCollidable;
		this.isCompleteX=isCompleteX;
		this.isCompleteY=isCompleteY;

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

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}

	public Vector2 getAbVel() {
		return abVel;
	}

	public void setAbVel(Vector2 abVel) {
		this.abVel = abVel;
	}

	public boolean isColliable() {
		return isColliable;
	}

	public void setColliable(boolean isColliable) {
		this.isColliable = isColliable;
	}

	public boolean isCompleteX() {
		return isCompleteX;
	}

	public void setCompleteX(boolean isCompleteX) {
		this.isCompleteX = isCompleteX;
	}

	public boolean isCompleteY() {
		return isCompleteY;
	}

	public void setCompleteY(boolean isCompleteY) {
		this.isCompleteY = isCompleteY;
	}
}

/* BlobDetector.java
 * 
 * SWEN30006 Part C - Driving It Home
 * Semester 1, 2016
 * 
 * by Group 60
 * Adrian Cheung 636330
 * Shi Chang Zhang 695434
 * Kevin Bastoul 802847
 * 
 * Description: This class is responsible for the creation
 * of Blobs.
 * 
 */

package group60.perception;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;

public class BlobDetector {
	
	/*
	 * lol more comments
	 */
	private KnownObjectHandler knownObjectsInfo;
	
	/**
	 * sigh why so many comments?
	 * @param k
	 */
	public BlobDetector(KnownObjectHandler k){
		knownObjectsInfo = k;
	}


	/**
	 * TODO comments are good
	 * @param map
	 * @return
	 */
	public ArrayList<Blob> generateBlobs(MultiMap map){
		int dim = map.getDim();
		ArrayList<Group> groups = new ArrayList<Group>();

		Color roadMarkingColor = null;
		for (KnownObject ko : knownObjectsInfo.getKnownObjects()) {
			if (ko.classification.equals(PerceptionResponse.Classification.RoadMarking)) {
				roadMarkingColor = ko.colour;
				//System.out.println(roadMarkingColor.toString());
				break;
			}
		}

		//Color roadMarkingColor = knownObjectsInfo.getKnownObjects().

		for(int row=0; row<dim; row++) {
			for (int col=0; col<dim; col++) {

				MultiMapCell currentCell = map.getCell(row, col);
				if (currentCell.isOccupied() || currentCell.getColor().equals(roadMarkingColor)) {
					//if (currentCell.getColor().equals(roadMarkingColor)) {System.out.println("match");}
					if (col!=0) {
						if (map.getCell(row, col-1).isGrouped()) {
							//add current cell to left-adjacent cell's group
							for (Group group : groups) {
								if (group.contains(map.getCell(row, col-1))) {
									group.addCell(currentCell);
									currentCell.setGrouped();
									break;
								}
							}
						}
					}

					if (row!=0 && !currentCell.isGrouped()) {
						if (map.getCell(row-1, col).isGrouped()) {
							//add current cell to above-adjacent cell's group
							for (Group group : groups) {
								if (group.contains(map.getCell(row-1, col))) {
									group.addCell(currentCell);
									currentCell.setGrouped();
									break;
								}
							}
						}
					}

					if (!currentCell.isGrouped()) {
						Group newGroup = new Group();
						newGroup.addCell(currentCell);
						groups.add(newGroup);
						currentCell.setGrouped();
					}
				}
			}
		}

		ArrayList<Blob> blobs = new ArrayList<Blob>();
		for (Group group : groups) {
			blobs.add(blobifyGroup(group));
		}

		return blobs;
	}
	
	/**
	 * TODO comments are bossss
	 * @param m
	 * @param x
	 * @param y
	 * @return
	 */
	private Blob blobify(MultiMap m, int x, int y){
		return null;
	}
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	private Blob joinBlobs(Blob[] b){
		return null;
	}
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	private Blob[] splitBlob(Blob b){
		return null;
	}

	private Blob blobifyGroup(Group g) {
		ArrayList<MultiMapCell> group = g.getGroup();
		int topEdge = group.get(0).getRow();
		int leftEdge = group.get(0).getCol();
		int rightEdge = group.get(0).getCol();
		int bottomEdge = group.get(0).getRow();

		for (MultiMapCell cell : group) {
			if (cell.getRow() <= topEdge) {
				topEdge = cell.getRow();
			} if (cell.getCol() <= leftEdge) {
				leftEdge = cell.getCol();
			} if (cell.getCol() >= rightEdge) {
				rightEdge = cell.getCol();
			} if (cell.getRow() >= bottomEdge) {
				bottomEdge = cell.getRow();
			}
		}


		int width = (rightEdge - leftEdge) + 1;
		int height = (bottomEdge - topEdge) + 1;
		//System.out.println("height: " + height);
		//System.out.println("width: " + width);
		Point2D.Double pt = new Point2D.Double(leftEdge+(width/2), topEdge+(height/2));
		Color color = group.get(group.size()/2).getColor();
		Vector2 abVel = group.get(group.size()/2).getAbsoluteVelocity();
		boolean isCollideable = group.get(0).isOccupied() ? true : false;
		boolean isCompleteX = true;
		boolean isCompleteY = true;

		Blob blob = new Blob(pt, width, height, color, abVel,
				isCollideable, isCompleteX, isCompleteY);

		return blob;
	}
}

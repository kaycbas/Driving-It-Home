/* Classifier.java
 * 
 * SWEN30006 Part C - Driving It Home
 * Semester 1, 2016
 * 
 * by Group 60
 * Adrian Cheung 636330
 * Shi Chang Zhang 695434
 * Kevin Bastoul 802847
 * 
 * Description: This class will handle the logic that is 
 * needed to classify Blobs.
 * 
 */

package group60.perception;

import java.util.ArrayList;

public class Classifier {

	/*
	 * Add some comments
	 */
	private KnownObjectHandler koh;

	/**
	 * Comments pls.
	 * 
	 * @param k
	 */
	public Classifier(KnownObjectHandler k) {
		this.koh = k;
	}

	/**
	 * Comment pls.
	 * 
	 * @param detectedBlobs
	 * @return
	 */
	public ArrayList<ClassifiedObject> classify(ArrayList<Blob> detectedBlobs) {
		double minSizeDiff;
		double minColourDiff;
		double thisSizeDiff;
		double thisColourDiff;
		KnownObject match = null;
		boolean blobStatic;
		ArrayList<KnownObject> candidates;
		ArrayList<ClassifiedObject> cos = new ArrayList<ClassifiedObject>();
		for (Blob b : detectedBlobs) {
			minSizeDiff = Double.MAX_VALUE;
			minColourDiff = Double.MAX_VALUE;
			if (Math.abs(b.getAbVel().len()) < 0.01) {
				blobStatic = true;
			} else {
				blobStatic = false;
			}
			candidates = new ArrayList<KnownObject>();
			// find min size difference
			for (KnownObject ko : this.koh.getKnownObjects()) {
				thisSizeDiff = ko.getSizeDifferent(b);
				if (thisSizeDiff < minSizeDiff) {
					minSizeDiff = thisSizeDiff;
				}
			}
			// get all known objects with identical size difference as
			// candidates
			for (KnownObject ko : this.koh.getKnownObjects()) {
				thisSizeDiff = ko.getSizeDifferent(b);
				if (Math.abs(thisSizeDiff - minSizeDiff) < 0.01) {
					//if (ko.collidableFlag == b.isColliable() && ko.staticFlag == blobStatic) {
					candidates.add(ko);
					//}
				}
			}
			// use colour as tie breaker if more than one candidate
			if (candidates.size() > 1) {
				for (KnownObject c : candidates) {
					thisColourDiff = c.getColourDifference(b);
					if (thisColourDiff < minColourDiff) {
						minColourDiff = thisColourDiff;
						match = c;
					}
				}
				cos.add(new ClassifiedObject(match, b.getPosition(), b.getAbVel()));
			} else if (candidates.size() == 1) {
				match = candidates.get(0);
				cos.add(new ClassifiedObject(match, b.getPosition(), b.getAbVel()));
			} else {
				System.out.println("No matches");
				//return cos;
			}
		}
		System.out.println("num cObjs: " + cos.size());
		return cos;
	}

}

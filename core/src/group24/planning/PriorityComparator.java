package group24.planning;

import java.util.Comparator;

import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;

public class PriorityComparator implements Comparator<PerceptionResponse> {
	public int compare(PerceptionResponse o1, PerceptionResponse o2) {
		// If o1's distance is closer to the car than o2
		if (o1.distance < o2.distance) {
			// return o1's
			return -1;
			// Else if o2's distance is closer to the car than o1
		} else if (o1.distance > o2.distance) {
			// return o2'
			return 1;
			// Else if they are equal
		} else {
			// If o1's ttc is sooner thatn o2's
			if (o1.timeToCollision < o2.timeToCollision) {
				// return o1's
				return -1;
				// Else if o2's ttc is sooner than o1's
			} else if (o1.timeToCollision > o2.timeToCollision) {
				// return o2's
				return 1;
				// Else if they are the same, they are equal
			} else {
				return 0;
			}
		}
	}
}

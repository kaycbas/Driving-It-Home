package group32.planning;

import java.util.HashMap;
import com.unimelb.swen30006.partc.core.infrastructure.*;
import com.unimelb.swen30006.partc.ai.interfaces.*;


/* Got rid of getters for Perception Response as Perception Response
 * attributes are already public
 */

public class PerceptionRanker {
	
	// Priority Values (Skeleton)
//	public enum Priority {
//		HIGHESTPRIORITY(5),
//		HIGHPRIORITY(4),
//		MEDIUMPRIORITY(3),
//		LOWPRIORITY(2),
//		LOWESTPRIORITY(1);
//		
//		// Priority Value 
//		private final int priValue;
//		
//		private Priority(int value) {
//			this.priValue = value;
//		}
//		public int getValue() {
//			return priValue;
//		}
//	};
	
	public enum Priority {
		HIGHESTPRIORITY,
		HIGHPRIORITY,
		MEDIUMPRIORITY,
		LOWPRIORITY,
		LOWESTPRIORITY};
	
	
	
	private final float MIN_TIME_TO_COL = 1;
	private HashMap<PerceptionResponse, Priority> rankings;
	
	public PerceptionRanker(PerceptionResponse[] p1) {
		// Assign all the rankings to the given Perception Response
		this.setRankings(p1);
	}
	
	
	// Ranks the perception to the appropriate priority
	// Decided to parse in Perception Response instead due to public visibility of attributes
	public Priority rank(PerceptionResponse p1) {
		// Prioritize Collision before anything else, immediate brake
		if (p1.timeToCollision <= this.MIN_TIME_TO_COL && p1.timeToCollision >= 0) {
			return Priority.HIGHESTPRIORITY;
		}
		
		// Next would be prioritizing traffic lights
		if (p1.objectType == PerceptionResponse.Classification.TrafficLight) {
			return Priority.HIGHESTPRIORITY;
		}
		
		// Next would be prioritizing the lane. If we are not in the right lane.
		if (p1.objectType == PerceptionResponse.Classification.LaneMarking) {
			// If we are in the wrong lane
			if (p1.direction.x < 0 || p1.direction.y < 0) {
				return Priority.HIGHESTPRIORITY;
			}
		}
		
		// Else default is Lowest priority
		return Priority.LOWESTPRIORITY;
	}
	
	/* Added additional getters and setters. getRankings() to retrieve the rankings and 
	 * setRankings method for assigning the rankings.
	 */

	// Retrieve Perception Response Ranking
	public HashMap<PerceptionResponse, Priority> getRankings() {
		return rankings;
	}
	
	// Set Perception Response
	public void setRankings(PerceptionResponse[] p1) {
		for (PerceptionResponse PerRep : p1) {
			this.rankings.put(PerRep, this.rank(PerRep));
		}
	}

}

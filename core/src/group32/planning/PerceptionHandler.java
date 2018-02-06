package group32.planning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import group32.planning.PerceptionRanker.Priority;
import com.unimelb.swen30006.partc.ai.interfaces.*;


public class PerceptionHandler {
	private HashMap<PerceptionResponse, Priority> rankings; // Added rankings as it needs its own copy to keep tabs on
	                                                        // what action to process next.
	private ArrayList<PerceptionResponse> highestPRs;       // Perception Responses with the highest priority
	private final float MIN_TIME_TO_COL = 1;                // Minimum time to collision
	private final int ZERO = 0;                             // For accessing the first element
	
	public PerceptionHandler(PerceptionRanker pr) {
		this.rankings = pr.getRankings(); // Get the rankings from the perception  Response
		this.highestPRs = new ArrayList<PerceptionResponse>(); // Initialize the highest priority in rankings
	}
	
	/* Was based on previous weighting system
	public float weightAction() {
		
	}
	*/
	
	// Get the highest priority items in rankings hashmap
	public void getHighestPriorityOnly() {
		// Highest Priority to look for in rankings. Highest priority first, lowest last
		PerceptionRanker.Priority highest_priority;
		
		// First check if there is highest priority
		if (this.rankings.containsValue(PerceptionRanker.Priority.HIGHESTPRIORITY)) {
			// Look for highest priority only
			highest_priority = PerceptionRanker.Priority.HIGHESTPRIORITY;
		} else if (this.rankings.containsValue(PerceptionRanker.Priority.HIGHPRIORITY)) {
			// If not look for high priority only
			highest_priority = PerceptionRanker.Priority.HIGHPRIORITY;
		} else if (this.rankings.containsValue(PerceptionRanker.Priority.MEDIUMPRIORITY)) {
			// If not look for medium priority
			highest_priority = PerceptionRanker.Priority.MEDIUMPRIORITY;
		} else if (this.rankings.containsValue(PerceptionRanker.Priority.LOWPRIORITY)) {
			// If not look for low priority
			highest_priority = PerceptionRanker.Priority.LOWPRIORITY;
		} else { 
			// Else just look for Lowest Priority
			highest_priority = PerceptionRanker.Priority.LOWESTPRIORITY;
		}
		
		for (Entry<PerceptionResponse, PerceptionRanker.Priority> pr : this.rankings.entrySet()) {
			// Add the highest priority perception response into the arraylist
			if (pr.getValue() == highest_priority) {
				this.highestPRs.add(pr.getKey());
			}
		}
		
		// Now if the the array is still empty, just add any perceptionResponse from rankings
		if (this.highestPRs.isEmpty()) {
			Entry<PerceptionResponse, Priority>  entry = this.rankings.entrySet().iterator().next();
		    this.highestPRs.add(entry.getKey());
		}
		
	}
	
	// Check if we are about to crash
	public boolean crash(PerceptionResponse p1) {
		if (p1.timeToCollision <= this.MIN_TIME_TO_COL) {
			return true;
		}
		return false;
	}
	
	// Determine the most important perceptionResponse to process
	public PerceptionResponse determinePerceptionResponse() {
		for (PerceptionResponse pr : this.highestPRs) {
			// If we are about to crash, this will be the most important one
			if (this.crash(pr)) {
				return pr;
			} else if (pr.objectType == PerceptionResponse.Classification.LaneMarking) {
				// If we are across the lane marking, then this will be the next important one
				return pr;
			} else if (pr.objectType == PerceptionResponse.Classification.TrafficLight) {
				// Traffic light would be the next most important one
				return pr;
			}
		}
		
		// If we get here, just return any
		return highestPRs.get(ZERO);
	}

}

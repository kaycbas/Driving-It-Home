package group32.planning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.unimelb.swen30006.partc.ai.interfaces.*;
import com.unimelb.swen30006.partc.core.infrastructure.TrafficLight;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Intersection.Direction;
import com.unimelb.swen30006.partc.roads.Road;


public class ActionHandler {
	// Extra attributes were added to ActionHandler in order to determine the appropriate action
	private Router rout;  // Router is needed to go in the right direction
	private final float MIN_DIST = 5;  // Minimum distance to react to traffic light changes
	private final float MIN_TIME_TO_COL = 2; // Minimum collision time allowed
	private final static float ROTATION_RATE = 150f; // Rate of rotation
	private Car car; // Car to process the actions
	
	// Constructor to pass in router and car
	public ActionHandler(Router rout, Car c1) {
		this.rout = rout;
		this.car = c1;
	}
	
	// Additional method to check if we are about to crash
	public boolean crash(PerceptionResponse p1) {
		if (p1.timeToCollision <= this.MIN_TIME_TO_COL) {
			return true;
		}
		return false;
	}
	
	
	// determineAction now has perceptionResponse and delta in order to process actions to the car
	public void determineAction(PerceptionResponse pr, float delta) {
		// Used to check if an action is processed yet given a perception response
		// If no immediate action has been processed, continue on the route
		boolean actionProcessed = false;
		
		// Go through each perception response case.
		switch (pr.objectType) {
		case TrafficLight:
			if (crash(pr)) {
				//Brake
				this.car.brake();
				
				// Action has been processed
				actionProcessed = true;
				break;	
			}

			// Traffic Light info
			HashMap<String, Object> trafficLightInfo = pr.information;
			
			if (trafficLightInfo.get("State") == TrafficLight.State.Red) {
				// If in range of traffic Light
				if (pr.distance <= this.MIN_DIST) {
					// Brake
					this.car.brake();
					
					// Action has been processed
					actionProcessed = true;
					break;	
				}
				
			} else if (trafficLightInfo.get("State") == TrafficLight.State.Amber) {
				// If in range of traffic Light
				if (pr.distance <= this.MIN_DIST) {
					// Brake
					this.car.brake();
					
					// Action has been processed
					actionProcessed = true;
					break;	
				}
				
			} else if (trafficLightInfo.get("State") == TrafficLight.State.Green) {
				// If in range of traffic Light
				if (pr.distance <= this.MIN_DIST) {
					// Accelerate
					this.car.accelerate();	
					
					// Action has been processed
					actionProcessed = true;
					break;	
				}
			} else if (trafficLightInfo.get("State") == TrafficLight.State.Off) {
				// Accelerate
				this.car.accelerate();
				
				// Action has been processed
				actionProcessed = true;
				break;	
			}
		
		case Building:
			if (crash(pr)) {
				//Brake
				this.car.brake();
				
				// Action has been processed
				actionProcessed = true;
				break;	
			}
		case Car:
			if (crash(pr)) {
				if (pr.direction.x < 0 || pr.direction.y < 0) {
					// Accelerrate
					this.car.accelerate();
				} else {
					// Brake
					this.car.brake();
				}
				
				// Action has been processed
				actionProcessed = true;
				break;	
			}
		case RoadMarking:
			break;
		case LaneMarking:
			// Going in the y-dir , but crossed the lane to the other side
			// Or, Going in the x-dir, but crossed the lane to the other side
			if (pr.direction.x < 0 || pr.direction.y < 0) {
				// Steer back to the left lane
				
				// Handle turning
		        float angle = 0;
		        angle += ROTATION_RATE*delta;
				this.car.turn(angle);
				
				// Action has been processed
				actionProcessed = true;
				break;	
			}
				
			
		case Sign:
			if (crash(pr)) {
				//Brake
				this.car.brake();
				
				// Action has been processed
				actionProcessed = true;
				break;	
			}
		case StreetLight:
			if (crash(pr)) {
				//Brake
				this.car.brake();
				
				// Action has been processed
				actionProcessed = true;
				break;	
			}
		default:
			// If there is no perceptionResponse case break out
			break;
		}
			// If an action is not processed yet then, drive the car according to route
		if (!actionProcessed) {
			this.driveOnRoute();
		}
		
	
		// Update the car
		this.car.update(delta);
	
	}
	
	// Extra method added to instruct the car to drive on the route to destination
	public void driveOnRoute() {
		Intersection currentIntersection = this.rout.intersectionAtPoint(car.getPosition());
		// Based on the router, follow the direction of the car.
		if(currentIntersection != null) {
			for(Map.Entry<Direction, Road> entry : currentIntersection.roads.entrySet()) {
				if(entry.getValue().getStartPos() == this.rout.getNextRoad().getStartPos()) {
					if(entry.getKey() == Direction.North) {
						// turn forward
						this.car.accelerate();
					} else if (entry.getKey() == Direction.East) { 
						//turn right
						this.car.accelerate();
					} else if (entry.getKey() == Direction.West) {
						//turn left
						this.car.accelerate();
					} else if (entry.getKey() == Direction.South) {
						//turn down
						this.car.accelerate();
					}
				}
			}
		}
		else {
			this.car.accelerate();
		}
	}


}

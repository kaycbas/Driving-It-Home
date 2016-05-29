package group24.planning;

import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.infrastructure.TrafficLight;
import com.unimelb.swen30006.partc.core.objects.Car;

public class TrafficLightHandler extends Handler {
	
	public TrafficLightHandler(Car car) {
		super(car);
	}

	@Override
	public void handle(PerceptionResponse pr) {
		if(pr != null && pr.distance < Group24Planner.BRAKE_DIST){
			if(pr.information.get("State") == TrafficLight.State.Red){
				this.car.brake();
			} else {
				this.car.accelerate();
			}
		
		}
	}
}

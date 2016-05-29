package group24.planning;

import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.objects.Car;

public class SignHandler extends Handler{
	
	private boolean stopped;
	
	public SignHandler(Car car) {
		super(car);
	}

	@Override
	public void handle(PerceptionResponse pr) {
		if(pr != null && pr.distance < Group24Planner.BRAKE_DIST){
			
			if(car.getVelocity().len() > 0 && !stopped){
				car.brake();
			} else {
				car.accelerate();
				stopped = true;
			}
		}
		else {
			car.accelerate();
		}
		
	}

}

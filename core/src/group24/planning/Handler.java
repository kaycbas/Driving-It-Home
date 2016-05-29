package group24.planning;

import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.objects.Car;

public abstract class Handler {
	public Car car;
	
	public Handler(Car car) {
		this.setCar(car);
	}
	
	public abstract void handle(PerceptionResponse pr);
	
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
}


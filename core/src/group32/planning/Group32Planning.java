package group32.planning;

import java.awt.geom.Point2D;
import com.unimelb.swen30006.partc.ai.interfaces.IPlanning;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.objects.Car;

public class Group32Planning implements IPlanning{
	
	public Car car;
	public Router router;
	
	public Group32Planning(Car c) {
		this.car = c;
		this.router = new Router(c);

	}

	@Override
	public boolean planRoute(Point2D.Double destination) {
		this.router.setDestination(destination);
		if(this.router.calculateRoute()) {
			this.router.printRoute();
			return true;
		} else {
			System.out.println("no route found");
			return false;
		}
	}

	@Override
	public void update(PerceptionResponse[] results, float delta) {
		// TODO Auto-generated method stub\
		
		// Rank the Perceptions
		PerceptionRanker pr = new PerceptionRanker(results);
		
		// Handle the Perceptions after ranking them
		PerceptionHandler ph = new PerceptionHandler(pr);
		ph.getHighestPriorityOnly(); 
		
		// Determine the action based on the results of the perceptionHandler
		ActionHandler ah = new ActionHandler(this.router, this.car);
		ah.determineAction(ph.determinePerceptionResponse(), delta); // Update the car
		
		// Update the eta
		this.router.calculateEta(delta);
	}

	@Override
	public float eta() {
		// TODO Auto-generated method stub
		return this.router.getEta();
	}

}

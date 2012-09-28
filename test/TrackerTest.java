import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class TrackerTest {
	private static ObservationSequence migrate;
	private static ObservationSequence feign;
	
	@Before
	public void setUp() {
		Vector<Action> actions = new Vector<Action>();
		
		actions.add(new Action(0, Action.ACTION_STOP, Action.ACTION_STOP, 0));
		actions.add(new Action(0, Action.ACTION_KEEPSPEED, Action.ACTION_STOP, Action.MOVE_EAST));
		actions.add(new Action(0, Action.ACTION_KEEPSPEED, Action.ACTION_STOP, Action.MOVE_EAST));
		actions.add(new Action(0, Action.ACTION_KEEPSPEED, Action.ACTION_STOP, Action.MOVE_EAST));
		actions.add(new Action(0, Action.ACTION_KEEPSPEED, Action.ACTION_STOP, Action.MOVE_EAST));
		
		migrate = new ObservationSequence(actions);
		
		actions.clear();
		actions.add(new Action(0, Action.ACTION_STOP, Action.ACTION_STOP, 0));
		actions.add(new Action(0, Action.ACTION_STOP, Action.ACTION_ACCELERATE, Action.MOVE_DOWN));
		actions.add(new Action(0, Action.ACTION_STOP, Action.ACTION_ACCELERATE, Action.MOVE_DOWN));
		actions.add(new Action(0, Action.ACTION_STOP, Action.ACTION_ACCELERATE, Action.MOVE_DOWN));
		actions.add(new Action(0, Action.ACTION_STOP, Action.ACTION_ACCELERATE, Action.MOVE_DOWN));
		
		feign = new ObservationSequence(actions);
		
	}
	
	@Test
	public void testIsMigrating() {
		int move = BirdTracker.isMigrating(migrate);
		System.out.println("isMigrating:" + move);
		assertEquals(Action.MOVE_EAST, move);
	}
	
	@Test
	public void testIsFeigningDeath() {
		int move = BirdTracker.isFeigningDeath(feign);
		System.out.println("isFeigning: " + move);
		assertEquals(Action.MOVE_DOWN, move);
	}
}

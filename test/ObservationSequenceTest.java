import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;


public class ObservationSequenceTest {
	
	ObservationSequence O;

	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testObservationSequence() {
		Vector<Action> facit = TestLibrary.getFlightPattern(0);
		O = new ObservationSequence(facit);
		
		Vector<Action> Oacts = new Vector<Action>();
		
		for(int t = 0; t < O.T; t++) {
			int Haction = O.action[t] / 3;
			int Vaction = O.action[t] % 3;
			int movement = O.movement[t];
			
			Oacts.add(new Action(0, Haction, Vaction, movement));
		}
		
		for(int t = 0; t < Oacts.size(); t++) {
			String Ostring = ObservationSequence.actionToString(Oacts.get(t));
			String Fstring = ObservationSequence.actionToString(facit.get(t));
			assertEquals(Ostring,Fstring);
		}
	}

	@Test
	public void testActionToString() {
		Action a = new Action(0,Action.ACTION_KEEPSPEED,Action.ACTION_ACCELERATE,Action.MOVE_WEST|Action.MOVE_UP);
		String actStr = ObservationSequence.actionToString(a);
		String facit = "(0) K A west/up";
		assertEquals(actStr,facit);
	}

	@Test
	public void testStringToAction() {
		String s = "(0) A A west/up";
		Action a = TestLibrary.stringToAction(s);
		assertEquals(ObservationSequence.actionToString(a),s);
		
		s = "(0) A A west";
		a = TestLibrary.stringToAction(s);
		assertEquals(ObservationSequence.actionToString(a),s);
		
		s = "(0) A A /up";
		a = TestLibrary.stringToAction(s);
		assertEquals(ObservationSequence.actionToString(a),s);
		
		s = "(0) A A ";
		a = TestLibrary.stringToAction(s);
		assertEquals(ObservationSequence.actionToString(a),s);
		
		s = "(0) A A west/up";
		a = TestLibrary.stringToAction(s);
		assertEquals(ObservationSequence.actionToString(a),s);
		
		s = "(0) A A west";
		a = TestLibrary.stringToAction(s);
		assertEquals(ObservationSequence.actionToString(a),s);
		
		s = "(0) A A /up";
		a = TestLibrary.stringToAction(s);
		assertEquals(ObservationSequence.actionToString(a),s);
		
		s = "(0) A A ";
		a = TestLibrary.stringToAction(s);
		assertEquals(ObservationSequence.actionToString(a),s);
	}
}

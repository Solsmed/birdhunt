import java.util.Vector;


public class TestLibrary {
	private static Vector<Action> flight;
	
	public static MarkovModel getModel_3x5() {
		double[][] A = new double[][]
				{{0.80, 0.05, 0.15},
				 {0.05, 0.90, 0.05},
				 {0.60, 0.10, 0.30}
				};
		
		double[][] B = new double[][]
				{{0.00, 0.50, 0.50, 0.00, 0.00},
				 {0.10, 0.10, 0.10, 0.35, 0.35},
				 {1.00, 0.00, 0.00, 0.00, 0.00}
				};
		
		double[] pi = {1.0, 0.00, 0.00};
		
		return new MarkovModel(A, B, pi);
	}
	
	public static BirdModel getModelledObservation_3x9() {
		double[][] A = new double[][]
				{
				{0.85, 0.15, 0.00},
				{0.04, 0.91, 0.05},
				{0.08, 0.04, 0.88}
				};
		
		double[][] B = new double[][]
				{
				{0.61, 0.15, 0.01, 0.14, 0.04, 0.00, 0.00, 0.00, 0.05},
				{0.04, 0.01, 0.00, 0.00, 0.00, 0.00, 0.77, 0.18, 0.00},
				{0.03, 0.00, 0.43, 0.06, 0.00, 0.45, 0.01, 0.00, 0.02}
				};
		
		double[] pi = {1.0/3, 1.0/3, 1.0/3};
		
		int[] O = new int[] {2, 0, 0, 8, 6, 6, 0, 6, 6, 0, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 2, 3, 2, 5, 0, 0, 0, 4, 6, 6, 6, 6, 2, 5, 2, 3, 2, 5, 5, 5, 2, 5, 5, 2, 5, 5, 2, 5, 5, 0, 6, 7, 6, 6, 0, 7, 6, 6, 6, 6, 7, 6, 7, 6, 6, 6, 6, 7, 6, 6, 7, 2, 5, 5, 2, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 7, 6, 6, 6, 7, 6, 6, 6, 7, 7, 6, 6, 6, 0, 6, 6, 6, 6, 6, 2, 5, 2, 2, 2, 3, 2, 5, 5, 2, 5, 5, 2, 2, 0, 0, 0, 0, 3, 6, 6, 6, 6, 6, 6, 7, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 3, 0, 7, 2, 5, 2, 2, 5, 5, 2, 5, 3, 5, 5, 5, 5, 2, 5, 5, 5, 2, 2, 5, 2, 0, 0, 3, 0, 0, 1, 8, 6, 0, 0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 0, 6, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 2, 8, 6, 6, 7, 7, 6, 6, 6, 7, 6, 6, 6, 7, 6, 6, 6, 0, 0, 0, 1, 0, 0, 0, 3, 0, 0, 1, 0, 0, 6, 6, 6, 6, 6, 7, 7, 6, 6, 2, 5, 2, 5, 0, 3, 0, 0, 8, 6, 6, 1, 6, 6, 6, 6, 7, 0, 1, 0, 0, 0, 0, 6, 7, 2, 0, 3, 0, 0, 0, 7, 6, 6, 6, 2, 3, 5, 5, 5, 5, 5, 2, 5, 5, 5, 5, 3, 6, 6, 6, 6, 6, 7, 6, 6, 2, 2, 5, 3, 2, 5, 2, 5, 0, 0, 1, 0, 0, 1, 8, 6, 6, 7, 6, 0, 7, 0, 6, 6, 7, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 6, 7, 0, 7, 6, 6, 6, 1, 6, 2, 5, 2, 2, 2, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0, 0, 4, 0, 0, 1, 0, 0, 1, 0, 0, 1, 3, 0, 0, 6, 7, 6, 6, 0, 0, 3, 0, 0, 3, 4, 3, 0, 0, 0, 0, 0, 3, 0, 0, 4, 3, 0, 0, 0, 3, 1, 8, 6, 6, 6, 6, 6, 2, 2, 2, 0, 5, 0, 6, 6, 6, 7, 7, 6, 6, 0, 1, 1, 0, 3, 0, 6, 6, 6, 6, 6, 6, 7, 6, 7, 6, 7, 6, 6, 7, 7, 6, 6, 7, 7, 6, 1, 4, 6, 0, 5, 5, 5, 6, 2, 5, 8, 0, 2, 6, 6, 6, 7, 6, 6, 6, 7, 0, 1, 3, 0, 3, 0, 0, 3, 3, 0, 6, 6, 0, 8, 2, 2};
		
		return new BirdModel(new ObservationSequence(O), new MarkovModel(A, B, pi));
	}
	
	public static BirdModel getModelledObservation_3x9(int LEN) {
		BirdModel mobs = getModelledObservation_3x9();
		int[] O = new int[mobs.O.T];
		if(LEN > mobs.O.T)
			LEN = mobs.O.T;
		System.arraycopy(mobs.O.action, 0, O, 0, LEN);
		mobs.O = new ObservationSequence(O);
		
		return mobs;
	}
	
	public static Action stringToAction(String s) {
		int birdNum = 0;
		int Haction = 0;
		int Vaction = 0;
		int movement = 0;
		
		int endIndex = s.indexOf(')');
		birdNum = Integer.parseInt(s.substring(1, endIndex));
		endIndex = s.indexOf(' ', endIndex + 1);
		char H = s.charAt(endIndex + 1);
		if(H == 'A')
			Haction = Action.ACTION_ACCELERATE;
		else if (H == 'K')
			Haction = Action.ACTION_KEEPSPEED;
		else if (H == 'S')
			Haction = Action.ACTION_STOP;
		
		endIndex = s.indexOf(' ', endIndex + 1);
		char V = s.charAt(endIndex + 1);
		if(V == 'A')
			Vaction = Action.ACTION_ACCELERATE;
		else if (V == 'K')
			Vaction = Action.ACTION_KEEPSPEED;
		else if (V == 'S')
			Vaction = Action.ACTION_STOP;
		
		endIndex = s.indexOf(' ', endIndex + 1);
		
		String moveString = s.substring(endIndex + 1);
		
		String[] motions = moveString.split("/");
		
		if(motions.length == 0)
			movement = Action.BIRD_STOPPED;
		else if (motions.length == 1) {
			if(motions[0].equals("east"))
				movement = Action.MOVE_EAST;
			else if (motions[0].equals("west"))
				movement = Action.MOVE_WEST;
		}
		else if (motions.length == 2) {
			if(motions[0].length() == 0) {
				if(motions[1].equals("up"))
					movement = Action.MOVE_UP;
				else if (motions[1].equals("down"))
					movement = Action.MOVE_DOWN;
				}
			else {
				int Hmove = 0;
				int Vmove = 0;
				
				if(motions[0].equals("east"))
					Hmove = Action.MOVE_EAST;
				else if (motions[0].equals("west"))
					Hmove = Action.MOVE_WEST;
				
				if(motions[1].equals("up"))
					Vmove = Action.MOVE_UP;
				else if (motions[1].equals("down"))
					Vmove = Action.MOVE_DOWN;
				
				movement = Hmove|Vmove;
			}
		}
		
		return new Action(birdNum, Haction, Vaction, movement);
	}
	
	public static Vector<Action> getFlightPattern(int pattern) {
		if(flight == null) {			
			flight = new Vector<Action>();
			
			for(int i = 0; i < flightRecords[pattern].length; i++) {
				flight.add(stringToAction(flightRecords[pattern][i]));
			}
		}
		return flight;
	}
	
	public static final String[][] flightRecords = new String[][]
			{
		{
			"(0) A A west/up",
			"(0) A K west/up",
			"(0) A S west",
			"(0) K A west/up",
			"(0) K K west/up",
			"(0) K S west",
			"(0) S A /up",
			"(0) S K /up",
			"(0) S S",
			"(0) A A west/down",
			"(0) A K west/down",
			"(0) K A west/down",
			"(0) K K west/down",
			"(0) S A /down",
			"(0) S K /down",
			"(0) A A east/up",
			"(0) A K east/up",
			"(0) A S east",
			"(0) K A east/up",
			"(0) K K east/up",
			"(0) K S east",
			"(0) A A east/down",
			"(0) A K east/down",
			"(0) K A east/down",
			"(0) K K east/down" 
		},
		{
			"(0) A S east",
			"(0) S S",
			"(0) A S east",
			"(0) A A east/down",
			"(0) A A east/down",
			"(0) A S east",
			"(0) A S east",
			"(0) S A /up",
			"(0) A A west/up",
			"(0) S A /up",
			"(0) S A /up",
			"(0) S S",
			"(0) A A east/down",
			"(0) S A /down",
			"(0) A S east",
			"(0) S A /up",
			"(0) S S",
			"(0) A S west",
			"(0) S S",
			"(0) A S east",
			"(0) K S east",
			"(0) A A east/up",
			"(0) A S east",
			"(0) A S east",
			"(0) S S",
			"(0) A S east",
			"(0) A A east/up",
			"(0) A S east",
			"(0) A S east",
			"(0) A A east/up",
			"(0) S A /up",
			"(0) A A west/up",
			"(0) S A /up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) S A /up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) K A east/up",
			"(0) A S east",
			"(0) K A east/down",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/down",
			"(0) A S east",
			"(0) A A east/down",
			"(0) K S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K A east/down",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/down",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/down",
			"(0) K S east",
			"(0) K S east",
			"(0) A A east/down",
			"(0) S S",
			"(0) S A /down",
			"(0) S A /down",
			"(0) S K /down",
			"(0) A A west/down",
			"(0) K A west/down",
			"(0) A A west/down",
			"(0) A A west/down",
			"(0) A K west/down",
			"(0) A A west/down",
			"(0) A K west/down",
			"(0) A K west/down",
			"(0) A A west/down",
			"(0) A A west/down",
			"(0) A A west/down",
			"(0) A A west/down",
			"(0) S S",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/up",
			"(0) K S east",
			"(0) S A /up",
			"(0) S S",
			"(0) S S",
			"(0) A A west/up",
			"(0) S A /up",
			"(0) A S east",
			"(0) A A east/down",
			"(0) A S east",
			"(0) S S",
			"(0) A A west/up",
			"(0) A S west",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) S A /up",
			"(0) S S",
			"(0) A S west",
			"(0) A S west",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) S A /up",
			"(0) S S",
			"(0) S A /down",
			"(0) A S west",
			"(0) S S",
			"(0) A S east",
			"(0) A A east/down",
			"(0) A S east",
			"(0) S S",
			"(0) S S",
			"(0) S A /down",
			"(0) A A west/down",
			"(0) A S west",
			"(0) A A west/down",
			"(0) A S west",
			"(0) A A west/up",
			"(0) A S west",
			"(0) A A west/down",
			"(0) S S",
			"(0) S A /up",
			"(0) S A /up",
			"(0) S A /up",
			"(0) A A east/up",
			"(0) S S",
			"(0) S A /down",
			"(0) A S east",
			"(0) A S east",
			"(0) S A /up",
			"(0) A A west/up",
			"(0) S S",
			"(0) A A east/down",
			"(0) K S east",
			"(0) S A /up",
			"(0) A A east/up",
			"(0) S S",
			"(0) S S",
			"(0) A S west",
			"(0) A S west",
			"(0) A S west",
			"(0) A S west",
			"(0) A A west/up",
			"(0) A S west",
			"(0) S S",
			"(0) S S",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) S A /up",
			"(0) S S",
			"(0) A S east",
			"(0) A S east",
			"(0) A S east",
			"(0) S A /down",
			"(0) S S",
			"(0) A S east",
			"(0) A A east/down",
			"(0) A A east/down",
			"(0) S S",
			"(0) S S",
			"(0) A A west/down",
			"(0) S S",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/down",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/down",
			"(0) K S east",
			"(0) A S east",
			"(0) K S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/up",
			"(0) A S east",
			"(0) K S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/down",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/up",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) A S east",
			"(0) K A east/up",
			"(0) K A east/up",
			"(0) K S east",
			"(0) K A east/down",
			"(0) K S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/down",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) A A east/down",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K A east/down",
			"(0) K A east/down",
			"(0) K A east/down",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) A S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/down",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/up",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) A A east/down",
			"(0) S A /down",
			"(0) A S west",
			"(0) A A west/down",
			"(0) A S west",
			"(0) S S",
			"(0) S S",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) S S",
			"(0) A A east/down",
			"(0) A S east",
			"(0) S A /up",
			"(0) S S",
			"(0) S A /down",
			"(0) A S east",
			"(0) S S",
			"(0) S S",
			"(0) A S west",
			"(0) A S west",
			"(0) K A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A K west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) S S",
			"(0) A A east/up",
			"(0) S A /up",
			"(0) S S",
			"(0) A S west",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) S A /up",
			"(0) A S west",
			"(0) A S west",
			"(0) A S west",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) S A /up",
			"(0) A S east",
			"(0) S A /down",
			"(0) S A /down",
			"(0) S S",
			"(0) A S west",
			"(0) A S west",
			"(0) S A /down",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) A S east",
			"(0) A A east/down",
			"(0) A A east/down",
			"(0) A S east",
			"(0) S A /up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) S A /up",
			"(0) S S",
			"(0) S A /down",
			"(0) A A west/down",
			"(0) A S west",
			"(0) S A /up",
			"(0) S S",
			"(0) S S",
			"(0) A A east/down",
			"(0) A A east/down",
			"(0) A A east/down",
			"(0) S S",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) A K east/up",
			"(0) A S east",
			"(0) A A east/up",
			"(0) K S east",
			"(0) K A east/down",
			"(0) K A east/down",
			"(0) K S east",
			"(0) K S east",
			"(0) K A east/up",
			"(0) K S east",
			"(0) A S east",
			"(0) K S east",
			"(0) A S east",
			"(0) A A east/up",
			"(0) S S",
			"(0) A S west",
			"(0) S A /up",
			"(0) S A /up",
			"(0) S A /up",
			"(0) S A /up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) K K west/up",
			"(0) K A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) K A west/up",
			"(0) A S west",
			"(0) A A west/down",
			"(0) S S",
			"(0) A A east/down",
			"(0) S A /down",
			"(0) S S",
			"(0) A A east/down",
			"(0) S S",
			"(0) A S east",
			"(0) A A east/up",
			"(0) S S",
			"(0) A A east/up",
			"(0) S S",
			"(0) A A east/up",
			"(0) A S east",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) S S",
			"(0) A S east",
			"(0) S A /down",
			"(0) S A /down",
			"(0) A S west",
			"(0) S A /up",
			"(0) A A east/up",
			"(0) A A east/up",
			"(0) S S",
			"(0) S S",
			"(0) A S east",
			"(0) A A east/down",
			"(0) A S east",
			"(0) S S",
			"(0) A A west/down",
			"(0) A A west/down",
			"(0) A A west/down",
			"(0) S S",
			"(0) A A east/down",
			"(0) A S east",
			"(0) S S",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) K A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) K A west/up",
			"(0) A A west/up",
			"(0) A A west/up",
			"(0) A S west",
			"(0) S S",
			"(0) A A west/up",
			"(0) A S west",
			"(0) A A west/up",
			"(0) A S west",
			"(0) A A west/down",
			"(0) A S west",
			"(0) A S west",
			"(0) A A west/up",
			"(0) A S west",
			"(0) S A /up",
			"(0) A A east/up",
			"(0) A S east",
			"(0) S S",
			"(0) S A /down",
			"(0) S S",
			"(0) A A west/down",
			"(0) S S",
			"(0) A S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) A S east",
			"(0) A S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east",
			"(0) K S east"
		}
			};
}

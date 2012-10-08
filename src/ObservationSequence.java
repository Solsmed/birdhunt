import java.util.Vector;

public class ObservationSequence {
	protected int T;
	protected int[] action;
	protected int[] movement;
	
	private int NUM_KEEP_W = 0;
	private int NUM_KEEP_E = 0;

	Vector<Action> originalSequence;
	
	public ObservationSequence(int[] O) {
		action = O;
		movement = new int[T];
		T = O.length;
		originalSequence = null;
	}
	
	public ObservationSequence(Vector<Action> sequence) {
		originalSequence = sequence;
		
		T = sequence.size();
		action = new int[T];
		movement = new int[T];
		
		updateStats();
		
		//System.out.println(this);		
	}
	
	protected void updateStats() {
		for(int a = 0; a < T; a++) {
			Action ca = originalSequence.get(a);
			action[a] = ca.GetHAction() * 3 + ca.GetVAction();
			movement[a] = ca.GetMovement();
			
			if(action[a] / 3 == Action.ACTION_KEEPSPEED)
				if((movement[a] & Action.MOVE_EAST) != 0)
					NUM_KEEP_E++;
				else if ((movement[a] & Action.MOVE_WEST) != 0)
					NUM_KEEP_W++;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(int t = 0; t < T; t++) {
			sb.append(actionToString(originalSequence.get(t)));
			sb.append('\n');
		}
		return sb.toString();
	}
	
	public int getMigrationDirection() {
		if(NUM_KEEP_W > NUM_KEEP_E)
			return 0;
		else
			return 1;
	}
	
	public static String actionToString(Action a) {
		if(a == null)
			return "null";
		
		StringBuffer s = new StringBuffer();
		
		s.append("(" + a.GetBirdNumber() + ") ");
		
		//s.append("H:");
		switch(a.GetHAction()) {
		case Action.ACTION_KEEPSPEED:
			s.append("K ");
			break;
		case Action.ACTION_ACCELERATE:
			s.append("A ");
			break;
		case Action.ACTION_STOP:
			s.append("S ");
			break;
		}
		
		//s.append(", V:");
		switch(a.GetVAction()) {
		case Action.ACTION_KEEPSPEED:
			s.append("K ");
			break;
		case Action.ACTION_ACCELERATE:
			s.append("A ");
			break;
		case Action.ACTION_STOP:
			s.append("S ");
			break;
		}
		
		//s.append(", M:");
		int move = a.GetMovement();
		if((move & Action.MOVE_WEST) != 0)
			s.append("west");
		else if ((move & Action.MOVE_EAST) != 0)
			s.append("east");
		
		if((move & Action.MOVE_UP) != 0)
			s.append("/up");
		else if ((move & Action.MOVE_DOWN) != 0)
			s.append("/down");
		
		return s.toString();
	}
}

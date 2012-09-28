import java.util.Vector;

public class ObservationSequence {
	//protected int[] H_action;
	//protected int[] V_action;
	protected int[] action;
	protected int[] movement;
	
	private int NUM_KEEP_W = 0;
	private int NUM_KEEP_E = 0;

	protected int T;
	
	public ObservationSequence(Vector<Action> sequence) {
		T = sequence.size();
		
		//H_action = new int[T];
		//V_action = new int[T];
		action = new int[T];
		movement = new int[T];
		
		for(int a = 0; a < T; a++) {
			Action ca = sequence.get(a);
			//H_action[a] = ca.GetHAction();
			//V_action[a] = ca.GetVAction();
			action[a] = ca.GetHAction() * 3 + ca.GetVAction();
			movement[a] = ca.GetMovement();
			
			//if(H_action[a] == Action.ACTION_KEEPSPEED)
			if(action[a] / 3 == Action.ACTION_KEEPSPEED)
				if((movement[a] & Action.MOVE_EAST) != 0)
					NUM_KEEP_E++;
				else if ((movement[a] & Action.MOVE_WEST) != 0)
					NUM_KEEP_W++;
		}
		
		/*
		StringBuffer sb = new StringBuffer();
		for(int t = 0; t < T; t++) {
			sb.append(actionToString(sequence.get(t)));
			sb.append('\n');
		}
		System.out.println(sb.toString());
		*/
		
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

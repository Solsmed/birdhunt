import java.util.Vector;

/**
 * @author Solsmed
 *
 */
public class ObservationSequence {
	protected int N = Player.N;
	protected int M = Player.M;
	
	protected double alpha[][];
	protected double beta[][];
	protected double c[];
	protected double gamma[][];
	protected double diGamma[][][];
	
	protected static final int H_K_W = 0;
	protected static final int H_K_E = 1;
	protected static final int H_A_W = 2;
	protected static final int H_A_E = 3;
	protected static final int H_S = 4;
	
	protected static final int V_K_U = 0;
	protected static final int V_K_D = 1;
	protected static final int V_A_U = 2;
	protected static final int V_A_D = 3;
	protected static final int V_S = 4;
	
	protected int[] sequence;
	
	protected int T;
	
	public ObservationSequence(Vector<Action> sequence, boolean isHorizontal) {
		this.sequence = new int[sequence.size()];
		for(int a = 0; a < sequence.size(); a++) {
			int action;
			if(isHorizontal)
				action = sequence.get(a).GetHAction();
			else 
				action = sequence.get(a).GetVAction();
			
			if(action == Action.ACTION_STOP) {
				if(isHorizontal)
					this.sequence[a] = H_S;
				else
					this.sequence[a] = V_S;
				continue;
			}
			
			int movement = sequence.get(a).GetMovement();

			if(isHorizontal)
				if((movement & Action.MOVE_WEST) > 0)
					if(action == Action.ACTION_KEEPSPEED)
						this.sequence[a] = H_K_W;
					else
						this.sequence[a] = H_A_W;
				else
					if(action == Action.ACTION_KEEPSPEED)
						this.sequence[a] = H_K_E;
					else
						this.sequence[a] = H_A_E;
			else
				if((movement & Action.MOVE_UP) > 0)
					if(action == Action.ACTION_KEEPSPEED)
						this.sequence[a] = V_K_U;
					else
						this.sequence[a] = V_A_U;
				else
					if(action == Action.ACTION_KEEPSPEED)
						this.sequence[a] = V_K_D;
					else
						this.sequence[a] = V_A_D;
		}
		
		T = this.sequence.length;
		
		alpha = new double[T][N];
		beta = new double[T][N];
		c = new double[T];
		gamma = new double[T][N];
		diGamma = new double[T][N][N];
	}
	
	public int predictHaction() {
		double maxProb = Double.NEGATIVE_INFINITY;
		int maxState = -1;
		
		for(int i = 0; i < N; i++) {
			if(alpha[T-1][i] > maxProb) {
				maxProb = alpha[T-1][i];
				maxState = i;
			}
		}
		
		return maxState;
		/*
		double[] nextStateProb = new double[N];
		
		for(int j = 0; j < N; j++) {
			double sum = 0;
			for(int i = 0; i < N; i++) {
				sum += diGamma[T-1][i][j];
			}
			nextStateProb[j] = sum;
		}
		
		int maxI = -1;
		double maxProb = Double.NEGATIVE_INFINITY;
		for(int j = 0; j < N; j++) {
			if(nextStateProb[j] > maxProb) {
				maxProb = nextStateProb[j];
				maxI = j;
			}
		}
		
		return maxI;
		*/
	}
}

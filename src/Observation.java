import java.util.Vector;

/**
 * @author Solsmed
 *
 */
public class Observation {
	protected int N = 4;
	protected int M = 3;
	
	protected double alpha[][];
	protected double beta[][];
	protected double c[];
	protected double gamma[][];
	protected double diGamma[][][];
	
	protected int[] Hsequence;
	protected int[] Vsequence;
	
	protected int T;
	
	public Observation(Vector<Action> Hsequence) {
		this.Hsequence = new int[Hsequence.size()];
		this.Vsequence = new int[Hsequence.size()];
		for(int a = 0; a < Hsequence.size(); a++) {
			this.Hsequence[a] = Hsequence.get(a).GetHAction();
			this.Vsequence[a] = Hsequence.get(a).GetVAction();
		}
		
		T = this.Hsequence.length;
		
		alpha = new double[T][N];
		beta = new double[T][N];
		c = new double[T];
		gamma = new double[T][N];
		diGamma = new double[T][N][N];
	}
	
	public int predictHaction() {
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
	}
}

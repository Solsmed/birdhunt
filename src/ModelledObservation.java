import java.util.Vector;

/**
 * @author Solsmed
 *
 */
public class ModelledObservation {
	protected int N = 3;
	protected int M = 3;
	
	protected double H_gamma[][];
	protected double H_diGamma[][][];
	
	protected double V_gamma[][];
	protected double V_diGamma[][][];
	
	int birdNumber;
	
    public static final String[] labels = new String[] {"Mi", "Qu", "Pa", "FD"};
    
    public static final double[][] BinitH = new double[][]
    	//	  K     A      s
    		{{1.00, 0.00, 0.00},  // M
			 {0.40, 0.30, 0.30},  // Q
			 {0.30, 0.70, 0.00},  // P
			 {0.00, 0.00, 1.00}}; // F
    
    public static final double[][] BinitV = new double[][]
        //	  K     A      s
    		{{0.00, 0.15, 0.85},  // M
			 {0.40, 0.30, 0.30},  // Q
			 {0.30, 0.70, 0.00},  // P
			 {0.00, 1.00, 0.00}}; // F  
	
	BirdModel lambda;
	ObservationSequence O;
	
	public ModelledObservation(ObservationSequence O) {
		this.lambda = HMMFunction.getInitModel(N, M);//getLabelledModel(O);
		this.O = O;
		
		H_gamma = new double[O.T][N];
		H_diGamma = new double[O.T][N][N];
		
		V_gamma = new double[O.T][N];
		V_diGamma = new double[O.T][N][N];
	}
	
	protected void iterateOnce() {
		if(!lambda.isOptimal) {
	    	double oldLogProb = lambda.logProb;
	    	
			lambda = HMMFunction.getRefinedModel(this);	
					
	    	if(lambda.logProb < oldLogProb)
	    		lambda.isOptimal = true;
		}
	}

	public Action predictDiscreetAction() {
		double[] HstateT = getProbStateT(lambda.A, H_diGamma);
		double[] VstateT = getProbStateT(lambda.A, V_diGamma);
		
		// make discreet
		int maxIndexH = 0;
		int maxIndexV = 0;
		double maxH = 0;
		double maxV = 0;
		for(int k = 0; k < M; k++) {
			if(HstateT[k] > maxH) {
				maxH = HstateT[k];
				maxIndexH = k;
			}
			if(VstateT[k] > maxV) {
				maxV = VstateT[k];
				maxIndexV = k;
			}
		}
		
		// resolve conflict
		int Tstate;
		if(maxIndexH != maxIndexV) {
			if(HstateT[maxIndexH] > VstateT[maxIndexV])
				Tstate = maxIndexH;
			else
				Tstate = maxIndexV;				
		} else
			Tstate = maxIndexH;
		
		// get most probable action for our state
		int Haction = 0;
		int Vaction = 0;
		maxH = 0;
		maxV = 0;
		for(int k = 0; k < M; k++) {
			if(lambda.Bh[Tstate][k] > maxH) {
				maxH = lambda.Bh[Tstate][k];
				Haction = k;
			}
			if(lambda.Bv[Tstate][k] > maxV) {
				maxV = lambda.Bv[Tstate][k];
				Vaction = k;
			}
		}
		
		return new Action(birdNumber, Haction, Vaction, getLastMovement());
	}
	
	public Action predictFuzzyAction() {
		double[] HstateT = getProbStateT(lambda.A, H_diGamma);
		double[] VstateT = getProbStateT(lambda.A, V_diGamma);
		
		// fuzzy observations
		double[] obsH = weightedObservations(HstateT, lambda.Bh);
		double[] obsV = weightedObservations(VstateT, lambda.Bv);
		
		int maxIndexH = 0;
		int maxIndexV = 0;
		double maxH = 0;
		double maxV = 0;
		for(int k = 0; k < M; k++) {
			if(obsH[k] > maxH) {
				maxH = obsH[k];
				maxIndexH = k;
			}
			if(obsV[k] > maxV) {
				maxV = obsV[k];
				maxIndexV = k;
			}
		}
			
		return new Action(birdNumber, maxIndexH, maxIndexV, getLastMovement());

	}
	
	private int getLastMovement() {
		for(int t = O.T-1; t >= 0; t--) {
			if(O.movement[t] > 0)
				return O.movement[t];
		}
		return 0;
	}
	
	// return fuzzy state vector for time T
	private double[] getProbStateT(double[][] A, double[][][] diGamma) {
		// calculate gamma[T-1]
		double[] predictedHGamma = new double[N];
		for(int j = 0; j < N; j++) {
			double jProb = 0;
			for(int i = 0; i < N; i++) {
				jProb += diGamma[O.T-2][i][j];
			}
			predictedHGamma[j] = jProb;
		}
		
		// Calculate prability-state vector at T, by doing [[A]]*[predictedHGamma]
		double[] stateT = new double[N];
		for(int i = 0; i < N; i++) {
			double sum = 0;
			for(int j = 0; j < N; j++) {
				sum += A[i][j] * predictedHGamma[j];
			}
			stateT[i] = sum;
		}
		
		return stateT;
	}
	
	// return fuzzy observations, given fuzzy state vector
	private double[] weightedObservations(double[] state, double[][] B) {
		double[] weightedObservations = new double[M];
		
		for(int k = 0; k < M; k++) {
			double sum = 0;
			for(int i = 0; i < N; i++) {
				sum += B[i][k] * state[i];
			}
			weightedObservations[k] = sum;
		}
		
		return weightedObservations;
	}
	
	public boolean refineModel(long deadlineTime) {
		// refine models
    	long start, stop;
    	long lastIterTime = 0;
    	    	
		while(System.currentTimeMillis() < deadlineTime - 1.05*lastIterTime && !lambda.isOptimal) {
			start = System.currentTimeMillis();
    		
			iterateOnce();
	    	
	    	stop = System.currentTimeMillis();
	    	lastIterTime = stop - start;
		}
				
		return lambda.isOptimal;
	}
}

/**
 * @author Solsmed
 *
 */
public class ModelledObservation {
	private int N;
	private int M;
	
	protected double gamma[][];
	protected double diGamma[][][];
    
	BirdModel lambda;
	ObservationSequence O;
	
	int birdNumber;
	int species = Duck.SPECIES_UNKNOWN;
	double speciesCertainty = 0;
	
	double lastProbability;
	
    //public static final String[] labels = new String[] {"Mi", "Qu", "Pa", "FD"};
    
    private static final double[][] BinitH = new double[][]
    	//	  K     A      S
    		{{1.00, 0.00, 0.00},  // M
			 {0.40, 0.30, 0.30},  // Q
			 {0.30, 0.70, 0.00},  // P
			 {0.05, 0.05, 0.90}}; // F
    
    private static final double[][] BinitV = new double[][]
        //	  k     a      s
    		{{0.00, 0.15, 0.85},  // M
			 {0.40, 0.30, 0.30},  // Q
			 {0.30, 0.70, 0.00},  // P
			 {0.19, 0.80, 0.01}}; // F  
	
    public static double[][] getJointInitB() {
    	int N = BinitH.length;
    	int M = BinitH[0].length * BinitV[0].length;
    	
    	double[][] jointInitB = new double[N][M];
    	
    	for(int i = 0; i < N; i++) {
    		for(int k = 0; k < M; k++) {
    			jointInitB[i][k] = BinitH[i][k / 3] * BinitV[i][k % 3];
    		}
    	}
    	
    	return jointInitB;
    }
	
    /**
     * Creates a ModelledObservation of O and guess. A/B/pi in guess will only be used if A/B/pi is null respectively.
     * 
     * @param O
     * @param guess
     */
    public ModelledObservation(ObservationSequence O, BirdModel guess) {
    	this(O, guess.N, guess.M);
    	
    	if(guess != null) {
    		if(guess.A != null)
    			this.lambda.A = guess.A;
    		if(guess.B != null)
    			this.lambda.B = guess.B;
    		if(guess.pi != null)
    			this.lambda.pi = guess.pi;
    	}
    	
    	N = guess.N;
    	M = guess.M;
    }
    
    /**
     * Creates a ModelledObservation of O using standard initialisation.
     * 
     * @param O
     */
	public ModelledObservation(ObservationSequence O, int N, int M) {
		this.lambda = HMMFunction.getInitBirdModel(N, M);
		
		this.N = N;
		this.M = M;
		
		setObservation(O);
	}
	
	public void setObservation(ObservationSequence O) {
		this.O = O;
		gamma = new double[O.T][N];
		diGamma = new double[O.T][N][N];
	}
	
	protected void iterateOnce() {
		if(!lambda.isOptimal) {
	    	double oldLogProb = lambda.logProb;
	    	
	    	//System.out.println("refining...");
			lambda = HMMFunction.getRefinedModel(this);	
					
	    	if(lambda.logProb < oldLogProb)
	    		lambda.isOptimal = true;
		}
	}

	public Action predictDiscreetAction() {
		double[] stateT = getProbStateT(lambda.A, diGamma);
		
		// calculate most probable discreet state
		int probState = 0;
		double maxState = 0;
		for(int k = 0; k < N; k++) {
			if(stateT[k] > maxState) {
				maxState = stateT[k];
				probState = k;
			}
		}
		
		// get most probable action for our state
		int action = 0;
		double maxAction = 0;
		for(int k = 0; k < M; k++) {
			if(lambda.B[probState][k] > maxAction) {
				maxAction = lambda.B[probState][k];
				action = k;
			}
		}
		
		lastProbability = maxAction * maxState;
		return new Action(birdNumber, action / 3, action % 3, getLastMovement());
	}
	
	/*
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
	*/
	
	/*
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
	*/
	
	public Action predictFuzzyAction() {
		double[] stateT = getProbStateT(lambda.A, diGamma);
		
		// fuzzy observations
		double[] obs = weightedObservations(stateT, lambda.B);
		
		int maxIndex = 0;
		double max = 0;
		for(int k = 0; k < M; k++) {
			if(obs[k] > max) {
				max = obs[k];
				maxIndex = k;
			}
		}
			
		return new Action(birdNumber, maxIndex / 3, maxIndex % 3, getLastMovement());
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
		double[] predictedGamma = new double[N];
		for(int j = 0; j < N; j++) {
			double jProb = 0;
			for(int i = 0; i < N; i++) {
				jProb += diGamma[O.T-2][i][j];
			}
			predictedGamma[j] = jProb;
		}
		
		// Calculate prability-state vector at T, by doing [predictedHGamma]*[[A]]
		double[] stateT = MatrixMath.multi(predictedGamma, A);
			
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
	
	public int getN() { return N; }
	
	public int getM() { return M; }
}

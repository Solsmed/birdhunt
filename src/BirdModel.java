/**
 * @author Solsmed
 *
 */
public class BirdModel {
	private int N;
	private int M;
	
	protected double alpha[][];
	protected double gamma[][];
	protected double diGamma[][][];
	protected double c[];
    
	public MarkovModel lambda;
	ObservationSequence O;
	
	int birdNumber;
	
	double lastProbability;
	int permutation;
	int timesShotAt = 0;
	//int speciesVotes[] = new int[4];
	
    //public static final String[] labels = new String[] {"Mi", "Qu", "Pa", "FD"};
	
    /**
     * Creates a ModelledObservation of O and guess. A/B/pi in guess will only be used if A/B/pi is null respectively.
     * 
     * @param O
     * @param guess
     */
    public BirdModel(ObservationSequence O, MarkovModel guess) {
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
	public BirdModel(ObservationSequence O, int N, int M) {
		this.lambda = HMMFunction.getInitBirdModel(N, M);
		
		this.N = N;
		this.M = M;
		
		if(O != null)
			setObservation(O);
	}
	
	public void setObservation(ObservationSequence O) {
		this.O = O;
		alpha = new double[O.T][N];
		c = new double[O.T];
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
	
	public Action predictAction() {
		double[][] alphaPossibleFutureProb = new double[N][M];
		
		double[] alphaFutureFactorA = new double[N];
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				alphaFutureFactorA[i] += (alpha[O.T-1][j] / c[O.T-1])*lambda.A[j][i];
		
		for(int i = 0; i < N; i++) {
			for(int Ofuture = 0; Ofuture < M; Ofuture++) {
				alphaPossibleFutureProb[i][Ofuture] = alphaFutureFactorA[i] * lambda.B[i][Ofuture];
			}
		}
		
		int maxState = -1;
		int maxEmission = -1;
		double maxProb = 0;
		
		for(int i = 0; i < N; i++)
			for(int j = 0; j < M; j++)
				if(alphaPossibleFutureProb[i][j] > maxProb) {
					maxProb = alphaPossibleFutureProb[i][j];
					maxState = i;
					maxEmission = j;
				}
		
		this.lastProbability = maxProb;
		
		return new Action(this.birdNumber, maxEmission / 3, maxEmission % 3, getLastMovement());
	}

	public Action predictDiscreteAction() {
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
    	
    	int maxIter = 400;
    	int iter = 0;
		while(System.currentTimeMillis() < deadlineTime - 1.05*lastIterTime && !lambda.isOptimal && iter < maxIter) {
			start = System.currentTimeMillis();
    		
			iterateOnce();
	    	iter++;
	    	
	    	stop = System.currentTimeMillis();
	    	lastIterTime = stop - start;
		}
		
		//System.out.println("(" + birdNumber + ") Iterations: " + iter);
		
		return lambda.isOptimal;
	}
	
	public boolean baumWelchTrain(int maxIter) {
		lambda = BaumWelch.train(this, maxIter);
		return lambda.isOptimal;
	}
	
	public int getN() { return N; }
	
	public int getM() { return M; }
	/*
	public void voteSpecies(int s) {
		this.speciesVotes[s]++;
	}
	
	public int getSpecies() {
		int max = -1;
		int mySpecies = -1;
		for(int s = 0; s < this.speciesVotes.length; s++)
			if(this.speciesVotes[s] > max) {
				max = this.speciesVotes[s];
				mySpecies = s;
			}
		
		return mySpecies;
	}
	
	public double getSpeciesConfidence() {
		int mySpecies = getSpecies();
		double sum = 0;
		for(int s = 0; s < this.speciesVotes.length; s++)
			sum += this.speciesVotes[s];
		
		return this.speciesVotes[mySpecies] / sum;
	}
	*/
}

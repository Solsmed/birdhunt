
public class Estimator {
	public static Model2D getLabelledModel(String[] labels, double[][] BinitH, double[][] BinitV, ObservationSequence2D O) {
		// Initialise
		int BigN = BinitH.length;
		int N = 3;
		int numPermutations = 4; // Math.chooseUnordered(BigN, N)
		
		int M = BinitH[0].length;
		
		Model2D m[] = new Model2D[numPermutations];
		for(int p = 0; p < numPermutations; p++) {
			Model H = HMMFunction.getInitModel(N, M);
			Model V = HMMFunction.getInitModel(N, M);
			m[p] = new Model2D(H, V);
		}
		
		int notIndex = 0;
		
		for(int p = 0; p < numPermutations; p++) {
			int mIndex = 0;
			m[p].H.stateLabels = new String[N];
			m[p].V.stateLabels = new String[N];
			for(int i = 0; i < BigN; i++) {
				if(i != notIndex) {
					System.arraycopy(BinitH[i], 0, m[p].H.B[mIndex], 0, M);
					System.arraycopy(BinitV[i], 0, m[p].V.B[mIndex], 0, M);
					m[p].H.stateLabels[mIndex] = labels[i];
					m[p].V.stateLabels[mIndex] = labels[i];
					mIndex++;
				}
			}
			notIndex++;
		}
		
		boolean allDone = false;
		int iter = 0;
		int maxIter = 30;
		while(!allDone && iter < maxIter) {
			allDone = true;
			iter++;
			for(int p = 0; p < numPermutations; p++) {
				iterateOnce(m[p], O);
				System.out.print(String.format("%.2f\t%.2f\t", m[p].H.logProb, m[p].V.logProb));
				allDone = allDone && m[p].isOptimal(); 
			}
			System.out.println();
		}
		
		double maxSumLogProb = Double.NEGATIVE_INFINITY;
		int mostProbableModelIndex = -1;
		
		for(int p = 0; p < numPermutations; p++) {
			double sumProb = m[p].H.logProb + m[p].V.logProb;
			if(sumProb > maxSumLogProb) {
				maxSumLogProb = sumProb;
				mostProbableModelIndex = p;
			}
		}
		
		return m[mostProbableModelIndex];
	}
	
	public static boolean refineModelLoop(Model2D lambda, ObservationSequence2D seq, long deadlineTime) {
		// refine models
    	long start, stop;
    	long lastIterTime = 0;
    	
		while(System.currentTimeMillis() < deadlineTime - 1.05*lastIterTime && !lambda.isOptimal()) {
			start = System.currentTimeMillis();
    		
			iterateOnce(lambda, seq);
	    	
	    	stop = System.currentTimeMillis();
	    	lastIterTime = stop - start;
		}
		
		return lambda.isOptimal();
	}
	
	private static void iterateOnce(Model2D lambda, ObservationSequence2D seq) {
		// Horizontal iteration
		if(!lambda.H.isOptimal) {
	    	lambda.H = HMMFunction.refineModel(lambda.H, seq.H);	
	    	
	    	double logProbH = 0;
	    	for(int t = 0; t < seq.H.T; t++) {
	    		logProbH += Math.log(seq.H.c[t]);
	    	}
	    	logProbH = -logProbH;
	    	
	    	if(logProbH > lambda.H.logProb)
	    		lambda.H.logProb = logProbH;
	    	else
	    		lambda.H.isOptimal = true;
		}
    	
    	// Vertical iteration
    	if(!lambda.V.isOptimal) {
	    	lambda.V = HMMFunction.refineModel(lambda.V, seq.V);
	    	
	    	double logProbV = 0;
	    	for(int t = 0; t < seq.V.T; t++) {
	    		logProbV += Math.log(seq.V.c[t]);
	    	}
	    	logProbV = -logProbV;
	    	
	    	if(logProbV > lambda.V.logProb)
	    		lambda.V.logProb = logProbV;
	    	else
	    		lambda.V.isOptimal = true;
    	}
	}
	
	public static Action predictAction(Model2D lambda, ObservationSequence2D O) {
		int bird = lambda.birdNumber;
		int actionH = O.H.predictAction();
		int actionV = O.V.predictAction();
		int movement = 0;
		
		switch(actionH) {
		case ObservationSequence.H_S:
			actionH = Action.ACTION_STOP;
			movement = movement | Action.BIRD_STOPPED;
			break;
		case ObservationSequence.H_A_E:
			actionH = Action.ACTION_ACCELERATE;
			movement = movement | Action.MOVE_EAST;
			break;
		case ObservationSequence.H_A_W:
			actionH = Action.ACTION_ACCELERATE;
			movement = movement | Action.MOVE_WEST;
			break;
		case ObservationSequence.H_K_E:
			actionH = Action.ACTION_KEEPSPEED;
			movement = movement | Action.MOVE_EAST;
			break;
		case ObservationSequence.H_K_W:
			actionH = Action.ACTION_KEEPSPEED;
			movement = movement | Action.MOVE_WEST;
			break;
		}
		
		switch(actionV) {
		case ObservationSequence.V_S:
			actionH = Action.ACTION_STOP;
			movement = movement | Action.BIRD_STOPPED;
			break;
		case ObservationSequence.V_A_D:
			actionH = Action.ACTION_ACCELERATE;
			movement = movement | Action.MOVE_DOWN;
			break;
		case ObservationSequence.V_A_U:
			actionH = Action.ACTION_ACCELERATE;
			movement = movement | Action.MOVE_UP;
			break;
		case ObservationSequence.V_K_D:
			actionH = Action.ACTION_KEEPSPEED;
			movement = movement | Action.MOVE_DOWN;
			break;
		case ObservationSequence.V_K_U:
			actionH = Action.ACTION_KEEPSPEED;
			movement = movement | Action.MOVE_UP;
			break;
		}
		
		return new Action(bird, actionH, actionV, movement);
	}
}

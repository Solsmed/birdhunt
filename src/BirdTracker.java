import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


public class BirdTracker {
	private int popSize;
	
	boolean[] knownBirds; 
    BirdModel[] birdModels;
    Set<BirdModel> killList;
    Vector<Integer> alive;
    Set<BirdModel> aliveAndIdentifiedBirds;
    final int TRACK_LIMIT = 20;
    double[][] distances;
    int[] typesCount = new int[4]; // white coloured black
    boolean blackHawkDown = false;
    Set<Integer> blacklist;
    
    private int[] getAllUnknownBirdIndices() {
    	Vector<Integer> unknownIndices = new Vector<Integer>();
    	for(int i = 0; i < popSize; i++) {
    		if(!knownBirds[i])
    			unknownIndices.add(i);
    	}
    	int[] indices = new int[unknownIndices.size()];
    	for(int i = 0; i < indices.length; i++) {
    		indices[i] = unknownIndices.get(i);
    	}
    	
    	return indices;
    }
        
	private static final double[] piInit = new double[] {1.0/3, 1.0/3, 1.0/3};
	
	private static final double[][] Ainit = new double[][]
			{
		{0.88, 0.06, 0.06},
		{0.06, 0.88, 0.06},
		{0.06, 0.06, 0.88},
			};
    /*
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
	*/
	private static final int PFM = 0;
	private static final int PFQ = 1;
	private static final int PMQ = 2;
	private static final int FMQ = 3;
	
	private static final double[][] Bstates = new double[][]
			{
// Horizontal
//    ===
//	Vertical
//
//	
//				A                 K                 S
//        =============     =============     =============
//		  A     K     S     A     K     S     A     K     S
		{0.61, 0.15, 0.01, 0.14, 0.04, 0.00, 0.00, 0.00, 0.05}, // Panicking
		{0.04, 0.01, 0.00, 0.00, 0.00, 0.00, 0.77, 0.18, 0.00}, // Feign Death
		{0.03, 0.00, 0.43, 0.06, 0.00, 0.45, 0.01, 0.00, 0.02}, // Migrating
		{0.12, 0.15, 0.00, 0.15, 0.50, 0.00, 0.00, 0.00, 0.08}, // Quacking
			};
	
	private static final MarkovModel[] PATTERNS = new MarkovModel[] {
    		new MarkovModel(Ainit, new double[][] {
			Bstates[0], // Panicking
			Bstates[1], // Feign Death
			Bstates[2], // Migrating
    		}, piInit),
    		
    		new MarkovModel(Ainit, new double[][] {
			Bstates[0], // Panicking
			Bstates[1], // Feign Death
			Bstates[3], // Quacking
    		}, piInit),
    		
    		new MarkovModel(Ainit, new double[][] {
			Bstates[0], // Panicking
			Bstates[2], // Migrating
			Bstates[3], // Quacking
    		}, piInit),
    		
    		new MarkovModel(Ainit, new double[][] {
			Bstates[1], // Feign Death
			Bstates[2], // Migrating
			Bstates[3], // Quacking
    		}, piInit),
	};
    /*
    public static double[][] getJointInitB() {
    	/*
    	int N = BinitH.length;
    	int M = BinitH[0].length * BinitV[0].length;
    	
    	double[][] jointInitB = new double[N][M];
    	
    	for(int i = 0; i < N; i++) {
    		for(int k = 0; k < M; k++) {
    			jointInitB[i][k] = BinitH[i][k / 3] * BinitV[i][k % 3];
    		}
    	}
    	
    	return jointInitB;
    }    */
    
    boolean spreeMode = true;
    
	public BirdTracker(int numBirds) {
		popSize = numBirds;

		killList = new HashSet<BirdModel>(numBirds);
		aliveAndIdentifiedBirds = new HashSet<BirdModel>(numBirds);
    	
    	distances = new double[numBirds][numBirds];
    	
    	knownBirds = new boolean[numBirds];
    	
    	birdModels = new BirdModel[numBirds];
    	for(int b = 0; b < numBirds; b++) {
    		birdModels[b] = new BirdModel(null, 3, 9);
    	}
    	
    	blacklist = new HashSet<Integer>();
    	
    	alive = new Vector<Integer>();
	}
	
	public void studyBirds(State pState, Deadline pDue) {
		// instinct
		int SPREE_BREAK = 150;
		if(pState.GetDuck(0).mSeq.size() < SPREE_BREAK) {
			spreeMode = true;
			
			killList.clear();
			
			for(int b = 0; b < popSize; b++) {
				ObservationSequence flight = new ObservationSequence(pState.GetDuck(b).mSeq);
				int movement = 0;
				if (isFeigningDeath(flight) != 0 || isMigrating(flight) != 0) {
					BirdModel bird = new BirdModel(flight, 3, 9);
					bird.birdNumber = b;
					killList.add(bird);
				}
				
				if(pDue.TimeUntil() < 30)
					return;
			}
			
			return;
		}
		
		spreeMode = false;
		
		alive.clear();
		for(int b = 0; b < popSize; b++)
			if(pState.GetDuck(b).IsAlive())
				alive.add(b);
		
		if(pState.GetDuck(0).mSeq.size() == SPREE_BREAK)
			System.out.println("================================ (" + alive.size() + " left) Score: " + pState.MyScore() + "" + (blackHawkDown ? " and Black Bird is down." : ""));
		
		

		// Model some still-unknown birds
		// add to "aliveAndIdentifiedBirds"
		int[] unknownIndices = getAllUnknownBirdIndices();

		int BATCH_LIMIT = 10;
		for(int u = 0; u < unknownIndices.length && u < BATCH_LIMIT; u++) {
			int b = unknownIndices[u];
			ObservationSequence seq = new ObservationSequence(pState.GetDuck(b).mSeq);

			BirdModel[] candidates = new BirdModel[] {
					new BirdModel(seq, PATTERNS[PFM]),
					new BirdModel(seq, PATTERNS[PFQ]),
					new BirdModel(seq, PATTERNS[PMQ]),
					new BirdModel(seq, PATTERNS[FMQ])
					};
			
			double maxProb = 0;
			int bestCandidate = -1;
			for(int c = 0; c < candidates.length; c++) {
				candidates[c].birdNumber = b;
				candidates[c].refineModel(System.currentTimeMillis() + 200);
				double prob = 0;
				for(int i = 0; i < candidates[c].getN(); i++) {
					prob += candidates[c].alpha[candidates[c].O.T-1][i];
				}
				if(prob > maxProb) {
					maxProb = prob;
					bestCandidate = c;
				}
			}

			if(bestCandidate != -1) {
				birdModels[b] = candidates[bestCandidate];
				birdModels[b].permutation = bestCandidate;
				typesCount[bestCandidate]++;
				knownBirds[b] = true;
				aliveAndIdentifiedBirds.add(birdModels[b]);
			}
		}
		
		System.out.print("                         Types: ");
		for(int t = 0; t < 4; t++) {
			System.out.print(typesCount[t] + " ");
		}
		System.out.println();
		
		killList.clear();
		
		Iterator<BirdModel> it = aliveAndIdentifiedBirds.iterator();
		while(it.hasNext()) {
			BirdModel bird = it.next();
			
			//System.out.println("targetting (" + bird.birdNumber + "/" + bird.permutation + ")");
			
			int type = bird.permutation;
			if(typesCount[type] > 3 || blackHawkDown || pState.GetNumDucks() == 1) {
				killList.add(bird);
			}
		}
			
		
		/*
		long lastIterTime = 0;
		long start = 0;
		
		long deadlineTime = 0;
		if(aliveAndIdentifiedBirds.size() > 0)
			deadlineTime = pDue.TimeUntil() / aliveAndIdentifiedBirds.size();
		

		
		deadlineTime += System.currentTimeMillis();
		
		boolean allDone = false;
		

			
			System.out.println("Alive birds: " + aliveBirds.size());
			// populate killList
			Iterator<ModelledObservation> it = aliveBirds.iterator();
			while(it.hasNext()) {
				ModelledObservation bird = it.next();
			
				/*
				if((isMigrating(bird.O) != 0 ||
				    isFeigningDeath(bird.O) != 0)/* &&
				    birdModels[b].species != Duck.SPECIES_UNKNOWN*//*) {

					killList.add(bird);
						}
				*/
				/*
				Action prediction = bird.predictAction();
				System.out.println("Prediction: " + ObservationSequence.actionToString(prediction) + ", Confidence: " + bird.lastProbability);
				if(bird.lastProbability > 0.00) {
					killList.add(bird);
				}
			}
						
			lastIterTime = System.currentTimeMillis() - start;
		}

*/
		/*
		for(int i = 0; i < popSize; i++) {
			System.out.println(matrixString(birdModels[i].lambda.A));
		}
		*/
		/*
		System.out.println();
		System.out.println(distancesString());
		System.out.println();
		*/
		//System.out.println(birdModels[0].lambda);
   	}
   	
	public Action getSuggestedAction() {
		Iterator<BirdModel> it = killList.iterator();
		Action action = Action.cDontShoot;
		boolean shoot = false;
		
		while(it.hasNext()) {
			BirdModel bird = it.next();
			if(spreeMode && !blacklist.contains(bird.birdNumber)) {
				int migrationKill = isMigrating(bird.O);
				int feignKill = isFeigningDeath(bird.O);
				
				if (migrationKill != 0) {
					action = new Action(bird.birdNumber, Action.ACTION_KEEPSPEED, Action.ACTION_STOP, migrationKill);
					shoot = true;
				}
				else if (feignKill != 0) {
					action = new Action(bird.birdNumber, Action.ACTION_STOP, Action.ACTION_ACCELERATE, feignKill);
					shoot = true;
				}
			} else {
				action = bird.predictAction();
				shoot = true;
			}
		}

		if(shoot) {
			if(birdModels[action.mBirdNumber].timesShotAt < 1) {
				birdModels[action.mBirdNumber].timesShotAt++;
			} else {
				blacklist.add(action.mBirdNumber);
				action = Action.cDontShoot;
			}
		}
		
		return action;
	}

	static int isMigrating(ObservationSequence O) {
		int CERTAINTY = 3;
		if(O.T < CERTAINTY + 1)
			return 0;
		
		int currentMove = O.movement[O.T-1] & (Action.MOVE_EAST|Action.MOVE_WEST);
		boolean sameAction = (O.action[O.T-1] / 3 == Action.ACTION_KEEPSPEED) && (O.action[O.T-1] % 3 == Action.ACTION_STOP);
				
		for(int t = O.T-2; t >= (O.T - CERTAINTY) && t >= 0; t--) {
			currentMove = currentMove & (O.movement[t] & (Action.MOVE_EAST|Action.MOVE_WEST));
			sameAction &= ((O.action[t] / 3 == Action.ACTION_KEEPSPEED) && (O.action[O.T-1] % 3 == Action.ACTION_STOP));
		}
		
		if(!sameAction)
			return 0;
		
		return currentMove;
	}
	
	static int isFeigningDeath(ObservationSequence O) {
		int CERTAINTY = 3;
		if(O.T < CERTAINTY + 1)
			return 0;
		
		int currentMove = O.movement[O.T-1] & (Action.MOVE_DOWN);
		boolean sameAction = (O.action[O.T-1] / 3 == Action.ACTION_STOP) && (O.action[O.T-1] % 3 == Action.ACTION_ACCELERATE);
		
		for(int t = O.T-2; t >= O.T - CERTAINTY && t >= 0; t--) {
			currentMove = currentMove & (O.movement[t] & (Action.MOVE_DOWN));
			sameAction &= ((O.action[O.T-1] / 3 == Action.ACTION_STOP) && (O.action[t] % 3 == Action.ACTION_ACCELERATE));
		}
		
		if(!sameAction)
			return 0;
		
		return currentMove;
	}

	public void registerDeadBird(int pDuck, int pSpecies) {
		knownBirds[pDuck] = true;
		
		if(pSpecies == Duck.SPECIES_BLACK) {
			System.out.println("                                      B L A C K   H A W K   D O W N !!!!!!");
			blackHawkDown = true;
		}
		/*
		if(pSpecies == Duck.SPECIES_WHITE)
			confirmedSpeciesCount[0]++;
		else if(pSpecies == Duck.SPECIES_BLACK) {
			confirmedSpeciesCount[2]++;
			System.out.println("                                      B L A C K   H A W K   D O W N !!!!!!");
			blackHawkDown = true;
		}
		else
			confirmedSpeciesCount[1]++;
			*/
		
		aliveAndIdentifiedBirds.remove(birdModels[pDuck]);
	}
	
	private String distancesString() {
		StringBuffer sb = new StringBuffer();
		
		for(int b = 0; b < popSize; b++) {
			for(int c = 0; c < b; c++) {
				if(distances[b][c] > 0.10 || Double.isNaN(distances[b][c])) {
					sb.append(String.format("%.2f ", distances[b][c]));
				} else
					sb.append("  *  ");
			}
			sb.append('\n');
		}
		
		return sb.toString();
	}
}

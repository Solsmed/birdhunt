import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class BirdTracker {
	private int popSize;
	
    ModelledObservation[] birdModels;
    Set<ModelledObservation> killList;
    Set<ModelledObservation> aliveBirds;
    final int TRACK_LIMIT = 20;
    double[][] distances;
	
	public BirdTracker(int numBirds) {
		popSize = numBirds;
		
    	killList = new HashSet<ModelledObservation>(numBirds);
    	aliveBirds = new HashSet<ModelledObservation>(numBirds);
    	
    	distances = new double[numBirds][numBirds];
	}
	
	public void studyBirds(State pState, Deadline pDue) {
		birdModels = new ModelledObservation[popSize];
		for(int b = 0; b < popSize; b++) {
			ObservationSequence seq = new ObservationSequence(pState.GetDuck(b).mSeq);
			birdModels[b] = new ModelledObservation(seq, 4, 9);
		}
		
		if(pState.GetDuck(0).mSeq.size() < 2)
			return;
		
		//System.out.println(birdModels[0].lambda);
		
		long lastIterTime = 0;
		long start = 0;
		
		updateAliveBirds(pState);
		
		long deadlineTime = 0;
		if(aliveBirds.size() > 0)
			deadlineTime = pDue.TimeUntil() / aliveBirds.size();
		
		killList.clear();
		
		deadlineTime += System.currentTimeMillis();
		
		boolean allDone = false;
		
		while(pDue.TimeUntil() > 1.15*lastIterTime && !allDone) {
			allDone = true;
			
			start = System.currentTimeMillis();

			// refine models
			for(int b = 0; b < popSize; b++) {
				boolean isDone = birdModels[b].refineModel(deadlineTime);
				allDone &= isDone;
			}
			
			// classify species
			for(int b = 0; b < popSize; b++) {
				for(int c = 0; c < b; c++) {
					distances[b][c] = distances[c][b] = MatrixMath.matrixNormDistance(birdModels[b].lambda.A, birdModels[c].lambda.A);
				}
			}
			
			// populate killList
			Iterator<ModelledObservation> it = aliveBirds.iterator();
			while(it.hasNext()) {
				ModelledObservation bird = it.next();
			
				if((isMigrating(bird.O) != 0 ||
				    isFeigningDeath(bird.O) != 0)/* &&
				    birdModels[b].species != Duck.SPECIES_UNKNOWN*/) {
					
					killList.add(bird);
						}
				
				bird.predictDiscreetAction();
				if(bird.lastProbability > 0.95) {
					killList.add(bird);
				}
			}
						
			lastIterTime = System.currentTimeMillis() - start;
		}
		/*
		for(int i = 0; i < popSize; i++) {
			System.out.println(matrixString(birdModels[i].lambda.A));
		}
		*/
		System.out.println();
		System.out.println(distancesString());
		System.out.println();
		//System.out.println(birdModels[0].lambda);
   	}
	
	private void updateAliveBirds(State pState) {
		aliveBirds.clear();
		for(int b = 0; b < popSize; b++) {
			if(pState.GetDuck(b).IsAlive())
				aliveBirds.add(birdModels[b]);
		}
	}
   	
	public Action getSuggestedAction() {
		System.out.println("Birds to kill: " + killList.size());
		Iterator<ModelledObservation> it = killList.iterator();
		
		while(it.hasNext()) {
			ModelledObservation bird = it.next();
			
			int migrationKill = isMigrating(bird.O);
			int feignKill = isFeigningDeath(bird.O);
			
			if (migrationKill != 0) {
				return new Action(bird.birdNumber, Action.ACTION_KEEPSPEED, Action.ACTION_STOP, migrationKill);
			}
			else if (feignKill != 0) {
				return new Action(bird.birdNumber, Action.ACTION_STOP, Action.ACTION_ACCELERATE, feignKill);
			}
			
			return bird.predictDiscreetAction();
		}

		return Action.cDontShoot;
	}

	static int isMigrating(ObservationSequence O) {
		int CERTAINTY = 4;
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
		birdModels[pDuck].species = pSpecies;
		birdModels[pDuck].speciesCertainty = 1.0;
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

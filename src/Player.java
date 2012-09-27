import java.util.Random;
import java.util.Date;
import java.util.Vector;

import markov.*;

public class Player {
	
    ///constructor
    private static int turnKeeper = 0;
    
    ///There is no data in the beginning, so not much should be done here.
    Player()
    {
    }

    public static final int N = 3;
    public static final int M = 5;
    
    ///shoot!

    ///This is the function where you should do all your work.
    ///
    ///you will receive a variable pState, which contains information about all ducks,
    ///both dead and alive. Each duck contains all past actions.
    ///
    ///The state also contains the scores for all players and the number of
    ///time steps elapsed since the last time this function was called.
    ///
    ///Check their documentation for more information.
    ///\param pState the state object
    ///\param pDue time before which we must have returned. To check how much time we have left
    ///you can use \p pDue.TimeUntil(), which returns the remaining time in milliseconds.
    ///That's the only safe way to use the pDue parameter
    ///\return the position we want to shoot at, or cDontShoot if we 
    ///prefer to pass
    Action Shoot(State pState,Deadline pDue)
    {
    	log2(turnKeeper + ": Shoot? Where!?");

        /*
         * Here you should write your clever algorithms to get the best action.
         * This skeleton never shoots.
         */
    	Model lambdaH = HMMFunction.getInitModel(N, M);
    	Model lambdaV = HMMFunction.getInitModel(N, M);
    	
    	System.out.println(lambdaH);
    	System.out.println(lambdaV);
    	/*	 a	k	s
    	 * M 
    	 * Q 
    	 * P 
    	 * F 
    	 */
    	/*
    	
    							//	 Kw     Ke    Aw    Ae     s
    	lambdaH.B = new double[][] {{0.50, 0.50, 0.00, 0.00, 0.00},  // M
    							    {0.20, 0.20, 0.10, 0.10, 0.50},  // Q
    							    {0.15, 0.15, 0.15, 0.15, 0.40},  // P
    							    {0.00, 0.00, 0.00, 0.00, 1.00}}; // F
    							   
								//   Ku     Kd    Au    Ad     s
    	lambdaV.B = new double[][] {{0.50, 0.50, 0.00, 0.00, 0.00},  // M
    							    {0.25, 0.25, 0.05, 0.05, 0.50},  // Q
    							    {0.15, 0.15, 0.15, 0.15, 0.40},  // P
    							    {0.00, 0.00, 0.00, 1.00, 0.00}}; // F
    	*/
    	/*
    	
		[0.41 0.40 0.19]
		[0.12 0.08 0.80]
		[0.42 0.01 0.57]
		[0.98 0.01 0.01]
		
		[0.17 0.40 0.43]
		[0.04 0.09 0.87]
		[0.14 0.01 0.85]
		[0.92 0.02 0.06]
 
    	 */
    	
    	int maxIters = 20000;
    	int iters = 0;
    	
    	/*
    	Duck duck = pState.GetDuck(0);

    	int[][] O = new int[pState.GetNumDucks()][];
    	
    	for(int d = 0; d < O.length; d++) {
    		int numActions = 2*pState.GetDuck(d).GetSeqLength();
    		O[d] = new int[numActions];
    		for(int a = 0; a < numActions; a++) {
    			O[d][2*a] = pState.GetDuck(d).mSeq.get(a).GetHAction();
    			O[d][2*a] = pState.GetDuck(d).mSeq.get(a).GetVAction();
    		}
    	}
    	*/
    	
    	ObservationSequence seqH = new ObservationSequence(pState.GetDuck(0).mSeq, true);
    	ObservationSequence seqV = new ObservationSequence(pState.GetDuck(0).mSeq, false);
    	
    	System.out.println("Actions: " + seqH.sequence.length);
    	/*
    	int[] O = new int[pState.GetDuck(0).mSeq.size()];
    	for(int o = 0; o < O.length; o++) {
    		O[o] = pState.GetDuck(0).mSeq.get(o).GetHAction();
    	}
    	
    	int T = O.length;
    	double[] c = new double[T];
    	*/
    	long start, stop;
    	long lastIterTime = 0;
    	double oldLogProbH = Double.NEGATIVE_INFINITY;
    	double oldLogProbV = Double.NEGATIVE_INFINITY;
    	double logProbH = 0;
    	double logProbV = 0;
    	boolean iterateV = true;
    	boolean iterateH = true;
    	
    	while(true) {
    		start = System.currentTimeMillis();
    		
    		if(iterateH) {
		    	lambdaH = HMMFunction.refineModel(lambdaH, seqH);	
		    	
		    	logProbH = 0;
		    	for(int t = 0; t < seqH.T; t++) {
		    		logProbH += Math.log(seqH.c[t]);
		    	}
		    	logProbH = -logProbH;
		    	
		    	if(logProbH > oldLogProbH)
		    		oldLogProbH = logProbH;
		    	else
		    		iterateH = false;
    		}
	    	
	    	// Vertical iteration
	    	if(iterateV) {
		    	lambdaV = HMMFunction.refineModel(lambdaV, seqV);
		    	
		    	logProbV = 0;
		    	for(int t = 0; t < seqV.T; t++) {
		    		logProbV += Math.log(seqV.c[t]);
		    	}
		    	logProbV = -logProbV;
		    	
		    	if(logProbV > oldLogProbV)
		    		oldLogProbV = logProbV;
		    	else
		    		iterateV = false;
	    	}
	    	
	    	stop = System.currentTimeMillis();
	    	lastIterTime = stop - start;
	    		    	
	    	iters++;
	    	if(iterateV && iterateH &&
	    	   pDue.TimeUntil() > (3*lastIterTime))
	    		continue;
	    	else
	    		break;
    	}

    	int Alen = 10;
    	System.out.print("Last H-actions: ");
    	for(int a = Alen; a > 0; a--) {
    		System.out.print(seqH.sequence[seqH.T-a] + ", ");
    	}
    	System.out.println();
    	System.out.print("Last V-actions: ");
    	for(int a = Alen; a > 0; a--) {
    		System.out.print(seqV.sequence[seqV.T-a] + ", ");
    	}
    	System.out.println();
    	
    	System.out.println("Next H-action?: " + seqH.predictHaction());
    	System.out.println("Next V-action?: " + seqV.predictHaction());
    	System.out.println();
    	System.out.print("Loop time: " + lastIterTime + " (remaining: " + pDue.TimeUntil() + ") ");
    	System.out.print("Iterations: " + iters + ", ");
    	System.out.println();
    	System.out.println("logProb: " + logProbH + " (old: " + oldLogProbH + ")");
    	System.out.println("logProb: " + logProbV + " (old: " + oldLogProbV + ")");
    	System.out.println();
    	System.out.print("Horizontal " + lambdaH + "\n");
    	System.out.print("Vertical " + lambdaV);
    	
    	
    	turnKeeper++;
        //this line doesn't shoot any bird
        return Action.cDontShoot;
        
        //this line would predict that bird 0 is totally stopped and shoot at it
        //return new Action(0,Action.ACTION_STOP,Action.ACTION_STOP,Action.BIRD_STOPPED);
    }

    private static void log(Object o) {
		System.out.println(o);
    }
    
    private static void log2(Object o) {
    	/*if(turnKeeper % 10 == 0)*/
    		System.out.println(o);
    }
    
    ///guess the species!

    ///This function will be called at the end of the game, to give you
    ///a chance to identify the species of the surviving ducks for extra
    ///points.
    ///
    ///For each alive duck in the vector, you must call the SetSpecies function,
    ///passing one of the ESpecies constants as a parameter
    ///\param pDucks the vector of all ducks. You must identify only the ones that are alive
    ///\param pDue time before which we must have returned
    void Guess(Duck[] pDucks,Deadline pDue)
    {
    	System.out.println("No idea, man...");
    	
        /*
         * Here you should write your clever algorithms to guess the species of each alive bird.
         * This skeleton guesses that all of them are white... they were the most likely after all!
         */
     
        for(int i=0;i<pDucks.length;i++)
        {
             if(pDucks[i].IsAlive())
                 pDucks[i].SetSpecies(Duck.SPECIES_WHITE);
        }
    }

    ///This function will be called whenever you hit a duck.
    ///\param pDuck the duck index
    ///\param pSpecies the species of the duck (it will also be set for this duck in pState from now on)    
    void Hit(int pDuck,int pSpecies)
    {
        System.out.println("HIT DUCK!!!");
    }
}

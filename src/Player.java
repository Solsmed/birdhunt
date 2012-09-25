import java.util.Random;
import java.util.Date;
import java.util.Vector;

import markov.*;

public class Player {
	
    ///constructor
    
    ///There is no data in the beginning, so not much should be done here.
    Player()
    {
    }

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
        /*
         * Here you should write your clever algorithms to get the best action.
         * This skeleton never shoots.
         */
    	Model lambda = HMMFunction.getInitModel(4, 3);
    	/*	 k	a	s
    	 * M 
    	 * Q 
    	 * P 
    	 * F 
    	 */
    	lambda.B = new double[][] {{0.93, 0.05, 0.02},
    							   {0.70, 0.05, 0.25},
    							   {0.20, 0.40, 0.40},
    							   {0.19, 0.80, 0.01}};
    	
    	int maxIters = 2000;
    	int iters = 0;
    	double oldLogProb = Double.NEGATIVE_INFINITY;
    	double logProb = 0;
    	
    	int[] O = new int[500];
    	int T = O.length;
    	double[] c = new double[T];
    	
    	
    	do {
	    	lambda = HMMFunction.refineModel(lambda, O, c);
	    	
	    	logProb = 0;
	    	for(int t = 0; t < T; t++) {
	    		logProb += Math.log(c[t]);
	    	}
	    	logProb = -logProb;
	    	
	    	oldLogProb = logProb;
    		iters++;
    	} while(iters < maxIters && logProb > oldLogProb);

    	
    	
        //this line doesn't shoot any bird
        return Action.cDontShoot;

        //this line would predict that bird 0 is totally stopped and shoot at it
        //return new Action(0,Action.ACTION_STOP,Action.ACTION_STOP,Action.BIRD_STOPPED);
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

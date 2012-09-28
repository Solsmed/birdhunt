import java.util.*;

public class Player {
	
    ///constructor
    private static int turnKeeper = 0;
    
    ///There is no data in the beginning, so not much should be done here.
    Player()
    {
    	colour = new HashMap<String, Set<Model2D>>();
    }

    public static final int N = 3;
    public static final int M = 5;
    
    public static final String[] labels = new String[] {"Mi", "Qu", "Pa", "FD"};
    
    public static final double[][] BinitH = new double[][]
    	//	  Kw     Ke    Aw    Ae     s
    		{{0.50, 0.50, 0.00, 0.00, 0.00},  // M
			 {0.16, 0.16, 0.16, 0.16, 0.34},  // Q
			 {0.15, 0.15, 0.35, 0.35, 0.00},  // P
			 {0.00, 0.00, 0.00, 0.00, 1.00}}; // F
    
    public static final double[][] BinitV = new double[][]
    	 //   Ku     Kd    Au    Ad     s
    		{{0.50, 0.50, 0.00, 0.00, 0.00},  // M
			 {0.16, 0.16, 0.16, 0.16, 0.34},  // Q
			 {0.15, 0.15, 0.35, 0.35, 0.00},  // P
			 {0.00, 0.00, 0.00, 1.00, 0.00}}; // F  
    
    public Map<String, Set<Model2D>> colour;
    
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
    	System.out.println(turnKeeper + ": Shoot? Where!?");

        /*
         * Here you should write your clever algorithms to get the best action.
         * This skeleton never shoots.
         */  	
    	
    	Model2D trueModel[] = new Model2D[pState.GetNumDucks()];
    	ObservationSequence2D seq[] = new ObservationSequence2D[pState.GetNumDucks()];
    	
    	for(int d = 0; d < pState.GetNumDucks(); d++) {
    		seq[d] = new ObservationSequence2D(pState.GetDuck(d).mSeq);
			trueModel[d] = Estimator.getLabelledModel(labels, BinitH, BinitV, seq[d]);
			seq[d].birdNumber = trueModel[d].birdNumber = d;
			mapAdd(trueModel[d]);
			System.out.println(trueModel[d]);
		}
    	
    	System.out.print("A-Lab: ");
		for(int i = 0; i < N; i++)
			System.out.print(String.format("%.2f ", seq[0].H.alpha[100][i]));
		System.out.println();

    	long lastIterTime = 0;
    	long start, stop;
    	boolean refined[] = new boolean[pState.GetNumDucks()];
    	int numUnrefinedDucks = pState.GetNumDucks();
    	
    	while (pDue.TimeUntil() > 1.15*lastIterTime) {
    		start = System.currentTimeMillis();
    		
    		long deadlineTime = (pDue.TimeUntil() / 2) / numUnrefinedDucks;
    		
    		numUnrefinedDucks = 0;
    		
    		for(int d = 0; d < pState.GetNumDucks(); d++) {
    			if(!refined[d]) {
    				if(Estimator.refineModelLoop(trueModel[d], seq[d], deadlineTime))
    					refined[d] = true;
    				else
    					numUnrefinedDucks++;
    			}
    		}
    		
    		stop = System.currentTimeMillis();
    		lastIterTime = stop - start;
    	}
    	
		System.out.print("Alpha: ");
		for(int i = 0; i < N; i++)
			System.out.print(String.format("%.2f ", seq[0].H.alpha[100][i]));
		System.out.println();

    	System.out.println("Next H-action?: " + seq[0].H.predictAction());
    	System.out.println("Next V-action?: " + seq[0].V.predictAction());
 
    	
    	turnKeeper++;
        //this line doesn't shoot any bird
        //return Action.cDontShoot;
        
        //this line would predict that bird 0 is totally stopped and shoot at it
    	Action action = Estimator.predictAction(trueModel[0], seq[0]);
    	System.out.println(ObservationSequence.actionToString(action));
        return action;
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
    
    void mapAdd(Model2D m) {
    	String key = m.getMapString();
    	
    	Set<Model2D> mSet = colour.get(key);
    	if(mSet == null) {
    		mSet = new HashSet<Model2D>();
    		colour.put(key, mSet);
    	}
    	
    	mSet.add(m);
    }
}

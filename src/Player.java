import java.util.*;

public class Player {
	
    ///constructor
    private static int turnKeeper = 0;
    BirdTracker tracker;
    
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
    	turnKeeper++;
    	System.out.println(turnKeeper + ": Shoot? Where!?");

        /*
         * Here you should write your clever algorithms to get the best action.
         * This skeleton never shoots.
         */ 
    	if(tracker == null)
    		tracker = new BirdTracker(pState.GetNumDucks());
    	
    	tracker.studyBirds(pState, pDue);
    	
    	Action headShot = tracker.getSuggestedAction();
    	System.out.println("My action: " + ObservationSequence.actionToString(headShot));
    	return headShot;
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
        System.out.println("                                  HIT DUCK!!!");
        tracker.registerDeadBird(pDuck, pSpecies);
    }
    /*
    void mapAdd(BirdModel m) {
    	String key = m.getMapString();
    	
    	Set<BirdModel> mSet = colour.get(key);
    	if(mSet == null) {
    		mSet = new HashSet<BirdModel>();
    		colour.put(key, mSet);
    	}
    	
    	mSet.add(m);
    }
    */
}

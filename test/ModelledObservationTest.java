import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;


public class ModelledObservationTest {
	BirdModel reality;
	BirdModel guess;
	ObservationSequence O;
	ObservationSequence Opartial;
	ModelledObservation modObs;
	int LEN = 501;
	int LENpartial = 500;

	public ModelledObservationTest() {
		System.out.println("-------------------------------------------");
		// Reality
		reality = TestLibrary.getModel_3x5();
		System.out.println("Real " + reality);
		
	}
	
	@Before
	public void setUp() throws Exception {
		// Guess
		double[][] gA = new double[][] 
				{
				{0.8, 0.1, 0.1},
				{0.1, 0.8, 0.1},
				{0.6, 0.1, 0.3}
				};
		double[][] gB = new double[][] 
				{
				{0.05, 0.40, 0.30, 0.05, 0.20},
				{0.10, 0.10, 0.10, 0.10, 0.60},
				{0.30, 0.30, 0.10, 0.15, 0.15}
				};
		guess = HMMFunction.getInitBirdModel(3, 5);
		guess.A = gA;
		guess.B = gB;
		
		// Observations
		// whole
		O = new ObservationSequence(new Vector<Action>());
		O.T = LEN;
		O.action = SequenceGenerator.generateSequence(reality.A, reality.B, reality.pi, LEN);
		O.movement = new int[LEN];
		// partial
		Opartial = new ObservationSequence(new Vector<Action>());
		Opartial.T = LENpartial;
		Opartial.action = new int[LENpartial];
		System.arraycopy(O.action, 0, Opartial.action, 0, LENpartial);
		Opartial.movement = new int[LENpartial];

		modObs = new ModelledObservation(Opartial, guess);
		
		BirdModel refined = modObs.lambda;
		
		int iter = 0;
		for(iter = 0; iter < 50000; iter++) {
	    	double oldLogProb = refined.logProb;
	    	
	    	refined = HMMFunction.getRefinedModel(modObs);
	    	modObs.lambda = refined;
					
	    	if(refined.logProb < oldLogProb) {
	    		refined.isOptimal = true;
	    		break;
	    	}
		}
		System.out.println("Stopped after " + iter);
		
		double distRealityArefinedA = MatrixMath.matrixNormDistance(reality.A, refined.A);
		double distRealityBrefinedB = MatrixMath.matrixNormDistance(reality.B, refined.B);
		
		System.out.println("rA-fA: " + distRealityArefinedA);
		System.out.println("rB-fB: " + distRealityBrefinedB);
		
		System.out.println("Refined: " + refined);
	}

	@Test
	public void testPredictDiscreetAction() {
		String prediction = ObservationSequence.actionToString(modObs.predictDiscreetAction());
		
		int t = LENpartial;
		
		char H = '?';
		switch(O.action[t] / 3) {
		case Action.ACTION_ACCELERATE: H = 'A'; break;
		case Action.ACTION_KEEPSPEED: H = 'K'; break;
		case Action.ACTION_STOP: H = 'S'; break;
		}
		
		char V = '?';
		switch(O.action[t] % 3) {
		case Action.ACTION_ACCELERATE: V = 'A'; break;
		case Action.ACTION_KEEPSPEED: V = 'K'; break;
		case Action.ACTION_STOP: V = 'S'; break;
		}
		
		String correct = "(0) " + H + " " + V;
		
		System.out.println(prediction);
		System.out.println(correct);
	}

	@Test
	public void testPredictFuzzyAction() {
		String prediction = ObservationSequence.actionToString(modObs.predictDiscreetAction());
		
		int t = LENpartial;
		
		char H = '?';
		switch(O.action[t] / 3) {
		case Action.ACTION_ACCELERATE: H = 'A'; break;
		case Action.ACTION_KEEPSPEED: H = 'K'; break;
		case Action.ACTION_STOP: H = 'S'; break;
		}
		
		char V = '?';
		switch(O.action[t] % 3) {
		case Action.ACTION_ACCELERATE: V = 'A'; break;
		case Action.ACTION_KEEPSPEED: V = 'K'; break;
		case Action.ACTION_STOP: V = 'S'; break;
		}
		
		String correct = "(0) " + H + " " + V;
		
		System.out.println(prediction);
		System.out.println(correct);
	}

	@Test
	public void testRefineModel() {
		fail("Not yet implemented");
	}

}

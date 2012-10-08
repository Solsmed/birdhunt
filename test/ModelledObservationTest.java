import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;


public class ModelledObservationTest {
	BirdModel xavi;
	MarkovModel xaviFacit;
	MarkovModel reality;
	MarkovModel guess;
	ObservationSequence O;
	ObservationSequence Opartial;
	BirdModel modObs;
	int LEN = 501;
	int LENpartial = 500;

	public ModelledObservationTest() {
		System.out.println("-------------------------------------------");
		// Reality
		reality = TestLibrary.getModel_3x5();
		System.out.println("Real (my)" + reality);
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
		int[][] seq = SequenceGenerator.generateSequence(reality.A, reality.B, reality.pi, LEN);
		O.action = seq[1];
		O.movement = new int[LEN];
		// partial
		Opartial = new ObservationSequence(new Vector<Action>());
		Opartial.T = LENpartial;
		Opartial.action = new int[LENpartial];
		System.arraycopy(O.action, 0, Opartial.action, 0, LENpartial);
		Opartial.movement = new int[LENpartial];

		modObs = new BirdModel(Opartial, guess);
		
		MarkovModel refined = modObs.lambda;
		
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
		
		double distRealityArefinedA = MatrixMath.matrixNorm2(reality.A, refined.A);
		double distRealityBrefinedB = MatrixMath.matrixNorm2(reality.B, refined.B);
		
		//System.out.println("rA-fA: " + distRealityArefinedA);
		//System.out.println("rB-fB: " + distRealityBrefinedB);
		
		System.out.println("Refined: " + refined);
	}
/*
	@Test
	public void testPredictDiscreteAction() {
		System.out.println("*\n*Test Predict Discrete Action\n*");
		String prediction = ObservationSequence.actionToString(modObs.predictDiscreteAction());
		
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
*/
/*
	@Test
	public void testPredictFuzzyAction() {
		System.out.println("*\n*Test Predict Fuzzy Action\n*");
		String prediction = ObservationSequence.actionToString(modObs.predictFuzzyAction());
		
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
*/
	@Test
	public void testRefineModel() {
		System.out.println("*\n*Test Refine Model\n*");
		
		System.out.println("=== Flat A, Real B, Full O ===");
		xavi = TestLibrary.getModelledObservation_3x9();
		MarkovModel xaviFacit = new MarkovModel(xavi.lambda);
		xavi.lambda.A = HMMFunction.getInitBirdModel(3, 9).A;
		
		long start = System.currentTimeMillis();
		boolean done = xavi.refineModel(start + 1000);
		System.out.println("Refinement performed in " + (System.currentTimeMillis() - start) + " ms.");
		System.out.println("Refinement done: " + done);
		double distArealA = MatrixMath.matrixNorm2(xavi.lambda.A, xaviFacit.A);
		double distBrealB = MatrixMath.matrixNorm2(xavi.lambda.B, xaviFacit.B);
		
		System.out.println(distArealA);
		System.out.println(distBrealB);
		
		assertTrue(distArealA <= 0.01); // 0.0017
		assertTrue(distBrealB <= 0.01); // 0.0026
		
		
		System.out.println();
		System.out.println("=== Flat A, Ballpark B, Partial O ===");
		xavi = TestLibrary.getModelledObservation_3x9(100);
		xavi.lambda.A = HMMFunction.getInitBirdModel(3, 9).A;
		xavi.lambda.pi = HMMFunction.getInitBirdModel(3, 9).pi;
		double[][] ballparkB = new double[][]
				{
			{0.51, 0.20, 0.05, 0.10, 0.09, 0.00, 0.00, 0.00, 0.05},
			{0.05, 0.00, 0.00, 0.00, 0.00, 0.00, 0.70, 0.25, 0.00},
			{0.10, 0.00, 0.33, 0.19, 0.00, 0.33, 0.00, 0.00, 0.05}
				};
		xavi.lambda.B = ballparkB;
		start = System.currentTimeMillis();
		done = xavi.refineModel(start + 1000);
		System.out.println("Refinement performed in " + (System.currentTimeMillis() - start) + " ms.");
		System.out.println("Refinement done: " + done);
		distArealA = MatrixMath.matrixNorm2(xavi.lambda.A, xaviFacit.A);
		distBrealB = MatrixMath.matrixNorm2(xavi.lambda.B, xaviFacit.B);
		
		System.out.println(distArealA);
		System.out.println(distBrealB);
		
		System.out.println(xavi.lambda);
		
		assertTrue(distArealA <= 0.10); // 0.0816
		assertTrue(distBrealB <= 0.10); // 0.0532
		
		
		
		System.out.println();
		System.out.println("=== Ballpark A, Flat B, Partial O ===");
		xavi = TestLibrary.getModelledObservation_3x9(100);
		xavi.lambda.B = HMMFunction.getInitBirdModel(3, 9).B;
		xavi.lambda.pi = HMMFunction.getInitBirdModel(3, 9).pi;
		double[][] ballparkA = new double[][]
				{
			{0.89, 0.06, 0.05},
			{0.06, 0.88, 0.06},
			{0.05, 0.06, 0.89},
				};
		xavi.lambda.A = ballparkA;
		start = System.currentTimeMillis();
		done = xavi.refineModel(start + 1000);
		System.out.println("Refinement performed in " + (System.currentTimeMillis() - start) + " ms.");
		System.out.println("Refinement done: " + done);
		distArealA = MatrixMath.matrixNorm2(xavi.lambda.A, xaviFacit.A);
		distBrealB = MatrixMath.matrixNorm2(xavi.lambda.B, xaviFacit.B);
		
		System.out.println(distArealA);
		System.out.println(distBrealB);
		
		System.out.println(xavi.lambda);
		
		//assertTrue(distArealA <= 0.10); // 0.0816
		//assertTrue(distBrealB <= 0.10); // 0.0532
		
		
		
		
		
		System.out.println();
		System.out.println("=== Ballpark A, Flat B, Partial O (Matlab remake) ===");
		xavi = TestLibrary.getModelledObservation_3x9(100);
		xavi.lambda.B = HMMFunction.getInitBirdModel(3, 9).B;
		xavi.lambda.pi = HMMFunction.getInitBirdModel(3, 9).pi;
		ballparkA = new double[][]
				{
			{0.89, 0.06, 0.05},
			{0.06, 0.88, 0.06},
			{0.05, 0.06, 0.89},
				};
		xavi.lambda.A = ballparkA;
		System.out.println("Go!");
		start = System.currentTimeMillis();
		done = xavi.baumWelchTrain(50);
		System.out.println("Refinement performed in " + (System.currentTimeMillis() - start) + " ms.");
		System.out.println("Refinement done: " + done);
		distArealA = MatrixMath.matrixNorm2(xavi.lambda.A, xaviFacit.A);
		distBrealB = MatrixMath.matrixNorm2(xavi.lambda.B, xaviFacit.B);
		
		System.out.println(distArealA);
		System.out.println(distBrealB);
		
		System.out.println(xavi.lambda);
		
		assertTrue(distArealA <= 0.10); // 0.0816
		assertTrue(distBrealB <= 0.10); // 0.0532
	}

}

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class HMMFunctionTest {

	@Before
	public void setUp() {

	}

	@Test
	public void testGetInitModel() {
		for(int N = 1; N < 10; N++) {
			for(int M = 1; M < 10; M++) {
				BirdModel lambda = HMMFunction.getInitBirdModel(N, M);
				for(int i = 0; i < N; i++) {

					boolean[] retA = testRow(lambda.A[i], N);
					boolean[] retB = testRow(lambda.B[i], M);
				
					for(int r = 0; r < retA.length; r++) {
						if(!retA[r] || !retB[r]) {
							System.out.println("fel!");
						}
						assertTrue(retA[r]);
						assertTrue(retB[r]);
					}
				}
			}
		}
	}
	
	private boolean[] testRow(double[] B, int M) {
		boolean[] ret = new boolean[3];
		
		double rowSum = 0;
		double min = 1.0;
		double max = 0.0;
		
		for(int j = 0; j < M; j++) {
			rowSum += B[j];
			if(B[j] < min)
				min = B[j];
			if(B[j] > max)
				max = B[j];				
		}
		
		ret[0] = (1.0/M + HMMFunction.NOISE_AMPLITUDE*1/M >= max);
		ret[1] = (1.0/M - HMMFunction.NOISE_AMPLITUDE*1/M <= min);
		ret[2] = (0.9999999999999996 <= rowSum && rowSum <= 1.0000000000000004);
		
		return ret;
	}
	
	@Test
	public void testNoiseRow() {
		double[] uniformVector;
		for(int len = 1; len < 25; len++) {
			uniformVector = new double[len];
			
			HMMFunction.fillWithStochasticNoise(uniformVector);
			
			boolean[] ret = testRow(uniformVector, len);
			
			for(int r = 0; r < ret.length; r++) {
				if(!ret[r]) {
					System.out.println("fel!");
					double sum = 0;
					System.out.println("UniformVector!");
					for(int i = 0; i < len; i++) {
						System.out.println("\t" + uniformVector[i]);
						sum += uniformVector[i];
					}
					System.out.println("len = " + len);
					System.out.println("sum = " + sum);
				}
				assertTrue(ret[r]);
			}
		}
		
	}

	@Test
	public void testRefineModel() {
		// Reality
		BirdModel reality = TestLibrary.getModel_3x5();

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
		BirdModel guess = HMMFunction.getInitBirdModel(3, 5);
		guess.A = gA;
		guess.B = gB;
		
		// Observations
		int LEN = 500;
		ObservationSequence O = new ObservationSequence(new Vector<Action>());
		O.T = LEN;
		O.action = SequenceGenerator.generateSequence(reality.A, reality.B, reality.pi, LEN);
		System.out.println("O.act.len = " + O.action.length);
		O.movement = new int[LEN];
		System.out.println("O.mov.len = " + O.movement.length);
		
		System.out.println("O.T = " + O.T);
		//ModelledObservation modObs = new ModelledObservation(O, 3, 5);
		ModelledObservation modObs = new ModelledObservation(O, guess);
		System.out.println("Init: " + modObs.lambda);
		
		BirdModel refined = modObs.lambda;
		
		int iter = 0;
		for(iter = 0; iter < 50000; iter++) {
	    	double oldLogProb = refined.logProb;
	    	
	    	//System.out.println("refining...");
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
		
		System.out.println("Real " + reality);
		System.out.println("Refined: " + refined);
		
		assertTrue(distRealityArefinedA <= 0.15);
		assertTrue(distRealityBrefinedB <= 0.15);
	}
/*
	@Test
	public void testFillGammas() {
		fail("Not yet implemented");
	}

	@Test
	public void testAlphaPass() {
		fail("Not yet implemented");
	}

	@Test
	public void testBetaPass() {
		fail("Not yet implemented");
	}

	@Test
	public void testDiGamma() {
		fail("Not yet implemented");
	}
	*/
}
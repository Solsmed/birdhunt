import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class HMMFunctionTest {

	BirdModel lambda;
	BirdModel reality;
	ObservationSequence O;
	
	@Before
	public void setUp() {
		int LEN = 500;
		Vector<Action> act = new Vector<Action>();
		O = new ObservationSequence(act);
		
		BirdModel reality = TestLibrary.getModel_3x5();
		
		O.action = SequenceGenerator.generateSequence(reality.A, reality.B, reality.pi, LEN);
		
		lambda = HMMFunction.getInitModel(reality.N, reality.M);
		//lambda.B = {};
	}

	@Test
	public void testGetInitModel() {
		for(int N = 1; N < 10; N++) {
			for(int M = 1; M < 10; M++) {
				BirdModel lambda = HMMFunction.getInitModel(N, M);
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
		ret[2] = (0.9999999999999999 <= rowSum && rowSum <= 1);
		
		return ret;
	}

/*
	@Test
	public void testRefineModel() {
		lambda.B = new double[][]
				{
				{0.10, 0.35, 0.35, 0.10, 0.10},
				{0.20, 0.20, 0.20, 0.20, 0.20},
				{1,00, 0,00, 0.00, 0.00, 0.00}
				};
		
		Model refined = HMMFunction.refineModel(lambda, O);
		
		for(int iter = 0; iter < 2000; iter++) {
			refined = HMMFunction.refineModel(refined, O);
		}
		
		double distRealityArefinedA = HMMFunction.matrixNormDistance(reality.A, refined.A);
		double distRealityBrefinedB = HMMFunction.matrixNormDistance(reality.B, refined.B);
		//double distRealityAlambdaA = HMMFunction.matrixNormDistance(reality.A, lambda.A);
		//double distRealityBlambdaB = HMMFunction.matrixNormDistance(reality.A, lambda.A);
		
		System.out.println("rA-fA: " + distRealityArefinedA);
		System.out.println("rB-fB: " + distRealityBrefinedB);
		//System.out.println("rA-lA: " + distRealityAlambdaA);
		//System.out.println("rB-lB: " + distRealityBlambdaB);
		
		System.out.println("Real " + reality);
		//System.out.println("Init: " + lambda);
		System.out.println("Refined: " + refined);
		
		//assertTrue(distRealityArefinedA <= 0.15);
		//assertTrue(distRealityBrefinedB <= 0.15);
	}
	*/
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
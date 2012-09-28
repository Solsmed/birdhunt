

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class HMMFunctionTest {

	Model lambda;
	Model reality;
	ObservationSequence O;
	
	double[][] A = new double[][]
			{{0.80, 0.05, 0.15},
			 {0.05, 0.90, 0.05},
			 {0.60, 0.10, 0.30}
			};
	double[][] B = new double[][]
			{{0.00, 0.50, 0.50, 0.00, 0.00},
			 {0.10, 0.10, 0.10, 0.35, 0.35},
			 {1,00, 0.00, 0.00, 0.00, 0.00}
			};
	
	double[] pi = {1.0, 0.00, 0.00};
	
	@Before
	public void setUp() {
		int LEN = 500;
		Vector<Action> act = new Vector<Action>();
		O = new ObservationSequence(act, true);
		O.sequence = SequenceGenerator.generateSequence(A, B, pi, LEN);
		O.T = LEN;
		int N = 3;
		int T = O.T;
		O.N = N;
		O.M = 5;
		
		O.alpha = new double[T][N];
		O.beta = new double[T][N];
		O.c = new double[T];
		O.gamma = new double[T][N];
		O.diGamma = new double[T][N][N];
		
		lambda = HMMFunction.getInitModel(3, 5);
		//lambda.B = {};
		reality = new Model(A, B, pi);
	}	
	
	@Test
	public void testGetInitModel() {
		fail("Not yet implemented");
	}

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
}

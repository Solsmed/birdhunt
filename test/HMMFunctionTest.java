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

	/*
	@Test
	public void testRefineModel() {
		
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
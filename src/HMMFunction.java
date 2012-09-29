import java.util.Arrays;
import java.util.Random;

public class HMMFunction {
	public static final double NOISE_AMPLITUDE = 0.15;
	
	/*
	public static ModelledObservation getLabelledModelledObservation(String[] labels, double[][] Binit, ObservationSequence O) {
		// Initialise
		int BigN = Binit.length;
		int N = ModelledObservation.N;
		int numPermutations = 4; // Math.chooseUnordered(BigN, N)
		
		int M = Binit[0].length;
		
		ModelledObservation m[] = new ModelledObservation[numPermutations];
		for(int p = 0; p < numPermutations; p++) {
			m[p] = new ModelledObservation(O);
		}
		
		int notIndex = 0;
		
		for(int p = 0; p < numPermutations; p++) {
			int mIndex = 0;
			m[p].lambda.stateLabels = new String[N];
			for(int i = 0; i < BigN; i++) {
				if(i != notIndex) {
					System.arraycopy(Binit[i], 0, m[p].lambda.B[mIndex], 0, M);
					m[p].lambda.stateLabels[mIndex] = labels[i];
					mIndex++;
				}
			}
			notIndex++;
		}
		
		boolean allDone = false;
		int iter = 0;
		int maxIter = 90;
		while(!allDone && iter < maxIter) {
			allDone = true;
			iter++;
			for(int p = 0; p < numPermutations; p++) {
				m[p].iterateOnce();
				System.out.print(String.format("%.2f", m[p].lambda.logProb));
				allDone = allDone && m[p].lambda.isOptimal; 
			}
			System.out.println();
		}
		
		double maxSumLogProb = Double.NEGATIVE_INFINITY;
		int mostProbableModelIndex = -1;
		
		for(int p = 0; p < numPermutations; p++) {
			double sumProb = m[p].lambda.logProb;
			if(sumProb > maxSumLogProb) {
				maxSumLogProb = sumProb;
				mostProbableModelIndex = p;
			}
		}
		
		return m[mostProbableModelIndex];
	}
	*/
	
	public static BirdModel getInitBirdModel(int N, int M) {
		BirdModel lambda = new BirdModel(new double[N][N], new double[N][M], new double[N], null);
		
		fillWithStochasticNoise(lambda.pi);
		for(int i = 0; i < N; i++) {
			fillWithStochasticNoise(lambda.A[i]);
			fillWithStochasticNoise(lambda.B[i]);
		}
			
		return lambda;
	}
	
	public static void fillWithStochasticNoise(double[] row) {
		int N = row.length;
		
		double Namp = NOISE_AMPLITUDE*(1.0/N);
		
		for(int i = 0; i < N; i++) {
			Arrays.fill(row, 1.0/N);
		}
		
		double[] noise = getNoiseVector(N, Namp);
		vectorAdd(row, noise);
	}
	
	private static double[] getNoiseVector(int N, double amplitude) {
		Random random = new Random();
		
		double[] noiseVector = new double[N];
		
		double sum = 0;
	
		for(int i = 0; i < N; i++) {
			noiseVector[i] = amplitude*(random.nextDouble()-0.5);
			sum += noiseVector[i];
		}
		
		double avg = sum / N;
		
		for(int i = 0; i < N; i++)
			noiseVector[i] -= avg;

		return noiseVector;
	}
	
	private static void vectorAdd(double[] a, double[] b) {
		for(int i = 0; i < a.length && i < b.length; i++)
			a[i] += b[i];
	}
	
	/**
	 * A new BirdModel object that is slightly better than the model described by the arguments.
	 * 
	 * @return A new BirdModel object that is slightly better than the model contained in the argument.
	 */
	public static BirdModel getRefinedModel(ModelledObservation bird) {
		BirdModel newLambda = new BirdModel(bird.lambda);
		
		double[] c = new double[bird.O.T];
		
		fillGammas(newLambda.A, newLambda.B, newLambda.pi, bird.O.action, c, bird.gamma, bird.diGamma);
		
		// re-estimate pi
		for(int i = 0; i < bird.getN(); i++) {
			newLambda.pi[i] = bird.gamma[0][i];
		}
		
		// re-estimate A
		for(int i = 0; i < bird.getN(); i++) {
			for(int j = 0; j < bird.getN(); j++) {
				double numer = 0;
				double denom = 0;
				for(int t = 0; t < bird.O.T - 1; t++) {
					numer += bird.diGamma[t][i][j];
					denom += bird.gamma[t][i];
				}
				newLambda.A[i][j] = numer / denom;
			}
		}
		
		// re-estimate Bh
		for(int i = 0; i < bird.getN(); i++) {
			for(int j = 0; j < bird.getM(); j++) {
				double numer = 0;
				double denom = 0;
				for(int t = 0; t < bird.O.T - 1; t++) {
					if(bird.O.action[t] == j) {
						numer += bird.gamma[t][i];
					}
					denom += bird.gamma[t][i];
				}
				newLambda.B[i][j] = numer / denom;
			}
		}
		
    	double logProb = 0;
    	for(int t = 0; t < bird.O.T; t++) {
    		logProb += Math.log(c[t]);
    	}
    	newLambda.logProb = -logProb;

		return newLambda;
	}
	
	// writes to c, gamma and diGamma
	private static void fillGammas(double[][] A, double[][] B, double[] pi, int[] O, double[] c, double[][] gamma, double[][][] diGamma) {
		int T = O.length;
		double[][] alpha = new double[T][A.length];
		double[][] beta = new double[T][A.length];
		
		alphaPass(A, B, pi, O, alpha, c);
		betaPass(A, B, O, beta, c);
		
		diGamma(A, B, O, alpha, beta, gamma, diGamma);
	}
	
	// writes to alpha[][] and c[]
	private static void alphaPass(double[][] A, double[][] B, double[] pi, int[] O, double[][] alpha, double[] c) {	
		int T = O.length;
		
		// compute alpha[0][i]
		c[0] = 0;
		for (int i = 0; i < A.length; i++) {
			alpha[0][i] = pi[i]*B[i][O[0]];
			c[0] += alpha[0][i];
		}
		
		// scale the alpha[0][i]
		c[0] = 1 / c[0];
		for (int i = 0; i < A.length; i++) {
			alpha[0][i] *= c[0];
		}
		
		// compute alpha[t][i]
		for(int t = 1; t < T; t++) {
			c[t] = 0;
			for(int i = 0; i < A.length; i++) {
				alpha[t][i] = 0;
				for(int j = 0; j < A.length; j++) {
					alpha[t][i] += alpha[t-1][j]*A[j][i];
				}
				alpha[t][i] *= B[i][O[t]];
				c[t] += alpha[t][i];
			}
			
			// scale alpha[t][i]
			c[t] = 1 / c[t];
			for (int i = 0; i < A.length; i++) {
				alpha[t][i] *= c[t];
			}
		}
	}
	
	// writes to beta[][]
	private static void betaPass(double[][] A, double[][] B, int[] O, double[][] beta, double[] c) {
		int T = O.length;
		
		// Let beta[T-1][i] = 1 scaled by c[T-1]
		for(int i = 0; i < A.length; i++) {
			beta[T-1][i] = c[T-1];
		}
		
		// beta pass
		for(int t = T - 2; t >= 0; t--) {
			for(int i = 0; i < A.length; i++) {
				beta[t][i] = 0;
				for(int j = 0; j < A.length; j++) {
					beta[t][i] += A[i][j]*B[j][O[t+1]]*beta[t+1][j];
				}
				// scale beta[t][i] with same scale factor as alpha[t][i]
				beta[t][i] *= c[t];
			}
		}
	}

	// writes to gamma and diGamma
	private static void diGamma(double[][] A, double[][] B, int[] O, double[][] alpha, double[][] beta, double[][] gamma, double[][][] diGamma) {
		int T = O.length;
		double[][] cache = new double[A.length][A.length];
		
		for(int t = 0; t < T - 1; t++) {
			double denom = 0;
			
			for(int i = 0; i < A.length; i++) {
				for(int j = 0; j < A.length; j++) {
					cache[i][j] = alpha[t][i]*A[i][j]*B[j][O[t+1]]*beta[t+1][j];
					denom += cache[i][j];
				}
			}
			
			for(int i = 0; i < A.length; i++) {
				gamma[t][i] = 0;
				for(int j = 0; j < A.length; j++) {
					diGamma[t][i][j] = cache[i][j] / denom;
					gamma[t][i] += diGamma[t][i][j];
				}
			}
		}
	}
}

package markov;

import java.util.Arrays;
import java.util.Random;

public class HMMFunction {
	public static Model getInitModel(int N, int M) {
		Model lambda = new Model(N, M);
		
		double Namp = 0.1*(1.0/N);
		double Mamp = 0.1*(1.0/M);
		
		for(int i = 0; i < N; i++) {
			Arrays.fill(lambda.A[i], 1.0/N);
			Arrays.fill(lambda.B[i], 1.0/M);
		}
		
		Arrays.fill(lambda.pi, 1.0/N);
		
		double[] piNoise = getNoiseVector(N, Namp);
		vectorAdd(lambda.pi, piNoise);
		
		for(int i = 0; i < N; i++) {
			double[] Anoise = getNoiseVector(N, Namp);
			double[] Bnoise = getNoiseVector(M, Mamp);
			vectorAdd(lambda.A[i], Anoise);
			vectorAdd(lambda.B[i], Bnoise);
		}
		
		return lambda;
	}
	
	private static double[] getNoiseVector(int N, double amplitude) {
		Random random = new Random();
		
		double[] noiseVector = new double[N];
		double maxAmplitude = 0;
				
		for(int i = 0; i < N; i++) {
			noiseVector[i] = random.nextDouble() - 0.5;
			if(Math.abs(noiseVector[i]) > maxAmplitude)
				maxAmplitude = noiseVector[i];
		}
		
		double sum = 0;
		double normalisationFactor = amplitude / maxAmplitude;
		for(int i = 0; i < N; i++) {
			noiseVector[i] *= normalisationFactor;
			sum += noiseVector[i];
		}
		
		double correction = sum / N;
		for(int i = 0; i < N; i++) {
			noiseVector[i] -= correction;
		}
		
		return noiseVector;
	}
	
	private static void vectorAdd(double[] a, double[] b) {
		for(int i = 0; i < a.length && i < b.length; i++)
			a[i] += b[i];
	}
	
	public static Model refineModel(Model oldLambda, int[] O, double[] c) {
		Model newLambda = new Model(oldLambda);
		
		int T = O.length;
		int N = oldLambda.N;
		double[][] gamma = new double[T][N];
		double[][][] diGamma = new double[T][N][N];
		
		fillGammas(oldLambda, O, c, gamma, diGamma);
		
		// re-estimate pi
		for(int i = 0; i < N; i++) {
			newLambda.pi[i] = gamma[0][i];
		}
		
		// re-estimate A
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				double numer = 0;
				double denom = 0;
				for(int t = 0; t < T - 1; t++) {
					numer += diGamma[t][i][j];
					denom += gamma[t][i];
				}
				newLambda.A[i][j] = numer / denom;
			}
		}
		
		// re-estimate B
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < newLambda.M; j++) {
				double numer = 0;
				double denom = 0;
				for(int t = 0; t < T - 1; t++) {
					if(O[t] == j) {
						numer += gamma[t][i];
					}
					denom += gamma[t][i];
				}
				newLambda.B[i][j] = numer / denom;
			}
		}

		return newLambda;
	}
	
	public static void fillGammas(Model lambda, int[] O, double[] c, double[][] gamma, double[][][] diGamma) {
		int T = O.length;
		double[][] alpha = new double[T][lambda.N];
		double[][] beta = new double[T][lambda.N];
		//double[][] gamma = new double[T][lambda.N];
		//double[][][] diGamma = new double[T][lambda.N][lambda.N];
		//double[] c = new double[T];
		
		alphaPass(lambda, O, alpha, c);
		betaPass(lambda, O, beta, c);
		
		diGamma(lambda, O, alpha, beta, gamma, diGamma);
		
		int a;
		for(int t = 0; t < T; t++) {
			for(int i = 0; i < lambda.N; i++) {
				if(Double.isNaN(gamma[t][i])) {
					a = 1;
				}
			}
		}
	}
	
	// writes to alpha[][] and c[]
	public static void alphaPass(Model lambda, int[] O, double[][] alpha, double[] c) {	
		int T = O.length;
		
		// compute alpha[0][i]
		c[0] = 0;
		for (int i = 0; i < lambda.N; i++) {
			alpha[0][i] = lambda.pi[i]*lambda.B[i][O[0]];
			c[0] += alpha[0][i];
		}
		
		// scale the alpha[0][i]
		c[0] = 1 / c[0];
		for (int i = 0; i < lambda.N; i++) {
			alpha[0][i] *= c[0];
		}
		
		// compute alpha[t][i]
		for(int t = 1; t < T; t++) {
			c[t] = 0;
			for(int i = 0; i < lambda.N; i++) {
				alpha[t][i] = 0;
				for(int j = 0; j < lambda.N; j++) {
					alpha[t][i] += alpha[t-1][j]*lambda.A[j][i];
				}
				alpha[t][i] *= lambda.B[i][O[t]];
				c[t] += alpha[t][i];
			}
			
			// scale alpha[t][i]
			c[t] = 1 / c[t];
			for (int i = 0; i < lambda.N; i++) {
				alpha[t][i] *= c[t];
			}
		}
	}
	
	// writes to beta[][]
	public static void betaPass(Model lambda, int[] O, double[][] beta, double[] c) {
		int T = O.length;
		
		// Let beta[T-1][i] = 1 scaled by c[T-1]
		for(int i = 0; i < lambda.N; i++) {
			beta[T-1][i] = c[T-1];
		}
		
		// beta pass
		for(int t = T - 2; t >= 0; t--) {
			for(int i = 0; i < lambda.N; i++) {
				beta[t][i] = 0;
				for(int j = 0; j < lambda.N; j++) {
					beta[t][i] += lambda.A[i][j]*lambda.B[j][O[t+1]]*beta[t+1][j];
				}
				// scale beta[t][i] with same scale factor as alpha[t][i]
				beta[t][i] *= c[t];
			}
		}
	}

	public static void diGamma(Model lambda, int[] O, double[][] alpha, double[][] beta, double[][] gamma, double[][][] diGamma) {
		int T = O.length;
		double[][] cache = new double[lambda.N][lambda.N];
		
		for(int t = 0; t < T - 1; t++) {
			double denom = 0;
			
			for(int i = 0; i < lambda.N; i++) {
				for(int j = 0; j < lambda.N; j++) {
					cache[i][j] = alpha[t][i]*lambda.A[i][j]*lambda.B[j][O[t+1]]*beta[t+1][j];
					denom += cache[i][j];
				}
			}
			
			for(int i = 0; i < lambda.N; i++) {
				gamma[t][i] = 0;
				for(int j = 0; j < lambda.N; j++) {
					diGamma[t][i][j] = cache[i][j] / denom;
					gamma[t][i] += diGamma[t][i][j];
				}
			}
		}
	}
	
	public static double matrixDistance() {
		return 0;
	}
}

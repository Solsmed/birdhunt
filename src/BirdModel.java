

public class BirdModel {
	public double[][] A;
	//public double[][] Bh;
	//public double[][] Bv;
	public double[][] B;
	
	public double[] pi;
	public String[] stateLabels;
	
	public double logProb = Double.NEGATIVE_INFINITY;
	boolean isOptimal = false;
	
	public int N;
	public int M;
	
	public BirdModel(BirdModel l) {
		A = new double[l.N][l.N];
		//Bh = new double[l.N][l.M];
		//Bv = new double[l.N][l.M];
		B = new double[l.N][l.M];
		pi = new double[l.N];
		if(l.stateLabels != null) {
			stateLabels = new String[l.stateLabels.length];
			System.arraycopy(l.stateLabels, 0, stateLabels, 0, stateLabels.length);
		}
		
		isOptimal = l.isOptimal;
		logProb = l.logProb;
		N = l.N;
		M = l.M;
		
		System.arraycopy(l.pi, 0, pi, 0, N);
		for(int i = 0; i < N; i++) {
			System.arraycopy(l.A[i], 0, A[i], 0, N);
			//System.arraycopy(l.Bh[i], 0, Bh[i], 0, M);
			//System.arraycopy(l.Bv[i], 0, Bv[i], 0, M);
			System.arraycopy(l.B[i], 0, B[i], 0, M);
		}
	}
	
	public BirdModel(int N, int M) {
		//this(new double[N][N], new double[N][M], new double[N][M], new double[N]);
		this(new double[N][N], new double[N][M], new double[N]);
	}
	
	//public BirdModel(double[][] A, double[][] Bh, double[][] Bv, double[] pi) {
	public BirdModel(double[][] A, double[][] B, double[] pi) {
		this.A = A;
		//this.Bh = Bh;
		//this.Bv = Bv;
		this.B = B;
		this.pi = pi;
		
		this.N = A.length;
		//this.M = Bh[0].length;
		this.M = B[0].length;
		
		logProb = Double.NEGATIVE_INFINITY;
		isOptimal = false;
		
		stateLabels = ModelledObservation.labels;
	}
	
	@Override
	public String toString() {
		StringBuffer output = new StringBuffer();
		
		output.append("                     " + stateLabels[0] + "   " + stateLabels[1] + "   " + stateLabels[2] + "   " + stateLabels[3] + "          Kk   Ka   Ks   Ak   Aa   As   Sk   Sa   Ss\n");
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < 1 + N + M/* + M*/; j++) {
				if(j == 0) {
					if(i == 0)
						output.append(stateLabels[i] + ": ¹ = [");
					else
						output.append(stateLabels[i] + ":     [");
				}
				
				if(j == 1) {
					output.deleteCharAt(output.length()-1);
					if(i == 0)
						output.append("]  A = [");
					else
						output.append("]      [");
				}
				
				if(j == N + 1) {
					output.deleteCharAt(output.length()-1);
					if(i == 0)
						output.append("]  B = [");
					else
						output.append("]      [");
				}
				/*
				if(j == M + N + 1) {
					output.deleteCharAt(output.length()-1);
					if(i == 0)
						output.append("]  Bv = [");
					else
						output.append("]       [");
				}
				*/
				if (j == 0)
					output.append(String.format("%.2f ", pi[i]));
				else if (j < N + 1)
					output.append(String.format("%.2f ", A[i][j-1]));
				else
					output.append(String.format("%.2f ", B[i][j-N-1]));
				/*
				else if (j < M + N + 1)
					output.append(String.format("%.2f ", Bh[i][j-N-1]));
				else
					output.append(String.format("%.2f ", Bv[i][j-M-N-1]));
					*/
			}
			output.deleteCharAt(output.length()-1);
			output.append("]\n");
		}
		
		return output.toString();
	}
	
	/*
	@Override
	public int hashCode() {
		return getMapString().hashCode();
	}
	
	public String getMapString() {
		if(stateLabels != null && stateLabels.length == 3)
			return (stateLabels[0] + stateLabels[1] + stateLabels[2]);
		return "";
	}
	*/
}

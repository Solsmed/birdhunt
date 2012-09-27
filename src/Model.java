

public class Model {
	public double[][] A;
	public double[][] B;
	public double[] pi;
	
	public int N;
	public int M;
	
	public Model(Model l) {
		A = new double[l.N][l.N];
		B = new double[l.N][l.M];
		pi = new double[l.N];
		N = l.N;
		M = l.M;
		
		System.arraycopy(l.pi, 0, pi, 0, N);
		for(int i = 0; i < N; i++) {
			System.arraycopy(l.A, 0, A, 0, N);
			System.arraycopy(l.B, 0, B, 0, M);
		}
	}
	
	public Model(int N, int M) {
		this(new double[N][N], new double[N][M], new double[N]);
	}
	
	public Model(double[][] A, double[][] B, double[] pi) {
		this.A = A;
		this.B = B;
		this.pi = pi;
		
		this.N = A.length;
		this.M = B[0].length;
	}
	
	public String toString() {
		StringBuffer output = new StringBuffer();
		
		output.append("Model:\n");
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < 1 + N + M; j++) {
				if(j == 0) {
					if(i == 0)
						output.append("¹ = [");
					else
						output.append("    [");
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
				
				if (j == 0)
					output.append(String.format("%.2f ", pi[i]));
				else if (j < N + 1)
					output.append(String.format("%.2f ", A[i][j-1]));
				else
					output.append(String.format("%.2f ", B[i][j-N-1]));
			}
			output.deleteCharAt(output.length()-1);
			output.append("]\n");
		}
		
		return output.toString();
	}
}

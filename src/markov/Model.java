package markov;

public class Model {
	protected double[][] A;
	protected double[][] B;
	protected double[] pi;
	
	protected int N;
	protected int M;
	
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
	
	public int M() { return M; }
	public int N() { return N; }
	
	public double A(int i, int j) {
		return A[i][j];
	}
	
	/*
	public double[][] A() {
		return A;
	}
	*/
	
	public double B(int j, int k) {
		return B[j][k];
	}
	
	/*
	public double[][] B() {
		return B;
	}
	*/
	
	public double Pi(int i) {
		return pi[i];
	}
	
	/*
	public double[] Pi() {
		return pi;
	}
	*/
}

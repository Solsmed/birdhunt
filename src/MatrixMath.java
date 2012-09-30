
public class MatrixMath {
	public static double dot(double[] a, double[] b) {
		double sum = 0;
		for(int i = 0; i < a.length; i++) {
			sum += a[i]*b[i];
		}
		
		return sum;
	}
	
	private static double[][] make2D(double[] u) {
		double[][] u2D = new double[1][u.length];
		//System.arraycopy(u, 0, u2D[0], 0, u.length);
		u2D[0] = u;
		return u2D;
	}
	
	public static double[] multi(double[][] a, double[] b) {
		return transpose(multi(a, transpose(make2D(b))))[0];
	}
	
	public static double[] multi(double[] a, double[][] b) {
		return multi(make2D(a), b)[0];
	}
	
	public static double[][] multi(double[][] a, double[][] b) {
		int N = a.length;
		int M = b[0].length;
		
		double[][] c = new double[N][M];
		
		double[][] bt = transpose(b);
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				c[i][j] = dot(a[i],bt[j]);
			}
		}
		
		return c;
	}
	
	public static double maxValue(double[] a) {
		return maxValue(make2D(a));
	}
	
	public static double maxValue(double[][] a) {
		int N = a.length;
		int M = a[0].length;
		
		double maxValue = Double.NEGATIVE_INFINITY;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(a[i][j] > maxValue)
					maxValue = a[i][j];
			}
		}
		
		return maxValue;
	}
	
	public static double[][] transpose(double[][] a) {
		int N = a.length;
		int M = a[0].length;
		
		double[][] at = new double[a.length][a[0].length];
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				at[i][j] = a[j][i];
			}
		}
		
		return at;
	}
	
	public static double matrixNormDistance(double[][] A, double[][] B) {
		// root of element-wise squared distance, normalised to [0..1]
		int N = A.length;
		int M = A[0].length;
		
		double sumSquaredDist = 0;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				sumSquaredDist += (A[i][j] - B[i][j])*(A[i][j] - B[i][j]);
			}
		}
		
		return Math.sqrt(sumSquaredDist / (N*M));
	}
	
	public static boolean equal(double[] a, double[][] b) {
		return equal(make2D(a), b);
	}

	public static boolean equal(double[][] a, double[] b) {
		return equal(a, make2D(b));
	}
	
	public static boolean equal(double[][] a, double[][] b) {
		int N = a.length;
		int M = a[0].length;
		
		boolean equal = true;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				equal &= (a[i][j] == b[i][j]);
			}
		}
		
		return equal;
	}
	
	public static String MatrixToString(double[][] a) {
		int N = a.length;
		
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < N; i++) {
			sb.append(MatrixToString(a[i]));
			sb.append('\n');
		}
		sb.deleteCharAt(sb.length()-1);
		
		return sb.toString();
	}
	
	public static String MatrixToString(double[] row) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("[");
		for(int j = 0; j < row.length; j++) {
			sb.append(row[j]);
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		
		return sb.toString();
	}
}

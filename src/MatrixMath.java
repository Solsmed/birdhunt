
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
		
		double[][] at = new double[a[0].length][a.length];
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				at[j][i] = a[i][j];
			}
		}
		
		return at;
	}
	
	public static double matrixNorm2(double[][] A, double[][] B) {
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
	
	public static double matrixNormInf(double[][] A, double[][] B) {
		// root of element-wise squared distance, normalised to [0..1]
		int N = A.length;
		int M = A[0].length;
		
		double max = 0;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				double norm = Math.abs(A[i][j] - B[i][j]);
				if(norm > max)
					max = norm;
			}
		}
		return max;
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
	
	public static double[][] copy(double[][] a) {
		double[][] c = new double[a.length][];
		
		for(int i = 0; i < a.length; i++) {
			c[i] = new double[a[i].length];
			System.arraycopy(a[i], 0, c[i], 0, a[i].length);
		}
		
		return c;
	}
	
	public static double[] rowSums(double[][] a) {
		double[] ret = new double[a.length];
		for(int i = 0; i < a.length; i++) {
			ret[i] = rowSum(a[i]); 
		}
		return ret;
	}
	
	public static double rowSum(double[] a) {
		double sum = 0;
		for(int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum;
	}
	
	public static double[][] makeRowStochastic(double[][] a) {
		double[] sums = rowSums(a);
		double[][] ret = new double[a.length][];
		
		for(int i = 0; i < a.length; i++) {
			ret[i] = new double[a[i].length];
			for(int j = 0; j < a[i].length; j++) {
				ret[i][j] = a[i][j] / sums[i];
			}
		}
		
		return ret;
	}
}

import java.util.Random;


public class SequenceGenerator {

	public static String seqToString(int[][] seq) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < seq.length; i++) {
			sb.append(seq[0][i]);
			sb.append(" -> ");
			sb.append(seq[1][i]);
			sb.append('\n');
		}
		return sb.toString();
	}
	
	/**
	 * generatedSequence[0 = state, 1 = observation][timeStep]
	 * 
	 * @param A
	 * @param B
	 * @param pi
	 * @param length
	 * @return
	 */
	public static int[][] generateSequence(double[][] A, double[][] B, double[] pi, int length) {
		int[][] observationSequence = new int[2][length];
		
		int currentState = getVectorIndex(pi);
		for(int i = 0; i < length; i++) {
			observationSequence[0][i] = currentState;
			observationSequence[1][i] = getVectorIndex(B[currentState]);
			currentState = getVectorIndex(A[currentState]);
		}
		
		return observationSequence;
	}
	
	private static int getVectorIndex(double[] row) {
		Random random = new Random();
		
		double marker = random.nextDouble();
		double progress = 0;
		for(int i = 0; i < row.length; i++) {
			progress += row[i];
			if(marker <= progress) {
				return i;
			}
		}
		return -1;
	}
}

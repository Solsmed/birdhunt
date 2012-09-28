import java.util.Random;


public class SequenceGenerator {

	public static String seqToString(int[] seq) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < seq.length; i++) {
			sb.append(seq[i]);
			sb.append(", ");
		}
		return sb.toString();
	}
	
	public static int[] generateSequence(double[][] A, double[][] B, double[] pi, int length) {
		int[] observationSequence = new int[length];
		
		int currentState = getVectorIndex(pi);
		for(int i = 0; i < observationSequence.length; i++) {
			observationSequence[i] = getVectorIndex(B[currentState]);
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

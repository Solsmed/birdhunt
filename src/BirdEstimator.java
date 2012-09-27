
public class BirdEstimator {
	public static Model estimate(String[] labels, double[][] BinitH, double[][] BinitV, ObservationSequence o) {
		// Initialise
		int BigN = BinitH.length;
		int N = 3;
		int numPermutations = 4; // BigN choose N
		
		int M = BinitH[0].length;
		
		int H = 0;
		int V = 1;
		
		Model m[][] = new Model[numPermutations][2];
		for(int c = 0; c < numPermutations; c++) {
			m[c][H] = HMMFunction.getInitModel(N, M);
			m[c][V] = HMMFunction.getInitModel(N, M);
		}
		
		int notIndex = 0;
		
		for(int p = 0; p < numPermutations; p++) {
			notIndex++;
			int mIndex = 0;
			for(int i = 0; i < BinitH.length; i++) {
				if(i != notIndex) {
					System.arraycopy(BinitH[i], 0, m[p][H].B[mIndex], 0, M);
					System.arraycopy(BinitV[i], 0, m[p][V].B[mIndex], 0, M);
					mIndex++;
				}
			}		
		}
		
		// refine models
		
		
		return null;
	}
}

package markov;


public class HMMFunction {
	public static Model getInitModel(int N, int M) {
		Model lambda = new Model(N, M);
		
		
		
		return lambda;
	}
	
	public static Model refineModel(Model oldLambda) {
		Model newLambda = new Model(oldLambda);
		
		
		
		return newLambda;
	}
}

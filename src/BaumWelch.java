public class BaumWelch {
	
	private static double[][] fs,bs;
	private static double[] scale;
	private static double logPseq;
	
	public static MarkovModel train(BirdModel modObs, int maxiter) {
		double tol = 1e-6;
		double trtol = tol;
		double etol = tol;
		
		MarkovModel trained = new MarkovModel(modObs.lambda);
		int numStates = modObs.lambda.N;
		int seqLength = modObs.O.T;
		int numEmissions = modObs.lambda.M;
		
		double[][] guessTR = modObs.lambda.A;
		double[][] guessE = modObs.lambda.B;
		int[] seq = modObs.O.action;
		
		double[][] TR = new double[modObs.lambda.N][modObs.lambda.N];
		double[][] E = new double[modObs.lambda.N][modObs.lambda.M];
		
		boolean converged = false;
		double loglik = 1; // loglik is the log likelihood of all sequences given the TR and E
		double[] logliks = new double[maxiter];
		for (int iteration = 1; iteration < maxiter; iteration++) {
		    double oldLL = loglik;
		    loglik = 0;
		    double[][] oldGuessE = MatrixMath.copy(guessE);
		    double[][] oldGuessTR = MatrixMath.copy(guessTR);
		    //for (int count = 1; count < numSeqs; count++) {
	        /*
	    	if cellflag
	            seq = seqs{count};
	            seqLength = length(seq);
	        else
	            seq = seqs(count,:);
	        end
	        */

            if(true) { // get the scaled forward and backward probabilities
	            // sets globals [~,logPseq,fs,bs,scale]
	            hmmdecode(seq,guessTR,guessE);
	            loglik = loglik + logPseq;
	            double[][] logf = log(fs);
	            double[][] logb = log(bs);
	            double[][] logGE = log(guessE);
	            double[][] logGTR = log(guessTR);
	            // f and b start at 0 so offset seq by one
	            int[] seqNew = new int[seq.length + 1];
	            System.arraycopy(seq, 0, seqNew, 1, seq.length);
	            seqNew[0] = 0;
	            seq = seqNew;
	            
	            for (int k = 0; k < numStates; k++) {
	                for (int l = 0; l < numStates; l++) {
	                    for (int i = 0; i < seqLength; i++) {
	                        TR[k][l] = TR[k][l] + Math.exp( logf[k][i] + logGTR[k][l] + logGE[l][seq[i+1]] + logb[l][i+1]) / scale[i+1];
	                    }
	                }
	            }

	            for (int k = 0; k < numStates; k++) {
	                for (int i = 0; i < numEmissions; i++) {
	                    for(int t = 0; t < seqLength; t++) {
	                    	if(seq[t] == i)
	                    		E[k][i] = E[k][i] + Math.exp(logf[k][t]+logb[k][t]);
	                    }
	                }
	            }
	    	} else {  // Viterbi training
	    		/*
	            [estimatedStates,logPseq]  = hmmviterbi(seq,guessTR,guessE);
	            loglik = loglik + logPseq;
	            % w = warning('off');
	            [iterTR, iterE] = hmmestimate(seq,estimatedStates,'pseudoe',pseudoE,'pseudoTR',pseudoTR);
	            %warning(w);
	            % deal with any possible NaN values
	            iterTR(isnan(iterTR)) = 0;
	            iterE(isnan(iterE)) = 0;
	            
	            TR = TR + iterTR;
	            E = E + iterE;
	            */
	    	}
		    //}
		    double[] totalEmissions = MatrixMath.rowSums(E);
		    double[] totalTransitions = MatrixMath.rowSums(TR);
		    
		    // avoid divide by zero warnings
		    for(int i = 0; i < numEmissions; i++)
		    	for(int j = 0; j < numStates; j++)
		    		guessE[j][i] = E[j][i] / totalEmissions[j];
		    
		    for(int i = 0; i < numStates; i++)
		    	for(int j = 0; j < numStates; j++)
		    		guessTR[j][i]  = TR[j][i] / totalTransitions[j];
		    
		    //guessE = MatrixMath.makeRowStochastic(E);
		    //guessTR = MatrixMath.makeRowStochastic(TR);
		    
		    // if any rows have zero transitions then assume that there are no
		    // transitions out of the state.
		    /*
		    if any(totalTransitions == 0)
		        noTransitionRows = find(totalTransitions == 0);
		        guessTR(noTransitionRows,:) = 0;
		        guessTR(sub2ind(size(guessTR),noTransitionRows,noTransitionRows)) = 1;
		    end
		    */
		    // clean up any remaining Nans
		    for(int i = 0; i < modObs.lambda.N; i++) {
		    	for(int j = 0; j < modObs.lambda.N; j++) {
		    		if(Double.isNaN(guessTR[i][j]))
		    			guessTR[i][j] = 0;
		    	}
		    	for(int j = 0; j < modObs.lambda.M; j++) {
		    		if(Double.isNaN(guessE[i][j]))
		    			guessE[i][j] = 0;
		    	}
		    }   
		    
		    /*
		    if verbose
		        if iteration == 1
		            fprintf('%s\n',getString(message('stats:hmmtrain:RelativeChanges')));
		            fprintf('   Iteration       Log Lik    Transition     Emmission\n');
		        else 
		            fprintf('  %6d      %12g  %12g  %12g\n', iteration, ...
		                (abs(loglik-oldLL)./(1+abs(oldLL))), ...
		                norm(guessTR - oldGuessTR,inf)./numStates, ...
		                norm(guessE - oldGuessE,inf)./numEmissions);
		        end
		    end
		    */
		    
		    // Durbin et al recommend loglik as the convergence criteria  -- we also
		    // use change in TR and E. Use (undocumented) option trtol and
		    // etol to set the convergence tolerance for these independently.
		    //
		    logliks[iteration] = loglik;
		    /*
		    if ((Math.abs(loglik-oldLL)/(1+Math.abs(oldLL))) < tol) {
		        if (MatrixMath.matrixNormInf(guessTR,oldGuessTR)/numStates < trtol) {
		            if (MatrixMath.matrixNormInf(guessE, oldGuessE)/numEmissions < etol) {
		                
		            	//if (verbose)
		                //    fprintf('%s\n',getString(message('stats:hmmtrain:ConvergedAfterIterations',iteration)))
		                
		                converged = true;
		                break;
		            }
		        }
		    }
*/
		    E = new double[modObs.lambda.N][modObs.lambda.M];
		    TR = new double[modObs.lambda.N][modObs.lambda.N];
		    //E =  pseudoE;
		    //TR = pseudoTR;
		}
		
		trained.A = guessTR;
		trained.B = guessE;
		trained.isOptimal = converged;
		trained.logProb = loglik;
		trained.pi = new double[] {1, 0, 0};
		
		return trained;
    }
	
	
	// sets globals [~,logPseq,fs,bs,scale]
	private static void hmmdecode(int[] seq, double[][] tr, double[][] e) {
		int[] seqNew = new int[seq.length + 1];
        System.arraycopy(seq, 0, seqNew, 1, seq.length);
        seqNew[0] = 0;
        seq = seqNew;
        
        int L = seq.length;
        int numStates = tr.length;
        
        fs = new double[numStates][L];
        fs[0][0] = 1;  // assume that we start in state 1.
        double[] s = new double[L];
        s[0] = 1;
        for (int count = 1; count < L; count++) {
            for (int state = 0; state < numStates; state++) {
            	double sum = 0;
            	for (int i = 0; i < numStates; i++) {
            		sum += fs[i][count-1] * tr[i][state];
            	}
                fs[state][count] = e[state][seq[count]] * sum;
            }
            // scale factor normalizes sum(fs,count) to be 1.
            double sum = 0;
        	for (int i = 0; i < numStates; i++) {
        		sum += fs[i][count];
        	}
            s[count] =  sum;
            
            for (int i = 0; i < numStates; i++)
            	fs[i][count] =  fs[i][count]/s[count];
        }
        
        bs = new double[numStates][L];
        for(int i = 0; i < numStates; i++)
        	for(int j = 0; j < L; j++)
        		bs[i][j] = 1;
        
        for (int count = L-2; count >= 0; count--) {
            for (int state = 0; state < numStates; state++) {
            	double sum = 0;
            	for (int i = 0; i < numStates; i++)
            		sum += tr[state][i] * bs[i][count+1] * e[i][seq[count+1]];
            	bs[state][count] = (1/s[count+1]) * sum; 
        	}
        }
        
        scale = s;
        logPseq = MatrixMath.rowSum(log(s));
	}
	
	
	/*
	private static void hmmdecode(int[] O, double[][] A, double[][] B, double[] pi) {
		scale = new double[O.length+1];
		fs = new double[O.length+1][A.length];
		bs = new double[O.length+1][A.length];
		
		// writes to alpha[][] and c[]
		HMMFunction.alphaPass(A, B, pi, O, fs, scale);
		
		// writes to beta[][]
		HMMFunction.betaPass(A, B, O, bs, scale);
		
		fs = MatrixMath.transpose(fs);
		bs = MatrixMath.transpose(bs);
		
		logPseq = MatrixMath.rowSum(scale);
	}
	*/
	
	/*
    private static double[][] exp(double a[][]) {
    	double[][] ret = new double[a.length][];
    	for(int i = 0; i < a.length; i++) {
    		ret[i] = log(a[i]);
    	}
    	return ret;
    }
    
    private static double[] exp(double a[]) {
    	double[] ret = new double[a.length];
    	for(int i = 0; i < a.length; i++)
    		ret[i] = log(a[i]);
    	return ret;
    }
    
    private static double exp(double a) {
    	return Math.exp(a);
    }*/
	
    private static double[][] log(double a[][]) {
    	double[][] ret = new double[a.length][];
    	for(int i = 0; i < a.length; i++) {
    		ret[i] = log(a[i]);
    	}
    	return ret;
    }
    
    private static double[] log(double a[]) {
    	double[] ret = new double[a.length];
    	for(int i = 0; i < a.length; i++)
    		ret[i] = log(a[i]);
    	return ret;
    }
    
    private static double log(double a) {
    	return Math.log(a);
    }
}

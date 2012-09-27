import java.util.Vector;

public class ObservationSequence2D {
	ObservationSequence H;
	ObservationSequence V;
	
	public int birdNumber;
	
	public ObservationSequence2D(Vector<Action> mSeq) {
		H = new ObservationSequence(mSeq, true);
    	V = new ObservationSequence(mSeq, false);
	}
}

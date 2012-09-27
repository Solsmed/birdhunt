
public class Model2D {
	Model H;
	Model V;
	public int birdNumber;
	
	public Model2D(Model modelH, Model modelV) {
		this.H = modelH;
		this.V = modelV;
	}
	
	public boolean isOptimal() {
		return H.isOptimal && V.isOptimal;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Model (" + birdNumber + "):\n");
		sb.append(H.toString());
		sb.append(V.toString());
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return getMapString().hashCode();
	}
	
	public String getMapString() {
		if(H.stateLabels != null && H.stateLabels.length == 3)
			return (H.stateLabels[0] + H.stateLabels[1] + H.stateLabels[2]);
		return "";
	}
}

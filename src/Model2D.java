
public class Model2D {
	Model H;
	Model V;
	
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
		sb.append(H.toString());
		sb.append('\n');
		sb.append(V.toString());
		return sb.toString();
	}
}

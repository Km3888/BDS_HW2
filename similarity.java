///Literally useless method, instruction calls for this to have its own class but I define these seperately in different classes
//(Probably gonna wanna delete this note before submission)
public class similarity {
	public static double dot(double[] u, double[] v) {
		assert u.length==v.length;
		double total=0;
		for (int i=0;i<u.length;i++) {
			total=total+(u[i]*v[i]);
		}
		return total;
	}
	
	public static double cosine(double[] u,double[] v) {
		double val1=Math.sqrt(dot(u,u));
		double val2=Math.sqrt(dot(v,v));
		double dotted=dot(u,v);
		return 1- (dotted/(val1*val2));
	}
	
}

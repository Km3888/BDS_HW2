import java.util.Random;
public class Test_PCA {
	public static void main(String[] args) {
		double[][] dummy=random_matrix(30);
		double[][] tester=PCA.pca(dummy, 2);
	}
	
	public static double[][] random_matrix(int n) {
		double[][] output=new double[n][n];
		Random rand=new Random();
		for (int i=0;i<n;i++) {
			for (int j=0;j<n;j++) {
				output[i][j]=rand.nextInt(100);
			}
		}
		return output;
	}
}

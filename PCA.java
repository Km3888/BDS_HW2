import com.sun.tools.javac.util.List;
import java.util.*;
import java.io.*;

//Only function you need here is the pca method, call this on the finalized matrix with n=2 and it'll reduce it for you
public class PCA{
	public static double[][] transpose(double[][] matrix) {
		int m=matrix.length;
		int n=matrix[0].length;
		double[][] output=new double[n][m];
		for (int i=0;i<m;i++) {
			for (int j=0;j<n;j++) {
				output[j][i]=matrix[i][j];
			}
		}
		return output;
	}
	
	public static double[] column_vec(double[][] matrix,int j) {
		int m=matrix.length;
		int n=matrix[0].length;
		double[] vec=new double[m];
		assert j<n;
		for (int i=0;i<m;i++) {
			vec[i]=matrix[i][j];
		}
		return vec;
	}
	
	public static double dot(double[] u, double[] v) {
		assert u.length==v.length;
		double total=0;
		for (int i=0;i<u.length;i++) {
			total=total+(u[i]*v[i]);
		}
		return total;
	}
	
	public static double[][] matrix_multiply(double[][] left,double[][] right){
		int m1=left.length;
		int m2=right.length;
		int n1=left[0].length;
		int n2=right[0].length;
		assert n1==m2;
		double[][] output=new double[m1][n2];
		for (int i=0;i<m1;i++) {
			for (int j=0;j<n2;j++) {
				output[i][j]=dot(left[i],column_vec(right,j));
			}
		}
		return output;
	}
	
	public static double[][] covariance_matrix(double[][] matrix){
		double[][] matrix_t=transpose(matrix);
		return matrix_multiply(matrix_t,matrix);
	}
	public static double[] vector_constant(double[] input,double c) {
		double[] output=new double[input.length];
		for (int i=0;i<input.length;i++) {
			output[i]=input[i]*c;
		}
		return output;
	}
	public static double[] vector_add(double[] input1,double[] input2) {
		double[] output=new double[input1.length];
		for (int i=0;i<input1.length;i++) {
			output[i]=input1[i]+input2[i];
		}
		return output;
	}
	
	public static double[] vector_sub(double[] input1,double[] input2) {
		double[] output=new double[input1.length];
		for (int i=0;i<input1.length;i++) {
			output[i]=input1[i]-input2[i];
		}
		return output;
	}
	
	public static double[][] echelon_form(double[][] ma){//Reduces (m)x(m+1) matrix to echelon form
		double[][] matrix=ma;
		int m=matrix.length;
		for (int i=0;i<m;i++) {
			//Iterate over all the columns
			double[] vec1=matrix[i];
			int j=0;
			while (matrix[i][i]==0) {
				//Make sure key value is nonzero
				if (j==m) {break;}
				if (matrix[i][i+j]!=0) {
					matrix[i]=matrix[i+j];
					matrix[j]=vec1;
					matrix[i]=vector_constant(matrix[i],1/matrix[i][i]);
				}
				j++;
			}
			matrix[i]=vector_constant(matrix[i],1/matrix[i][i]);
			vec1=matrix[i];
			for (int k=i+1;k<m;k++) {
				double entry=matrix[k][i];
				if (entry!=0);{
					double[] altered=vector_sub(matrix[k],vector_constant(vec1,entry));
					matrix[k]=altered;
				}
			}
		}
		matrix[m-1]=vector_constant(matrix[m-1],matrix[m-1][m-1]);
		return matrix;
	}
	
	public static double[][] reduced_echelon(double[][] ma){
		double[][] matrix=echelon_form(ma);
		int m=matrix.length;
		for (int i=0;i<m;i++) {
			//Note that here i is the column index, opposite of usual
			for (int j=0;j<i;j++) {
				if (matrix[j][i]!=0) {
					matrix[j]=vector_sub(matrix[j],vector_constant(matrix[i],matrix[j][i]));
				}
			}
		}
		return matrix;
	}
	public static double[] solution(double[][] matrix, double[] vec) {
		int m=matrix.length;
		double[][] big_mat=new double[m][m+1];
		for (int i=0;i<m;i++) {
			for (int j=0;j<m;j++) {
				big_mat[i][j]=matrix[i][j];
			}
			big_mat[i][m]=vec[i];
		}
		double[][] solved=reduced_echelon(big_mat);
		return column_vec(solved,m);
	}	
	public static double[] normalize(double[] v) {
		return vector_constant(v,1/Math.sqrt(dot(v,v)));
	}	
	public static double[][] identityMatrix(int n) {
		double[][] I=new double[n][n];
		for (int i=0;i<n;i++) {
			for (int j=0;j<n;j++) {
				if (i==j) {
					I[i][j]=1;
				}
				else {
					I[i][j]=0;
				}
			}
		}
		return I;
	}
	
	public static double[][] matrixSub(double[][] m1,double[][] m2) {
		int m=m1.length;
		double[][] output=new double[m][m];
		for (int i=0;i<m;i++) {
			for (int j=0;j<m;j++) {
				output[i][j]=m1[i][j]-m2[i][j];
			}
		}
		return output;
	}
	
	public static double[][] matrix_scalar(double[][] matrix,double scalar){
		int m=matrix.length;
		double[][] output=new double[m][m];
		for (int i=0;i<m;i++) {
			for (int j=0;j<m;j++) {
				output[i][j]=matrix[i][j]*scalar;
			}
		}
		return output;
	}
	public static double[] matrixVec(double[][] matrix,double[] vector) {
		int m=matrix.length;
		double[] output=new double[m];
		for (int i=0;i<m;i++) {
			output[i]=dot(matrix[i],vector);
		}
		return output;
	}
	public static double length(double[] v) {
		return Math.sqrt(dot(v,v));
	}
	public static double[][] rayleigh(double[][] matrix,double start) {
		boolean close=false;
		int m=matrix.length;
		double[] guess=new double[m];
		for (int i=0;i<m;i++) {
			guess[i]=Math.random()*100;
		}
		double[] eigenvector=normalize(guess);
		double eigenvalue=start;
		int iters=0;
		while (close==false) {
			assert length(eigenvector)==1;
			double[][] scaled=matrix_scalar(identityMatrix(m),eigenvalue);
			double[][] combined=matrixSub(matrix,scaled);
			double[] n_eigenvector=normalize(solution(combined,eigenvector));
			double n_eigenvalue=dot(n_eigenvector,matrixVec(matrix,n_eigenvector));
			double[] diff=vector_sub(matrixVec(matrix,n_eigenvector),vector_constant(n_eigenvector,n_eigenvalue));
			double error_bound=Math.sqrt(dot(diff,diff));
			eigenvector=n_eigenvector;
			eigenvalue=n_eigenvalue;
			if (error_bound<0.1){
				break;
			}
			iters++;
			if (iters>100000) {
				double[][] output=new double[2][m];
				for (int i=0;i<2;i++) {
					for(int j=0;j<m;j++) {
						output[i][j]=0;
					}
				}
				return output;
			}
		}
		double[][] both=new double[2][m];
		both[0]=eigenvector;
		both[1][0]=eigenvalue;
		return both;
	}
	public static double[][] find_eigenvalues(double[][] matrix) {
		int m=matrix.length;
		double[] output=new double[m];
		int found=0;
		int start=0;
		double[][] vectors=new double[m+1][m];
		while (true) {
			double[][] values=rayleigh(matrix,matrix[start][start]);
			double[] vector=values[0];
			double eigen=values[1][0];
			boolean novel=true;
			if (eigen<.001){
				novel=false;
			}
			for (int i=0;i<found;i++) {
				if (Math.abs(eigen-output[i])<.01) {
					novel=false;
				}
			}
			if (novel) {
				output[found]=eigen;
				vectors[found]=vector;
				found++;
				System.out.println("Found "+found+" eigenvectors");
				if (found>=m*.8) {
					break;
				}
			}
			else {
				start++;
				if (start==m) {
					start=0;
				}
			}
		}
		vectors[m]=output;
		return vectors;
	}
	
	public static double[][] find_some_eigens(double[][] matrix, int n){
		int m=matrix.length;
		double[] output=new double[m];
		double[] eigens=new double[n];
		int found=0;
		int start=0;
		List<Double> start_vals=new ArrayList<Double>();
		List<Double> eigen_list=new ArrayList<Double>();
		double max_val=0;
		double min_val=-1;
		double min_dist_pos=Double.POSITIVE_INFINITY;
		double min_dist_neg=-.5;
		for (int i=0;i<m;i++) {
			double val=matrix[i][i];
			if (val>max_val) {
				max_val=val;
			}
			if (val<min_dist_pos) {
				min_dist_pos=val;
			}
			start_vals.add(val);
		}
		double[][] vectors=new double[m+1][m];
		while (true) {
			double[][] values=rayleigh(matrix,start_vals.get(start));
			double[] vector=values[0];
			double eigen=values[1][0];
			boolean novel=true;
			if (eigen<.001){
				novel=false;
			}
			for (int i=0;i<found;i++) {
				if (Math.abs(eigen-output[i])<.01) {
					novel=false;
				}
			}
			if (novel) {
				eigens[found]=eigen;
				eigen_list.add(eigen);
				output[found]=eigen;
				vectors[found]=vector;
				found++;
				System.out.println("Found "+found+" eigenvectors/values");
				if (found==n) {
					break;
				}
			}
			else {
				start++;
				if (start==start_vals.size()) {
					assert (false);
					start=0;
					max_val=1.1*max_val;
					min_val=1.1*min_val;
					min_dist_pos=.9*min_dist_pos;
					min_dist_neg=.9*min_dist_neg;
					start_vals.add(min_dist_pos);
					start_vals.add(min_dist_neg);
					start_vals.add(max_val);
					start_vals.add(min_val);
				}
			}
		}
		vectors[m]=output;
		return vectors;
	}
	public static double[][] make_eigenvalue_matrix(double[][] matrix,int n){
		int m=matrix.length;
		double[][] output=new double[n][m];
		double[][] eigens=find_some_eigens(matrix,matrix.length);
		double[] eigenvalues=eigens[m];
		int[] used=new int[m];
		for (int i=0;i<n;i++) {
			double max=0;
			int best=0;
			for (int j=0;j<m;j++) {
				if (eigenvalues[j]>max) {
					boolean no=false;
					for (int k=0;k<i;k++) {
						if (used[k]==j) {
							no=true;
						}
					}
					if (no==false) {
						max=eigenvalues[j];
						best=j;
					}
				}
			}
			used[i]=best;
			output[i]=eigens[best];
		}
		return transpose(output);
	}
	public static double[][] centered(double[][] matrix){
		int n=matrix.length;
		double[][] output=new double[n][n];
		for (int j=0;j<n;j++) {
			double total=0;
			//first we calculate the mean of column i
			for (int i=0;i<n;i++) {
				total=total+matrix[i][j];
			}
			double average=total/n;
			for (int i=0;i<n;i++) {
				output[i][j]=matrix[i][j]-average;
			}
		}
		return output;
	}
	public static double[][] pca(double[][] matrix, int n){
		double[][] B=centered(matrix);
		double[][] covariance=covariance_matrix(B);
		double[][] eigenmatrix=make_eigenvalue_matrix(covariance,n);
		return matrix_multiply(matrix,eigenmatrix);
		
	}
}
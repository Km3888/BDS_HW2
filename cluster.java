import java.util.*;

//Only method you need to call here is improve_kmeans, which will return an integer array
//If the ith element of the array is equal to j then that means the ith datapoint belongs 
//the jth class
import java.io.*;
public class cluster {

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
	
	public static int[] kmeans(double[][] matrix,double[][] class_matrix,int k){
		boolean converged=false;
		int num_docs=matrix.length;
		int dim=matrix[0].length;
		int[] assignment=new int[num_docs];
		while (converged==false) {
			int[] new_assignment=new int[num_docs];
			//Repeat process until convergence
			for (int i=0;i<num_docs;i++) {
				//Loop over all the documents and assign them to the closest centroid
				double[] doc=matrix[i];
				double min_dist=Double.POSITIVE_INFINITY;
				int best=0;
				for (int j=0;j<k;j++) {
					//Loop over all the centroids and find the closest one
					double[] centroid=class_matrix[j];
					double distance=cosine(doc,centroid);
					if (distance<min_dist) {
						best=j;
						min_dist=distance;
					}
				}
				new_assignment[i]=best;
			}
			converged=true;
			for (int x=0;x<num_docs;x++) {
				if (assignment[x]!=new_assignment[x]) {
					converged=false;
				}
			}
			assignment=new_assignment;
			for (int i=0;i<k;i++) {
				//Loop over all the classes, setting the centroid to be the average of the docs
				double[] running=new double[dim];
				int total_docs=0;
				for (int j=0;j<num_docs;j++) {
					if (assignment[j]==i) {
						for (int m=0;m<dim;m++) {
							running[m]=running[m]+ matrix[j][m];
							total_docs++;
						}
					}
				}
				for (int l=0;l<dim;l++) {
					running[l]=running[l]/total_docs;
				}
				class_matrix[i]=running;
			}
		}
		return assignment;
	}
	
	public static double[][] kmeans_init(double[][] matrix,int k){
		int num_docs=matrix.length;
		int dim=matrix[0].length;
		double[][] centroids=new double[k][dim];
		Random r=new Random();
		int first_ind=r.nextInt(num_docs);
		centroids[0]=matrix[first_ind];//Assign the first centroid to be a random 
		int[] assignment=new int[num_docs];
		double[] d_squared=new double[num_docs];
		for (int i=1;i<k;i++) {
			//Repeat to find k-1 centroids			
			for (int j=0;j<num_docs;j++) {
				//loop over all the docs to assign them and find their distances
				int best_assignment=0;
				double smallest_distance=Double.POSITIVE_INFINITY;
				double[] datapoint=matrix[j];
				for (int l=0;l<i;l++) {
					//loop over all the existing centroids to find the one with the shorted distance
					double[] centroid=centroids[l];
					double distance=cosine(datapoint,centroid);
					if (distance<smallest_distance) {
						smallest_distance=distance;
						best_assignment=l;
					}
				}
				d_squared[j]=smallest_distance*smallest_distance;
				assignment[j]=best_assignment;
				
			}
			//Compute cumulative probability array
			double[] cumulative=new double[num_docs];
			cumulative[0]=d_squared[0];
			for (int j=1;j<num_docs;j++) {
				cumulative[j]=cumulative[j-1]+d_squared[j];
			}
			double p=java.lang.Math.random()*cumulative[num_docs-1];
			for (int j=0;j<num_docs;j++) {
				if (cumulative[j]>=p) {
					centroids[i]=matrix[j];
					break;
					
				}
			}
		}
	return centroids;
		
	}
	public static int[] improved_kmeans(double[][] matrix,int k) {
		double[][] centroids=kmeans_init(matrix,k);
		return kmeans(matrix,centroids,k);
	}
}

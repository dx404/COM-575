package pipeline.math;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public class BaryCen {
	public float alpha, beta, gamma;
	public Vector4f v_a, v_b, v_c;
	public Vector4f target = new Vector4f();
	
	public int x_min, x_max;
	public int y_min, y_max;
	public Vector3f centroid = new Vector3f(-1.1f, -1.1f, -1.1f);
	
	//keep track of the interior pixel indexes;
	public int[][] interior;
	
	private float x_min_f, x_max_f;
	private float y_min_f, y_max_f;
	
	private float beta_0, beta_x, beta_y;
	private float gamma_0, gamma_x, gamma_y;
	
	public BaryCen(Vector4f v_a, Vector4f v_b, Vector4f v_c){
		this.v_a = v_a;
		this.v_b = v_b;
		this.v_c = v_c;
		getBound();
		getCentroid();
		computeConst();
	}
	
	private void computeConst(){
		float beta_denominator = 
				(v_a.y - v_c.y) * v_b.x + 
				(v_c.x - v_a.x) * v_b.y + 
				v_a.x * v_c.y - v_c.x * v_a.y;
		
		beta_0 = (v_a.x * v_c.y - v_c.x * v_a.y) / beta_denominator;
		beta_x = (v_a.y - v_c.y) / beta_denominator;
		beta_y = (v_c.x - v_a.x)/ beta_denominator;
		
		
		float gamma_denominator = 
				(v_a.y - v_b.y) * v_c.x + 
				(v_b.x - v_a.x) * v_c.y + 
				v_a.x * v_b.y - v_b.x * v_a.y;
		
		gamma_0 =  (v_a.x * v_b.y - v_b.x * v_a.y) / gamma_denominator;
		gamma_x =  (v_a.y - v_b.y) / gamma_denominator;
		gamma_y =  (v_b.x - v_a.x) / gamma_denominator;

	}
	
	private BaryCen setTarget(Vector4f target){
		this.target = target;
		getCoordinates();		
		return this;
	}
	
	public BaryCen setTarget(float x, float y){
		this.target = new Vector4f(x, y, 0, 1);
		getCoordinates();		
		return this;
	}
	
	private BaryCen setTarget(int x, int y){
		this.target.x = x;
		this.target.y = y;
		getCoordinates();		
		return this;
	}
	
	private void getCoordinates(){
		beta = beta_0 + beta_x * target.x + beta_y * target.y;
		gamma = gamma_0 + gamma_x * target.x + gamma_y * target.y;
		


		alpha = 1 - beta - gamma;
	}
	
	private void getBound(){
		x_min_f = v_a.x < v_b.x ? v_a.x : v_b.x;
		if (v_c.x < x_min_f) x_min_f = v_c.x;
		
		x_max_f = v_a.x > v_b.x ? v_a.x : v_b.x;
		if (v_c.x > x_max_f) x_max_f = v_c.x;
		
		y_min_f = v_a.y < v_b.y ? v_a.y : v_b.y;
		if (v_c.y < y_min_f) y_min_f = v_c.y;
		
		y_max_f = v_a.y > v_b.y ? v_a.y : v_b.y;
		if (v_c.y > y_max_f) y_max_f = v_c.y;
		
		x_min = (int) Math.floor(x_min_f);
		x_max = (int) Math.ceil(x_max_f);
		y_min = (int) Math.floor(y_min_f);
		y_max = (int) Math.ceil(y_max_f);
		
		interior = new int[(x_max - x_min + 1)*(y_max - y_min + 1)][2];
		interior[0][0] = -1;
		interior[0][1] = -1;
	}
	
	public void getCentroid(){
		centroid.x = (v_a.x + v_b.x + v_c.x) / 3;
		centroid.y = (v_a.y + v_b.y + v_c.y) / 3;
		centroid.z = (v_a.z + v_b.z + v_c.z) / 3;
	}
	
	//whole points
	public int[][] getInteriors(){
		
		setTarget(x_min, y_min);
		
		int x=0, y=0, k=0;
		float beta_row_start = beta;
		float gamma_row_start = gamma;
		//all rows but the last one
		for (y = y_min; y <= y_max; y++){
			beta = beta_row_start;
			gamma = gamma_row_start;
			alpha = 1 - beta - gamma;
			
			for (x = x_min; x <= x_max; x++){
				if (alpha >= 0 && beta >= 0 && gamma >= 0){
					interior[k][0] = x;
					interior[k][1] = y;
					k++;
				}
				beta += beta_x;
				gamma += gamma_x;
				alpha = 1 - beta - gamma;
			}
			
			beta_row_start += beta_y;
			gamma_row_start += gamma_y;
		}
		//mark the end
		interior[k][0] = -1;
		interior[k][1] = k;
		return interior;
	}
	
	public void print(){
		System.out.println("alpha = " + alpha);
		System.out.println("beta = " + beta);
		System.out.println("gamma = " + gamma);
		System.out.println("x_min = " + x_min);
		System.out.println("x_max = " + x_max);
		System.out.println("y_min = " + y_min);
		System.out.println("y_max = " + y_max);
		
		System.out.println("beta_0 = " + beta_0);
		System.out.println("beta_x = " + beta_x);
		System.out.println("beta_y = " + beta_y);
		System.out.println("gamma_0= " + gamma_0);
		System.out.println("gamma_x = " + gamma_x);
		System.out.println("gamma_y = " + gamma_y);
		
		for (int i = 0; interior[i][0] >= 0; i++){
			System.out.println("x = " + interior[i][0] + ", y = " + interior[i][1]);
		}
	}
}

/*	The source code for computing beta and gamma using 
 *  direct formulas. Incremental version was actually used instead.
	beta = (
		(v_a.y - v_c.y) * target.x + 
		(v_c.x - v_a.x) * target.y + 
		v_a.x * v_c.y - v_c.x * v_a.y
		)/(
		(v_a.y - v_c.y) * v_b.x + 
		(v_c.x - v_a.x) * v_b.y + 
		v_a.x * v_c.y - v_c.x * v_a.y
	);
	
	gamma = (
		(v_a.y - v_b.y) * target.x + 
		(v_b.x - v_a.x) * target.y + 
		v_a.x * v_b.y - v_b.x * v_a.y
		)/(
		(v_a.y - v_b.y) * v_c.x + 
		(v_b.x - v_a.x) * v_c.y + 
		v_a.x * v_b.y - v_b.x * v_a.y
	);
*/

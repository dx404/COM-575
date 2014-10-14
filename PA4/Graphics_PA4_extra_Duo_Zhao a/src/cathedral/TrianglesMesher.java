package cathedral;

import java.util.ArrayList;
import ray.math.Triangle;
import ray.math.Vector3;

public class TrianglesMesher {
	public ArrayList<Triangle> gTriangles;
	private static Vector3 pt_spherical_ref = new Vector3();
	
	//manually preset boundary
	public float theta_low, theta_high; 
	public float phi_low, phi_high;
	public int theta_steps, phi_steps;
	
	public ArrayList<Triangle>[][] cellRecorder ; //triangles to test
	public float theta_step_size, phi_step_size;
	
	public TrianglesMesher(ArrayList<Triangle> gTri, Vector3 pt_ref){
		gTriangles = gTri;
		pt_spherical_ref = pt_ref;
		for (Vector3 v : Triangle.gPositions)
			v.getSpherical(pt_ref);
	}
	
	public static void setRef (Vector3 pt_ref){
		pt_spherical_ref = pt_ref;
		for (Vector3 v : Triangle.gPositions)
			v.getSpherical(pt_ref);
	}
	
	public void setThetaBoudary(float theta_low, float theta_high, int theta_steps){
		this.theta_low = theta_low;
		this.theta_high = theta_high;
		this.theta_steps = theta_steps;
	}
	public void setPhiBoundary(float phi_low, float phi_high, int phi_steps){
		this.phi_low = phi_low;
		this.phi_high = phi_high;
		this.phi_steps = phi_steps;
	}
	
	@SuppressWarnings("unchecked")
	public void build(){
		theta_step_size = (theta_high - theta_low)/theta_steps;
		phi_step_size = (phi_high - phi_low)/phi_steps;
		cellRecorder = new ArrayList [theta_steps][phi_steps];
		for (int i = 0; i < theta_steps; i++)
			for (int j = 0; j < phi_steps; j++)
				cellRecorder[i][j] = new ArrayList<Triangle>();
		
		for (Triangle tri : gTriangles){
			tri.getSphericalBound(pt_spherical_ref);
			int theta_start_index = (int) Math.floor((tri.theta_min - theta_low)/theta_step_size);
			int theta_end_index = (int) Math.ceil((tri.theta_max - theta_low)/theta_step_size);
			int phi_start_index = (int) Math.floor((tri.phi_min - phi_low)/phi_step_size);
			int phi_end_index = (int) Math.ceil((tri.phi_max - phi_low)/phi_step_size);
			for (int i = Math.max(0, theta_start_index); i < Math.min(theta_steps, theta_end_index); i++)
				for (int j = Math.max(0, phi_start_index); j < Math.min(phi_steps, phi_end_index); j++)
					cellRecorder[i][j].add(tri);
		}
	}
	
	public ArrayList<Triangle> getPotentialTris(float theta, float phi){
		int theta_index =  (int) Math.floor((theta - theta_low)/theta_step_size);
		int phi_index = (int) Math.floor((phi - phi_low)/phi_step_size);
		if (theta_index < 0 ||
			theta_index >= theta_steps ||
			phi_index < 0 ||
			phi_index >= phi_steps) {
				return new ArrayList<Triangle>();
		}
		return cellRecorder[theta_index][phi_index];
	}
}

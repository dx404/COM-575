package ray.math;

import java.util.ArrayList;


public class Triangle {
	public int[] indices = new int[3];
	public Vector3 v0, v1, v2;
	public Vector3 unitNormal; //with positive z value
	
	//for the boounding box
	public float xmin, ymin, zmin;
	public float xmax, ymax, zmax;
	
	//spherical coordinate
	public float radius_min, theta_min, phi_min;
	public float radius_max, theta_max, phi_max;
	
	public Vector3 
			ka = new Vector3(0, 0, 0), 
			kd = new Vector3(1, 1, 1), 
			ks = new Vector3(0, 0, 0);
	public float sp = 0; //specular power
	public float rw; //reflection weight
	
	public static ArrayList<Vector3> gPositions; //Reference to vertext set
	public static void setVerticesSet(ArrayList<Vector3> gPositions){
		Triangle.gPositions = gPositions;
	}
	
	public void getSphericalBound(Vector3 pt){
		v0.getSpherical(pt);
		v1.getSpherical(pt);
		v2.getSpherical(pt);
		
		radius_min = v0.radius;
		if (v1.radius < radius_min) radius_min = v1.radius;
		if (v2.radius < radius_min) radius_min = v2.radius;
		
		theta_min = v0.theta;
		if (v1.theta < theta_min) theta_min = v1.theta;
		if (v2.theta < theta_min) theta_min = v2.theta;
		
		phi_min = v0.phi;
		if (v1.phi < phi_min) phi_min = v1.phi;
		if (v2.phi < phi_min) phi_min = v2.phi;
		
		radius_max = v0.radius;
		if (v1.radius > radius_max) radius_max = v1.radius;
		if (v2.radius > radius_max) radius_max = v2.radius;
		
		theta_max = v0.theta;
		if (v1.theta > theta_max) theta_max = v1.theta;
		if (v2.theta > theta_max) theta_max = v2.theta;
		
		phi_max = v0.phi;
		if (v1.phi > phi_max) phi_max = v1.phi;
		if (v2.phi > phi_max) phi_max = v2.phi;
		
	}
	
	public void calVerices(){
		v0 = gPositions.get(indices[0]);
		v1 = gPositions.get(indices[1]);
		v2 = gPositions.get(indices[2]);
	}
	
	public void getBound(){
		xmin = v0.x;
		if (v1.x < xmin) xmin = v1.x;
		if (v2.x < xmin) xmin = v2.x;
		
		ymin = v0.y;
		if (v1.y < ymin) ymin = v1.y;
		if (v2.y < ymin) ymin = v2.y;
		
		zmin = v0.z;
		if (v1.z < zmin) zmin = v1.z;
		if (v2.z < zmin) zmin = v2.z;
		
		xmax = v0.x;
		if (v1.x > xmax) xmax = v1.x;
		if (v2.x > xmax) xmax = v2.x;
		
		ymax = v0.y;
		if (v1.y > ymax) ymax = v1.y;
		if (v2.y > ymax) ymax = v2.y;
		
		zmax = v0.z;
		if (v1.z > zmax) zmax = v1.z;
		if (v2.z > zmax) zmax = v2.z;
	}
	
	public void calUnitNormal(){
		float nx, ny, nz;
		nx = v2.y *(v1.z - v0.z) +
				v1.y *(v0.z - v2.z) + 
				v0.y *(v2.z - v1.z);
		ny = v2.x *(v0.z - v1.z) +
				v0.x *(v1.z - v2.z) + 
				v1.x *(v2.z - v0.z);
		nz = v2.x *(v1.y - v0.y) +
				v1.x *(v0.y - v2.y) + 
				v0.x *(v2.y - v1.y);
		unitNormal = new Vector3(nx, ny, nz).normalize(0);
	}
	
	public static Float[] getMaxSpherical(){
		return Vector3.getMaxSpherical(gPositions);
	}
	public static Float[] getMinSpherical(){
		return Vector3.getMinSpherical(gPositions);
	}

	public RayTouchInfo getIntersect(Ray ray) {
		RayTouchInfo rti = new RayTouchInfo(this, ray);
		float dx = ray.direction.x;
		float dy = ray.direction.y;
		float dz = ray.direction.z;
		
		float x0 = this.v0.x - ray.origin.x;
		float y0 = this.v0.y - ray.origin.y;
		float z0 = this.v0.z - ray.origin.z;
		
		float x1 = this.v1.x - ray.origin.x;
		float y1 = this.v1.y - ray.origin.y;
		float z1 = this.v1.z - ray.origin.z;
		
		float x2 = this.v2.x - ray.origin.x;
		float y2 = this.v2.y - ray.origin.y;
		float z2 = this.v2.z - ray.origin.z;
		
		rti.t = (-x2*y1*z0 + x1* y2* z0 + x2*y0*z1 - x0*y2*z1 - x1*y0*z2 + 
				x0*y1*z2)/(dz*(x2*y0 + x0*y1 - x2*y1 - x0*y2 + x1*(-y0 + y2)) + 
						dy*(x1*z0 - x2*z0 - x0*z1 + x2*z1 + x0*z2 - x1*z2) + 
						dx*(-y1*z0 + y2*z0 + y0*z1 - y2*z1 - y0*z2 + y1*z2));
		rti.alpha = (-dz*x2*y1 + dz*x1*y2 + dy*x2*z1 - dx*y2*z1 - dy*x1*z2 + 
				dx*y1*z2)/(dz*(x2*y0 + x0*y1 - x2*y1 - x0*y2 + x1*(-y0 + y2)) + 
						dy*(x1*z0 - x2*z0 - x0*z1 + x2*z1 + x0*z2 - x1*z2) + 
						dx*(-y1*z0 + y2*z0 + y0*z1 - y2*z1 - y0*z2 + y1*z2));
		rti.beta = (dz*x2*y0 - dz*x0*y2 - dy*x2*z0 + dx*y2*z0 + dy*x0*z2 - 
				dx*y0*z2)/(dz*(x2*y0 + x0*y1 - x2*y1 - x0*y2 + x1*(-y0 + y2)) + 
						dy*(x1*z0 - x2*z0 - x0*z1 + x2*z1 + x0*z2 - x1*z2) + 
						dx*(-y1*z0 + y2*z0 + y0*z1 - y2*z1 - y0*z2 + y1*z2));
		rti.gamma = 1 - rti.alpha - rti.beta;
		
		float px = ray.origin.x + rti.t * ray.direction.x;
		float py = ray.origin.y + rti.t * ray.direction.y;
		float pz = ray.origin.z + rti.t * ray.direction.z;
		rti.point = new Vector3 (px, py, pz);
		if (rti.t < ray.start || rti.t > ray.end ||
				rti.alpha < 0 || rti.beta < 0 || rti.gamma < 0)
			return null;
		return rti;
	}
	
}

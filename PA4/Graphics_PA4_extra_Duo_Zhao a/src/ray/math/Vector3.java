package ray.math;

import java.util.ArrayList;

public class Vector3 {
	public float x, y, z;
	public float radius, theta, phi;
	
	public Vector3() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vector3(Vector3 t) {
		x = t.x;
		y = t.y;
		z = t.z;
	}
	
	public Vector3 set (Vector3 t) {
		x = t.x;
		y = t.y;
		z = t.z;
		return this;
	}
	
	public Vector3 set (float inX, float inY, float inZ) {
		this.x = inX;
		this.y = inY;
		this.z = inZ;
		return this;
	}
	
	public Vector3 scale (float factor) {
		this.x *= factor;
		this.y *= factor;
		this.z *= factor;
		return this;
	}
	
	public Vector3 add (Vector3 t) {
		this.x += t.x;
		this.y += t.y;
		this.z += t.z;
		return this;
	}
	
	public Vector3 scaleAdd(float scale, Vector3 t) {
		this.x += scale * t.x;
		this.y += scale * t.y;
		this.z += scale * t.z;
		return this;
	}

	public float dot (Vector3 t) {
		return this.x * t.x + this.y * t.y + this.z * t.z;
	}
	
	public Vector3 thruProduct(Vector3 t){
		this.x *= t.x;
		this.y *= t.y;
		this.z *= t.z;
		return this;
	}

	public float squareSum(){
		return x * x + y * y + z * z;
	}


	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public Vector3 normalize() {
		float dist = (float) Math.sqrt(x * x + y * y + z * z);
		if (dist != 0) {
			x /= dist;
			y /= dist;
			z /= dist;
		}
		return this;
	}
	
	public Vector3 normalize(int i) {
		float dist = (float) Math.sqrt(x * x + y * y + z * z);
		if (dist != 0) {
			x /= dist;
			y /= dist;
			z /= dist;
		}
		if (i == 0 ||
			(i == 1 && x < 0) ||
			(i == 2 && y < 0) ||
			(i == 3 && z < 0) ||
			(i == -1 && x > 0) ||
			(i == -2 && y > 0) ||
			(i == -3 && z > 0) ){
			x = -x;
			y = -y;
			z = -z;
		}
		
		return this;
	}
	
	public Vector3 positiveZ(){
		if (z < 0){
			x = -x;
			y = -y;
			z = -z;		
		}
		return this;
	}
	
	public Vector3 interpolate(Vector3 a, Vector3 b, float alpha) {
		this.x = a.x * (1 - alpha) + b.x * alpha;
		this.y = a.y * (1 - alpha) + b.y * alpha;
		this.z = a.z * (1 - alpha) + b.z * alpha;
		return this;
	}
	
	public static Vector3 getAdd(Vector3 v1, Vector3 v2){
		float new_x = v1.x + v2.x;
		float new_y = v1.y + v2.y;
		float new_z = v1.z + v2.z;
		return new Vector3(new_x, new_y, new_z);
	}
	
	public Vector3 getSub(Vector3 v){
		float new_x = this.x - v.x;
		float new_y = this.y - v.y;
		float new_z = this.z - v.z;
		return new Vector3(new_x, new_y, new_z);
	}
	
	public static Vector3 getAverage (Vector3[] vList){
		int n = vList.length;
		float vx=0, vy=0, vz=0;
		for (Vector3 v : vList){
			vx += v.x;
			vy += v.y;
			vz += v.z;
		}
		return new Vector3 (vx/n, vy/n, vz/n);
	}

	public void getSpherical(Vector3 pt_ref){
		float x_ref = x - pt_ref.x;
		float y_ref = y - pt_ref.y;
		float z_ref = z - pt_ref.z;
		radius = (float) Math.sqrt(x_ref * x_ref + y_ref * y_ref + z_ref * z_ref);
		theta = (float) Math.acos(z_ref / radius);
		phi = (float) Math.atan2(y_ref, x_ref);		
	}
	
	public static Vector3 getMax(ArrayList<Vector3> vl){
		float xmax = Float.NEGATIVE_INFINITY;
		float ymax = Float.NEGATIVE_INFINITY;
		float zmax = Float.NEGATIVE_INFINITY;
		for (Vector3 v : vl){
			if (v.x > xmax) xmax = v.x;
			if (v.y > ymax) ymax = v.y;
			if (v.z > zmax) zmax = v.z;
		}
		return new Vector3(xmax, ymax, zmax);
	}
	
	public static Vector3 getMin(ArrayList<Vector3> vl){
		float xmin = Float.POSITIVE_INFINITY;
		float ymin = Float.POSITIVE_INFINITY;
		float zmin = Float.POSITIVE_INFINITY;
		for (Vector3 v : vl){
			if (v.x < xmin) xmin = v.x;
			if (v.y < ymin) ymin = v.y;
			if (v.z < zmin) zmin = v.z;
		}
		return new Vector3 (xmin, ymin, zmin);
	}
	
	public static Float[] getMaxSpherical(ArrayList<Vector3> vl){
		float radius_max = Float.NEGATIVE_INFINITY;
		float theta_max = Float.NEGATIVE_INFINITY;
		float phi_max = Float.NEGATIVE_INFINITY;
		for (Vector3 v : vl){
			if (v.radius > radius_max) 	radius_max = v.radius;
			if (v.theta > theta_max) 	theta_max = v.theta;
			if (v.phi > phi_max) 		phi_max = v.phi;
		}
		Float [] rtp = {radius_max, theta_max, phi_max};
		return rtp;
	}
	
	public static Float[] getMinSpherical(ArrayList<Vector3> vl){
		float radius_min = Float.POSITIVE_INFINITY;
		float theta_min = Float.POSITIVE_INFINITY;
		float phi_min = Float.POSITIVE_INFINITY;
		for (Vector3 v : vl){
			if (v.radius < radius_min) 	radius_min = v.radius;
			if (v.theta < theta_min) 	theta_min = v.theta;
			if (v.phi < phi_min) 		phi_min = v.phi;
		}
		Float [] rtp = {radius_min, theta_min, phi_min};
		return rtp;
	}
	
	public static Float[] getMaxSpherical(Vector3[][] vll){
		float radius_max = Float.NEGATIVE_INFINITY;
		float theta_max = Float.NEGATIVE_INFINITY;
		float phi_max = Float.NEGATIVE_INFINITY;
		for (Vector3[] vl : vll) if (vl != null)
			for(Vector3 v : vl) if (v != null) {
				if (v.radius > radius_max) 	radius_max = v.radius;
				if (v.theta > theta_max) 	theta_max = v.theta;
				if (v.phi > phi_max) 		phi_max = v.phi;
			}
		Float [] rtp = {radius_max, theta_max, phi_max};
		return rtp;
	}
	
	public static Float[] getMinSpherical(Vector3[][] vll){
		float radius_min = Float.POSITIVE_INFINITY;
		float theta_min = Float.POSITIVE_INFINITY;
		float phi_min = Float.POSITIVE_INFINITY;
		for (Vector3[] vl : vll) if (vl != null)
			for(Vector3 v : vl) if (v != null){
				if (v.radius < radius_min) 	radius_min = v.radius;
				if (v.theta < theta_min) 	theta_min = v.theta;
				if (v.phi < phi_min) 		phi_min = v.phi;
			}
		Float [] rtp = {radius_min, theta_min, phi_min};
		return rtp;
	}
}
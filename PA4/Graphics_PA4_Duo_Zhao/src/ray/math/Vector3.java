package ray.math;

public class Vector3 extends Tuple3 {
	
	public Vector3() {
		super(0, 0, 0);
	}

	public Vector3(Tuple3 t) {
		super(t.x, t.y, t.z);
	}

	public Vector3(double x, double y, double z) {
		super(x, y, z);
	}

	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public Vector3 normalize() {
		double dist = Math.sqrt(x * x + y * y + z * z);
		if (dist != 0) {
			x /= dist;
			y /= dist;
			z /= dist;
		}
		return this;
	}

	public Vector3 add(Vector3 vector) {
		this.x += vector.x;
		this.y += vector.y;
		this.z += vector.z;
		return this;
	}
	
	public Vector3 interpolate(Vector3 a, Vector3 b, double alpha) {
		this.x = a.x * (1 - alpha) + b.x * alpha;
		this.y = a.y * (1 - alpha) + b.y * alpha;
		this.z = a.z * (1 - alpha) + b.z * alpha;
		return this;
	}
	
	public static Vector3 getAdd(Vector3 v1, Vector3 v2){
		double new_x = v1.x + v2.x;
		double new_y = v1.y + v2.y;
		double new_z = v1.z + v2.z;
		return new Vector3(new_x, new_y, new_z);
	}
	
	public static Vector3 getAverage (Vector3[] vList){
		int n = vList.length;
		double vx=0, vy=0, vz=0;
		for (Vector3 v : vList){
			vx += v.x;
			vy += v.y;
			vz += v.z;
		}
		return new Vector3 (vx/n, vy/n, vz/n);
	}

}
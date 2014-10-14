package ray.math;

import java.util.ArrayList;

public class Ray {
	public Vector3 origin = new Vector3();
	public Vector3 direction = new Vector3();

	//boundary points for t
	public float start = 1e-6f; 
	public float end = Float.POSITIVE_INFINITY; 

	public Ray(Vector3 newOrigin, Vector3 newDirection) {
		origin.set(newOrigin);
		direction.set(newDirection);
	}
	
	public void set(Vector3 newOrigin, Vector3 newDirection) {
		origin.set(newOrigin);
		direction.set(newDirection);
	}
	
	public Vector3 evaluate (float t) {
		float px = this.origin.x + t * this.direction.x;
		float py = this.origin.y + t * this.direction.y;
		float pz = this.origin.z + t * this.direction.z;
		return new Vector3(px, py, pz);
	}

	public boolean isIntersectAny(ArrayList<Triangle> tris){
		for (Triangle tri : tris){
			if (tri.getIntersect(this) != null){
				return true;
			}
		}
		return false;
	}
		
	public RayTouchInfo getFirstTouch(ArrayList<Triangle> tris){
		RayTouchInfo rti = null;
		float t = this.end;
		for (Triangle tri : tris){
			RayTouchInfo rti_iterator = tri.getIntersect(this);
			if (rti_iterator != null && t > rti_iterator.t){
				rti = rti_iterator;
				t = rti_iterator.t;
			}
		}
		return rti;
	}
}
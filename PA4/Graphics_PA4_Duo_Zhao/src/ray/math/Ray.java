package ray.math;

import java.util.ArrayList;
import ray.scene.Surface;

public class Ray {

	public Point3 origin = new Point3();
	public Vector3 direction = new Vector3();

	//boundary points for t
	public Double start = 1e-6; 
	public Double end = Double.POSITIVE_INFINITY; 

	public Ray(Point3 newOrigin, Vector3 newDirection) {
		origin.set(newOrigin);
		direction.set(newDirection);
	}
	
	public void set(Point3 newOrigin, Vector3 newDirection) {
		origin.set(newOrigin);
		direction.set(newDirection);
	}
	
	public Point3 evaluate (double t) {
		double px = this.origin.x + t * this.direction.x;
		double py = this.origin.y + t * this.direction.y;
		double pz = this.origin.z + t * this.direction.z;
		return new Point3(px, py, pz);
	}

	public boolean isIntersectAny(ArrayList<Surface> sfs){
		for (Surface sf : sfs){
			if (sf.getIntersect(this) != null){
				return true;
			}
		}
		return false;
	}
	
	public RayTouchInfo getFirstTouch(ArrayList<Surface> sfs){
		RayTouchInfo rti = null;
		double t = this.end;
		for (Surface sf : sfs){
			RayTouchInfo rti_iterator = sf.getIntersect(this);
			if (rti_iterator != null && t > rti_iterator.t){
				rti = rti_iterator;
				t = rti_iterator.t;
			}
		}
		return rti;
	}
}
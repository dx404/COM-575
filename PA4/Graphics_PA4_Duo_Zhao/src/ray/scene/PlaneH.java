package ray.scene;

import ray.math.Point3;
import ray.math.Ray;
import ray.math.RayTouchInfo;
import ray.math.Vector3;

public class PlaneH extends Surface{
	private double height = -2;
	
	public PlaneH (double h){
		height = h;
	}

	@Override
	public RayTouchInfo getIntersect(Ray rayIn) {
		double t = (height - rayIn.origin.y)/rayIn.direction.y;
		if ( t > rayIn.start ){
			return new RayTouchInfo (this, t, rayIn);
		}
		return null ;
	}

	@Override
	public Vector3 getUnitNormal(Point3 p) {
		if (height < 0){
			return new Vector3 (0, 1, 0);
		}
		else {
			return new Vector3 (0, -1, 0);
		}
	}

}

package ray.math;

import ray.scene.Surface;

public class RayTouchInfo {
	public Point3 point = new Point3();
	public Surface surface = null;
	public double t = 0;
	
	public Ray rayIn, rayOut;
	public Vector3 normal;

	public RayTouchInfo (Surface s, double t, Ray rayIn){
		this.point = rayIn.evaluate(t);
		this.surface = s;
		this.t = t;
		this.rayIn = rayIn;
	}
		
	public Ray getReflectRay(){
		rayOut = null;
		normal = surface.getUnitNormal(point);
		Vector3 inDir = rayIn.direction;
		Vector3 outDir = new Vector3 (inDir);	
		double projectLengh = inDir.dot(normal);
		
		outDir.scaleAdd (-2*projectLengh, normal);
		rayOut = new Ray(point, outDir);
		return rayOut;
	}

}

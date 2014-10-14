package ray.math;

public class RayTouchInfo {
	public Vector3 point = new Vector3();
	public Triangle tri = null;
	public float t = 0;
	
	public float alpha, beta, gamma;
	
	public Ray rayIn, rayOut;
	public Vector3 normal;
	
	public RayTouchInfo(Triangle tri, Ray rayIn){
		this.point = rayIn.evaluate(t);
		this.tri = tri;
		this.rayIn = rayIn;
	}

	public RayTouchInfo (Triangle tri, float t, Ray rayIn){
		this.point = rayIn.evaluate(t);
		this.tri = tri;
		this.t = t;
		this.rayIn = rayIn;
	}
		
	public Ray getReflectRay(){
		rayOut = null;
		normal = tri.unitNormal;
		Vector3 inDir = rayIn.direction;
		Vector3 outDir = new Vector3 (inDir);	
		float projectLengh = inDir.dot(normal);
		
		outDir.scaleAdd (-2*projectLengh, normal);
		rayOut = new Ray(point, outDir);
		return rayOut;
	}

}

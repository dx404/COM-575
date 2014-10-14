package ray.scene;

import ray.math.Point3;
import ray.math.Ray;
import ray.math.RayTouchInfo;
import ray.math.Vector3;

public abstract class Surface {

//	protected Shader shader = Shader.DEFAULT_MATERIAL;
//	public void setShader(Shader material) { this.shader = material; }
//	public Shader getShader() { return shader; }
	
	public abstract RayTouchInfo getIntersect(Ray rayIn);
	
	public abstract Vector3 getUnitNormal(Point3 p);
	public Vector3 ka, kd, ks;
	public double sp; //specular power
	public double rw; //reflection weight
	
}

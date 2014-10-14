package ray.scene;

import ray.math.Point3;
import ray.math.Quadratic;
import ray.math.Ray;
import ray.math.RayTouchInfo;
import ray.math.Vector3;

public class Sphere extends Surface {
	
	private Point3 center = new Point3(0, 0, 0);
	public double radius = 1.0;
	public void setCenter(Point3 center) {
		this.center.set(center);
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public Sphere(Point3 c, double r) {
		this.center.set(c);
		this.radius = r;
	}
	
	public Sphere(double x, double y, double z, double r){
		this.center = new Point3(x, y, z);
		this.radius = r;
	}
	
	public String toString() {
		return "sphere " + center + " " + radius + " end";
	}
	
	/**
	 * Part 2 added
	 */
	public Vector3 ka, kd, ks;
	
	public boolean isIntersect(Ray rayIn) {
		double a = rayIn.direction.squareSum();
		double b = 2 * rayIn.origin.dot(rayIn.direction) 
				- 2 * center.dot(rayIn.direction);
		double c = center.squareSum() 
				+ rayIn.origin.squareSum() 
				- 2 * center.dot(rayIn.origin) 
				- radius * radius;
		Quadratic quad = new Quadratic(a, b, c);
		Double t_small = quad.getRootSmall();
		if (!t_small.isNaN()){
			return true;
		}
		return false;
	}
	
	@Override
	public RayTouchInfo getIntersect(Ray rayIn) {
		double a = rayIn.direction.squareSum();
		double b = 2 * rayIn.origin.dot(rayIn.direction) 
				- 2 * center.dot(rayIn.direction);
		double c = center.squareSum() 
				+ rayIn.origin.squareSum() 
				- 2 * center.dot(rayIn.origin) 
				- radius * radius;
		Quadratic quad = new Quadratic(a, b, c,rayIn.start, rayIn.end);
		Double t_small = quad.getRootSmall();
		if (!t_small.isNaN()){
			return new RayTouchInfo(this, t_small, rayIn);
		}
		return null;
	}
	
	/**
	 * @param p is a surface point of a sphere
	 * @return normal vector at this point
	 */
	public Vector3 getUnitNormal(Point3 p){
		double nx = (p.x - center.x)/radius;
		double ny = (p.y - center.y)/radius;
		double nz = (p.z - center.z)/radius;
		return new Vector3 (nx, ny, nz);
	}

}
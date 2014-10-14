package ray.scene;

import ray.math.Point3;

/**
 * For now just for white light
 */
public class Light {
	
	/** Where the light is located in space. */
	public Point3 position;
	public double intensity;
	
	public void setPosition(Point3 position) {
		this.position = position;
	}
	
	public Light(double x, double y, double z, double intensity) {
		this.position = new Point3 (x, y, z);
		this.intensity = intensity;
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "light: " + position + " " + intensity;
	}
}
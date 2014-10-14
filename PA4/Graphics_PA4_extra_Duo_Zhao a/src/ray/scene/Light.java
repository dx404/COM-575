package ray.scene;

import ray.math.Vector3;

/**
 * For now just for white light
 */
public class Light {
	
	/** Where the light is located in space. */
	public Vector3 position;
	public float intensity;
	
	public void setPosition(Vector3 position) {
		this.position = position;
	}
	
	public Light(float x, float y, float z, float intensity) {
		this.position = new Vector3 (x, y, z);
		this.intensity = intensity;
	}
	
	/**
	 * @see Object#toString()
	 */
	public String toString() {
		return "light: " + position + " " + intensity;
	}
}
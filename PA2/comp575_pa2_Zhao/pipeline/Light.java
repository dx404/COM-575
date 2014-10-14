package pipeline;

import javax.vecmath.Vector3f;

public class Light {
	public Vector3f pos;
	public float intensity = 1;
	
	public Light(Vector3f pos, float intensity){
		this.pos = pos;
		this.intensity = intensity;
	}
	
	public Light(float p_x, float p_y, float p_z, float intensity){
		this.pos = new Vector3f(p_x, p_y, p_z);
		this.intensity = intensity;
	}
}

package pipeline.math;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public class VectorKit {
	public static Vector3f Interpolate(
			float alpha, float beta, float gamma, 
			Vector3f n0, Vector3f n1, Vector3f n2){
		Vector3f n = new Vector3f(0.0f, 0.0f, 0.0f);
		n.x = alpha * n0.x + beta * n1.x + gamma * n2.x;
		n.y = alpha * n0.y + beta * n1.y + gamma * n2.y;
		n.z = alpha * n0.z + beta * n2.z + gamma * n2.z;
		return n;
	}
	
	public static Vector3f Interpolate(float alpha, float beta, float gamma, 
			Vector4f n0, Vector4f n1, Vector4f n2){
		Vector3f n = new Vector3f(0.0f, 0.0f, 0.0f);
		n.x = alpha * n0.x + beta * n1.x + gamma * n2.x;
		n.y = alpha * n0.y + beta * n1.y + gamma * n2.y;
		n.z = alpha * n0.z + beta * n2.z + gamma * n2.z;
		return n;
	}
}

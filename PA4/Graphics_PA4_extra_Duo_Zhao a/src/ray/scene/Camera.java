package ray.scene;

import ray.math.Vector3;

public class Camera {
	public Vector3 position = new Vector3(0, -10, 0);
	public Vector3 u = new Vector3(0, 0, 1);
	public Vector3 v = new Vector3(0, 1, 0);
	public Vector3 w = new Vector3(-1, 0, 0);

}

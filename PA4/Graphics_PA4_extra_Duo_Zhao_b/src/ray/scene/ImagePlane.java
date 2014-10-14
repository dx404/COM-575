package ray.scene;


import java.util.ArrayList;

import cathedral.TrianglesMesher;

import ray.math.Triangle;
import ray.math.Vector3;
import ray.math.Ray;
import ray.shader.Pixel;
import ray.shader.TriangleShader;

public class ImagePlane {
	public Camera cam;
	public TrianglesMesher mesh;
	public Light light;

	public float projDistance = 0.1f;
	public float left = -0.1f, right = 0.1f;
	public float bottom = -0.1f, top = 0.1f;
	
	public int width = 512;
	public int height = 512;
	
	public float radius_min, theta_min, phi_min;
	public float radius_max, theta_max, phi_max;

	public Pixel[][] imagePixels = new Pixel[width][height];
	public Vector3[][] pixelPos = new Vector3[width][height];

	public static ImagePlane getDefault(){
		return new ImagePlane();
	}
	
	public void loadPixelPos(){
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				//under u, v, w frame
				float x_cam = left + (0.5f + i) * (right - left) / width;
				float y_cam = bottom + (0.5f + j) * (top - bottom) / height;
				float z_cam = - projDistance;
				float x_scene = cam.u.x * x_cam + cam.v.x * y_cam + cam.w.x * z_cam + cam.position.x;
				float y_scene = cam.u.y * x_cam + cam.v.y * y_cam + cam.w.y * z_cam + cam.position.y;
				float z_scene = cam.u.z * x_cam + cam.v.z * y_cam + cam.w.z * z_cam + cam.position.z;
				pixelPos[j][i] = new Vector3(x_scene, y_scene, z_scene);
				pixelPos[j][i].getSpherical(cam.position);
				
			}
		Float[] minSphericalInfo = Vector3.getMinSpherical(pixelPos);
		radius_min = minSphericalInfo[0];
		theta_min  = minSphericalInfo[1];
		phi_min    = minSphericalInfo[2];

		Float[] maxSphericalInfo = Vector3.getMaxSpherical(pixelPos);
		radius_max = maxSphericalInfo[0];
		theta_max  = maxSphericalInfo[1];
		phi_max    = maxSphericalInfo[2];

	}
	
	public void meshImagePlaneMap(){
		mesh.setThetaBoudary(theta_min, theta_max, 256);
		mesh.setPhiBoundary(phi_min, phi_max, 256);
		mesh.build();
		int sum = 0, max = 0;
		for (int i = 0; i < mesh.theta_steps; i++)
			for (int j = 0; j < mesh.phi_steps; j++){
				int size =  mesh.cellRecorder[i][j].size();
				if (size > 0){
					sum += size;
					if (size > max) max = size;
					//System.out.println("( " + i + ", " + j + ") " + size);
				}
			}
		System.out.println("theta_min = " + theta_min);
		System.out.println("theta_max = " + theta_max);
		System.out.println("phi_min = " + phi_min);
		System.out.println("phi_max = " + phi_max);
		System.out.println("sum = " + sum);
		System.out.println("sum = " + sum);
	}

	public void loadScene(){
		Vector3 zero3 = new Vector3(0, 0, 0);
		loadPixelPos();
		meshImagePlaneMap();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				Ray primaryRay = new Ray (cam.position, pixelPos[j][i].getSub(cam.position));
				imagePixels[j][i] = new Pixel (0, 0, 0);
				primaryRay.direction.getSpherical (zero3);
				float theta = primaryRay.direction.theta;
				float phi = primaryRay.direction.phi;
				ArrayList<Triangle> trl = mesh.getPotentialTris(theta, phi);
				TriangleShader shader = new TriangleShader(primaryRay, trl, light);
				shader.noShadowShade();
				imagePixels[j][i].set(shader.mixedColor);

			}
	}
	
}

package ray.scene;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.Random;

import ray.math.Point3;
import ray.math.Ray;
import ray.math.Vector3;
import ray.shader.LocalSurfaceShader;
import ray.shader.Pixel;
import ray.shader.SurfaceShader;

/**
 * For now this Image Plane is a vertical one
 * of the form z = -0.1
 * @author Duo Zhao
 *
 */
public class ImagePlane {

	public double projDistance = 0.1;
	public double left = -0.1, right = 0.1;
	public double bottom = -0.1, top = 0.1;
	
	Point3 eyePos = new Point3 (0, 0, 0);

	public int width = 512;
	public int height = 512;
	
	public int sampleSize = 8;
	
	Random randGen = new Random();

	public Pixel[][] imagePixels = new Pixel[width][height];

	public static ImagePlane getDefault(){
		return new ImagePlane();
	}

	public void loadSceneReflect (ArrayList<Surface> sfs, Light lightSource){
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				//Pixel coordinate to physical coordinate on the plane
				double x = left + (0.5 + i) * (right - left) / width;
				double y = bottom + (0.5 + j) * (top - bottom) / height;
				Ray primaryRay = new Ray (eyePos, new Vector3(x, y, -projDistance));
				imagePixels[j][i] = new Pixel (0, 0, 0);
				LocalSurfaceShader shader = new LocalSurfaceShader(primaryRay, sfs, lightSource);
				//shader.directShade();
				shader.recursiveShade(0);
				imagePixels[j][i].set(shader.mixedColor);
			}
	}
	
	public void loadSceneReflectConcurrent (ArrayList<Surface> sfs, Light lightSource){
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				double x = left + (0.5 + i) * (right - left) / width;
				double y = bottom + (0.5 + j) * (top - bottom) / height;
				Ray primaryRay = new Ray (eyePos, new Vector3(x, y, -projDistance));
				imagePixels[j][i] = new Pixel (0, 0, 0);
				LocalSurfaceShader shader = new LocalSurfaceShader(primaryRay, sfs, lightSource);
				SurfaceShader macroSharder = new SurfaceShader(shader, imagePixels, i, j);
				executor.execute(macroSharder);
			}
	    // Wait until all threads are finish
		executor.shutdown();
		while (!executor.isTerminated());
	    System.out.println("Finished all threads");
	}
	
	public void loadSceneReflectAntialiasing (ArrayList<Surface> sfs, Light lightSource){
		Point3 eyePos = new Point3 (0, 0, 0);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				imagePixels[j][i] = new Pixel(0, 0, 0);
				Vector3[] sampleRGB = new Vector3[sampleSize * sampleSize];			
				for (int k = 0; k < sampleSize * sampleSize; k++){
					sampleRGB[k] = new Vector3 (0,0,0);
					//Pixel coordinate to physical coordinate on the plane
					double x = left + (randGen.nextDouble() + i)*(right - left)/width;
					double y = bottom + (randGen.nextDouble() + j)*(top - bottom)/height;
					Ray primaryRay = new Ray (eyePos, new Vector3(x, y, -projDistance));
					LocalSurfaceShader shader = new LocalSurfaceShader(primaryRay, sfs, lightSource);
					shader.recursiveShade(0);
					sampleRGB[k] = new Vector3(shader.mixedColor);
				}
				Vector3 avgVec = Vector3.getAverage(sampleRGB);
				imagePixels[j][i].set(avgVec);
			}
		
	}

}

package ray.shader;

import java.util.ArrayList;

import ray.math.Point3;
import ray.math.Ray;
import ray.math.RayTouchInfo;
import ray.math.Vector3;
import ray.scene.Light;
import ray.scene.Surface;

public class LocalSurfaceShader {
	public Surface surface = null;
	public Point3 localPoint = new Point3();
	public Light lightSource = null;
	public Point3 viewPoint = new Point3();
	
	public boolean isVacant = false;
	public boolean isShadow = false;
	public ArrayList<Surface> envSurfaces;
	
	public Vector3 normal = new Vector3();
	
	public Vector3 ambient = new Vector3();
	public Vector3 diffuse = new Vector3();
	public Vector3 specular = new Vector3();
	
	public Vector3 mixedColor = new Vector3();
	
	private Vector3 pt2Light = new Vector3();
	private Vector3 pt2Viewer = new Vector3();
	private Vector3 viewer2pt = new Vector3();
	
	public Ray viewRay; //eye2pt
	public Ray pt2LightRay;
	private RayTouchInfo viewRayfirstTouchInfo;
	private Ray reflectRay;
	
	private double nl, nh;
	private Vector3 h = new Vector3();
	
	public static final int RECURSIVE_DEPTH = 2;
	public LocalSurfaceShader shaderNext;
	
	public void directShade(){
		if (isVacant)
			return;
		loadAmbientColor();
		if (isShadow){
			mixedColor.x = ambient.x;
			mixedColor.y = ambient.y;
			mixedColor.z = ambient.z;
		}
		else{
			loadDiffuseColor();
			loadSpecularColor();
			mixedColor.x = ambient.x + diffuse.x + specular.x;
			mixedColor.y = ambient.y + diffuse.y + specular.y;
			mixedColor.z = ambient.z + diffuse.z + specular.z;
		}
	}
	
	public void recursiveShade (int level){
		if (level > RECURSIVE_DEPTH || isVacant)
			return;
		directShade();		
		reflectRay = viewRayfirstTouchInfo.getReflectRay();
		shaderNext = new LocalSurfaceShader(reflectRay, envSurfaces, lightSource);
		shaderNext.recursiveShade(level + 1);
		mixedColor.interpolate (mixedColor, shaderNext.mixedColor, surface.rw);
	}
	
	public LocalSurfaceShader(Surface surface, Point3 localPoint){
		this.surface = surface;
		this.localPoint = localPoint;
		loadSurfaceNormal();
	}
	
	public LocalSurfaceShader(Ray viewRay, ArrayList<Surface> sfs, Light lightSource){
		this.viewRay = viewRay;
		this.viewPoint = viewRay.origin;
		viewRayfirstTouchInfo = viewRay.getFirstTouch(sfs);
		if (viewRayfirstTouchInfo == null){
			isVacant = true;
		}
		else {
			isVacant = false;
			surface = viewRayfirstTouchInfo.surface;
			localPoint = viewRayfirstTouchInfo.point;
			loadSurfaceNormal();
			setLightSource(lightSource);
			setViewPoint(viewRay.origin);
			setEnvSurfaces(sfs);
		}
	}
	
	public void setLightSource(Light lightSource){
		this.lightSource = lightSource;
		pt2Light.x =  lightSource.position.x - localPoint.x;
		pt2Light.y =  lightSource.position.y - localPoint.y;
		pt2Light.z =  lightSource.position.z - localPoint.z;
		pt2Light.normalize();
		nl = normal.dot(pt2Light);
		pt2LightRay = new Ray(localPoint, pt2Light);
	}
	
	public void setViewPoint(Point3 viewPoint){
		this.viewPoint = viewPoint;
		pt2Viewer.x = viewPoint.x - localPoint.x;
		pt2Viewer.y = viewPoint.y - localPoint.y;
		pt2Viewer.z = viewPoint.z - localPoint.z;
		pt2Viewer.normalize();
		
		viewer2pt.x = - pt2Viewer.x;
		viewer2pt.y = - pt2Viewer.y;
		viewer2pt.z = - pt2Viewer.z;
		viewRay = new Ray(viewPoint, viewer2pt);
		
		h.x = pt2Viewer.x + pt2Light.x;
		h.y = pt2Viewer.y + pt2Light.y;
		h.z = pt2Viewer.z + pt2Light.z;
		h.normalize();
		nh = normal.dot(h);
	}
	
	public void setEnvSurfaces(ArrayList<Surface> sfs){
		envSurfaces = sfs;
		isShadow = pt2LightRay.isIntersectAny(envSurfaces);
	}
	
	private void loadSurfaceNormal(){
		normal = surface.getUnitNormal(localPoint);
	}
	
	public void loadAmbientColor(){
		ambient.x = lightSource.intensity * surface.ka.x;
		ambient.y = lightSource.intensity * surface.ka.y;
		ambient.z = lightSource.intensity * surface.ka.z;	
	}
	
	public void loadDiffuseColor(){
		if (nl > 0){
			double dfScale = nl * lightSource.intensity;
			diffuse.x = dfScale * surface.kd.x;
			diffuse.y = dfScale * surface.kd.y;
			diffuse.z = dfScale * surface.kd.z;
		}
		else {
			diffuse.x = 0;
			diffuse.y = 0;
			diffuse.z = 0;
		}
	}
	
	public void loadSpecularColor(){
		if (nh > 0){
			double spScale = lightSource.intensity * Math.pow (nh, surface.sp);
			specular.x = spScale * surface.ks.x;
			specular.y = spScale * surface.ks.y;
			specular.z = spScale * surface.ks.z;
		}
		else {
			specular.x = 0;
			specular.y = 0;
			specular.z = 0;
		}
	}
	
}

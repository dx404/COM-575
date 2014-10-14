package ray.shader;

import java.util.ArrayList;

import cathedral.TrianglesMesher;

import ray.math.Triangle;
import ray.math.Vector3;
import ray.math.Ray;
import ray.math.RayTouchInfo;
import ray.scene.Light;

public class TriangleShader {
	public Triangle tri = null;
	public Vector3 localPoint = new Vector3();
	public Light lightSource = null;
	public Vector3 viewPoint = new Vector3();
	
	public boolean isVacant = false;
	public boolean isShadow = false;
	public ArrayList<Triangle> envTris;
	
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
	public RayTouchInfo viewRayfirstTouchInfo;
	
	private float nl, nh;
	private Vector3 h = new Vector3();
	
	public static final int RECURSIVE_DEPTH = 2;
	public TriangleShader shaderNext;
	
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
	
	public void noShadowShade(){
		if (isVacant)
			return;
		loadAmbientColor();
		loadDiffuseColor();
		loadSpecularColor();
		mixedColor.x = ambient.x + diffuse.x + specular.x;
		mixedColor.y = ambient.y + diffuse.y + specular.y;
		mixedColor.z = ambient.z + diffuse.z + specular.z;
	}
		
	public TriangleShader(Triangle tri, Vector3 localPoint){
		this.tri = tri;
		this.localPoint = localPoint;
		loadSurfaceNormal();
	}
	
	public TriangleShader(Ray viewRay, ArrayList<Triangle> tris, Light lightSource){
		this.viewRay = viewRay;
		this.viewPoint = viewRay.origin;
		viewRayfirstTouchInfo = viewRay.getFirstTouch(tris);
		if (viewRayfirstTouchInfo == null){
			isVacant = true;
		}
		else {
			isVacant = false;
			tri = viewRayfirstTouchInfo.tri;
			localPoint = viewRayfirstTouchInfo.point;
			loadSurfaceNormal();
			setLightSource(lightSource);
			setViewPoint(viewRay.origin);
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
	
	public void setViewPoint(Vector3 viewPoint){
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
	
	public void setEnvTriangles(ArrayList<Triangle> tris){
		envTris = tris;
		isShadow = pt2LightRay.isIntersectAny(tris);
	}
	
	private void loadSurfaceNormal(){
		normal = tri.unitNormal;
	}
	
	public void loadAmbientColor(){
		ambient.x = lightSource.intensity * tri.ka.x;
		ambient.y = lightSource.intensity * tri.ka.y;
		ambient.z = lightSource.intensity * tri.ka.z;	
	}
	
	public void loadDiffuseColor(){
		if (nl > 0){
			float dfScale = nl * lightSource.intensity;
			diffuse.x = dfScale * tri.kd.x;
			diffuse.y = dfScale * tri.kd.y;
			diffuse.z = dfScale * tri.kd.z;
		}
		else {
			diffuse.x = 0;
			diffuse.y = 0;
			diffuse.z = 0;
		}
	}
	
	public void loadSpecularColor(){
		if (nh > 0){
			float spScale = (float) (lightSource.intensity * Math.pow (nh, tri.sp));
			specular.x = spScale * tri.ks.x;
			specular.y = spScale * tri.ks.y;
			specular.z = spScale * tri.ks.z;
		}
		else {
			specular.x = 0;
			specular.y = 0;
			specular.z = 0;
		}
	}
	
}

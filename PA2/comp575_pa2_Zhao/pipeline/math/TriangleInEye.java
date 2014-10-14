package pipeline.math;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import pipeline.Light;
import pipeline.gui.Pixel;

/**
 * The flat shading is implemented in this class
 */
public class TriangleInEye {
	public Vector3f a = new Vector3f(0,0,0);
	public Vector3f b = new Vector3f(0,0,0);
	public Vector3f c = new Vector3f(0,0,0);
	public Vector3f normal = new Vector3f(-1.0f, -1.0f, -1.0f); //suface normal towards with positive towards eye component
	public Vector3f centroid = new Vector3f(-1.1f, -1.1f, -1.1f);
	
	public Pixel flatPixel;
	public Vector3f rgb = new Vector3f(0,0,0);
	public Matrix4f World2Eye;
	
	public TriangleInEye(Vector3f a, Vector3f b, Vector3f c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public TriangleInEye(Vector4f a, Vector4f b, Vector4f c){
		this.a.x = a.x;
		this.a.y = a.y;
		this.a.z = a.z;
		
		this.b.x = b.x;
		this.b.y = b.y;
		this.b.z = b.z;
		
		this.c.x = c.x;
		this.c.y = c.y;
		this.c.z = c.z;
	}
	
	//Environment of the triangle
	Light light        = new Light(-4.0f, 4.0f, -3.0f, 1.0f);
	
	float ambient_intensity = 0.2f;
	Vector3f ambient   = new Vector3f(0.0f, 1.0f, 0.0f);
	Vector3f diffusion = new Vector3f(0.0f, 0.5f, 0.0f);
	Vector3f specular  = new Vector3f(0.5f, 0.5f, 0.5f);
	float specular_power = 32;
	
	public void getCentroid(){
		centroid.x = (a.x + b.x + c.x) / 3;
		centroid.y = (a.y + b.y + c.y) / 3;
		centroid.z = (a.z + b.z + c.z) / 3;
	}
	
	//Assume a is the base vector
	public void getNormal(){
		Vector3f ab = new Vector3f(b);
		Vector3f ac = new Vector3f(c);
		ab.sub(a);
		ac.sub(a);
		normal.cross(ab, ac);
		normal.normalize();
		if (normal.z < 0){
			normal.negate();
		}

	}
	
	public void getFlatPixel(){
		Vector3f ambient_rgb = new Vector3f(0,0,0);
		Vector3f diffusion_rgb = new Vector3f(0,0,0);
		Vector3f specular_rgb = new Vector3f(0,0,0);
		getCentroid();
		getNormal();
		
		//set ambient color
		ambient_rgb.scale(ambient_intensity, ambient);
		
		//set diffusion color
		Vector3f pt2Light = new Vector3f(light.pos);
		pt2Light.sub(centroid); 
		pt2Light.normalize();
		float nl = normal.dot(pt2Light);
		if (nl > 0)
			diffusion_rgb.scale(light.intensity * nl, diffusion);
		
		//load specular eye is at origin in eye space
		//Vector3 pt2Eye = Point3.getVector3(pt, eyePos).normalize();
		Vector3f pt2Eye = new Vector3f(0,0,0);
		pt2Eye.sub(centroid);
		pt2Eye.normalize();
		
		//h = Vector3.getAdd(pt2Eye, pt2Light).normalize();
		Vector3f h = new Vector3f(0,0,0);
		h.add(pt2Eye, pt2Light);
		h.normalize();
		float nh = normal.dot(h);
		if (nh > 0){
			specular_rgb.scale(light.intensity * (float)Math.pow(nh, specular_power), specular);
		}
		
		
		rgb.add(ambient_rgb);
		rgb.add(diffusion_rgb);
		rgb.add(specular_rgb);
		flatPixel = new Pixel(rgb);
	}
	
	
}

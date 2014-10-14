package pipeline.math;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import pipeline.Light;
import pipeline.gui.Pixel;


//for Gouraud Shading
public class VertexInEye {
	public Vector3f v = new Vector3f(0,0,0);

	public Pixel GouraudPixel;
	public Vector3f rgb = new Vector3f(0,0,0);
	
	public VertexInEye (Vector3f v){
		this.v.x = v.x;
		this.v.y = v.y;
		this.v.z = v.z;
	}

	public VertexInEye(Vector4f v){
		this.v.x = v.x;
		this.v.y = v.y;
		this.v.z = v.z;
	}

	//sphere center
	public Vector3f center = new Vector3f(0.0f, 0.0f, -7.0f);
	public Vector3f normal = new Vector3f(-1.0f, -1.0f, -1.0f); // vertex-wise
	
	//Environment of the vertex, default environment
	Light light        = new Light(-4.0f, 4.0f, -3.0f, 1.0f);

	float ambient_intensity = 0.2f;
	Vector3f ambient   = new Vector3f(0.0f, 1.0f, 0.0f);
	Vector3f diffusion = new Vector3f(0.0f, 0.5f, 0.0f);
	Vector3f specular  = new Vector3f(0.5f, 0.5f, 0.5f);
	float specular_power = 32;

	//Assume a is the base vector
	public void getNormal(){
		normal.sub(v, center);
		normal.normalize();
		if (normal.z < 0){
			normal.negate();
		}

	}

	public void getGouraudPixel(){
		Vector3f ambient_rgb = new Vector3f(0,0,0);
		Vector3f diffusion_rgb = new Vector3f(0,0,0);
		Vector3f specular_rgb = new Vector3f(0,0,0);
		getNormal();

		//set ambient color
		ambient_rgb.scale(ambient_intensity, ambient);

		//set diffusion color
		Vector3f pt2Light = new Vector3f(light.pos);
		pt2Light.sub(v); 
		pt2Light.normalize();
		float nl = normal.dot(pt2Light);
		if (nl > 0)
			diffusion_rgb.scale(light.intensity * nl, diffusion);

		//load specular eye is at origin in eye space
		//Vector3 pt2Eye = Point3.getVector3(pt, eyePos).normalize();
		Vector3f pt2Eye = new Vector3f(0,0,0);
		pt2Eye.sub(v);
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
		GouraudPixel = new Pixel(rgb);
	}

}

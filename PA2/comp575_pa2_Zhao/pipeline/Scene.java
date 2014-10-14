package pipeline;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import pipeline.gui.Pixel;
import pipeline.math.BaryCen;
import pipeline.math.Matrix4f;

class Scene {
	public Vector4f[] vertices;
	public Vector4f[] vInWorld;
	public Vector4f[] vInEye;
	public Vector4f[] vInCanonicalViewVolum;
	public Vector4f[] vOnScreen;

	
	public Matrix4f modelTran = new Matrix4f();
	public Matrix4f eyeTran = new Matrix4f();
	public Matrix4f projTran = new Matrix4f();
	public Matrix4f viewPortTran = new Matrix4f();
	
	private Matrix4f compTran  = new Matrix4f();
	
	public int screenWidth = 512, screenHeight = 512;
	public int gNumVertices = 0;    // Number of 3D vertices.
	public int	gNumTriangles = 0;    // Number of triangles.
	
	public int[] gIndexBuffer;
	
	public void loadDefault(){
		int width = 32, height = 16;

		float theta, phi;
		int t;

		gNumVertices = (height - 2) * width + 2;
		gNumTriangles = (height - 2) * (width - 1) * 2;

		// TODO: Allocate an array for gNumVertices vertices.
		vertices = new Vector4f[gNumVertices];

		gIndexBuffer = new int[3*gNumTriangles];

		t = 0;
		for (int j = 1; j < height-1; ++j)
		{
			for (int i = 0; i < width; ++i)
			{
				theta = (float) ((float) j / (height-1) * Math.PI);
				phi = (float) ((float) i / (width-1)  * Math.PI * 2);

				float x = (float) (Math.sin(theta) * Math.cos(phi));
				float y = (float) Math.cos(theta);
				float z = (float) (- Math.sin(theta) * Math.sin(phi));

				// TODO: Set vertex t in the vertex array to {x, y, z}.
				vertices[t] = new Vector4f(x, y, z, 1);

				t++;
			}
		}

		// TODO: Set vertex t in the vertex array to {0, 1, 0}.
		vertices[t] = new Vector4f(0, 1, 0, 1);

		t++;

		// TODO: Set vertex t in the vertex array to {0, -1, 0}.
		vertices[t] = new Vector4f(0, -1, 0, 1);

		t++;

		t = 0;
		for (int j = 0; j < height-3; ++j)
		{
			for (int i = 0; i < width-1; ++i)
			{
				gIndexBuffer[t++] = j*width + i;
				gIndexBuffer[t++] = (j+1)*width + (i+1);
				gIndexBuffer[t++] = j*width + (i+1);
				gIndexBuffer[t++] = j*width + i;
				gIndexBuffer[t++] = (j+1)*width + i;
				gIndexBuffer[t++] = (j+1)*width + (i+1);
			}
		}
		for (int i = 0; i < width-1; ++i)
		{
			gIndexBuffer[t++] = (height-2)*width;
			gIndexBuffer[t++] = i;
			gIndexBuffer[t++] = i + 1;
			gIndexBuffer[t++] = (height-2)*width + 1;
			gIndexBuffer[t++] = (height-3)*width + (i+1);
			gIndexBuffer[t++] = (height-3)*width + i;
		}
		
			System.out.print("gNumTriangles=" + gNumTriangles);
		
		// The index buffer has now been generated. Here's how to use to determine
		// the vertices of a triangle. Suppose you want to determine the vertices
		// of triangle i, with 0 <= i < gNumTriangles. Define:
		//
		// k0 = gIndexBuffer[3*i + 0]
		// k1 = gIndexBuffer[3*i + 1]
		// k2 = gIndexBuffer[3*i + 2]
		//
		// Now, the vertices of triangle i are at positions k0, k1, and k2 (in that
		// order) in the vertex array (which you should allocate yourself at line
		// 27).
		//
		// Note that this assumes 0-based indexing of arrays (as used in C/C++,
		// Java, etc.) If your language uses 1-based indexing, you will have to
		// add 1 to k0, k1, and k2.
		
	}
	
	public void setupModelTran(float scale, Vector3f center){
		Matrix4f scaleMatrix = new Matrix4f().setScale(scale);
		Matrix4f tranlateMatrix = new Matrix4f().setTranslate(center);
		this.modelTran = tranlateMatrix.Mul(scaleMatrix);
	}
	
	public void setupEyeTran(Vector3f u, Vector3f v, Vector3f w, Vector3f p){
		eyeTran = new Matrix4f();
		Vector4f temp = new Vector4f(p.x, p.y, p.z, 1.0f);
		
		eyeTran.m[0][0] = u.x;
		eyeTran.m[0][1] = u.y;
		eyeTran.m[0][2] = u.z;
		eyeTran.m[1][0] = v.x;
		eyeTran.m[1][1] = v.y;
		eyeTran.m[1][2] = v.z;
		eyeTran.m[2][0] = w.x;
		eyeTran.m[2][1] = w.y;
		eyeTran.m[2][2] = w.z;
		temp = eyeTran.Mul(temp);
		eyeTran.m[0][3] = -temp.x;
		eyeTran.m[1][3] = -temp.y;
		eyeTran.m[2][3] = -temp.z;
	}
	public void setupProjTran(float l, float r, float b, float t, float n, float f){
		float[][] projContent = {
				{2*n/(r-l),          0,  (l+r)/(l-r),           0},
				{        0,  2*n/(t-b),  (b+t)/(b-t),           0},
				{        0,          0,  (n+f)/(n-f), 2*f*n/(f-n)},
				{        0,          0,            1,           0}
		};
		projTran = new Matrix4f(projContent);
	}
	
	public void setupViewPortTran(int nx, int ny){
		float[][] viewPortContent = {
				{nx/2,    0,  0, (nx-1)/2},
				{   0, ny/2,  0, (ny-1)/2},
				{   0,    0,  1,        0},
				{   0,    0,  0,        1}
		};
		viewPortTran = new Matrix4f(viewPortContent);
	}
	
	public void setupCompTrans(){
		
		compTran = viewPortTran.Mul(projTran).Mul(eyeTran).Mul(modelTran);
	}
	
	public Matrix4f getCompTrans(){
		return compTran;
	}
	
	public void ApplyAllTrans(){
		vInEye = eyeTran.Mul(modelTran).Mul(vertices);
		vOnScreen = compTran.Mul(vertices);
		normalize_W(vOnScreen);
	}
	
	
	public void print(Vector4f[] vl){
		for (Vector4f v : vl){
			System.out.println("{" + v.x + ',' + v.y + "},");
		}
	}
	
	private void normalize_W(Vector4f[] vl){
		for (Vector4f v : vl){
			v.x /= v.w;
			v.y /= v.w;
			v.z /= v.w;
			v.w = 1;
		}
	}
}

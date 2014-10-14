package cathedral;

import java.nio.*;
import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import ray.math.Triangle;
import ray.math.Vector3;
import ray.scene.Camera;
import ray.scene.ImagePlane;
import ray.scene.Light;
import ray.shader.Pixel;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.*;


public class Renderer implements GLEventListener {

	@Override
	public void display(GLAutoDrawable gLDrawable) {
		int width = 512, height = 512;
		
		System.out.println("display() called");
		final GL2 gl = gLDrawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		MeshLoader mhld = new MeshLoader("data/sibenik.obj");
		mhld.load_mesh();
		
		ImagePlane iPlane = ImagePlane.getDefault();
		iPlane.cam = new Camera();
		iPlane.mesh = new TrianglesMesher(mhld.gTriangles, iPlane.cam.position);
		iPlane.light = new Light (0, 0, 0, 1);
		
		long start = System.currentTimeMillis();
		iPlane.loadScene();
		long end = System.currentTimeMillis();
		
		System.out.println("Running Time: " + (end - start)*0.001 + " s");
		ByteBuffer srcBuffer = Pixel.wrapToUsignedBytes(iPlane.imagePixels, width, height);

        gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        gl.glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
        gl.glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);

        gl.glDrawPixels(iPlane.width, iPlane.height, GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, srcBuffer);
		gl.glFlush();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(GLAutoDrawable gLDrawable) {
		System.out.println("init() called");
		GL2 gl = gLDrawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glShadeModel(GL2.GL_SMOOTH);
	}

	@Override
	public void reshape (GLAutoDrawable gLDrawable, int x, int y, int width, int height) 
	{
		System.out.println("reshape() called: x = "+x+", y = "+y+", width = "+width+", height = "+height);

		if (height <= 0) // avoid a divide by zero error!
		{
			height = 1;
		}

	}

	private FloatBuffer arrayToBuffer(float [] x){
		int n = x.length;
		FloatBuffer col = FloatBuffer.allocate(n);
		for (int i=0;i<n;i++) col.put(i,x[i]);
		return col;
	}

	public void setPixel(GL2 gl,int x, int y, float [] col)
	{
		gl.glRasterPos2i(x,y);
		gl.glDrawPixels(1, 1, GL2.GL_RGB, GL2.GL_FLOAT, arrayToBuffer(col) );
	}

}

package pipeline.gui;

import java.nio.*;
import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import static javax.media.opengl.GL2.*;


public class Renderer implements GLEventListener {
	Pixel[][] screenPixels;
	int width, height;
	
	
	public Renderer(Pixel[][] p, int w, int h){
		screenPixels = p;
		width = w;
		height = h;
	}
	
	private GLU glu = new GLU();

	@Override
	public void display(GLAutoDrawable gLDrawable) {
		int width = 512, height = 512;

		final GL2 gl = gLDrawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		
		ByteBuffer srcBuffer = Pixel.wrapToUsignedBytes(screenPixels, width, height);

		gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		gl.glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
		gl.glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);

		gl.glDrawPixels(512, 512, GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, srcBuffer);
		gl.glFlush();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(GLAutoDrawable gLDrawable) {
		GL2 gl = gLDrawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glShadeModel(GL2.GL_FLAT);
	}

	@Override
	public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) 
	{
		final GL2 gl = gLDrawable.getGL().getGL2();

		if (height <= 0) // avoid a divide by zero error!
		{
			height = 1;
		}

		final float h = (float) width / (float) height;

		glu.gluOrtho2D (0, width, 0, height);
		gl.glLoadIdentity();

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(90.0f, h, 0.1, 10.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

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

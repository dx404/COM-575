package pipeline;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.vecmath.Vector3f;

import pipeline.gui.Renderer;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;


public class Entry implements Runnable{
	//corresponding to a) b) c) d) part
//	public static final char mode = 'a'; 
//	public static final char mode = 'b'; 
//	public static final char mode = 'c'; 
	public static final char mode = 'd'; 
			
			
	private static final String TITLE = "Pipeline PA2 - Duo Zhao";
	private static final int CANVAS_WIDTH = 512, CANVAS_HEIGHT = 512;

	public static void main(String[] args) {
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas glcanvas = new GLCanvas(caps);
		
		Scene scene = new Scene();
		scene.loadDefault();
		scene.setupModelTran(2, new Vector3f (0.0f, 0.0f, -7.0f));
		scene.setupEyeTran(
				new Vector3f(1, 0, 0), 
				new Vector3f(0, 1, 0), 
				new Vector3f(0, 0, 1), 
				new Vector3f(0, 0, 0)
				);
		
		scene.setupProjTran(-0.1f, 0.1f, -0.1f, 0.1f, -0.1f, -1000f);
		scene.setupViewPortTran(CANVAS_WIDTH, CANVAS_HEIGHT);
		scene.setupCompTrans();
		scene.ApplyAllTrans();
//		System.out.println(scene.modelTran);
//		System.out.println(scene.eyeTran);
//		System.out.println(scene.projTran);
//		System.out.println(scene.viewPortTran);
//		System.out.println(scene.getCompTrans());
		
		Rasterizer rs_a = new Rasterizer(scene);
		Rasterizer rs_b = new Rasterizer(scene);
		Rasterizer rs_c = new Rasterizer(scene);
		Rasterizer rs_d = new Rasterizer(scene);
		rs_a.rasterize_a();
		rs_b.rasterize_b();
		rs_c.rasterize_c();
		rs_d.rasterize_d();
		
		Renderer render_a = new Renderer(rs_a.screenPixels, 512, 512);
		Renderer render_b = new Renderer(rs_b.screenPixels, 512, 512);
		Renderer render_c = new Renderer(rs_c.screenPixels, 512, 512);
		Renderer render_d = new Renderer(rs_d.screenPixels, 512, 512);
		
		switch(mode){
			case 'a': glcanvas.addGLEventListener(render_a);
					break;
			case 'b': glcanvas.addGLEventListener(render_b);
					break;
			case 'c': glcanvas.addGLEventListener(render_c);
					break;
			case 'd': glcanvas.addGLEventListener(render_d);
					break;
			default: glcanvas.addGLEventListener(render_d);
					break;
		}
		
		JFrame frame = new JFrame(TITLE);
		frame.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		frame.add(glcanvas);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}}
		);
		
	
		
		Entry run1 = new Entry();
		Thread t1 = new Thread(run1);
		t1.start();
	}

	@Override
	public void run() {}

}

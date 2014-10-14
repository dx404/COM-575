package pipeline;

import javax.vecmath.Vector3f;

import pipeline.gui.Pixel;
import pipeline.math.BaryCen;
import pipeline.math.Fragment;
import pipeline.math.TriangleInEye;
import pipeline.math.VectorKit;
import pipeline.math.VertexInEye;

public class Rasterizer {
	Scene scene;
	public Pixel[][] screenPixels;
	public int width, height;
	public TriangleInEye tri;
	
	public Vector3f[] v_rgb; //Gouraud shading
	public Vector3f[] v_normal; //for phong shading interpolation
	public Float[][] depthBuffer;
	
	public Rasterizer(Scene scene){
		this.scene = scene;
		width = scene.screenWidth;
		height = scene.screenHeight;
		screenPixels = new Pixel[width][height];
		depthBuffer = new Float[width][height];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				depthBuffer[y][x] = Float.NEGATIVE_INFINITY;
		createBlackScreen();
	}
	
	public void rasterize_a(){
		createBlackScreen();
		BaryCen bc;
		int arrayToMark[][];
		int x, y; // coordinates to mark scene.gNumTriangles
		for (int tri_index = 0 ; tri_index < scene.gNumTriangles; tri_index++){
			int base = 3 * tri_index;
				int k0 = scene.gIndexBuffer[base];
				int k1 = scene.gIndexBuffer[base + 1];
				int k2 = scene.gIndexBuffer[base + 2];
				
				bc = new BaryCen(scene.vOnScreen[k0], scene.vOnScreen[k1], scene.vOnScreen[k2]);
				arrayToMark = bc.getInteriors();
				for (int i = 0; arrayToMark[i][0] >= 0 /*&& arrayToMark[i][1] >= 0*/  ; i++){
					x = arrayToMark[i][0];
					y = arrayToMark[i][1];
					screenPixels[y][x].set(1, 1, 1);
				}
		}
	}
	
	public void rasterize_b(){
		createBlackScreen();
		BaryCen bc;
		int arrayToMark[][];
		int x, y; // coordinates to mark scene.gNumTriangles
		for (int tri_index = 0 ; tri_index < scene.gNumTriangles; tri_index++){
			int base = 3 * tri_index;
//			try{
				int k0 = scene.gIndexBuffer[base];
				int k1 = scene.gIndexBuffer[base + 1];
				int k2 = scene.gIndexBuffer[base + 2];
				
				bc = new BaryCen(scene.vOnScreen[k0], scene.vOnScreen[k1], scene.vOnScreen[k2]);
				tri = new TriangleInEye(scene.vInEye[k0], scene.vInEye[k1], scene.vInEye[k2]);
				tri.getFlatPixel();
				arrayToMark = bc.getInteriors();
				for (int i = 0; arrayToMark[i][0] >= 0 /*&& arrayToMark[i][1] >= 0*/  ; i++){
					x = arrayToMark[i][0];
					y = arrayToMark[i][1];
					if (tri.centroid.z > depthBuffer[y][x]){
						depthBuffer[y][x] = tri.centroid.z;
						screenPixels[y][x].set(tri.rgb);
					}
				}
		}
	}
	
	public void rasterize_c(){
		createBlackScreen();
		v_rgb = new Vector3f[scene.gNumVertices];
		//Ground Vertex Processing
		for (int v_index = 0; v_index < scene.gNumVertices; v_index++){
			VertexInEye vtx = new VertexInEye(scene.vInEye[v_index]);
			vtx.getGouraudPixel();
			v_rgb[v_index] = new Vector3f(vtx.rgb);
		}
		
		BaryCen bc;
		int arrayToMark[][];
		int x, y; // coordinates to mark scene.gNumTriangles
		for (int tri_index = 0 ; tri_index < scene.gNumTriangles; tri_index++){
			int base = 3 * tri_index;
				int k0 = scene.gIndexBuffer[base];
				int k1 = scene.gIndexBuffer[base + 1];
				int k2 = scene.gIndexBuffer[base + 2];
				
				bc = new BaryCen(scene.vOnScreen[k0], scene.vOnScreen[k1], scene.vOnScreen[k2]);
				arrayToMark = bc.getInteriors();
				for (int i = 0; arrayToMark[i][0] >= 0 /*&& arrayToMark[i][1] >= 0*/  ; i++){
					x = arrayToMark[i][0];
					y = arrayToMark[i][1];
					bc.setTarget(x, y);
					if (bc.centroid.z > depthBuffer[y][x]){
						depthBuffer[y][x] = bc.centroid.z;
						screenPixels[y][x].setWeighTriAvg(bc.alpha, bc.beta, bc.gamma, 
								v_rgb[k0], v_rgb[k1], v_rgb[k2]);
					}
				}
		}
	}
	
	//phong
	public void rasterize_d(){
		createBlackScreen();
		v_normal = new Vector3f[scene.gNumVertices];
		//Ground Vertex Processing
		for (int v_index = 0; v_index < scene.gNumVertices; v_index++){
			VertexInEye vtx = new VertexInEye(scene.vInEye[v_index]);
			vtx.getNormal(); // only use vertex normal
			v_normal[v_index] = new Vector3f(vtx.normal);
		}
		
		BaryCen bc;
		int arrayToMark[][];
		int x, y; // coordinates to mark scene.gNumTriangles
		for (int tri_index = 0 ; tri_index < scene.gNumTriangles; tri_index++){
			int base = 3 * tri_index;
				int k0 = scene.gIndexBuffer[base];
				int k1 = scene.gIndexBuffer[base + 1];
				int k2 = scene.gIndexBuffer[base + 2];
				
				bc = new BaryCen(scene.vOnScreen[k0], scene.vOnScreen[k1], scene.vOnScreen[k2]);
				arrayToMark = bc.getInteriors();
				for (int i = 0; arrayToMark[i][0] >= 0 ; i++){
					x = arrayToMark[i][0];
					y = arrayToMark[i][1];
					bc.setTarget(x, y);
					
					Vector3f itp_fragPoint = VectorKit.Interpolate(
							bc.alpha, bc.beta, bc.gamma, 
							scene.vInEye[k0], scene.vInEye[k1], scene.vInEye[k2]);
					
					Vector3f itp_normal = VectorKit.Interpolate(
							bc.alpha, bc.beta, bc.gamma, 
							v_normal[k0], v_normal[k1], v_normal[k2]);
					itp_normal.normalize();
					
					Fragment phongFrag = new Fragment(itp_fragPoint, itp_normal);
					phongFrag.getPhongPixel();

					if (itp_fragPoint.z > depthBuffer[y][x]){
						depthBuffer[y][x] =itp_fragPoint.z;
						screenPixels[y][x].set(phongFrag.rgb);
					}
				}
		}
	}
	
	private void createBlackScreen(){
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				screenPixels[y][x] = new Pixel(0,0,0);
	}
	
}

package ray.shader;

public class SurfaceShader implements Runnable{
	public Pixel[][] imagePixels;
	public LocalSurfaceShader localShader;
	public int i, j;

	public SurfaceShader(LocalSurfaceShader lshader, Pixel[][] imagePixels, int i, int j){
		this.localShader = lshader;
		this.imagePixels = imagePixels;
		this.i = i;
		this.j = j;
	}
	
	@Override
	public void run() {
		localShader.recursiveShade(0);
		imagePixels[j][i].set(localShader.mixedColor);
		
	}
}

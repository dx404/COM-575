package ray.shader;

import java.nio.*;

import ray.math.Vector3;

public class Pixel {
	public double red, green, blue;
	public Pixel (double r, double g, double b){
		red = r;
		green = g;
		blue = b;
	}

	public Pixel (Vector3 rgb){
		red = Math.min(1, rgb.x);
		green = Math.min(1, rgb.y);
		blue = Math.min(1, rgb.z);
	}

	public byte[] toByteArray(){
		byte [] bya = new byte[3];
		bya[0] = (red < 0.5)? 
				(byte) Math.round(255 * red)
				:(byte)(Math.round(255 * red) - 256);
				bya[1] = (green < 0.5)? 
						(byte) Math.round(255 * green)
						:(byte)(Math.round(255 * green) - 256);
						bya[2] = (blue < 0.5)? 
								(byte) Math.round(255 * blue)
								:(byte)(Math.round(255 * blue) - 256);
								return bya;
	}

	public void set (double r, double g, double b){
		red = r;
		green = g;
		blue = b;
	}

	public void set (Vector3 rgb){	
		red = Math.min(1, rgb.x);
		green = Math.min(1, rgb.y);
		blue = Math.min(1, rgb.z);

		red = Math.pow(red, 1/2.2);
		green = Math.pow(green, 1/2.2);
		blue = Math.pow(blue, 1/2.2);
	}

	public static ByteBuffer wrapToUsignedBytes(Pixel[][] pixels, int width, int height){
		byte[] src = new byte[3 * width * height];
		int index = 0;
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++){
				byte[] rgb_Bytes = pixels[i][j].toByteArray();
				src[index++] = rgb_Bytes[0];
				src[index++] = rgb_Bytes[1];
				src[index++] = rgb_Bytes[2];
			}
		return ByteBuffer.wrap(src);
	}

	public void clamp(double min, double max) {

		red = Math.max(Math.min(red, max), min);
		green = Math.max(Math.min(green, max), min);
		blue = Math.max(Math.min(blue, max), min);
	}

	/**
	 * Gamma corrects this color.
	 * @param gamma the gamma value to use (2.2 is generally used).
	 */
	public void gammaCorrect(double gamma) {

		double inverseGamma = 1.0 / gamma;
		this.red = Math.pow(red, inverseGamma);
		this.green = Math.pow(green, inverseGamma);
		this.blue = Math.pow(blue, inverseGamma);
	}


	public String toString(){
		return "r: " + red + " g: " + green + " b: " + blue;
	}
}

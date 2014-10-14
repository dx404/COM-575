package pipeline.gui;

import java.nio.*;
import javax.vecmath.Vector3f;


public class Pixel {
	public double red, green, blue;
	public Pixel (float r, float g, float b){
		red = r;
		green = g;
		blue = b;
	}

	public Pixel (){
		red = 0;
		green = 0;
		blue = 0;
	}
	
	public Pixel (Vector3f rgb){
		red = rgb.x;
		green = rgb.y;
		blue = rgb.z;
		
		double max_intensity = red > green ? red: green;
		max_intensity = blue > max_intensity ? blue : max_intensity;
		if (max_intensity > 1){
			red /= max_intensity;
			green /= max_intensity;
			blue /= max_intensity;
		}
	}

	public byte[] toByteArray(){
		byte [] bya = {
				(byte) Math.round(red * 255),
				(byte) Math.round(green * 255),
				(byte) Math.round(blue * 255)
		};
		return bya;
	}

	public void set (float r, float g, float b){
		red = r;
		green = g;
		blue = b;
	}
	
	public void set (Vector3f rgb){
		red = Math.min(1, rgb.x);
		green = Math.min(1, rgb.y);
		blue = Math.min(1, rgb.z);
		
		gammaCorrect(2.2);
	}
	
	public void setTriAvg (Vector3f rgb_0, Vector3f rgb_1, Vector3f rgb_2) {
		red = (rgb_0.x + rgb_1.x + rgb_2.x) / 3;
		green = (rgb_0.y + rgb_1.y + rgb_2.y) / 3;
		blue = (rgb_0.z + rgb_1.z + rgb_2.z) / 3;
		
		if (red > 1) red = 1;
		if (green > 1) green = 1;
		if (blue > 1) blue =1;
		
		gammaCorrect(2.2);
	}
	
	public void setWeighTriAvg (
			float alpha, float beta, float gamma, 
			Vector3f rgb_0, Vector3f rgb_1, Vector3f rgb_2
		) {
		red = alpha * rgb_0.x + beta * rgb_1.x + gamma * rgb_2.x;
		green = alpha * rgb_0.y + beta * rgb_1.y + gamma * rgb_2.y;
		blue = alpha * rgb_0.z + beta * rgb_1.z + gamma *rgb_2.z;
		
		if (red > 1) red = 1;
		if (green > 1) green = 1;
		if (blue > 1) blue =1;
		
		gammaCorrect(2.2);
	}
	
	
	public void gammaCorrect(double gamma){
		red = Math.pow(red, 1/gamma);
		green = Math.pow(green, 1/gamma);
		blue = Math.pow(blue, 1/gamma);
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
	
	public String toString(){
		return "r: " + red + " g: " + green + " b: " + blue;
	}
}

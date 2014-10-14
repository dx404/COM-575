package cathedral;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ray.math.Triangle;
import ray.math.Vector3;

public class MeshLoader {
	private String fileName;
	public ArrayList<Vector3> gPositions = new ArrayList<Vector3>();
	public ArrayList<Triangle> gTriangles = new ArrayList<Triangle>();
	
	public MeshLoader(String fileName){
		this.fileName = fileName;
	}
	
	private int face_index(final String streamedString){
		String[] tokens = streamedString.split("/");
		return Integer.parseInt(tokens[0]);
	}
	
	void load_mesh(){
		try {
			BufferedReader fin = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = fin.readLine()) != null){
				if (line.length() <= 1)
					continue;
				String[] tokens = line.split("\\s+");
				if (tokens[0].equals("v")){
					float x = Float.parseFloat(tokens[1]);
					float y = Float.parseFloat(tokens[2]);
					float z = Float.parseFloat(tokens[3]);
											
					xmin = Math.min(x, xmin);
					xmax = Math.max(x, xmax);
					ymin = Math.min(y, ymin);
					ymax = Math.max(y, ymax);
					zmin = Math.min(z, zmin);
					zmax = Math.max(z, zmax);

					Vector3 position = new Vector3(x, y, z);
					gPositions.add(position);
				}
				else if (tokens[0].equals("f"))
				{
					int a = face_index(tokens[1]);
					int b = face_index(tokens[2]);
					int c = face_index(tokens[3]);
					Triangle triangle= new Triangle();
					triangle.indices[0] = a - 1;
					triangle.indices[1] = b - 1;
					triangle.indices[2] = c - 1;
					gTriangles.add(triangle);
				}
			}
			fin.close();
			System.out.println(line);
		}
		catch(IOException e){
			System.out.printf("ERROR: Unable to load mesh from %s!\n", fileName);
			System.exit(0);
		}
		
		Triangle.setVerticesSet(gPositions);
		for (Triangle tri : gTriangles){
			tri.calVerices();
			tri.calUnitNormal();
		}
		System.out.println(
					"Loaded mesh from " + fileName + ". (" +
					gPositions.size() + " vertices, " +
					gTriangles.size() + " triangles)" 
				);

		System.out.println(
				"(" +  xmin + ", " + ymin + ", " + zmin + ") to " +
						"(" +  xmax + ", " + ymax + ", " + zmax + ")"
				);
	}
	
	float xmin = Float.MAX_VALUE;
	float xmax = -Float.MAX_VALUE;
	float ymin = Float.MAX_VALUE;
	float ymax = -Float.MAX_VALUE;
	float zmin = Float.MAX_VALUE; 
	float zmax = -Float.MAX_VALUE; 
	
}

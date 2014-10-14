package ray.math;

/**
 * For computation of the a x^2 + b x + c = 0 form of equations
 * for real numbers and read solutions.
 * @author Duo Zhao;
 *
 */
public class Quadratic {
	private Double a, b, c;
	
	private Double rootMin = Double.NEGATIVE_INFINITY; 
	private Double rootMax = Double.POSITIVE_INFINITY;
	
	private Double rootSmall, rootLarge;
	
	private double delta; 
	
	public Quadratic (double a, double b, double c){
		this.a = a;
		this.b = b;
		this.c = c;
		solveIt();
	}
	
	public void set (double a, double b, double c){
		this.a = a;
		this.b = b;
		this.c = c;
		solveIt();
	}
	
	public Quadratic (double a, double b, double c, Double start, Double end){
		this.a = a;
		this.b = b;
		this.c = c;
		this.rootMin = start;
		this.rootMax = end;
		solveIt();
	}
	
	public void set (double a, double b, double c, Double start, Double end){
		this.a = a;
		this.b = b;
		this.c = c;
		this.rootMin = start;
		this.rootMax = end;
		solveIt();
	}
	
	public Double getRootLarge(){
		return rootLarge;
	}
	
	public Double getRootSmall(){
		return rootSmall;
	}
	
	private void solveIt(){
		delta = b * b - 4 * a * c;
		if (a == 0 || delta < 0){
			this.rootSmall = Double.NaN;
			this.rootLarge = Double.NaN;
		}
		else{
			this.rootSmall = (-b - Math.sqrt(delta))/(2*a);
			this.rootLarge = (-b + Math.sqrt(delta))/(2*a);
			if (rootSmall < rootMin || rootSmall > rootMax){
				rootSmall = Double.NaN;
			}
			if (rootLarge < rootMin || rootLarge > rootMax){
				rootLarge = Double.NaN;
			}
		}
	}
}

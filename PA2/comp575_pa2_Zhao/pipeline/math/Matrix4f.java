package pipeline.math;

import javax.vecmath.Vector4f;
import javax.vecmath.Vector3f;

public class Matrix4f {

	public float[][] m = new float[4][4];

	public Matrix4f() {
		setIdentity();
	}
	
	public Matrix4f(float[][] newM) {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				m[i][j] = newM[i][j];
	}
	
	public void setIdentity() {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				m[i][j] = 0;
		for (int i = 0; i < 4; i++)
			m[i][i] = 1;
	}

	public Matrix4f set(float[][] newM) {
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				m[i][j] = newM[i][j];
		return this;
	}
	
	public Matrix4f clear(){
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				m[i][j] = 0;
		return this;
	}


	public Matrix4f set(Matrix4f newMat) {
		set(newMat.m);
		return this;
	}
	
	public Vector4f Mul(Vector4f vIn) {
		float[] vInArray = {vIn.x, vIn.y, vIn.z, vIn.w};
		float[] vOutArray = new float[4];
		
		for (int i = 0; i < 4; i++){
			vOutArray[i] = 0;
			for (int j = 0; j < 4; j++)
				vOutArray[i] += this.m[i][j]*vInArray[j];
		}
		return new Vector4f(vOutArray);
	}
	
	public Matrix4f Mul(Matrix4f mIn){
		float[][] mOut = new float[4][4];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++){
				mOut[i][j] = 0;
				for (int k = 0; k < 4; k++)
					mOut[i][j] += this.m[i][k]*mIn.m[k][j];
			}
		return new Matrix4f(mOut);
	}
	
	public Vector4f[] Mul(Vector4f[] vInArray) {
		Vector4f[] vOutArray = new Vector4f[vInArray.length];
		for (int i = 0; i < vInArray.length; i++){
			float x_in = vInArray[i].x;
			float y_in = vInArray[i].y;
			float z_in = vInArray[i].z;
			float w_in = vInArray[i].w;
			
			float x_out = m[0][0] * x_in + m[0][1] * y_in + m[0][2] * z_in + m[0][3] * w_in;
			float y_out = m[1][0] * x_in + m[1][1] * y_in + m[1][2] * z_in + m[1][3] * w_in;
			float z_out = m[2][0] * x_in + m[2][1] * y_in + m[2][2] * z_in + m[2][3] * w_in;
			float w_out = m[3][0] * x_in + m[3][1] * y_in + m[3][2] * z_in + m[3][3] * w_in;
			vOutArray[i] = new Vector4f(x_out, y_out, z_out, w_out);
		}
		return vOutArray;
	}

	/**
	 * Sets this matrix to be a camera to frame matrix.
	 * @param u The u vector of the frame.
	 * @param v The v vector of the frame.
	 * @param w The w vector of the frame.
	 * @param p The origin of the frame.
	 */
	public void setCtoF(Vector3f u, Vector3f v, Vector3f w, Vector3f p) {
		setIdentity();
		m[0][0] = u.x;
		m[0][1] = u.y;
		m[0][2] = u.z;
		m[1][0] = v.x;
		m[1][1] = v.y;
		m[1][2] = v.z;
		m[2][0] = w.x;
		m[2][1] = w.y;
		m[2][2] = w.z;
		Vector4f temp = new Vector4f(p.x, p.y, p.z, 1.0f);
		rightMultiply(temp);
		m[0][3] = -temp.x;
		m[1][3] = -temp.y;
		m[2][3] = -temp.z;
	}

	/**
	 * Sets this matrix to be a frame to camera matrix.
	 * 
	 * @param u The u vector of the frame.
	 * @param v The v vector of the frame.
	 * @param w The w vector of the frame.
	 * @param p The origin of the frame.
	 */
	public void setFtoC(Vector3f u, Vector3f v, Vector3f w, Vector3f p) {

		setIdentity();
		m[0][0] = u.x;
		m[0][1] = v.x;
		m[0][2] = w.x;
		m[0][3] = p.x;
		m[1][0] = u.y;
		m[1][1] = v.y;
		m[1][2] = w.y;
		m[1][3] = p.y;
		m[2][0] = u.z;
		m[2][1] = v.z;
		m[2][2] = w.z;
		m[2][3] = p.z;
	}

	/**
	 * This sets the current matrix to a rotation about the indicated axis.
	 * 
	 * @param angle The magnitude (in radians) of the rotation.
	 * @param axis The axis about which the rotation will occur.
	 */
	public void setRotate(float angle, Vector3f axis) {

		Vector3f u = new Vector3f(axis);
		u.normalize();
		float[] ua = new float[3];
		u.get(ua);
		setIdentity();
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				m[i][j] = ua[i] * ua[j];
		float cosTheta = (float) Math.cos(angle);
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				m[i][j] += cosTheta * ((i == j ? 1.0f : 0.0f) - ua[i] * ua[j]);
		float sinTheta = (float) Math.sin(angle);
		m[1][2] -= sinTheta * u.x;
		m[2][1] += sinTheta * u.x;
		m[2][0] -= sinTheta * u.y;
		m[0][2] += sinTheta * u.y;
		m[0][1] -= sinTheta * u.z;
		m[1][0] += sinTheta * u.z;
	}

	/**
	 * This sets the matrix to a translation transformation.
	 * 
	 * @param v The vector describing the translation.
	 */
	public Matrix4f setTranslate(Vector3f v) {
		setIdentity();
		m[0][3] = v.x;
		m[1][3] = v.y;
		m[2][3] = v.z;
		return this;
	}

	/**
	 * This sets the matrix to a scaling transformation.
	 * 
	 * @param v The vector that describes the scaling.
	 */
	public Matrix4f setScale(Vector3f v) {
		setIdentity();
		m[0][0] = v.x;
		m[1][1] = v.y;
		m[2][2] = v.z;
		return this;
	}
	
	public Matrix4f setScale(float s){
		setIdentity();
		m[0][0] *= s;
		m[1][1] *= s;
		m[2][2] *= s;
		return this;
	}

	/** Temporary storage for doing a matrix multiply */
	private float[][] tempM = new float[4][4];

	/**
	 * Concatenates t onto this matrix, and places the result in the second
	 * parameter. result = t*this. There are no restrictions of the form t !=
	 * result, or result != this, etc.
	 * 
	 * @param t The left operand.
	 * @param result The destination for the result.
	 */
	public void leftMultiply(Matrix4f t, Matrix4f result) {

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {

				tempM[i][j] = 0;
				for (int k = 0; k < 4; k++) {
					tempM[i][j] += t.m[i][k] * m[k][j];
				}

			}
		}

		result.set(tempM);
	}

	/**
	 * Sets this matrix to be the left product of t and itself. This = t*this.
	 * 
	 * @param t The left hand operand.
	 */
	public void leftCompose(Matrix4f t) {

		leftMultiply(t, this);
	}

	/**
	 * Concatenates this matrix onto t, and places the result in the second
	 * parameter. result = this(t. There are no restrictions of the form t !=
	 * result, or result != this, etc.
	 * 
	 * @param t The right hand operand
	 * @param result The destination for the result.
	 */
	public void rightMultiply(Matrix4f t, Matrix4f result) {

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				tempM[i][j] = 0;
				for (int k = 0; k < 4; k++) {
					tempM[i][j] += m[i][k] * t.m[k][j];
				}
			}
		}

		result.set(tempM);
	}

	/**
	 * Sets this matrix to be the right product of t and itself. This = this*t.
	 * 
	 * @param t The right hand operand.
	 */
	public void rightCompose(Matrix4f t) {

		rightMultiply(t, this);
	}

	/** Temporary storage for doing a vector multiply */
	private float[] tempV = new float[4];

	/** Convenience vector used to make indexing into a Vector4f easier */
	private float[] tempVOut = new float[4];

	/**
	 * Transforms the vector by the current matrix and stores the result in the
	 * given vector.
	 * 
	 * @param vInOut The vector to transform, and the destination vector.
	 */
	public void rightMultiply(Vector4f vInOut) {

		rightMultiply(vInOut, vInOut);
	}

	/**
	 * Transforms the v vector by this matrix, and places the result in vOut. v
	 * and vOut may reference the same object.
	 * 
	 * @param v The vector to transform.
	 * @param vOut The destination vector.
	 */
	public void rightMultiply(Vector4f v, Vector4f vOut) {

		v.get(tempV);
		for (int i = 0; i < 4; i++) {
			tempVOut[i] = 0;
			for (int j = 0; j < 4; j++) {
				tempVOut[i] += m[i][j] * tempV[j];
			}
		}
		vOut.set(tempVOut);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		return "Matrix4f: [" + m[0][0] + "  " + m[0][1] + "  " + m[0][2] + "  " + m[0][3] + "\n" + "           " + m[1][0] + "  " + m[1][1] + "  " + m[1][2] + "  " + m[1][3] + "\n" + "           "
				+ m[2][0] + "  " + m[2][1] + "  " + m[2][2] + "  " + m[2][3] + "\n" + "           " + m[3][0] + "  " + m[3][1] + "  " + m[3][2] + "  " + m[3][3] + "]\n";
	}

}

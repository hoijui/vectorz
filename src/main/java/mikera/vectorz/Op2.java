package mikera.vectorz;

import mikera.arrayz.INDArray;
import mikera.matrixx.AMatrix;
import mikera.vectorz.impl.ADenseArrayVector;

/**
 * Abstract class for representing a unary operation
 * 
 * @author Mike
 */
public abstract class Op2 {
	
	public abstract double apply(double x, double y);
	
	/**
	 * Applies the inverse of this Op. Throws an error if the inverse function does not exist.
	 * Returns Double.NaN if no inverse exists for the specific value of y.
	 * 
	 * @param y
	 * @return
	 */
	public double applyInverse(double y) {
		throw new UnsupportedOperationException("Inverse not defined for operator: "+this.toString());
	}
	
	public void applyTo(AVector a, AVector b) {
		if (a instanceof ADenseArrayVector) {
			applyTo((ADenseArrayVector)a,b);
		} else {
			a.applyOp(this,b);
		}
	}
	
	public void applyTo(AMatrix a, AMatrix b) {
		a.applyOp(this, b);
	}
	
	public void applyTo(AVector v, int start, int length, AVector b) {
		if (start<0) throw new IllegalArgumentException("Negative start position: "+start);
		if ((start==0)&&(length==v.length())) {
			v.applyOp(this,b);
		} else {
			v.subVector(start, length).applyOp(this,b);
		}
	}
	
	public void applyTo(AScalar a, AScalar b) {
		a.set(apply(a.get(),b.get()));
	}
	
	public void applyTo(ADenseArrayVector a, AVector b) {
		applyTo(a.getArray(), a.getArrayOffset(),a.length(),b);
	}
	
	
	public void applyTo(INDArray a, INDArray b) {
		if (a instanceof AVector) {
			applyTo((AVector)a,b.broadcastLike(a));
		} else if (a instanceof AMatrix) {
			applyTo((AMatrix)a,b.broadcastLike(a));
		} else if (a instanceof AScalar) {
			applyTo((AScalar)a,b.broadcastLike(a));
		} else {
			a.applyOp(this,b);
		}
	}

	public void applyTo(double[] data, int start, int length, AVector b) {
		b.checkLength(length);
		for (int i=0; i<length; i++) {
			double x=data[start+i];
			data[start+i]=apply(x,b.unsafeGet(i));
		}
	}
	
	public void applyTo(double[] data, int start, int length, double b) {
		for (int i=0; i<length; i++) {
			double x=data[start+i];
			data[start+i]=apply(x,b);
		}
	}
	
	public void applyTo(double[] data, AVector b) {
		applyTo(data,0,data.length,b);
	}
	
	public void applyTo(double[] data, double b) {
		applyTo(data,0,data.length,b);
	}
	
	public boolean hasDerivative() {
		return false;
	}
	
	public boolean hasDerivativeForOutput() {
		return hasDerivative();
	}
	
	public boolean hasInverse() {
		return false;
	}
	
	/**
	 * Returns true if the operator is stochastic, i.e returns random values for at least some inputs
	 * @return
	 */
	public boolean isStochastic() {
		return false;
	}
	
	public double averageValue() {
		throw new UnsupportedOperationException();
	}
	
	public double minValue() {
		return Double.NEGATIVE_INFINITY;
	}
	
	public double maxValue() {
		return Double.POSITIVE_INFINITY;
	}
	
	public double minDomain() {
		return Double.NEGATIVE_INFINITY;
	}
	
	public double maxDomain() {
		return Double.POSITIVE_INFINITY;
	}
	
	public boolean isDomainBounded() {
		return (minDomain()>=-Double.MAX_VALUE)||(maxDomain()<=Double.MAX_VALUE);
		
	}
	
	/**
	 * Validates whether all values in a double[] are within the possible output range for this Op
	 * @param output
	 * @return
	 */
	public boolean validateOutput(double[] output) {
		double min=minValue();
		double max=maxValue();
		for (double d: output) {
			if ((d<min)||(d>max)) return false;
		}
		return true;
	}
	
	/**
	 * Creates a copy of the values of src in dest, constraining them to be within the valid
	 * range of output values from this Op
	 * @param src
	 * @param dest
	 * @param offset
	 * @param length
	 */
	public void constrainValues(double[] src, double[] dest, int offset, int length) {
		if (!isBounded()) {
			System.arraycopy(src, 0, dest, offset, length);
		}
		double min=minValue();
		double max=maxValue();
		
		for (int i=offset; i<(offset+length); i++) {
			double v=src[i];
			if (v>max) {
				dest[i]=max;
			} else if (v<min) {
				dest[i]=min;
			} else {
				dest[i]=v;
			}
		}		
	}
	
	public boolean isBounded() {
		return (minValue()>=-Double.MAX_VALUE)||(maxValue()<=Double.MAX_VALUE);
	}
	
	@Override public String toString() {
		return getClass().toString();
	}

	/**
	 * Method to reduce over a sequence of zeros. Optimises for common stable cases.
	 * @param init
	 * @param length
	 * @return
	 */
	public double reduceZeros(double init, int length) {
		if (length==0) return init;
		if (length==1) return apply(init,0.0);
		
		if (isStochastic()) {
			// can't guarantee stability
			for (int i=0; i<length; i++) {
				init=apply(init,0.0);
			}
			return init;
		} else {
			double r1=apply(init,0.0);
			if (r1==init) return r1; // is stable after one applications to zero?
			double r2=apply(r1,0.0); 
			if (r2==r1) return r2; // is stable after two applications to zero?
			for (int i=2; i<length; i++) {
				r2=apply(r2,0.0);
			}
			return r2;
		}
	}
}

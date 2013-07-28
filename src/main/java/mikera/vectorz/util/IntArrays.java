package mikera.vectorz.util;

import mikera.util.Rand;

public class IntArrays {
	public static final int[] EMPTY_INT_ARRAY=new int[0];
	
	public static int[] removeIndex(int[] data, int index) {
		int len=data.length;
		int[] result=new int[len-1];
		System.arraycopy(data, 0, result, 0, index);
		System.arraycopy(data, index+1, result, index, len-index-1);
		return result;
	}

	public static int[] reverse(int[] data) {
		int n = data.length;
		int[] result=new int[n];
		for (int i=0; i<n; i++) {
			result[n-1-i]=data[i];
		}
		return result;
	}

	public static int[] consArray(int a, int[] as) {
		int len=as.length;
		int[] nas=new int[len+1];
		nas[0]=a;
		System.arraycopy(as, 0, nas, 1, len);
		return nas;
	}

	public static long[] copyIntsToLongs(int[] src, long[] dst) {
		for (int i=0; i<src.length; i++) {
			dst[i]=src[i];
		}
		return dst;
	}
	
	public static long[] copyIntsToLongs(int[] src) {
		long[] dst=new long[src.length];
		return copyIntsToLongs(src,dst);
	}

	public static long arrayProduct(int[] shape) {
		long r=1;
		for (int x:shape) {
			r*=x;
		}
		return r;
	}

	public static long arrayProduct(int[] shape, int from, int to) {
		long r=1;
		for (int i=from; i<to; i++) {
			r*=shape[i];
		}
		return r;
	}

	public static final int[] calcStrides(int[] shape) {
		int dimensions=shape.length;
		int[] stride=new int[dimensions];
		int st=1;
		for (int j=dimensions-1; j>=0; j--) {
			stride[j]=st;
			st*=shape[j];
		}
		return stride;
	}
	
	public static boolean equals(int[] as, int[] bs) {
		int n=as.length;
		if (n!=bs.length) return false;
		for (int i=0; i<n; i++) {
			if (as[i]!=bs[i]) return false;
		}
		return true;
	}

	/**
	 * Creates a randomised int[] array, each element in the range [0..max)
	 * where max is the corresponding element in the shape array
	 * 
	 * @param shape
	 * @return
	 */
	public static int[] rand(int[] shape) {
		int n=shape.length;
		int[] result=new int[n];
		for (int i=0; i<n; i++) {
			result[i]=Rand.r(shape[i]);
		}
		return result;
	}

	public static int dotProduct(int[] xs, int[] ys) {
		int result=0;
		int n=xs.length;
		if (ys.length!=n) throw new IllegalArgumentException("Different array sizes");
		for (int i=0; i<n; i++) {
			result+=xs[i]*ys[i];
		}
		return result;
	}

	public static int[] decrementAll(int[] xs) {
		int len=xs.length;
		int[] rs=new int[len];
		for (int i=0; i<len; i++) {
			rs[i]=xs[i]-1;
		}
		return rs;
	}
	
	public static int[] incrementAll(int[] xs) {
		int len=xs.length;
		int[] rs=new int[len];
		for (int i=0; i<len; i++) {
			rs[i]=xs[i]+1;
		}
		return rs;
	}
}

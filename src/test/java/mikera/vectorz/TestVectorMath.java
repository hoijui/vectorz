package mikera.vectorz;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestVectorMath {

	@Test public void testDotProduct() {
		assertEquals(10.0,new Vector3(1,2,3).dotProduct(new Vector3(3,2,1)),0.000001);
	}
	
	@Test public void testMagnitude() {
		assertEquals(14.0,new Vector3(1,-2,3).magnitudeSquared(),0.000001);
		assertEquals(5.0,new Vector2(3,4).magnitude(),0.000001);
	}
	
	public void doMultiplyTests(AVector v) {
		v=v.clone();
		double m=v.magnitude();
		v.multiply(2.0);
		assertEquals(m*2.0, v.magnitude(),0.0001);
	}
	
	public void doNormaliseTests(AVector v) {
		v=v.clone();
		v.normalise();
		if (v.magnitude()>0.0) {
			assertEquals(1.0,v.magnitude(),0.0001);
		}
	}
	
	public void doFillTests(AVector v) {
		v=v.clone();
		v.fill(13.0);
		int len=v.length();
		for (int i=0; i<len; i++) {
			assertEquals(13.0,v.get(i),0.0);
		}	
	}
	
	public void doAdditionTests(AVector v) {
		v=v.clone();
		AVector ones=v.clone();
		ones.fill(1.0);
		
		AVector av=v.clone();
		av.add(ones);
		av.addMultiple(ones,1.5);
		
		int len=v.length();
		for (int i=0; i<len; i++) {
			assertEquals(v.get(i)+2.5,av.get(i),0.0001);
		}
	}
	
	public void doSubtractionTests(AVector v) {
		v=v.clone();
		AVector ones=v.clone();
		ones.fill(1.0);
		
		AVector av=v.clone();
		av.add(ones);
		av.sub(ones);
		assertEquals(v,av);
		
		av.addMultiple(ones,4);
		av.subMultiple(ones,1.5);

		int len=v.length();
		for (int i=0; i<len; i++) {
			assertEquals(v.get(i)+2.5,av.get(i),0.0001);
		}
	}
	
	private void doMagnitudeTests(AVector v) {
		assertEquals(v.magnitude(),Vectorz.createMutableVector(v).magnitude(),0.000001);
		
	}
	public void doGenericMaths(AVector v) {
		doFillTests(v);
		doMultiplyTests(v);
		doAdditionTests(v);
		doSubtractionTests(v);
		doNormaliseTests(v);
		doMagnitudeTests(v);
	}
	


	@Test public void testGenericMaths() {
		doGenericMaths(new Vector3(1.0,2.0,3.0));
		doGenericMaths(Vectorz.create(1,2,3,4,5,6,7));
		doGenericMaths(Vectorz.join(new Vector2(1.0,2.0),Vectorz.create(1,2,3,4,5,6,7)));
		doGenericMaths(Vectorz.create(1,2,3,4,5,6,7).subVector(2,3));
		
		for (int dim=0; dim<10; dim++) {
			AVector v=Vectorz.createLength(dim);
			doGenericMaths(v);
		}

	}
	
	@Test public void test3DMath() {
		Vector3 v=Vector3.of(1,2,3);
		
		Vector3 v2=v.clone();
		v2.add(v);
		v2.multiply(0.5);
		
		assertTrue(v.epsilonEquals(v2));
	}
	
	@Test public void testAngle() {
		Vector3 v=Vector3.of(1,2,3);
		assertEquals(0.0, v.angle(v),0.0001);
		
		Vector3 v2=v.clone();
		v2.negate();
		assertEquals(Math.PI, v.angle(v2),0.0001);
	}
}

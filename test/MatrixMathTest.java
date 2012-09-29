import static org.junit.Assert.*;

import org.junit.Test;


public class MatrixMathTest {

	@Test
	public void testDot() {
		double[] a = new double[] {1, 2, 3, 4};
		double aa = MatrixMath.dot(a, a);
		
		assertTrue(30 == aa);
	}

	@Test
	public void testMulti() {
		double[][] a = new double[][]
				{
				{0.5, 3.1, 0},
				{0.1, 0, 15},
				{0, 26, -4}
				};
		
		double[][] b = new double[][]
				{
				{1.5, 1, 9},
				{4, -3, 2},
				{5, 2, 3}
				};
		
		double[] u = new double[] {1,2,3};
		
		double[][] ab = new double[][]
				{
				{13.15, -8.8, 10.7},
				{75.15, 30.1, 45.9},
				{84, -86, 40}
				};

		double[][] ua = new double[][]
				{
				{0.7, 81.1, 18}
				};
		
		double[][] tab = MatrixMath.multi(a, b);
		double[] tua = MatrixMath.multi(u, a);
		
		System.out.println(MatrixMath.MatrixToString(tab));
		System.out.println();
		System.out.println(MatrixMath.MatrixToString(ab));
		System.out.println();
		System.out.println();
		System.out.println(MatrixMath.MatrixToString(tua));
		System.out.println();
		System.out.println(MatrixMath.MatrixToString(ua));
		
		assertTrue(MatrixMath.equal(ab, tab));
		assertTrue(MatrixMath.equal(ua, tua));
	}

	@Test
	public void testTranspose() {
		double[][] a = new double[][] {{1}};
		double[][] at = new double[][] {{1}};
		
		double[][] b = new double[][]
				{
				{11, 12},
				{21, 22}
				};
		double[][] bt = new double[][]
				{
				{11, 21},
				{12, 22}
				};
		
		double[][] c = new double[][]
				{
				{11, 12, 13},
				{21, 22, 23},
				{31, 32, 33}
				};
		double[][] ct = new double[][]
				{
				{11, 21, 31},
				{12, 22, 32},
				{13, 23, 33}
				};
		
		double[][] tat = MatrixMath.transpose(a);
		double[][] tatt = MatrixMath.transpose(at);
		
		double[][] tbt = MatrixMath.transpose(b);
		double[][] tbtt = MatrixMath.transpose(bt);
		
		double[][] tct = MatrixMath.transpose(c);
		double[][] tctt = MatrixMath.transpose(ct);
		
		assertTrue(MatrixMath.equal(tat, at));
		assertTrue(MatrixMath.equal(tatt, a));
		
		assertTrue(MatrixMath.equal(tbt, bt));
		assertTrue(MatrixMath.equal(tbtt, b));
		
		assertTrue(MatrixMath.equal(tct, ct));
		assertTrue(MatrixMath.equal(tctt, c));
	}
	/*
	@Test
	public void testMatrixNormDistance() {
		fail("Not yet implemented");
	}
	*/
}


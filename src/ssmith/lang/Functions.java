package ssmith.lang;

import java.util.Random;

public final class Functions {

	private static Random random = new Random();

	public static int rnd(int a, int b) {
		int var = b + 1 - a;
		return random.nextInt(var) + a;
	}

	
	public static byte rndByte(int a, int b) {
		assert a <= 127 && b <= 127;
		int var = b + 1 - a;
		return (byte)(random.nextInt(var) + a);
	}

	
	public static float rndFloat(float a, float b) {
		return (random.nextFloat() * (b - a)) + a;
	}

	
	public static double rndDouble(double a, double b) {
		return (random.nextDouble() * (b - a)) + a;
	}

	
	public static void delay(int milliseconds) {
		if (milliseconds > 0) {
			try {
				Thread.sleep(milliseconds);
			}
			catch (InterruptedException e) {
			}
		}
	}

	
	public static void delay(long milliseconds) {
		if (milliseconds > 0) {
			try {
				Thread.sleep(milliseconds);
			}
			catch (InterruptedException e) {
			}
		}
	}

	
}



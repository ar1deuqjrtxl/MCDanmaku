/*
 * RNG.java Random number generator 클래스
 * 
 * from https://github.com/Java2hu/Java2hu/blob/cf804b16f85aedfbc9c632a207973dd856de2e57/core/src/java2hu/RNG.java
 *
 *
 * Author: Java2hu
 */


package com.example.danmaku.util;

import java.util.Random;

public class RNG
{
	private static Random random;
	private static long seed;
	
	static
	{
		generate();
	}
	
	public static long generate()
	{
		long seed = (long) (Math.random() * Long.MAX_VALUE);
		setSeed(seed);
		return seed;
	}
	
	public static void setSeed(long seed)
	{
		RNG.random = new Random(seed);
		RNG.seed = seed;
	}
	
	public static long getSeed()
	{
		return seed;
	}
	
	public static Random get()
	{
		return random;
	}
	
	public static double random()
	{
		return get().nextDouble();
	}
	
	public static double randomMirror()
	{
		return (random() * 2d) - 1d;
	}
	
	public static double multiplier(long ticks, long currentTick)
	{
		return (currentTick % (double)ticks) / ticks;
	}
	
	public static boolean booleanMultiplier(long ticks, long currentTick)
	{
		return multiplierMirror(ticks, currentTick) < 0 ? false : true;
	}
	
	public static double multiplierMirror(long ticks, long currentTick)
	{
		return ((currentTick % (2d * ticks)) / ticks) - 1;
	}
	
	public final static boolean[] BOOLS = new boolean[] { true, false };
}
/*
 * MathUtil.java 수학 유틸리티 클래스
 * 
 * from https://github.com/Java2hu/Java2hu/blob/cf804b16f85aedfbc9c632a207973dd856de2e57/core/src/java2hu/util/MathUtil.java
 *
 *
 * Author: Java2hu
 */

package com.example.danmaku.util;

import com.example.danmaku.entities.PositionEntity;

public class MathUtil
{
	public static double getAngle(PositionEntity a, PositionEntity b)
	{
		if(a == null || b == null)
			return 0.0;
		
		return getAngle(a.x, a.y, b.x, b.y);
	}

	public static double getAngle(float xa, float ya, float xb, float yb)
	{
		double dx = (double) (xa - xb);
		double dz = (double) (ya - yb);
	
		float yaw = (float) Math.toDegrees(Math.atan2(dz, dx));

		return yaw;
	}
	
	public static double getDistance(float xa, float ya, float xb, float yb)
	{
		double deltaX = xa - xb;
		double deltaY = ya - yb;
		
		double distanceSquared = deltaX * deltaX + deltaY * deltaY;
		double distance = Math.sqrt(distanceSquared);
		
		return distance;
	}
	
	public static double getDistance(PositionEntity from, PositionEntity to)
	{
		return getDistance(from.x, from.y, to.x, to.y);
	}
	
	public static boolean inBoundary(float x, float y, float xMin, float xMax, float yMin, float yMax)
	{
		return x > xMin && x < xMax && y > yMin && y < yMax;
	}
	
	public static double normalizeDegree(float angle)
	{  
		angle = ((angle % 360) + 360) % 360;
		
		return angle;
	}
	
	public static double getDifference(double a, double b)
	{
		return Math.abs(a-b);
	}
	
	public static class SinCosTable
	{
		public int precision; // gradations per degree
		private int modulus;
		private double[] sin;
		
		public SinCosTable(int decimals)
		{
			precision = 1;
			
			for(int i = 0; i < decimals; i++)
			{
				precision = precision * 10;
			}
			
			modulus = Math.round(360f*precision);
			
			sin = new double[modulus];
			
		    for (double i = 0; i < 360; i += 1f / precision)
		    {
		        sin[toArrayPosition(i)]=Math.sin(Math.toRadians(i));
		    }
		}
		
		public int toArrayPosition(double degree)
		{
			degree = normalizeDegree((float) degree);
			
			int pos = (int) Math.round(degree * precision);
			
			if(pos == sin.length)
				pos = 0;
			
			return pos;
		}
		
		// Private function for table lookup
		private double sinLookup(double deg)
		{
		    return sin[toArrayPosition(deg)];
		}

		// These are your working functions:
		public double sin(double a)
		{
		    return sinLookup(a);
		}
		public double cos(double a)
		{
		    return sinLookup(a + 90f);
		}
	}
	
	private static SinCosTable table = new SinCosTable(3);
	
	public static double fastSin(double degree)
	{
		return table.sin(degree);
	}
	
	public static double fastCos(double degree)
	{
		return table.cos(degree);
	}

	public static double roundOff(double doubleValue, float places)
	{
		double multiplier = Math.pow(10, places);
		
		return Math.round(doubleValue * multiplier) / multiplier;
	}
}
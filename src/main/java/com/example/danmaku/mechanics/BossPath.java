/*
 * BossPath.java 보스의 Path 임플리먼테이션 클래스
 * 
 * driven from https://github.com/Java2hu/Java2hu/blob/cf804b16f85aedfbc9c632a207973dd856de2e57/core/src/java2hu/pathing/SimpleTouhouBossPath.java
 *
 *
 * Last edited on: 2024-08-20
 * Author: Java2hu, SteelPanty
 */

package com.example.danmaku.mechanics;

import com.example.danmaku.entities.Boss;
import com.example.danmaku.entities.PositionEntity;
import com.example.danmaku.mechanics.PathingHelper.Path;
import com.example.danmaku.util.Rectangle;

 
public class BossPath extends Path
{

	public BossPath(Boss onwer)
	{
		this(onwer, 50, 100);
	}
	
	public BossPath(Boss onwer, float width, float height)
	{
		this(onwer, getRectangle(width, height));
	}
	
	private static Rectangle getRectangle(float width, float height)
	{
		Rectangle box = new Rectangle(50, 25, (int) 0, (int) 0);
		
		return box;
	}
	
	public BossPath(Boss onwer, Rectangle rect)
	{
		super(onwer);
		this.bounds = rect;

        float targetX = (float)((float) bounds.x + ((float) Math.random() * (float) bounds.width));
		float targetY = (float)((float) bounds.y + ((float) Math.random() * (float) bounds.height));
		
		addPosition(new PositionEntity(targetX, targetY));
	}
	
	
	public BossPath(Boss onwer, float center_x, float center_y, float width, float height)
	{
		super(onwer);
		
		float minX = center_x - (width / 2f);
		float minY = center_y - (height / 2f);
				
		this.bounds = new Rectangle(minX, minY, width, height);
		
		float targetX = (float)(bounds.x + (Math.random() * bounds.width));
		float targetY = (float)(bounds.y + (Math.random() * bounds.height));
		
		addPosition(new PositionEntity(targetX, targetY));
	}
	
	private Rectangle bounds;
	
	@Override
	public void tick()
	{
		super.tick();
	};
}
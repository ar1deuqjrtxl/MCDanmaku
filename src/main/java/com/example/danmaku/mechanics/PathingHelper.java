/*
 * PathingHelper.java : 목적지로 단순이동하는 Path 정의
 * 
 * driven from https://github.com/Java2hu/Java2hu/blob/cf804b16f85aedfbc9c632a207973dd856de2e57/core/src/java2hu/pathing/PathingHelper.java
 * 
 * Last edited on: 2024-08-20
 * Authors: Java2hu, SteelPanty
 */

package com.example.danmaku.mechanics;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.example.danmaku.entities.Boss;
import com.example.danmaku.entities.PositionEntity;


public class PathingHelper
{
	public PathingHelper(Boss owner)
	{
		this.owner = owner;
	}
	
	public Boss owner;	
	private Path currentPath;
	
	public Path getCurrentPath()
	{
		return currentPath;
	}
	
	public void tick()
	{
		if(getCurrentPath() != null)
			getCurrentPath().tick();
	}
	
	public void setCurrentPath(Path currentPath)
	{
		this.currentPath = currentPath;
	}
	
	public SinglePositionPath setCurrentPath(PositionEntity destination, float duration)
	{
		SinglePositionPath path = new SinglePositionPath(owner, destination, duration);
		
		setCurrentPath(path);
		
		return path;
	}
	
	
	public static class Path
	{
		private ArrayList<PositionEntity> path = new ArrayList<PositionEntity>();
		private ArrayList<Consumer<Path>> onDone = new ArrayList<Consumer<Path>>();
		
		public ArrayList<PositionEntity> getPositions()
		{
			return path;
		}

		public void addPosition(PositionEntity pos)
		{
			getPositions().add(pos);
		}
		
		public void removePosition(PositionEntity pos)
		{
			getPositions().remove(pos);
		}
		
		protected Boss owner;
		
		protected Path(Boss owner)
		{
			this.owner = owner;
		}
		
		private Float speedPerTick = 2.0f / 20.0f;
		
		public void onDone(Consumer<Path> consumer)
		{
			onDone.add(consumer);
		}

		public void tick()
		{
			// 경로에 더 이상 목표 지점이 없으면 종료
			if (path.isEmpty())
				return;

			// 현재 목표 지점
			PositionEntity target = path.get(0);

			// 이동 벡터 계산
			float dx = target.x - owner.getPosition().x;
			float dy = target.y - owner.getPosition().y;
			float distance = (float) Math.sqrt(dx * dx + dy * dy);
			
			// 목표 지점에 가까워지면 해당 지점을 경로에서 제거
			if (distance < 1.0f) { 
				path.remove(0);
				return;
			}

			// 속도에 따라 위치 이동
			float moveX = (dx / distance) * (float) this.speedPerTick;
			float moveY = (dy / distance) * (float) this.speedPerTick;
			owner.getPosition().x = owner.getPosition().x + moveX;
			owner.getPosition().y = owner.getPosition().y + moveY;
		}
	
	}
}
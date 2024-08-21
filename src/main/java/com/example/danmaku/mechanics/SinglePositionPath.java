/*
 * SinglePositionPath.java : 목적지로 단순이동하는 Path 정의
 * 
 * Copyright (c) 2024 SteelPanty
 *
 * This file is part of a project licensed under the MIT License.
 *
 * Created on: 2024-08-20
 * Author: SteelPanty
 */
 
package com.example.danmaku.mechanics;

import com.example.danmaku.entities.Boss;
import com.example.danmaku.entities.PositionEntity;

public class SinglePositionPath extends BossPath
{
	public SinglePositionPath(Boss object, PositionEntity pos, float time)
	{
		super(object);
		addPosition(pos);
	}
	
}
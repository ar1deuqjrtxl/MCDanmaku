/*
 * Spellcard.java : 스펠카드의 추상클래스
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

public abstract class Spellcard
{
	public Boss owner;
	public int tick = 0;

	private float timeLeft = 0.0f;
	private boolean timedOut = false;
	private float timeExpire = 0.0f;

	public Spellcard(Boss owner)
	{
		this.owner = owner;
	}
	
	public void run()
	{
			
		final float timeLeft = getTimeLeft();
		
		if(timeLeft > 0)
			setTimeLeft(timeLeft - (1/20.0f));

		tick(tick++);
	}

	public void setTimeExpire(float timeExpire)
	{
		this.timeExpire = timeExpire;
		setTimeLeft(timeExpire);
	}
	
	public float getTime()
	{
		return timeExpire;
	}
	
	public void setTimeLeft(float timeLeft)
	{
		this.timeLeft = timeLeft;
		
		if((!timedOut) && (timeLeft < 0.0f))
		{
			onTimeOut();
		} 
	}
	
	public float getTimeLeft()
	{
		return timeLeft;
	}
	
	public boolean isTimedOut()
	{
		return timedOut;
	}
	
	public void onTimeOut()
	{
		timedOut = true;
	}
	
	public abstract void tick(int tick);
}
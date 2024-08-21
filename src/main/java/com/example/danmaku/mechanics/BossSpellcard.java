/*
 * BossSpellcard.java 스펠카드의 제네릭클래스
 * 
 * https://github.com/Java2hu/Java2hu/blob/cf804b16f85aedfbc9c632a207973dd856de2e57/core/src/java2hu/spellcard/BossSpellcard.java
 *
 * Author: Java2hu
 */

 
package com.example.danmaku.mechanics;

import com.example.danmaku.entities.Boss;

public abstract class BossSpellcard<T extends Boss> extends Spellcard
{
	private T owner;
	
	public BossSpellcard(T owner)
	{
		super(owner);
		this.owner = owner;
	}

	@Override
	public void tick(int tick)
	{
		tick(tick, owner);
	}
	
	public abstract void tick(int tick, final T boss);
}
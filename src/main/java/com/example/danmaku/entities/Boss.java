/*
 * Boss.java : 보스 추상 클래스
 * 
 * driven from https://github.com/Java2hu/Java2hu/blob/cf804b16f85aedfbc9c632a207973dd856de2e57/core/src/java2hu/object/enemy/greater/Boss.java
 *
 * Last edited on: 2024-08-20
 * Author: Java2hu, SteelPanty
 */

package com.example.danmaku.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;

import com.example.danmaku.mechanics.PathingHelper;


public abstract class Boss
{
	protected Creeper body = null;

	public boolean isDead;

    public float maxHealth = 0.0f;
	public float damageDealt = 0.0f;	
    public float x = 0.0f;
    public float y = 0.0f;

	public BossBar bossBar;
	
	public Boss(float maxHealth, float x, float y)
	{
        this.maxHealth = maxHealth;
		position = new PositionEntity(x, y);
	}

	public static Player getClosestPlayer(Location location, double radius) {
		Player p_found = null;
		double minDist = 99999;

		for (Player _p : Bukkit.getOnlinePlayers()) {
			double dist = location.distance(_p.getLocation());

			if (radius > dist && dist < minDist)
			{
				minDist = Math.min(dist, minDist);
				p_found = _p;
			}
		}

		//Bukkit.getLogger().info(p.getName());
		return p_found;
	}

    public float getX()
    {
        return position.x;
    }

    public float getY()
    {
        return position.y;
    }
	

	protected PathingHelper pathing = new PathingHelper(this);
    private PositionEntity position = null;

    public PathingHelper getPathing()
	{
		return pathing;
	}

    public PositionEntity getPosition() {
        return position;
    }

    public void setX(float f) {
        this.position.x = f;
    }

    public void setY(float f) {
        this.position.x = y;
    }

	public void dealDamage(float f) {
		this.damageDealt += f;
	}

    public abstract void tick();

	public abstract Creeper getBody();


}
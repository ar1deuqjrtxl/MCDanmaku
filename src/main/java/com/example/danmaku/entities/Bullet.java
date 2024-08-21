/*
 * Bullet.java : 탄막 구현 클래스
 * 
 * driven from https://github.com/Java2hu/Java2hu/blob/cf804b16f85aedfbc9c632a207973dd856de2e57/core/src/java2hu/object/bullet/Bullet.java
 *
 * Last edited on: 2024-08-20
 * Author: Java2hu, SteelPanty
 */

package com.example.danmaku.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.inventory.ItemStack;

import com.example.danmaku.util.MathUtil;

public class Bullet extends PositionEntity {

    public static final int SMALL = 0;
    public static final int BIG = 1;
	public static final int GLOWING = 2;

	ZombieVillager body = null;
    private static final float TPS = 20.0f;

    public Bullet(Creeper owner, float x, float y, int tan_type) {
		super(x, y);
		
		// 플레이어 높이 1.8 block
		float offset = 1.8f;
		float multiplier = 1.0f;
		if (tan_type == SMALL)
			multiplier = 1.0f; // 탄환의 높이가 2 (사람 머리 약간 위)
		else if (tan_type == BIG)
			multiplier = 2.0f;// 탄환의 높이가 4
		else if (tan_type == GLOWING)
			multiplier = 3.0f;

		this.body = (ZombieVillager) owner.getWorld().spawn(owner.getLocation().add(0, -(offset*multiplier) + 1.8f, 0), ZombieVillager.class);
		body.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(multiplier);
		body.setSilent(true); // 소리 제거
        body.setInvisible(true); // 필요 시 투명화
        body.setAI(false); // AI 비활성화로 움직이지 않게 설정
        body.setInvulnerable(true); // 필요 시 무적화
		body.getEquipment().clear();

		// 탄 타입에 따른 스프라이트
		if (tan_type == SMALL)
			body.getEquipment().setHelmet(new ItemStack(Material.PHANTOM_MEMBRANE));
		else if (tan_type == BIG)
			body.getEquipment().setHelmet(new ItemStack(Material.PORKCHOP));
		else if (tan_type == GLOWING)
			body.getEquipment().setHelmet(new ItemStack(Material.COOKED_PORKCHOP));
    }


	public void setRotationFromVelocity()
	{
		setRotationFromVelocity(90f); // Standard offset is 90f.
	}
	
	public void setRotationFromVelocity(float offsetDegree)
	{
		//setRotationDeg((float) (Math.atan2(velocityY, velocityX) * (180 / Math.PI) - offsetDegree));
	}

    protected float velocityX;
	protected float velocityY;
	

	public void onUpdateDelta(float delta)
	{
		this.x = this.x- (velocityX * delta);
		this.y = this.y - (velocityY * delta);
	}
	
	/**
	 * Legacy method to set velocity from ticks (and is calculated to velocity per second)
	 * @param velocityX
	 */
	@Deprecated
	public void setVelocityXTick(double velocityX)
	{
		float perSecond = (float) (velocityX * Bullet.TPS);
		
		this.velocityX = perSecond;
	}
	
	/**
	 * Legacy method to set velocity from ticks (and is calculated to velocity per second)
	 * @param velocityY
	 */
	@Deprecated
	public void setVelocityYTick(double velocityY)
	{
		float perSecond = (float) (velocityY * Bullet.TPS);
		
		this.velocityY = perSecond;
	}
	
	/**
	 * Legacy method to return the velocity x from ticks (and is calculated from velocity per second to velocity per tick)
	 */
	@Deprecated
	public float getVelocityXTick()
	{
		return velocityX / Bullet.TPS;
	}
	
	/**
	 * Legacy method to return the velocity y from ticks (and is calculated from velocity per second to velocity per tick)
	 */
	@Deprecated
	public float getVelocityYTick()
	{
		return velocityY / Bullet.TPS;
	}
	
	public void setVelocityX(double velocityX)
	{
		this.velocityX = (float) velocityX;
	}
	
	public void setVelocityY(double velocityY)
	{
		this.velocityY = (float) velocityY;
	}
	
	public double getVelocityX()
	{
		return velocityX;
	}
	
	public double getVelocityY()
	{
		return velocityY;
	}

	public void onUpdate(long tick)
	{

	}
	
	@Deprecated
	public void setDirectionDegTick(double degree, double speed)
	{
		setDirectionRadsTick(Math.toRadians(degree), speed);
	}
	
	@Deprecated
	public void setDirectionRadsTick(double radians, double speed)
	{
		setVelocityXTick((MathUtil.fastCos(Math.toDegrees(radians)) * speed));
		setVelocityYTick((MathUtil.fastSin(Math.toDegrees(radians)) * speed));
	}
	
	public void setDirectionDeg(double degree, double speed)
	{
		setDirectionRads(Math.toRadians(degree), speed);
	}
	
	public void setDirectionRads(double radians, double speed)
	{
		setVelocityX((float) (MathUtil.fastCos(Math.toDegrees(radians)) * speed));
		setVelocityY((float) (MathUtil.fastSin(Math.toDegrees(radians)) * speed));
	}
	
	public void haltMovement()
	{
		setVelocityX(0);
		setVelocityY(0);
	}


    public void tick() {

		this.x = this.x + getVelocityXTick();
		this.y = this.y + getVelocityYTick();
        this.body.teleport(new Location(body.getWorld(), this.x, (float) body.getLocation().getY(), this.y));

		this.onUpdate(1);
    }

	public void kill() {
		this.body.remove();
	}

	public Entity getBody() {
		return body;
	}

}



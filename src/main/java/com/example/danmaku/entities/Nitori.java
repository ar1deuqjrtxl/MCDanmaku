/*
 * Nitori.java : 니토리 보스 구현 클래스
 * 
 * driven from https://github.com/Java2hu/Java2hu/blob/cf804b16f85aedfbc9c632a207973dd856de2e57/allstar/src/java2hu/allstar/enemies/day4/Nitori.java
 *
 * Last edited on: 2024-08-20
 * Author: Java2hu, SteelPanty
 */

package com.example.danmaku.entities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.example.danmaku.App;
import com.example.danmaku.mechanics.BossSpellcard;
import com.example.danmaku.mechanics.BossPath;
import com.example.danmaku.util.MCUtil;
import com.example.danmaku.util.MathUtil;
import com.example.danmaku.util.RNG;

public class Nitori extends Boss
{
    public final static String NAME = "카와시로 니토리";
	public final static String SPELLCARD_NAME = "수부";
	public final static String SPELLCARD_SUBNAME = "「캇파의 포로로카」";
	private World world;
	
	
	public Nitori(Player summoner, float maxHealth, float x, float y)
	{
		super(maxHealth, x, y);

        this.isDead = false;
		this.world = summoner.getWorld();
		this.body = createBody(this.world);
		
        body.setHealth(20.0f);
        body.teleport(new Location(body.getWorld(), x, -60.0, y));
		
		
		App.GetRunner().getSpellcards().add(new NonSpell(this));
		App.GetRunner().getSpellcards().add(new Spell(this));

		// 보스바 생성 (기본적으로 빨간색 바)
        bossBar = Bukkit.createBossBar(NAME, BarColor.RED, BarStyle.SOLID);
		bossBar.setVisible(true);
		for (Player _p : Bukkit.getOnlinePlayers()) {
			bossBar.addPlayer(_p);
		}
		
        // 주기적으로 보스의 체력을 확인하고 보스바 업데이트
        new BukkitRunnable() {
            @Override
            public void run() {
				if (App.GetRunner().boss != null)
				{
					if (body != null && !body.isDead()) {
						double healthPercentage = 1.0 - (App.GetRunner().boss.damageDealt / App.GetRunner().boss.maxHealth);
						bossBar.setProgress(Math.max(healthPercentage, 0.0)); // 보스 체력에 따른 바 업데이트
					} else {
						bossBar.setVisible(false); // 보스가 없거나 죽었을 경우 보스바 숨기기
					}
				}
            }
        }.runTaskTimer(App.getInstance(), 0L, 10L); // 0 틱 시작, 매 10틱마다 업데이트
    }

	private Creeper createBody(World world) {
		Creeper body = (Creeper) world.spawn(new Location(world, x, -60, y), Creeper.class);
        body.setCustomName("Boss");
        body.setSilent(true); // 소리 제거
        //vex.setInvisible(true); // 필요 시 투명화
        body.setAI(false); // AI 비활성화로 움직이지 않게 설정
        body.setInvulnerable(true); // 필요 시 무적화

        MCUtil.disguiseAsPlayer(body, "G_Gaming1");
        body.setCustomName("Nitori");

		return body;
	}

	public static class NonSpell extends BossSpellcard<Boss>
	{	
		public NonSpell(Boss owner)
		{
			super(owner);
			setTimeExpire(25.0f * 3.0f);
		}
		
		@Override
		public void tick(int tick, Boss boss)
		{
            
			final Player closet_p = getClosestPlayer(boss.body.getLocation(), 5000);
			int cycle = tick % 50 - 6;

			if (cycle == 33) {
				boss.getPathing().setCurrentPath(new BossPath(boss));
			}
			
			if (cycle >= 0 && cycle < 32 && tick % 3 == 0) {
				int numBullets = (cycle / 3) % 16;

				if (numBullets <= 13) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.playSound(p.getLocation(), "minecraft:tan00", 1.0f, 1.0f);
					}
				} else {
					numBullets = -1;
				}

				for (int i = 0; i <= numBullets; i++) {
					for (int j = 0; j < 3; j++) {
						Bullet b = new Bullet(boss.body, boss.getX(), boss.getY(), Bullet.SMALL);
						b.setDirectionDeg(90 + (120 * j) + (10 * i) - ((cycle > 15 ? 4.5f : 5.5f) * numBullets), 20f);
						
						App.GetRunner().spawn(b);
					}
				}
			}

			if (cycle == 40) {
				for (Player p : Bukkit.getOnlinePlayers()) {
                	p.playSound(p.getLocation(), "minecraft:tan00", 1.0f, 1.0f);
            	}

				for (int i = 0; i < 7; i++) {

					float deg = (float)MathUtil.getAngle(new PositionEntity(closet_p.getLocation().getX() + App.OFFSET_CAMERA_TO_FIELD_X, closet_p.getLocation().getZ()),
					new PositionEntity(boss.getBody().getLocation().getX(), boss.getBody().getLocation().getZ()));
					
					boss.body.setRotation(deg, boss.body.getLocation().getPitch());
					Bullet b = new Bullet(boss.body, boss.getX(), boss.getY(), Bullet.BIG);
                    b.setDirectionDeg((float) deg + 15 * i - 45, 12f);
					App.GetRunner().spawn(b);
				}
			}
		}
	}

	public static class Spell extends BossSpellcard<Boss>
	{
		public Spell(Boss owner)
		{
			super(owner);
			setTimeExpire(28.0f * 2.0f);
		}

        @Override
        public void tick(int tick, Boss boss) {

            final Player player = getClosestPlayer(boss.body.getLocation(), 5000);

			if (tick == 0)
			{
				for (Player p : Bukkit.getOnlinePlayers()) 
				{
					p.sendTitle(ChatColor.GOLD + SPELLCARD_NAME, ChatColor.YELLOW + SPELLCARD_SUBNAME, 10, 70, 20);
					p.playSound(p.getLocation(), "minecraft:spellcard", 1.0f, 1.0f);
				}
			}

			if (tick % 3 == 0)
			{
				for (Player p : Bukkit.getOnlinePlayers()) {
                	p.playSound(p.getLocation(), "minecraft:tan00", 1.0f, 1.0f);
            	}
			}
            
            float gameMaxX = 50.0f;
            float gameCenterX = 25.0f;
            float gameHeight = 100.0f;

			Bullet b = new Bullet(boss.body, gameMaxX * (float) RNG.random(), 0, Bullet.GLOWING) {
				float v_Y = (float) -(gameHeight / 1.4f) / 10.0f;
				boolean bounced = false;
                
				@Override
				public void onUpdate(long tick)
				{
					float MinY = 0.0f;

					v_Y += 4;
					if (!bounced && this.y < MinY + 5) {
						v_Y = -10;
						bounced = true;
					}
					setVelocityY(v_Y / 2.5f);

					super.onUpdate(tick);
				}
			};
			b.setVelocityX((gameCenterX - b.body.getLocation().getX()) / 7.5f);
			App.GetRunner().spawn(b);
			
			final int startFiring = 100;

			//if (tick == startFiring)
			
			if (tick >= startFiring) {
				int cycle = tick % 50;
				if (cycle == 0 || cycle == 13 ) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.playSound(p.getLocation(), "minecraft:tan00", 1.0f, 1.0f);
					}
					
					for (int i = 0; i < 5; i++) {
						Bullet shot = new Bullet(boss.body, boss.getX(), boss.getY(), Bullet.GLOWING);
						shot.setDirectionDeg((float) MathUtil.getAngle(boss.getPosition(),
						new PositionEntity(player.getLocation().getX() + App.OFFSET_CAMERA_TO_FIELD_X, player.getLocation().getZ())) + 20 * i - 40, 24f);
						App.GetRunner().spawn(shot);
					}
				}

				if (cycle == 25) {
					boss.getPathing().setCurrentPath(new BossPath(boss));
				}
			}
        }
	}

	@Override
	public void tick() {
		
		if (!isDead && (this.body == null || this.body.isDead()))
		{
			this.body = createBody(this.world);
		}
		//if (this.damageDealt > this.maxHealth && App.GetRunner().getSpellcards().size() > 0)
		//	App.GetRunner().getSpellcards().get(0).onTimeOut();

		getPathing().tick();

		Location loc = body.getLocation().clone();
		loc.setX(this.getPosition().x);
		loc.setZ(this.getPosition().y);

		this.body.teleport(loc);
		this.body.setHealth(20.0f);
		this.body.setInvulnerable(true);
		
	}

	public Creeper getBody() {
		return body;
	}

}

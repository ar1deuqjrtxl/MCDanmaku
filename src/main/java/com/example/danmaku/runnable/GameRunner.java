/*
 * GameRunner.java : 게임 메인 루프
 * 
 *  Copyright (c) 2024 SteelPanty
 *
 * This file is part of a project licensed under the MIT License.
 *
 * Created on: 2024-08-20
 * Author: SteelPanty
 */

package com.example.danmaku.runnable;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.example.danmaku.App;
import com.example.danmaku.entities.Boss;
import com.example.danmaku.entities.Bullet;
import com.example.danmaku.entities.Nitori;
import com.example.danmaku.mechanics.BossSpellcard;
import com.example.danmaku.util.MCUtil;

public class GameRunner extends BukkitRunnable{

    public Boss boss = null;
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private ArrayList<BossSpellcard<Boss>> spells = new ArrayList<BossSpellcard<Boss>>();
    private boolean isLose;

    public void spawnBoss(Player player, String boss_name) {
        //player.getLocation().add(0, 0, 20)
        App.GetRunner().isLose = false;
        
        this.boss = new Nitori(player, Bukkit.getOnlinePlayers().size() * 20.0f, 25.0f, 75.0f);
        
    
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), "minecraft:mysound", 1.0f, 1.0f);
        }


        new BukkitRunnable() {
            @Override
            public void run() {
                if (App.GetRunner().boss != null)
                {
                    if(!App.GetRunner().boss.isDead)
                    {
                        checkPlayers();
                    }                    
                }
                
            }

            private void checkPlayers() {
                int aliveCount = 0;
        
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode() != GameMode.SPECTATOR && !player.isDead()) {
                        aliveCount++;
                    }
                }
        
                // 만약 살아있는 플레이어가 없다면 패배 메시지 출력
                if (aliveCount == 0) {
                    // 모든 플레이어에게 패배 메시지 출력
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.sendTitle(ChatColor.RED + "패배!", ChatColor.RED + "모든 플레이어가 사망했습니다!", 10, 70, 20);
                    App.GetRunner().isLose = true;
                    App.GetRunner().reset();
                    
                }
            }
        }.runTaskTimer(App.getInstance(), 0L, 20L);

    }



    public LivingEntity keepMonsterExistLookAt(Player player, boolean from_player) {
        // 몬스터가 없으면 소환
		
        if (player == null)
            return null;

        LivingEntity monster = null;

		if (player.getMetadata("spawned_monster").size() > 0)
            monster = (LivingEntity) player.getMetadata("spawned_monster").get(0).value();

		if (boss != null && monster != null && !monster.isDead())
        {
            if (from_player)
                MCUtil.makeEntityLookAtLocation(monster, boss.getBody());
            else
                MCUtil.makeEntityLookAtLocation(boss.getBody(), monster);
        }
        else
        {
            if (monster == null || monster.isDead()) 
            {
                Location playerLocation = player.getLocation();
                Location monsterLocation = new Location(player.getWorld(), playerLocation.getX() + App.OFFSET_CAMERA_TO_FIELD_X, -60, playerLocation.getZ());
                monster = (LivingEntity) player.getWorld().spawnEntity(monsterLocation, EntityType.CREEPER);

                MCUtil.disguiseAsPlayer(monster, player.getName());
                monster.setAI(false);  // AI를 비활성화해 움직이지 않도록 설정
                monster.setInvulnerable(true);
                player.setMetadata("spawned_monster", new FixedMetadataValue(App.getInstance(), monster));
            }
        }

        return monster;
    }

    @Override
    public void run() {
        
        for (Player player : Bukkit.getOnlinePlayers()) 
        {
            player.setRotation(270.0f, 45.0f);
            player.setWalkSpeed(1f);
        }

        // 보스 생존시 쳐다보기 + 틱

        if (boss != null && !boss.isDead)
        {
            keepMonsterExistLookAt(Nitori.getClosestPlayer(boss.getBody().getLocation(), 5000), false);
            boss.tick();

            if (spells.size() > 0) 
            {
                BossSpellcard<Boss> currentSpell = spells.get(0);
                currentSpell.run();
    
                if (boss.damageDealt > boss.maxHealth)
                {
                    currentSpell.setTimeLeft(-1.0f);
                    boss.damageDealt = 0.0f;
                    Bukkit.getLogger().info("Spell Break");
                    spells.remove(0);
                }
                else if (currentSpell.isTimedOut())
                {
                    boss.damageDealt = 0.0f;
                    Bukkit.getLogger().info("Timed Out");
                    spells.remove(0);
                }            
            } else 
            {    
               this.reset();
            }
        }
        
        // 보스 캐릭터
        for (Player player : Bukkit.getOnlinePlayers()) 
        {
            LivingEntity monster = keepMonsterExistLookAt(player, true);
            // 기존 몬스터 위치 갱신   
            Location playerLocation = player.getLocation();
            monster.teleport(new Location(player.getWorld(), playerLocation.getX() + App.OFFSET_CAMERA_TO_FIELD_X, -60, playerLocation.getZ()));  // Y좌표 고정
            monster.setInvisible(player.getGameMode()!=GameMode.ADVENTURE);

            player.getInventory().setItem(0, new ItemStack(Material.STICK));
        }
        

        // 불릿 틱

        Iterator<Bullet> iterator = bullets.iterator();
        float margin = 5.0f;

        while (iterator.hasNext()) {
            Bullet element = iterator.next();
            element.tick();
            element.getBody().setRotation(180.0f+(float)Math.toDegrees(-Math.atan2(element.getVelocityX(), element.getVelocityY())), -90.0f);
            
            if (element.x > App.ARENA_WIDTH + margin || element.y > App.ARENA_HEIGHT + margin) {
                element.kill();
                iterator.remove(); 
            } else if (element.x < 0.0f - margin || element.y < 0.0f - margin) {
                element.kill();
                iterator.remove(); 
            } else if (Math.abs(element.x) > 100.0f || Math.abs(element.y) > 100.0f) {
                element.kill();
                iterator.remove(); 
            } 
            

            
        }

    }

    public void spawn(Bullet b) {
        bullets.add(b);
    }

    public ArrayList<BossSpellcard<Boss>> getSpellcards() {
        return spells;
    }

    public void reset() {
        
        if (this.boss != null)
        {
            boss.isDead = true;
            if (this.isLose == false)
            {
                this.boss.bossBar.setVisible(false);

                if (this.boss.getBody() != null)
                {
                    Creeper _c = this.boss.getBody();
                    _c.setHealth(0);

                    

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), "minecraft:enep00", 1.0f, 1.0f);
                        p.sendTitle(ChatColor.GOLD + "승리!", ChatColor.YELLOW + "축하합니다!", 10, 70, 20);
                    }
                }
            }
            
        }

        this.isLose = true;
        
        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            // 엔티티가 플레이어가 아닌 경우 제거
            if (!(entity instanceof Player)) {
                entity.remove();
            }
        }

        spells.clear();
        bullets.clear();
        
        //this.boss = null;

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.stopSound("minecraft:mysound");
        }

    }
    
}



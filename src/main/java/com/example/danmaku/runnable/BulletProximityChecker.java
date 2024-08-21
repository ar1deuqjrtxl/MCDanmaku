/*
 * BulletProximityChecker.java : 보스 탄환의 충돌처리
 * 
 * Copyright (c) 2024 SteelPanty
 *
 * This file is part of a project licensed under the MIT License.
 *
 * Created on: 2024-08-20
 * Author: SteelPanty
 */

package com.example.danmaku.runnable;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.example.danmaku.App;

import java.util.List;

public class BulletProximityChecker extends BukkitRunnable {

    private JavaPlugin plugin;

    public BulletProximityChecker(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            if (player.isDead() || player.getGameMode()!=GameMode.ADVENTURE)
                continue;

            Location _p_loc = player.getLocation();
            Location _loc = new Location(player.getWorld(), _p_loc.getX() + App.OFFSET_CAMERA_TO_FIELD_X, App.BOTTOM_OF_FIELD_Y, _p_loc.getZ());
            
            List<Entity> nearbyEntities = (List<Entity>) player.getWorld().getNearbyEntities(_loc, 1, 1, 1); // 반경 1 블록 내 엔티티 확인

            // ZombieVillager; 즉, 적 탄환이면 충돌처리
            for (Entity entity : nearbyEntities) {
                if (entity instanceof ZombieVillager) {
                    ZombieVillager _bullet = (ZombieVillager) entity;

                    for (Player p : Bukkit.getOnlinePlayers()) {
						p.playSound(p.getLocation(), "minecraft:death", 1.0f, 1.0f);
					}

                    // 1초 무적 처리 및 폭발 효과 실행
                    player.damage(8.0); // 4하트의 데미지
                    player.setInvulnerable(true);
                    Location location = player.getLocation();
                    location.getWorld().createExplosion(location, 0, false, false);

                    // 주변 ZombieVillager 제거
                    for (Entity nearbyEntity : _bullet.getNearbyEntities(5, 5, 5)) {
                        if (nearbyEntity instanceof ZombieVillager) {
                            nearbyEntity.remove();
                        }
                    }

                    // 1초 후 무적 해제 및 데미지 처리
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        player.setInvulnerable(false);
                    }, 20L); // 20L = 1초 후 실행

                    _bullet.remove(); // 탄막 제거
                    
                    break;
                }
            }
        }
    }
}
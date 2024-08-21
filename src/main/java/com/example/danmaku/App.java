/*
 * Copyright (c) 2024 SteelPanty
 *
 * This file is part of a project licensed under the MIT License.
 *
 * Created on: 2024-08-20
 * Author: SteelPanty
 */

package com.example.danmaku;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.example.danmaku.commands.MakeArenaCommand;
import com.example.danmaku.commands.ResetCommand;
import com.example.danmaku.commands.SpawnNitoriCommand;
import com.example.danmaku.runnable.GameRunner;
import com.example.danmaku.runnable.BulletProximityChecker;

public class App extends JavaPlugin implements Listener{
    
    // 플레이어 위치와 실제 오브젝트간 간격차 (Orthographic projection 리소스팩 사용) 45도 pitch. -90 yaw
    public static final float OFFSET_CAMERA_TO_FIELD_X = 50.0f;  // 위의 카메라에서 -> 필드로 -50 (카메라가 x -25면, 실제로는 25에 출력)
    private static final float OFFSET_CAMERA_TO_FIELD_Y = -50.0f; // 위의 카메라에서 -> 필드로 50 (카메라가 y -25면, 실제로는 -75에 출력)
    public static final float BOTTOM_OF_FIELD_Y = -60.0f;

    public static final float ARENA_WIDTH = 50.0f; // x 0~50
    public static final float ARENA_HEIGHT = 100.0f; // z 0~100

    private static App instance = null;
    private static GameRunner runner = null;

    // 러너 인스턴스 싱글톤
    public static GameRunner GetRunner() {
        if (App.runner == null)
        {
            App.runner = (GameRunner) new GameRunner();
            App.runner.runTaskTimer(App.getInstance(), 0L, 1L);
        }

        return App.runner;
    }

    public static JavaPlugin getInstance() {
        return App.instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("DanmakuPlugin has been enabled!");

        // 명령어 등록
        this.getCommand("makearena").setExecutor(new MakeArenaCommand());
        this.getCommand("spawnnitori").setExecutor(new SpawnNitoriCommand());
        this.getCommand("resetgamemode").setExecutor(new ResetCommand());

        // 이벤트 리스너 등록
        //getServer().getPluginManager().registerEvents(new VexCollisionListener(this), this);
        
        // 주기적으로 플레이어 근처 Vex를 체크하는 작업 실행
        new BulletProximityChecker(this).runTaskTimer(this, 0L, 20L); // 20틱(1초)마다 실행
        
        // 러너 인스턴스 시작
        GetRunner();
        
        // 이벤트 등록
        getServer().getPluginManager().registerEvents(this, this);
        
    }

    @Override
    public void onDisable() {
        getLogger().info("DanmakuPlugin has been disabled!");
    }

    // 몬스터가 죽을 때 아이템을 드롭하지 않도록 처리
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // 이벤트가 발생한 엔티티가 몬스터일 때만 처리
        if (event.getEntity() instanceof Monster) {
            // 드롭될 아이템을 모두 제거
            event.getDrops().clear();
        }
    }

    // 플레이어가 죽었을 때 스펙테이터 모드로 전환
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // 사망한 플레이어를 스펙테이터 모드로 전환
        event.getEntity().setGameMode(GameMode.SPECTATOR);
    }

    // 자키의 치킨 부분 때버리기
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (event.getEntityType().equals(EntityType.CHICKEN)) {
            if (event.getEntity().getPassenger() != null) {
                event.setCancelled(true);
            }
        }
    }
    
    // 플레이어 발사체 처리
    @EventHandler
    public void onPlayerThrowSnowball(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK) {
            
            // 혹시나 이상한짓 막기
            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    
                // 플레이어가 보는 방향으로 눈덩이를 던짐
                Location __loc = event.getPlayer().getLocation();
                __loc.setPitch(-90.0f);
                
                Zombie _bullet = (Zombie) event.getPlayer().getWorld().spawn(__loc.add(App.OFFSET_CAMERA_TO_FIELD_X, App.OFFSET_CAMERA_TO_FIELD_Y - 1, 0), Zombie.class);
                _bullet.setInvisible(true);
                _bullet.setAI(false);
                _bullet.setInvulnerable(true);
                _bullet.setGravity(false);
                _bullet.setVelocity(new Vector(0, 0, 0));
                _bullet.setGlowing(false);
                

                // 머리위에 스프라이트
                _bullet.getEquipment().setHelmet(new ItemStack(Material.BEEF));
                
                
                // 최초 발사 지점으로 부터 선형보간
                Location _first_loc = _bullet.getLocation().clone();
                
                new BukkitRunnable() {
                    int step = 0;
                    
                    @Override
                    public void run() {
                        if (step >= 20) {
                            this.cancel();
                            return;
                        }


                        if (App.GetRunner().boss != null) {
                            // 목표 지점의 위치를 가져옴 (보스 위치)
                            Vector toBoss = App.GetRunner().boss.getBody().getLocation().toVector();
                            
                            // 처음 위치에서 보스 위치까지의 벡터 계산
                            Vector direction = toBoss.clone().subtract(_first_loc.toVector());
                            
                            // 현재 스텝에 따라 선형 보간된 위치를 계산
                            Vector interpolatedVector = direction.multiply((double)step / 20.0);
                            
                            // 새 위치를 계산하고 좀비를 이동
                            Location newLocation = _first_loc.clone().add(interpolatedVector);
                            _bullet.teleport(newLocation);

                            //Bukkit.getLogger().info(newLocation.toString());
                        }
                        step++;
                    }
                }.runTaskTimer(this, 0L, 1L);  // 매 틱마다 (1틱 = 50ms) 실행, 20틱 동안 실행
                
                // 1초 뒤 좀비 파괴하고 데미지 추가 (유도탄인척 하기; 실제로 충돌 처리는 안함)
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    _bullet.remove();
                    if (App.GetRunner().boss == null || App.GetRunner().boss.getBody() == null || App.GetRunner().boss.getBody().isDead()) {
                        return;
                    }
                    App.GetRunner().boss.dealDamage(0.1f);
                }, 20L);
            
            }
            
            
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // 접속한 플레이어에게 메시지 출력
        event.getPlayer().sendMessage("어서오세요, " + event.getPlayer().getName() + "!");
        event.getPlayer().sendMessage("캐릭터가 보이지 않으면 F5를 눌러주세요");
        event.getPlayer().sendMessage("게임이 시작되면 막대기를 우클릭하세요. (유도탄)");
    }
}

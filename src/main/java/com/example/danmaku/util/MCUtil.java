/*
 * MCUtil.java 버킷관련 유틸리티 함수
 * 
 * Copyright (c) 2024 SteelPanty
 *
 * This file is part of a project licensed under the MIT License.
 *
 * Created on: 2024-08-20
 * Author: SteelPanty
 */

package com.example.danmaku.util;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;

public class MCUtil {
        // 보스가 플레이어를 바라보도록 설정
    public static void makeEntityLookAtLocation(LivingEntity from, LivingEntity to) {

        Location fromLocation = from.getLocation();
        Location toLocation = to.getLocation();

        // 두 위치 간의 상대적인 방향 계산
        double dx = toLocation.getX() - fromLocation.getX();
        double dz = toLocation.getZ() - fromLocation.getZ();
        double dy = toLocation.getY() + 1 - fromLocation.getY(); // 플레이어의 눈 위치를 고려

        // Yaw 계산 (좌우 방향)
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;

        // Pitch 계산 (상하 방향)
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        float pitch = (float) -Math.toDegrees(Math.atan(dy / distanceXZ));

		pitch = 0.0f;
		
        fromLocation.setYaw(yaw);
        fromLocation.setPitch(pitch);
		from.setRotation(yaw, pitch); // 회전 적용
    }

        // Creeper를 플레이어처럼 변장시키는 메서드
    public static void disguiseAsPlayer(LivingEntity creeper, String playerName) {
        PlayerDisguise disguise = new PlayerDisguise(playerName);
        DisguiseAPI.disguiseToAll(creeper, disguise);
    }
}

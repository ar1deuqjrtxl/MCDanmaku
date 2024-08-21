/*
 * MakeArenaCommand.java : 아레나를 생성하는 커맨드
 * 
 * Copyright (c) 2024 SteelPanty
 *
 * This file is part of a project licensed under the MIT License.
 *
 * Created on: 2024-08-20
 * Author: SteelPanty
 */

package com.example.danmaku.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.example.danmaku.App;

public class MakeArenaCommand implements CommandExecutor {

    public MakeArenaCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && sender.isOp()) {
            Player player = (Player) sender;
            Location startLocation = new Location(player.getWorld(), 0, -60, 0);//player.getLocation();

            int length = 100;
            int width = 50;
            int height = 3;
            Material wallMaterial = Material.SMOOTH_QUARTZ;
            Material floorMaterial = Material.GRASS_BLOCK;
            Material barrierMaterial = Material.BARRIER;

            // 아레나 평면 바닥 생성
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < length; z++) {
                    Location blockLocation = startLocation.clone().add(x, -1, z);
                    blockLocation.getBlock().setType(floorMaterial); // 바닥을 grass로 설정
                }
            }

            for (int x = 0; x < width; x++) {
                for (int z = 0; z < length; z++) {
                    Location blockLocation = new Location(player.getWorld(), x-App.OFFSET_CAMERA_TO_FIELD_X, -10, z);
                    blockLocation.getBlock().setType(barrierMaterial);
                }
            }

            // 아레나 벽 생성
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Location wall1 = startLocation.clone().add(x, y, 0);
                    Location wall2 = startLocation.clone().add(x, y, length - 1);
                    wall1.getBlock().setType(wallMaterial);
                    wall2.getBlock().setType(wallMaterial);
                }
                for (int z = 0; z < length; z++) {
                    Location wall1 = startLocation.clone().add(0, y, z);
                    Location wall2 = startLocation.clone().add(width - 1, y, z);
                    wall1.getBlock().setType(wallMaterial);
                    wall2.getBlock().setType(wallMaterial);
                }
            }


            for (int y = height; y < height + 60; y++) {
                for (int x = 0; x < width; x++) {
                    Location wall1 = startLocation.clone().add(x-App.OFFSET_CAMERA_TO_FIELD_X, y, 0);
                    Location wall2 = startLocation.clone().add(x-App.OFFSET_CAMERA_TO_FIELD_X, y, length - 1);
                    wall1.getBlock().setType(barrierMaterial);
                    wall2.getBlock().setType(barrierMaterial);
                }
                for (int z = 0; z < length; z++) {
                    Location wall1 = startLocation.clone().add(-App.OFFSET_CAMERA_TO_FIELD_X, y, z);
                    Location wall2 = startLocation.clone().add(width - 1 - App.OFFSET_CAMERA_TO_FIELD_X, y, z);
                    wall1.getBlock().setType(barrierMaterial);
                    wall2.getBlock().setType(barrierMaterial);
                }
            }
            

            player.sendMessage("Arena created!");
            return true;
        }

        return false;
    }
}

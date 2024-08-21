/*
 * ResetCommand.java : 플레이어들을 부활시키는 커맨드
 * 
 * Copyright (c) 2024 SteelPanty
 *
 * This file is part of a project licensed under the MIT License.
 *
 * Created on: 2024-08-20
 * Author: SteelPanty
 */

package com.example.danmaku.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.example.danmaku.App;

public class ResetCommand implements CommandExecutor {

    public ResetCommand() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && sender.isOp()) {

             // 명령어가 "/resetgamemode"일 경우 처리
            if (command.getName().equalsIgnoreCase("resetgamemode")) {
                // 서버에 접속한 모든 플레이어를 순회
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // 게임모드를 어드벤처 모드로 설정
                    player.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage("너를 어드벤처 모드로 변경했어");
                    player.teleport(new Location(player.getWorld(), -25, -9, 25));
                    player.setHealth(20.f);
                }

                // 명령어를 실행한 사람에게도 완료 메시지 전송
                sender.sendMessage("All players' game modes have been reset to Adventure mode. XD");
                
                App.GetRunner().reset();

                sender.sendMessage("And everything has been reset... Hopefully... :D");

                return true;
            }
        } 
        return false;

    }

}
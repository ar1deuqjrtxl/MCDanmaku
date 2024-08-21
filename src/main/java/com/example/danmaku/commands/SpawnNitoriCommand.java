/*
 * SpawnNitoriCommand.java : 니토리를 생성하는 커맨드
 * 
 * Copyright (c) 2024 SteelPanty
 *
 * This file is part of a project licensed under the MIT License.
 *
 * Created on: 2024-08-20
 * Author: SteelPanty
 */

package com.example.danmaku.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.example.danmaku.App;

public class SpawnNitoriCommand implements CommandExecutor{

    public SpawnNitoriCommand() {
    }

    // OP 플레이어가 호출하면 니토리를 소환한다. 서버에서는 소환불가능
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && sender.isOp()) {
            App.GetRunner().spawnBoss((Player) sender, "Nitori");
        }

        return false;
    }
}

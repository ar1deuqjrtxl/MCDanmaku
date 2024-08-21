# Introduction

I created a multiplayer Danmaku(bullet hell) shooting game as a spigot plugin for Minecraft. 

# Minimum Server Requirements

- **RAM**: At least 1GB
- **CPU**: Decent processor recommended

# How to Create the Arena

Make sure to set your server to **Superflat World** mode in the server properties.

To generate the arena, use the `/makearena` command. This will create an arena from X: 0~50 and Z: 0~100. The top of the arena is sealed with barriers, as the camera is positioned above it.

# How to Play

1. Use `/resetgamemode` to reset the game.
2. Spawn the boss using `/spawnnitori`.
3. Right-click while holding a stick to shoot talismans.
4. The more teammates you have, the stronger the enemies become.

# 🖥️  Features Implemented

- Spell Cards
- Boss Movement
- Bullet Trajectory and Collision Handling
- Game Progression Logic
- Refactoring and Integration
- Win/Loss Detection
- Miscellaneous Features

# Code & Server Pack

The code might be a bit messy since I haven’t used Java much, but I wanted to release it first and gradually improve it over time. 

I’m using **LibsDisguises** for monster skins for actor, but note that I built it with the latest version, which may be a bit unstable.  
[Download the latest stable build of LibsDisguises here](https://ci.md-5.net/job/LibsDisguises/lastStableBuild/).

There are still a lot of bugs, so if you plan to use this right now, please be aware of the issues and use it at your own risk!

# Credits

I borrowed quite a bit from the **[Java2hu](https://github.com/Java2hu/Java2hu)** project, especially for bullet generation. I’ve credited them in the source code.

Enjoy playing!

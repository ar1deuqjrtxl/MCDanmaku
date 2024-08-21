/*
 * PositionEntity.java : 2D 평면의 포지션 엔티티 기본정의
 * 
 * Copyright (c) 2024 SteelPanty
 *
 * This file is part of a project licensed under the MIT License.
 *
 * Created on: 2024-08-20
 * Author: SteelPanty
 */

package com.example.danmaku.entities;

public class PositionEntity {
    public float x;
    public float y;

    public PositionEntity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PositionEntity(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public String toString() {
        return "Position [x=" + x + ", y=" + y + "]";
    }
}
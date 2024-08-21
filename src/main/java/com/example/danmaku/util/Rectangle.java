/*
 * Rectangle.java 사각형 클래스
 * 
 * Copyright (c) 2024 SteelPanty
 *
 * This file is part of a project licensed under the MIT License.
 *
 * Created on: 2024-08-20
 * Author: SteelPanty
 */

package com.example.danmaku.util;


public class Rectangle {
        public int width;
        public int height;
        public int x; // X coordinate of the top-left corner
        public int y; // Y coordinate of the top-left corner
    
        // Constructor
        public Rectangle(int width, int height, int x, int y) {
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
        }

        public Rectangle(float width, float height, float x, float y) {
            this.width = (int) width;
            this.height = (int) height;
            this.x = (int) x;
            this.y = (int) y;
        }
    
        // Getters and Setters
        public int getWidth() {
            return width;
        }
    
        public void setWidth(int width) {
            this.width = width;
        }
    
        public int getHeight() {
            return height;
        }
    
        public void setHeight(int height) {
            this.height = height;
        }
    
        public int getX() {
            return x;
        }
    
        public void setX(int x) {
            this.x = x;
        }
    
        public int getY() {
            return y;
        }
    
        public void setY(int y) {
            this.y = y;
        }
    
        public int getArea() {
            return width * height;
        }
    
        public int getPerimeter() {
            return 2 * (width + height);
        }
    
        public boolean contains(int px, int py) {
            return px >= x && px <= x + width && py >= y && py <= y + height;
        }
    
        @Override
        public String toString() {
            return "Rectangle [width=" + width + ", height=" + height + ", x=" + x + ", y=" + y + "]";
        }
    }
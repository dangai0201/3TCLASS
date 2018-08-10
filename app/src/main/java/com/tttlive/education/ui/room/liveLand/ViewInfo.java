package com.tttlive.education.ui.room.liveLand;

/**
 * Author: sunny
 * Time: 2018/8/3
 * description:
 */

public class ViewInfo {

    private int width;
    private int height;
    private float translationX;
    private float translationY;

    public ViewInfo() {
    }

    public ViewInfo(int width, int height, float translationX, float translationY) {
        this.width = width;
        this.height = height;
        this.translationX = translationX;
        this.translationY = translationY;
    }

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

    public float getTranslationX() {
        return translationX;
    }

    public void setTranslationX(float translationX) {
        this.translationX = translationX;
    }

    public float getTranslationY() {
        return translationY;
    }

    public void setTranslationY(float translationY) {
        this.translationY = translationY;
    }

    @Override
    public String toString() {
        return "ViewInfo{" +
                "width=" + width +
                ", height=" + height +
                ", translationX=" + translationX +
                ", translationY=" + translationY +
                '}';
    }
}

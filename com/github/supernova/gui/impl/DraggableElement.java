package com.github.supernova.gui.impl;

import com.github.supernova.util.input.MouseUtil;

import javax.vecmath.Vector2f;

public abstract class DraggableElement {

    private float posX, posY, width, height;

    private boolean dragging;

    private int mouseButton;

    private Vector2f mouseOffset;


    public DraggableElement(float posX, float posY, float width, float height, int mouseButton) {
        dragging = false;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.mouseButton = mouseButton;
    }

    public abstract void drawElement();

    public void updateElement() {
        if(dragging) {
            Vector2f mouse = MouseUtil.getMousePos();
            setPos(mouse.x - mouseOffset.x, mouse.y - mouseOffset.y);
        }
    }

    public boolean onMouseClick(float mouseX, float mouseY, int mouseButton) {
        this.dragging = hovered(mouseX, mouseY) && mouseButton == this.mouseButton;
        if(mouseOffset == null && this.dragging) {
            Vector2f mouse = MouseUtil.getMousePos();
            this.mouseOffset = new Vector2f(mouse.x - getPosX(), mouse.y - getPosY());
            return true;
        }
        return false;
    }

    public void onMouseReleased() {
        this.dragging = false;
        mouseOffset = null;
    }

    public boolean hovered(float mouseX, float mouseY) {
        return mouseX >= this.posX && mouseY >= this.posY && mouseX < this.posX + this.width && mouseY < this.posY + this.height;
    }
    public void setPos(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }
    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }
    public float getHeight() {
        return height;
    }
    public float getWidth() {
        return width;
    }
    public boolean dragging() {
        return this.dragging;
    }
    public void setPosX(float posX) {
        this.posX = posX;
    }
    public void setPosY(float posY) {
        this.posY = posY;
    }
    public void setWidth(float width) {
        this.width = width;
    }
    public void setHeight(float height) {
        this.height = height;
    }
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }
}

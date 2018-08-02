package com.tttlive.education.ui.room.webviewtool;

public class WebViewToolBean {

    private int id;
    private String name;
    private String toolName;
    private int iconNormal;
    private int iconHover;
    private boolean selected;

    public WebViewToolBean(int id, String name, String toolName, int iconNormal, int iconHover, boolean selected) {
        this.id = id;
        this.name = name;
        this.toolName = toolName;
        this.iconNormal = iconNormal;
        this.iconHover = iconHover;
        this.selected = selected;
    }

    public WebViewToolBean(int id, String name, String toolName, int iconNormal, int iconHover) {
        this(id, name, toolName, iconNormal, iconHover, false);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public int getIconNormal() {
        return iconNormal;
    }

    public void setIconNormal(int iconNormal) {
        this.iconNormal = iconNormal;
    }

    public int getIconHover() {
        return iconHover;
    }

    public void setIconHover(int iconHover) {
        this.iconHover = iconHover;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

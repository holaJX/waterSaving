package com.dyzhsw.efficient.entity;

import java.util.List;

public class MenuRoute {

    private String id;
    private String path;
    private String component;
    private String redirect;
    private String name;
    private String meta;
    private Boolean hidden;
    private List<MenuRoute> children;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public List<MenuRoute> getChildren() {
        return children;
    }

    public void setChildren(List<MenuRoute> children) {
        this.children = children;
    }
}

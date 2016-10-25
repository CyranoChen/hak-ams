package com.wonders.frame.ams.component;

/**
 * Created by wsq on 2015/8/29.
 */
public class JqxDatafield {
    private String name;
    private String type;

    public JqxDatafield() {
    }

    public JqxDatafield(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

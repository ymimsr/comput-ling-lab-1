package ru.nsu.fit.comput_ling_lab_1.domain;

public class Grammeme {
    private Grammeme parent;
    private String name = "";
    private String alias;
    private String description;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public String getDescription() {
        return description;
    }

    public Grammeme getParent() {
        return parent;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParent(Grammeme parent) {
        this.parent = parent;
    }
}

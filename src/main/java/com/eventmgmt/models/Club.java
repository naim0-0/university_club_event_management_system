package com.eventmgmt.models;

public class Club {
    private int id;
    private String name;
    private int presidentId;

    public Club(int id, String name, int presidentId) {
        this.id = id;
        this.name = name;
        this.presidentId = presidentId;
    }

    public Club(String name, int presidentId) {
        this.name = name;
        this.presidentId = presidentId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPresidentId() { return presidentId; }
    public void setPresidentId(int presidentId) { this.presidentId = presidentId; }

    @Override
    public String toString() { return name; }
}

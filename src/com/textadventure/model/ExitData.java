package com.textadventure.model;

public class ExitData {
    private String targetRoom;
    private Conditions conditions;

    public String getTargetRoom() {
        return targetRoom;
    }

    public Conditions getConditions() {
        return conditions;
    }

    public void setTargetRoom(String targetRoom) {
        this.targetRoom = targetRoom;
    }

    public void setConditions(Conditions conditions) {
        this.conditions = conditions;
    }

    public ExitData() {}
}
package com.textadventure.model;

public class Conditions {
    private Object requiresItem;
    private String failMessage;

    public Object getRequiresItem() {
        return requiresItem;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public void setRequiresItem(Object requiresItem) {
        this.requiresItem = requiresItem;
        System.out.println("[Debug Cond Mod] requiresItem set to: " + requiresItem);
    }

    public void setFailMessage(String failMessage) {
        this.failMessage = failMessage;
        System.out.println("[Debug Cond Mod] failMessage set to: " + failMessage);
    }

    public Conditions() {}
}
package com.textadventure.model;

import java.util.List;

public class Item {

    private String name;
    private String description;
    private List<String> items;
    private Usability usability;

    public Item(String name, String description) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Item name cannot be null or empty.");

        if (description == null)
            throw new IllegalArgumentException("Item description cannot be null.");

        this.name = name;
        this.description = description;
        this.usability = null;
        this.items = null;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<String> getItems() {
        return (items == null) ? List.of() : items;
    }

    public Usability getUsability() {
        return usability;
    }

    public void setUsability(Usability usability) {
        this.usability = usability;
    }

    public static class Usability {
        private String target;
        private String effectDescription;
        private boolean consumesItem;
        private String unlocksExit;
        private String removesTarget;
        private String addsTarget;
        private String changesRoomDescriptionTo;
        private String addsItemToInventory;
        private ExitModification modifiesExit;

        public String getTarget() { return target; }
        public String getEffectDescription() { return effectDescription; }
        public ExitModification getModifiesExit() { return modifiesExit; }
        public boolean isConsumesItem() { return consumesItem; }
        public String getUnlocksExit() { return unlocksExit; }
        public String getRemovesTarget() { return removesTarget; }
        public String getAddsTarget() { return addsTarget; }
        public String getChangesRoomDescriptionTo() { return changesRoomDescriptionTo; }
        public String getAddsItemToInventory() { return addsItemToInventory; }

        public Usability() {}
    }
}
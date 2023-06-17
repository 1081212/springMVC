package org.example.shipserver.entity;

public class ShipLevel {
    Integer shipId;
    String level;

    @Override
    public String toString() {
        return "ShipLevel{" +
                "shipId=" + shipId +
                ", level='" + level + '\'' +
                '}';
    }

    public Integer getShipId() {
        return shipId;
    }

    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

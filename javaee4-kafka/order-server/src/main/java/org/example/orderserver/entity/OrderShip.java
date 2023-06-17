package org.example.orderserver.entity;

public class OrderShip {
    Integer orderId;
    Integer shipId;

    int state; // 0-等待 1-接受

    @Override
    public String toString() {
        return "OrderShip{" +
                "orderId=" + orderId +
                ", shipId=" + shipId +
                ", state=" + state +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getShipId() {
        return shipId;
    }

    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }
}

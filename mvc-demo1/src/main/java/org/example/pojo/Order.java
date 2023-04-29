package org.example.pojo;

public class Order {
    Integer orderId;
    Integer shipId;
    Integer uid;
    String trueName;
    String truePhone;

    Double weight;
    Integer startProvinceAdcode;
    Integer startCityAdcode;
    Integer endProvinceAdcode;
    Integer endCityAdcode;

    Double totalPrice;

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", shipId=" + shipId +
                ", uid=" + uid +
                ", trueName='" + trueName + '\'' +
                ", truePhone='" + truePhone + '\'' +
                ", weight=" + weight +
                ", startProvinceAdcode=" + startProvinceAdcode +
                ", startCityAdcode=" + startCityAdcode +
                ", endProvinceAdcode=" + endProvinceAdcode +
                ", endCityAdcode=" + endCityAdcode +
                ", totalPrice=" + totalPrice +
                ", isGo=" + isGo +
                '}';
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    Integer isGo;

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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getTruePhone() {
        return truePhone;
    }

    public void setTruePhone(String truePhone) {
        this.truePhone = truePhone;
    }

    public Integer getStartProvinceAdcode() {
        return startProvinceAdcode;
    }

    public void setStartProvinceAdcode(Integer startProvinceAdcode) {
        this.startProvinceAdcode = startProvinceAdcode;
    }

    public Integer getStartCityAdcode() {
        return startCityAdcode;
    }

    public void setStartCityAdcode(Integer startCityAdcode) {
        this.startCityAdcode = startCityAdcode;
    }

    public Integer getEndProvinceAdcode() {
        return endProvinceAdcode;
    }

    public void setEndProvinceAdcode(Integer endProvinceAdcode) {
        this.endProvinceAdcode = endProvinceAdcode;
    }

    public Integer getEndCityAdcode() {
        return endCityAdcode;
    }

    public void setEndCityAdcode(Integer endCityAdcode) {
        this.endCityAdcode = endCityAdcode;
    }

    public Integer getIsGo() {
        return isGo;
    }

    public void setIsGo(Integer isGo) {
        this.isGo = isGo;
    }
}

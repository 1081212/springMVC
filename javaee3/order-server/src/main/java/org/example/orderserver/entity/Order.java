package org.example.orderserver.entity;

public class Order {
    Integer orderId;
    Integer uid;
    String trueName;
    String truePhone;

    Double weight;
    String startProvinceAdcode;
    String startCityAdcode;
    String endProvinceAdcode;
    String endCityAdcode;

    Double totalPrice;
    String level;
    Integer isGo;//0-下单未派遣 1-派遣 2-船只接受 3-船只到达 4-用户确认

    Integer isPay;


    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", uid=" + uid +
                ", trueName='" + trueName + '\'' +
                ", truePhone='" + truePhone + '\'' +
                ", weight=" + weight +
                ", startProvinceAdcode='" + startProvinceAdcode + '\'' +
                ", startCityAdcode='" + startCityAdcode + '\'' +
                ", endProvinceAdcode='" + endProvinceAdcode + '\'' +
                ", endCityAdcode='" + endCityAdcode + '\'' +
                ", totalPrice=" + totalPrice +
                ", level='" + level + '\'' +
                ", isGo=" + isGo +
                ", isPay=" + isPay +
                '}';
    }

    public Integer getIsPay() {
        return isPay;
    }

    public void setIsPay(Integer isPay) {
        this.isPay = isPay;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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



    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public String getStartProvinceAdcode() {
        return startProvinceAdcode;
    }

    public void setStartProvinceAdcode(String startProvinceAdcode) {
        this.startProvinceAdcode = startProvinceAdcode;
    }

    public String getStartCityAdcode() {
        return startCityAdcode;
    }

    public void setStartCityAdcode(String startCityAdcode) {
        this.startCityAdcode = startCityAdcode;
    }

    public String getEndProvinceAdcode() {
        return endProvinceAdcode;
    }

    public void setEndProvinceAdcode(String endProvinceAdcode) {
        this.endProvinceAdcode = endProvinceAdcode;
    }

    public String getEndCityAdcode() {
        return endCityAdcode;
    }

    public void setEndCityAdcode(String endCityAdcode) {
        this.endCityAdcode = endCityAdcode;
    }

    public Integer getIsGo() {
        return isGo;
    }

    public void setIsGo(Integer isGo) {
        this.isGo = isGo;
    }
}

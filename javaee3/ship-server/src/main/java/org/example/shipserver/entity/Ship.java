package org.example.shipserver.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Ship {

    @ApiModelProperty(value = "船只id",example = "1")
    Integer shipId;
    String name;
    String phone;
    String shipModel;
    Double shipLength;
    Double load;

    Double price;



    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setShipModel(String shipModel) {
        this.shipModel = shipModel;
    }

    public void setShipLength(Double shipLength) {
        this.shipLength = shipLength;
    }

    public void setLoad(Double load) {
        this.load = load;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getShipModel() {
        return shipModel;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "shipId=" + shipId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", shipModel='" + shipModel + '\'' +
                ", shipLength=" + shipLength +
                ", load=" + load +
                ", price=" + price +
                '}';
    }

    public Integer getShipId() {
        return shipId;
    }

    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getShipLength() {
        return shipLength;
    }

    public Double getLoad() {
        return load;
    }
}

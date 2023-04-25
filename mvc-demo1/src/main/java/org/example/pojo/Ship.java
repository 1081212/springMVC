package org.example.pojo;

public class Ship {
    Integer id;
    String name;
    String phone;
    String shipModel;
    Double shipLength;
    Double load;

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", shipModel='" + shipModel + '\'' +
                ", shipLength=" + shipLength +
                ", load=" + load +
                '}';
    }

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

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Double getShipLength() {
        return shipLength;
    }

    public Double getLoad() {
        return load;
    }
}

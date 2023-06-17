package org.example.userserver.client;

import org.springframework.stereotype.Component;

@Component
public class ShipClientFalback implements ShipClient{
    @Override
    public String getAllShips(int pageNum) {
        return "Falback";
    }
}

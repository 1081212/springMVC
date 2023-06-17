package org.example.adminserver.client;

import org.springframework.stereotype.Component;

@Component
public class UserClientFalback implements UserClient{

    @Override
    public String getAllUser(int pageNum) {
        return "Falback";
    }

    @Override
    public String updateUser() {
        return "Falback";
    }
}
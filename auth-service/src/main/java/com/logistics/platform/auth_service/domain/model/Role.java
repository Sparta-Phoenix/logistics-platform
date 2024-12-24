package com.logistics.platform.auth_service.domain.model;

import lombok.Getter;

@Getter
public enum Role {
    MASTER("ROLE_MASTER"), 
    HUB_MANAGER("ROLE_HUB_MANAGER"), 
    DELIVERY_MANAGER("ROLE_DELIVERY_MANAGER"), 
    COMPANY_MANAGER("ROLE_COMPANY_MANAGER");


    private final String role;

    Role(String role) {
        this.role =role;
    }

}

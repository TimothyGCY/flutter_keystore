package com.bleckshiba.flutter_keystore;

import java.util.Arrays;

public enum StorageAction {

    READ("read"),
    WRITE("write"),
    DELETE("delete"),
    ;

    private final String action;

    StorageAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    static StorageAction fromString(String action) {
        return Arrays.stream(values())
                .filter(a -> a.getAction().equals(action))
                .findFirst()
                .orElse(null);
    }
}

package ru.training.at.hwapi.data;

public enum PermissionLevel {
    PRIVATE("private"),
    PUBLIC("public"),
    ORG("org");

    public String permissionLevel;

    PermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}

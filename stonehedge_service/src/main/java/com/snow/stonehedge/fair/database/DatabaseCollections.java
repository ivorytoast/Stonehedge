package com.snow.stonehedge.fair.database;

public enum DatabaseCollections {
    CANDIDATES("candidates"),
    ORDERS("orders");

    private String databaseName;

    public String getDatabaseName() {
        return this.databaseName;
    }

    DatabaseCollections(String databaseName) {
        this.databaseName = databaseName;
    }
}

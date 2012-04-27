package com.thoughtworks.sql;

public class Function extends DBObject<Function>{

    protected Function(String functionName) {
        super(functionName);
    }

    public static Function function(String function) {
        return new Function(function);
    }
}

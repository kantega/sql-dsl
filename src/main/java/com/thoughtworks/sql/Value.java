package com.thoughtworks.sql;

public class Value {

	private final Field field;
	private final String value;

	public Value(Field field, String value) {
		this.field = field;
		this.value = value;
	}

	public Field getField() {
		return field;
	}

	public String getValue() {
		return value;
	}
}

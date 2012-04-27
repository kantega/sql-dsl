package com.thoughtworks.sql;

import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.sql.Constants.*;
import static com.thoughtworks.sql.Table.table;

public class Insert {
	private Table table;
	private List<Value> values = new ArrayList<Value>();

	private Insert(Table table) {
		this.table = table;
	}

	public static Insert into(Table table) {
		return new Insert(table);
	}

	//For fluent language when imported statically
	public static Insert insertInto(Table table) {
		return into(table);
	}

	public Insert values(Value... values) {
		for (Value val : values) {
			this.values.add(val);
		}
		return this;
	}

	@Override
	public boolean equals(Object o) {
		return this == o || !(o == null || getClass() != o.getClass()) && this.toString().equals(o.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder();
		visitInsertClause(sql);
		visitValuesClause(sql);
		return sql.toString();
	}

	private void visitInsertClause(StringBuilder sql) {
		if (table == null || values.isEmpty())
			return;
		sql.append(INSERT).append(SPACE).append(INTO).append(SPACE).append(table.expression).append(SPACE);
	}

	private void visitValuesClause(StringBuilder sql) {
		if (values.isEmpty()) {
			return;
		}
		sql.append(SPACE).append(LEFT_PARENTHESIS);
		for (int i = 0; i < values.size(); i++) {
			Value value = values.get(i);
			sql.append(SPACE).append(value.getField().expression).append(SPACE);
			if (i < values.size() - 1)
				sql.append(COMMA);
		}
		sql.append(RIGHT_PARENTHESIS).append(SPACE).append(VALUES).append(SPACE).append(LEFT_PARENTHESIS);
		for (int i = 0; i < values.size(); i++) {
			Value value = values.get(i);
			sql.append(SPACE).append(value.getValue()).append(SPACE);
			if (i < values.size() - 1)
				sql.append(COMMA);
		}

		sql.append(RIGHT_PARENTHESIS);

	}

	public Table as(String alias) {
		return table(LEFT_PARENTHESIS + this.toString() + RIGHT_PARENTHESIS).as(alias);
	}
}

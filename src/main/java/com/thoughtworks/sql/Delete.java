package com.thoughtworks.sql;

import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.sql.Constants.*;
import static com.thoughtworks.sql.Table.table;

public class Delete {
	private Table table;
    private List<Criterion> criterions = new ArrayList<Criterion>();

    private Delete(Table table) {
		this.table = table;
    }

    public static Delete from(Table table) {
      return new Delete(table);
    }

	//For fluent language when imported statically
	public static Delete deleteFrom(Table table){
		return from(table);
	}

    public Delete where(Criterion criterion) {
        criterions.add(criterion);
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
		visitDeleteClause(sql);
        visitFromClause(sql);
        visitWhereClause(sql);
        return sql.toString();
    }

	private void visitDeleteClause(StringBuilder sql){
		if(table==null)
			return;
		sql.append(DELETE).append(SPACE);
		
	}

    private void visitWhereClause(StringBuilder sql) {
        if (criterions.isEmpty()) {
            return;
        }
        sql.append(SPACE).append(WHERE);
        for (Criterion criterion : criterions) {
            sql.append(SPACE).append(criterion).append(SPACE);
        }
    }

    private void visitFromClause(StringBuilder sql) {
        if (table == null) {
            return;
        }
        sql.append(FROM).append(SPACE).append(table).append(SPACE);
    }

    public Table as(String alias) {
        return table(LEFT_PARENTHESIS + this.toString() + RIGHT_PARENTHESIS).as(alias);
    }

}

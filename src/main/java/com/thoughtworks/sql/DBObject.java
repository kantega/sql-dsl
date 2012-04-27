package com.thoughtworks.sql;

import static com.thoughtworks.sql.Constants.AS;
import static com.thoughtworks.sql.Constants.SPACE;

public abstract class DBObject<T extends DBObject> {
    protected String alias;
    public final String expression;

    protected DBObject(String expression){
        this.expression = expression;
    }

    public T as(String alias) {
        this.alias = alias;
        return (T) this;
    }

    public boolean hasAlias() {
        return alias != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBObject dbObject = (DBObject) o;

        return toString().equals(dbObject.toString());
    }

    @Override
    public int hashCode() {
        int result = alias != null ? alias.hashCode() : 0;
        result = 31 * result + (expression != null ? expression.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(expression);
        if (hasAlias()) {
            sb.append(SPACE).append(AS).append(SPACE).append(alias);
        }
        return sb.toString();
    }
}

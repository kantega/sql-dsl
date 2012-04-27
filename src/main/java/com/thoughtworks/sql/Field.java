package com.thoughtworks.sql;

import static com.thoughtworks.sql.Constants.*;

public class Field extends DBObject<Field> {

	public final String table;

	protected Field(String table, String expression) {
		super(expression);
		this.table = table;
	}

	public String getFullyQualifiedName() {
		StringBuilder sb = new StringBuilder( table + "." + expression);
		if (hasAlias()) {
			sb.append(SPACE).append(AS).append(SPACE).append(alias);
		}
		return sb.toString();
	}

    /**
     * field constructor taking both table and fieldname in the same string
     * @param tableDotField on the form table.field
     * @return the created field
     */
    public static Field field(String tableDotField) {
        if(!tableDotField.matches("\\w+\\.\\w+")){
            throw new IllegalArgumentException("Should be on form table.field");
        }
        String[] split = tableDotField.split("\\.");
        return new Field(split[0], split[1]);
    }

	public static Field field(String table, String expression) {
		return new Field(table, expression);
	}

	public Criterion eq(Object value) {
		return UnaryCriterion.eq(this, value);
	}

	public Criterion eq(Field value) {
		return UnaryCriterion.eq(this, value);
	}

	public Criterion neq(Object value) {
		return UnaryCriterion.neq(this, value);
	}

	public Criterion gt(Object value) {
		return UnaryCriterion.gt(this, value);
	}

	public Criterion lt(final Object value) {
		return UnaryCriterion.lt(this, value);
	}

	public Criterion isNull() {
		return UnaryCriterion.isNull(this);
	}

	public Criterion isNotNull() {
		return UnaryCriterion.isNotNull(this);
	}

	public Value value(String value) {
		return new Value(this, value);
	}

	public Criterion between(final Object lower, final Object upper) {
		final Field field = this;
		return new Criterion(null) {

			protected void populate(StringBuilder sb) {
				sb.append(field).append(SPACE).append(BETWEEN).append(SPACE).append(lower).append(SPACE).append(AND)
						.append(SPACE).append(upper);
			}
		};
	}

	public Criterion like(final String value) {
		return UnaryCriterion.like(this, value);
	}

	public <T> Criterion in(final T... value) {
		final Field field = this;
		return new Criterion(Operator.in) {

			protected void populate(StringBuilder sb) {
				sb.append(field).append(SPACE).append(Operator.in).append(SPACE).append(LEFT_PARENTHESIS);
				for (T t : value) {
					sb.append(t.toString()).append(COMMA);
				}
				sb.deleteCharAt(sb.length() - 1).append(RIGHT_PARENTHESIS);
			}
		};
	}

	public Criterion in(final Field expression, final Query query) {
		final Field field = this;
		return new Criterion(Operator.in) {

			protected void populate(StringBuilder sb) {
				sb.append(field).append(SPACE).append(Operator.in).append(SPACE).append(LEFT_PARENTHESIS).append(query)
						.append(RIGHT_PARENTHESIS);
			}
		};
	}

	@Override
	public String toString() {
		return getFullyQualifiedName();
	}
}

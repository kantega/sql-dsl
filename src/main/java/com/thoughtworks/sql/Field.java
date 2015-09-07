package com.thoughtworks.sql;

import static com.thoughtworks.sql.Constants.*;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class Field extends DBObject<Field> {

    public final String table;
    public final boolean distinct;

    protected Field(String table, String expression, boolean distinct) {
        super(expression);
        this.table = table;
        this.distinct = distinct;
    }
	public String getFullyQualifiedName() {
		StringBuilder sb = new StringBuilder( );
        if(isNotBlank(table)){
            sb.append(table);
            sb.append(".");
        }
        sb.append(expression);

		if (hasAlias()) {
			sb.append(SPACE).append(AS).append(SPACE).append(alias);
		}
		return sb.toString();
	}

    public String getName(){
        return expression;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public static Field field(String tableDotField) {
        return new Field("", tableDotField, false);
    }

    public static Field field(String tableDotField, boolean distinct) {
        return new Field("", tableDotField, distinct);
    }

    public static Field field(String table, String expression, boolean distinct) {
        return new Field(table, expression, distinct);
    }

    public static Field field(String table, String expression) {
        return new Field(table, expression, false);
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

    public <T> Criterion notIn(final T... value) {
        final Field field = this;
        return new Criterion(Operator.notIn) {

            protected void populate(StringBuilder sb) {
                sb.append(field).append(SPACE).append(Operator.notIn).append(SPACE).append(LEFT_PARENTHESIS);
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

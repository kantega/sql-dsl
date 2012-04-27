package com.thoughtworks.sql;

import org.junit.Test;

import static com.thoughtworks.sql.Field.field;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FieldTest {

    @Test
    public void should_return_true_if_field_has_alias_name() {
        Field field = field("t.ss");
        assertFalse(field.hasAlias());
        field.as("s");
        assertTrue(field.hasAlias());
    }

}
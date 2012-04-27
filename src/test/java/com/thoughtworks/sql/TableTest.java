package com.thoughtworks.sql;

import org.junit.Test;

import static com.thoughtworks.sql.Field.field;
import static com.thoughtworks.sql.Table.table;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class TableTest {

    @Test
    public void should_get_field_from_table_directly(){
        Field field = table("table").field("id");
        Field sameField = field("table.id");
        assertThat(field, equalTo(sameField));
    }
}

package com.thoughtworks.sql;

import org.junit.Test;

import static com.thoughtworks.sql.Field.field;

public class GroupByTest {
    @Test
    public void should_generate_groupby_clause() {
        GroupBy groupBy = GroupBy.groupBy(field("table.field"));
    }
}
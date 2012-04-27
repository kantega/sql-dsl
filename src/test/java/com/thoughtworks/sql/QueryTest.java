package com.thoughtworks.sql;

import org.junit.Ignore;
import org.junit.Test;

import static com.thoughtworks.sql.Criterion.and;
import static com.thoughtworks.sql.Field.field;
import static com.thoughtworks.sql.Function.function;
import static com.thoughtworks.sql.Join.*;
import static com.thoughtworks.sql.Order.asc;
import static com.thoughtworks.sql.Order.desc;
import static com.thoughtworks.sql.Query.select;
import static com.thoughtworks.sql.Table.table;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class QueryTest {
    @Test
    @Ignore
    public void should_assemble_simple_query_sql() {
        Function field = function("getdate()");
       /* Query query = select(field);
        assertThat(query.toString(), equalTo("SELECT getdate() "));*/
        assertThat(select(field("isnull(null, 'true')")).toString(), equalTo("SELECT isnull(null, 'true') "));
    }

    @Test
    public void should_select_all_fields_if_no_given_field() {
        assertThat(select().toString(), equalTo("SELECT * "));
    }

    @Test
    public void should_append_select_fields_to_sql() {
        Field first = field("getdate()");
        Query query = select(first);
        Field second = field("getdate()").as("currentTime");
        query.appendSelectFields(second);
        assertThat(query, equalTo(select(first, second)));
        Field third = field("ss");
        Field forth = field("dd").as("f");
        query.appendSelectFields(third, forth);
        assertThat(query, equalTo(select(first, second, third, forth)));
    }

    @Test
    public void should_add_from_clause_to_sql() {
        Query query = select(field("s"));
        query.from(table("d"));
        assertThat(query.toString(), equalTo("SELECT s FROM d "));
    }

    @Test
    public void should_append_inner_join_clause_after_from_clause() {
        Query query = select(field("d")).from(table("table1"));
        query.join(inner(table("table2"), table("table1").field("id").eq(table("table2").field("id"))));
        assertThat(query.toString(), equalTo("SELECT d FROM table1 INNER JOIN table2 ON (table1.id=table2.id) "));
    }

    @Test
    public void should_prepend_join_clause_before_first_join_clause_existing() {
        Query query = select(field("d")).from(table("table1"))
                .join(inner(table("table2"), table("table1").field("id").eq(table("table2").field("id"))),
                        inner(table("table3"), table("table1").field("id").eq(table("table3").field("id"))));
        assertThat(query.toString(), equalTo("SELECT d FROM table1 "
                + "INNER JOIN table2 ON (table1.id=table2.id) "
                + "INNER JOIN table3 ON (table1.id=table3.id) "));
    }

    @Test
    public void should_append_where_clause_after_sql() {
        Query query = select().from(table("table")).where(field("field").eq("?"));
        assertThat(query.toString(), equalTo("SELECT * FROM table WHERE (field=?) "));
    }

    @Test
    public void should_allow_constraints_chain_in_where_clause() {
        Query query = select().from(table("table")).where(and(field("a").eq("'a'"), field("b").eq("'b'")));
        assertThat(query.toString(), equalTo("SELECT * FROM table WHERE ((a='a') AND (b='b')) "));
    }

    @Test
    public void should_append_group_by_clause_after_where_clause() {
        Query query = select().from(table("table")).where(field("d").eq("'d'")).groupBy(field("d"));
        assertThat(query.toString(), equalTo("SELECT * FROM table WHERE (d='d') GROUP BY d "));
    }

    @Test
    public void should_append_multiple_groupBies_after_from_clause() {
        Query query = select().from(table("table")).groupBy(field("d"), field("c"));
        assertThat(query.toString(), equalTo("SELECT * FROM table GROUP BY d, c "));
    }

    @Test
    public void should_append_having_clause_after_groupBy_clause(){
        Query query = select().from(table("table")).groupBy(field("d")).having(field("d").eq("'d'"));
        assertThat(query.toString(), equalTo("SELECT * FROM table GROUP BY d HAVING (d='d') "));
    }

    @Test
    public void should_append_orderBy_fields_after_where_clause() {
        assertThat(select().from(table("table")).orderBy(asc(field("d"))).toString(),
                equalTo("SELECT * FROM table ORDER BY d ASC "));
        assertThat(select().from(table("table")).orderBy(desc(field("d"))).toString(),
                equalTo("SELECT * FROM table ORDER BY d DESC "));
    }

    @Test
    public void should_generate_full_sql() {
        Table t = table("table").as("t");
        Table t1 = table("table1").as("t1");
        Field tId = t.field("id");
        Field t1Id = t1.field("id");
        Field t1Time = t1.field("time");

        Query query = select(tId).from(t).join(inner(t1, tId.eq(t1Id)))
                .where(and(tId.eq("'a'"), t1Time.between("'1900'", "'2000'")))
                .groupBy(tId).having(tId.gt("1"))
                .orderBy(asc(tId));
        assertThat(query.toString(), equalTo("SELECT t.id "
                + "FROM table AS t "
                + "INNER JOIN table1 AS t1 ON (t.id=t1.id) "
                + "WHERE ((t.id='a') AND (t1.time BETWEEN '1900' AND '2000')) "
                + "GROUP BY t.id HAVING (t.id>1) "
                + "ORDER BY t.id ASC "));
    }

    @Test
    public void should_append_joins_of_different_types() {
        Query query = select().from(table("table"))
                .join(left(table("table2"), table("table").field("id").eq(table("table2").field("id"))));
        assertThat(query.toString(), equalTo("SELECT * FROM table LEFT JOIN table2 ON (table.id=table2.id) "));
        query = select().from(table("table"))
                .join(right(table("table2"), table("table").field("id").eq(table("table2").field("id"))));
        assertThat(query.toString(), equalTo("SELECT * FROM table RIGHT JOIN table2 ON (table.id=table2.id) "));
        query = select().from(table("table"))
                .join(out(table("table2"), table("table").field("id").eq(table("table2").field("id"))));
        assertThat(query.toString(), equalTo("SELECT * FROM table OUT JOIN table2 ON (table.id=table2.id) "));
    }

    @Test
    public void should_accept_sub_query_as_data_set() {
        Query query = select().from(select().from(table("table")).as("table2"));
        assertThat(query.toString(), equalTo("SELECT * FROM (SELECT * FROM table ) AS table2 "));
    }

    @Test
    public void should_accept_sub_query_in_where_clause() {
        Query query = select().from(table("table")).where(field("d").in(field("d"), select().from(table("table2"))));
        assertThat(query.toString(), equalTo("SELECT * FROM table WHERE (d IN (SELECT * FROM table2 )) "));
    }

}
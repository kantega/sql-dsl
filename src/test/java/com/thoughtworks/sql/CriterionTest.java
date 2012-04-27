package com.thoughtworks.sql;

import org.junit.Ignore;
import org.junit.Test;

import static com.thoughtworks.sql.Criterion.*;
import static com.thoughtworks.sql.Field.field;
import static com.thoughtworks.sql.Query.select;
import static com.thoughtworks.sql.Table.table;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class CriterionTest {

    @Test
    public void should_toString_of_single_criterion() {
        assertThat(field("t","d").eq("'d'").toString(), equalTo("(t.d='d')"));
        assertThat(field("t","d").neq("'d'").toString(), equalTo("(t.d<>'d')"));
        assertThat(field("t","d").gt("'d'").toString(), equalTo("(t.d>'d')"));
        assertThat(field("t","d").lt("'d'").toString(), equalTo("(t.d<'d')"));
        assertThat(field("t","d").isNull().toString(), equalTo("(t.d IS NULL)"));
        assertThat(field("t","d").isNotNull().toString(), equalTo("(t.d IS NOT NULL)"));
        assertThat(field("t","d").between("'d1'", "'d2'").toString(), equalTo("(t.d BETWEEN 'd1' AND 'd2')"));
        assertThat(field("t","d").like("'%d'").toString(), equalTo("(t.d LIKE '%d')"));
        assertThat(field("t","d").in("'d1'", "'d2'").toString(), equalTo("(t.d IN ('d1','d2'))"));
    }

    @Test
    public void should_generate_constraint_by_partial_criterions_and_logic_operator() {
        assertThat(and(field("t","d").eq("'d'"), field("t","a").neq("'a'")).toString(), equalTo("((t.d='d') AND (t.a<>'a'))"));
        assertThat(or(field("t","d").eq("'d'"), field("t","a").neq("'a'")).toString(), equalTo("((t.d='d') OR (t.a<>'a'))"));
    }

    @Test
    public void should_create_exists_criterion(){
        assertThat(exists(select().from(table("table"))).toString(), equalTo("(EXISTS (SELECT * FROM table ))"));
    }
    
    @Test
    @Ignore
    public void should_reverse_given_criterion_by_not(){
        assertThat(not(field("t","d").eq("'d'")).toString(), equalTo("t.d<>'d'"));
    }



}

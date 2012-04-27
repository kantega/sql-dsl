package com.thoughtworks.sql;

import org.junit.Test;

import static com.thoughtworks.sql.Operator.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class OperatorTest {

    @Test
    public void should_get_operator_string() {
        assertThat(eq.toString(), equalTo("="));
        assertThat(neq.toString(), equalTo("<>"));
        assertThat(isNull.toString(), equalTo("IS NULL"));
        assertThat(isNotNull.toString(), equalTo("IS NOT NULL"));
        assertThat(gt.toString(), equalTo(">"));
        assertThat(lt.toString(), equalTo("<"));
        assertThat(gte.toString(), equalTo(">="));
        assertThat(lte.toString(), equalTo("<="));
        assertThat(and.toString(), equalTo("AND"));
        assertThat(or.toString(), equalTo("OR"));
        assertThat(not.toString(), equalTo("NOT"));
        assertThat(in.toString(), equalTo("IN"));
        assertThat(exists.toString(), equalTo("EXISTS"));
        assertThat(like.toString(), equalTo("LIKE"));
    }

    @Test
    public void should_get_contrary_operator_given_operator() {
        assertThat(eq.getContrary(), equalTo(neq));
        assertThat(neq.getContrary(), equalTo(eq));
        assertThat(isNull.getContrary(), equalTo(isNotNull));
        assertThat(isNotNull.getContrary(), equalTo(isNull));
        assertThat(gt.getContrary(), equalTo(lte));
        assertThat(lte.getContrary(), equalTo(gt));
        assertThat(lt.getContrary(), equalTo(gte));
        assertThat(gte.getContrary(), equalTo(lt));
        assertThat(in.getContrary().toString(), equalTo("NOT IN"));
        assertThat(exists.getContrary().toString(), equalTo("NOT EXISTS"));
        assertThat(like.getContrary().toString(), equalTo("NOT LIKE"));
    }
    
    @Test
    public void should_have_synonymity_to_operator_and_contrary(){
        assertThat(eq.getContrary().getContrary(), equalTo(eq));
        assertThat(in.getContrary().getContrary(), equalTo(in));
        assertThat(exists.getContrary().getContrary(), equalTo(exists));
        assertThat(like.getContrary().getContrary(), equalTo(like));
    }
}
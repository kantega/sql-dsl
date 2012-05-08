package com.thoughtworks.sql;

import org.junit.Ignore;
import org.junit.Test;

import static com.thoughtworks.sql.ActivityDb.*;
import static com.thoughtworks.sql.Criterion.*;
import static com.thoughtworks.sql.Field.field;
import static com.thoughtworks.sql.Join.left;
import static com.thoughtworks.sql.Query.select;
import static com.thoughtworks.sql.Table.table;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
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
        assertThat(field("t","d").notIn("'d1'", "'d2'").toString(), equalTo("(t.d NOT IN ('d1','d2'))"));
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

    @Test
    public void testWithComplexQueryExample(){
        Query query = select(utfortDato, aktivitetBeskrivelse, fk_aktivitetstype, fk_kategori, aktorLandId, agentFornavn, agentEtternavn, agentId, aktorId).from(aktivitet)
                .join(
                        left(henvendelse, henvendelseField.eq(pk_id_henvendelse)),
                        left(agent, agentFK.eq(agent_pk_id)),
                        left(aktor, fk_erAktor.eq(aktorId)),
                        left(aktorLand, aktorLandpkid.eq(fk_UtfortForAktor))
                ).where(and(utfortDato.between("'2012-04-01'", "'2012-05-01'"),agentFK.notIn("0", "1")));
        assertEquals("SELECT aktivitet.UtfortDato,aktivitet.Beskrivelse,aktivitet.fk_AktivitetsType,aktivitet.fk_Kategori,aktor_land.land,aktor.fornavn,aktor.etternavn,agent.pk_id AS agentid,aktor.pk_id " +
                "FROM aktivitet LEFT JOIN henvendelse ON (aktivitet.fk_Henvendelse=henvendelse.pk_ID) " +
                "LEFT JOIN agent ON (aktivitet.fk_BetjenesAvAgent=agent.pk_id) " +
                "LEFT JOIN aktor ON (agent.fk_ErAktor=aktor.pk_id) " +
                "LEFT JOIN aktor_land ON (aktor_land.aktor=aktivitet.fk_UtfortForAktor) " +
                "WHERE ((aktivitet.UtfortDato BETWEEN '2012-04-01' AND '2012-05-01') " +
                "AND (aktivitet.fk_BetjenesAvAgent NOT IN (0,1)))", query.toString().trim());
    }

}

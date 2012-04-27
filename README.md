sql-dsl
=======

Sql like DSL for java programmer to write sql instead of using strings

The library was originally created by some one else on http://code.google.com/p/sql-dsl. We was on the look out for this functionality, and started using it. Since we wanted to build it with maven, and have done some changes, we decided to fork it.

The creators reason for creating the project:
   As a java programmer, to develop a system, mostly we need to create sql by different criterions. 
   If we introduce hibernate, it provides criterion module to us, and if we introduce ibatis, it provides sql fragments to consist the last sql. 
   But, how if we don't introduce them? How can we enjoy the convenience by querying by criteria? 
   Yes, that's why I create this project to provide a very thin layer for java programmer to ease the pain of assembling sql string.

Sample
======
    Table t = table("table").as("t");
    Table t1 = table("table1").as("t1");
    Field tId = t.field("id");
    Field t1Id = t1.field("id");
    Field t1Time = t1.field("time");

    Sql sql = select(tId).from(t).join(inner(t1, tId.eq(t1Id)))
          .where(and(tId.eq("'a'"), t1Time.between("'1900'", "'2000'")))
          .groupBy(tId).having(tId.gt("1"))
          .orderBy(asc(tId));
    assertThat(sql.toString(), equalTo("SELECT t.id "
           + "FROM table AS t "
           + "INNER JOIN table1 AS t1 ON (t.id=t1.id) "
           + "WHERE ((t.id='a') AND (t1.time BETWEEN '1900' AND '2000')) "
           + "GROUP BY t.id HAVING (t.id>1) "
           + "ORDER BY t.id ASC "));

package com.thoughtworks.sql;

import static com.thoughtworks.sql.Table.table;

public class ActivityDb {
    public static final Table henvendelse = table("henvendelse");
    public static final Table aktivitet = table("aktivitet");
    public static final Table kontaktKanal = table("KontaktKanal");
    public static final Table aktor = table("aktor");
    public static final Table agent = table("agent");
    public static final Table koder = table("koder");
    public static final Table aktorLand = table("aktor_land");

    public static final Field agentEtternavn = aktor.field("etternavn");
    public static final Field agentFornavn = aktor.field("fornavn");
    public static final Field aktorId = aktor.field("pk_id");

    public static final Field agentId = agent.field("pk_id").as("agentid");
    public static final Field agent_pk_id = agent.field("pk_id");
    public static final Field fk_erAktor = agent.field("fk_ErAktor");

    public static final Field fk_adresse = henvendelse.field("adresse");
    public static final Field pk_id_henvendelse = henvendelse.field("pk_ID");

    public static final Field fk_kategori = aktivitet.field("fk_Kategori");
    public static final Field fk_aktivitetstype = aktivitet.field("fk_AktivitetsType");
    public static final Field henvendelseField = aktivitet.field("fk_Henvendelse");
    public static final Field agentFK = aktivitet.field("fk_BetjenesAvAgent");
    public static final Field utfortDato = aktivitet.field("UtfortDato");
    public static final Field aktivitetBeskrivelse = aktivitet.field("Beskrivelse");
    public static final Field fk_UtfortForAktor = aktivitet.field("fk_UtfortForAktor");


    public static final Field kontaktkanalLand = kontaktKanal.field("fk_Land");
    public static final Field pk_id_kontaktKanal = kontaktKanal.field("pk_ID");
    public static final Field typeKontaktkanalId = kontaktKanal.field("fk_TypeKontaktKanal");
    public static final Field fk_Aktor = kontaktKanal.field("fk_Aktor");

    public static final Field kode_pk_id = koder.field("pk_id").as("id");
    public static final Field beskrivelse = koder.field("beskrivelse").as("navn");
    public static final Field kodenavn = koder.field("kodenavn");

    public static final Field aktorLandpkid = aktorLand.field("aktor");
    public static final Field aktorLandId = aktorLand.field("land");


}

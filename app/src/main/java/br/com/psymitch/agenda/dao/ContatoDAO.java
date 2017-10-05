package br.com.psymitch.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.psymitch.agenda.modelo.Contato;

/**
 * Created by CsGo on 11/03/2017.
 */

public class ContatoDAO extends SQLiteOpenHelper {
    public ContatoDAO(Context context) {
        super(context, "Agenda", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE CONTATOS (ID INTEGER PRIMARY KEY,NOME TEXT NOT NULL ,TELEFONE TEXT,ENDERECO TEXT,EMAIL TEXT,NOTA REAL,CAMINHOFOTO TEXT,SITE TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                String sql = "ALTER TABLE CONTATOS ADD COLUMN CAMINHOFOTO TEXT;ALTER TABLE CONTATOS ADD COLUMN SITE TEXT;";
                db.execSQL(sql);
                break;

            case 2:
                sql = "ALTER TABLE CONTATOS ADD COLUMN SITE TEXT;";
                db.execSQL(sql);
                break;
        }
    }

    public void atualiza(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegaDadosContato(contato);
        String[] params = {contato.getId().toString()};
        db.update("CONTATOS", dados, "id = ?", params);
    }

    public void insere(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegaDadosContato(contato);
        db.insert("CONTATOS", null, dados);
    }

    @NonNull
    private ContentValues pegaDadosContato(Contato contato) {
        ContentValues dados = new ContentValues();
        dados.put("NOME", contato.getNome());
        dados.put("TELEFONE", contato.getTelefone());
        dados.put("ENDERECO", contato.getEndereco());
        dados.put("EMAIL", contato.getEmail());
        dados.put("NOTA", contato.getNota());
        dados.put("CAMINHOFOTO", contato.getCaminhoFoto());
        dados.put("SITE", contato.getSite());
        return dados;
    }

    public List<Contato> buscaContatos() {
        String sql = "SELECT * FROM CONTATOS;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Contato> contatos = new ArrayList<Contato>();
        while (c.moveToNext()) {
            Contato contato = new Contato();
            contato.setId(c.getLong(c.getColumnIndex("ID")));
            contato.setNome(c.getString(c.getColumnIndex("NOME")));
            contato.setTelefone(c.getString(c.getColumnIndex("TELEFONE")));
            contato.setEndereco(c.getString(c.getColumnIndex("ENDERECO")));
            contato.setEmail(c.getString(c.getColumnIndex("EMAIL")));
            contato.setNota(c.getDouble(c.getColumnIndex("NOTA")));
            contato.setCaminhoFoto(c.getString(c.getColumnIndex("CAMINHOFOTO")));
            contato.setSite(c.getString(c.getColumnIndex("SITE")));

            contatos.add(contato);
        }
        c.close();
        return contatos;
    }

    public void deletar(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {contato.getId().toString()};
        db.delete("CONTATOS", "ID = ?", params);
    }

    public boolean ehAluno(String telefone) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT + FROM CONTATOS WHERE TELEFONE = ?", new String[]{telefone});
        int resultados = c.getCount();
        c.close();
        return resultados > 0;
    }
}

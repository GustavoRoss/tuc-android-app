package br.com.fabricadeprogramador.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.fabricadeprogramador.model.ItemCesta;
import br.com.fabricadeprogramador.model.Produto;

public class BdManager extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "tucBd";
    private static final int VERSAO = 1;
    private static final String TABELA_PRODUTO = "tb_produto";
    private static final int ID = 0;
    private static final int DESCRICAO = 1;
    private static final int VALOR = 2;
    private static final int CODIGOBARRA = 3;
    private static final int IMAGE = 4;
    private static final int QUANTIDADE = 5;

    public BdManager(Context context){
        super(context, NOME_BANCO, null, VERSAO);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " +TABELA_PRODUTO+ " ( id integer primary key, descricao text, valor double, " +
                "codigoBarra text, image integer, quantidade integer )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "drop table if exists " +TABELA_PRODUTO;
        db.execSQL(sql);

        onCreate(db);
    }

    public void save(ItemCesta p){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("descricao", p.getProduto().getDescricao());
        values.put("valor", p.getProduto().getValor());
        values.put("codigoBarra", p.getProduto().getCodigoBarra());
        values.put("image", p.getProduto().getImage());
        values.put("quantidade", p.getQuantidade());
        db.insert(TABELA_PRODUTO, null, values);
        db.close();
    }

    public List<ItemCesta> findAlltoItemCesta(){
        List<ItemCesta> list = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select  * from " + TABELA_PRODUTO;
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                ItemCesta p = new ItemCesta();
                p.getProduto().setId(cursor.getInt(ID));
                p.getProduto().setDescricao(cursor.getString(DESCRICAO));
                p.getProduto().setValor(cursor.getDouble(VALOR));
                p.getProduto().setCodigoBarra(cursor.getString(CODIGOBARRA));
                p.getProduto().setImage(cursor.getInt(IMAGE));
                p.setQuantidade(cursor.getInt(QUANTIDADE));
                list.add(p);
            }while(cursor.moveToNext());
        }
        return list;
    }


}

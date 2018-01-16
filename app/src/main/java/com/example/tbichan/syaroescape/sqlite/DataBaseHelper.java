package com.example.tbichan.syaroescape.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 5515012o on 2017/12/12.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "test.db";//DB名
    public final static String DB_TABLE = "test";   //テーブル名
    public final static int DB_VERSION = 1;      //バージョン

    public final static int NETWORK_ID = 1;     // ネットワークID
    public final static int PLAYER_NAME = 2;     // プレイヤーネーム

    private static DataBaseHelper dataBaseHelper = null;

    private SQLiteDatabase db;

    public static DataBaseHelper init(Context context) {
        dataBaseHelper = new DataBaseHelper(context);
        return dataBaseHelper;
    }

    public static DataBaseHelper getDataBaseHelper() {

        return dataBaseHelper;
    }

    // コンストラクタ
    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        // データベースオブジェクト取得
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " +
                DB_TABLE + "(id text primary key, info text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DB_TABLE);
        onCreate(db);
    }

    public String read(int id) throws Exception {
        Cursor c = db.query(DataBaseHelper.DB_TABLE, new String[]{"id", "info"},
                "id='" + String.valueOf(id) + "'", null, null, null, null);
        if (c.getCount() == 0) throw new Exception();
        c.moveToFirst();
        String str = c.getString(1);
        c.close();

        return str;
    }

    public void write(int id, String info) {

        String idStr = String.valueOf(id);

        ContentValues values = new ContentValues();
        values.put("id", idStr);
        values.put("info", info);

        int colNum = db.update(DataBaseHelper.DB_TABLE, values, "id=?", new String[]{idStr});
        if (colNum == 0) db.insert(DataBaseHelper.DB_TABLE, "", values);

    }
}

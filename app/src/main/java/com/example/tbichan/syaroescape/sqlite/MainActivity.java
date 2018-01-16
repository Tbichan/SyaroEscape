/*
package com.example.tbichan.syaroescape.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private final static int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
    private final static int MP = LinearLayout.LayoutParams.MATCH_PARENT;

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // レイアウト作成
        LinearLayout layout = new LinearLayout(this);
        layout.setBackgroundColor(Color.WHITE);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);

        // テキスト作成
        editText = new EditText(this);
        editText.setLayoutParams(new LinearLayout.LayoutParams(MP, WC));
        layout.addView(editText);

        // 読み込み
        String str = "";

        try {
            str = DataBaseHelper.getDataBaseHelper(this).read(0);
        } catch (Exception e) {

        }


        // ボタン作成
        final Button button = new Button(this);
        button.setText(str);
        button.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String info = editText.getText().toString();
                DataBaseHelper.getDataBaseHelper(MainActivity.this).write(info);
                button.setText(info);

            }
        });
        layout.addView(button);


    }
}
*/
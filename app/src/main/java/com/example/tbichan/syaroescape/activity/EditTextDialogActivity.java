package com.example.tbichan.syaroescape.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tbichan.syaroescape.R;

/**
 * ダイアログ用のActivity
 */
public class EditTextDialogActivity extends Activity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_dialog);

        editText = new EditText(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        AlertDialog.Builder ad = new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //入力した文字をトースト出力する
                        Toast.makeText(EditTextDialogActivity.this,
                                editText.getText().toString(),
                                Toast.LENGTH_LONG).show();
                        finish();

                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // Resultがキャンセルのとき呼ばれる
                        finish();
                    }
                });

        // 表示
        ad.show();

    }
}

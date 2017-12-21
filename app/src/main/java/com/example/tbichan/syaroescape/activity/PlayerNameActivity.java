package com.example.tbichan.syaroescape.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.ui.EditAlertListenerManager;

public class PlayerNameActivity extends Activity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_player_name);

        editText = new EditText(this);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setOnClickListener(new View.OnClickListener() {

            // クリックしたら有効にする
            @Override
            public void onClick(View v) {
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();

            }
        });

        AlertDialog.Builder ad = new AlertDialog.Builder(this)
                .setTitle("名前をおしえてね")
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // クリック伝達
                        EditAlertListenerManager.getPositiveListener().onClick(editText);

                        finish();

                    }
                })
                .setCancelable(false);

        // 表示
        ad.show();
    }
}

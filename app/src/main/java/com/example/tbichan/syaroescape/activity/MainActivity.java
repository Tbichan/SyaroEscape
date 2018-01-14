package com.example.tbichan.syaroescape.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.dialog.CustomDialogFragment;
import com.example.tbichan.syaroescape.findbattleuser.FindBattleUserScene;
import com.example.tbichan.syaroescape.menu.MenuScene;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.*;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.title.TitleScene;
import com.example.tbichan.syaroescape.ui.EditAlertListenerManager;
import com.example.tbichan.syaroescape.ui.UIListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    protected GlView glView;

    protected static MainActivity instance;

    // 初期シーン
    SceneBase firstScene;

    // エディットテキスト
    private EditText editText;

    private AlertDialog ad = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d("mainactivity", "MainActivity作成");

        instance = this;

        // 初期シーン
        firstScene = new TitleScene();

        // 初期ビュー
        glView = new GlView(this);
        setContentView(glView);

        // ブロードキャストリスナー
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null) {
                    if (action.equals(Intent.ACTION_SCREEN_ON)) {
                        // 画面ON時
                        Log.d("surface", "SCREEN_ON");
                        // テクスチャリロード
                        //MainActivity.getGlView().loadTexAll();
                    } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                        // 画面OFF時
                        Log.d("surface", "SCREEN_OFF");
                    }
                }
            }
        };

        try {

            registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
            registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        }
        catch (Exception e) {

        }


        editText = new EditText(instance);
        //showTextDialog("名前をおしえてね");
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (glView != null) glView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //if (glView != null) glView.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("mainactivity", "MainActivity削除");
        glView.destroyGame();
        finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        } else {
            // 戻るキーなら終了させない
            return false;
        }
    }

    public static String loadFile(String pass) {
        // ファイル読み込みテスト

        InputStream is = null;
        BufferedReader br = null;
        String text = "";
        try {
            try {
                // assetsフォルダ内の sample.txt をオープンする
                is = instance.getAssets().open(pass);
                br = new BufferedReader(new InputStreamReader(is));

                // １行ずつ読み込み、改行を付加する
                String str;
                while ((str = br.readLine()) != null) {
                    text += str + "\n";

                }
            } finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
        } catch (Exception e){
            // エラー発生時の処理
        }


        return text;
    }

    // ビューを設定する。
    public static void setGlView(GlViewBase glView) {
        instance.setContentView(glView);
    }

    // ビューを取得する。
    public static GlView getGlView(){
        return instance.glView;
    }

    public static Context getContext() { return (Context)instance; }

    public static SceneBase getFirstScene(){
        return instance.firstScene;
    }

    // 名前ダイアログを表示します。
    public static void showNameDialog(UIListener pos){

        // 登録
        EditAlertListenerManager.setPositiveListener(pos);

        // 画面遷移
        Intent intent = new Intent(instance, PlayerNameActivity.class);
        instance.startActivityForResult(intent, 1);

        // Activityのアニメーション設定
        //instance.overridePendingTransition(R.anim.in_anim, R.anim.out_anim);
    }

    // OKCancelダイアログを表示します。
    public static void showOKCancelDialog(UIListener positive, UIListener negative){

        // 登録
        EditAlertListenerManager.setPositiveListener(positive);
        EditAlertListenerManager.setNegativeListener(negative);

        // 画面遷移
        Intent intent = new Intent(instance, OKCancelActivity.class);
        instance.startActivityForResult(intent, 1);

        // Activityのアニメーション設定
        //instance.overridePendingTransition(R.anim.in_anim, R.anim.out_anim);

    }

    // OKCancelダイアログを表示します。
    public static void showOKDialog(String title, String message, UIListener pos){

        // 登録
        EditAlertListenerManager.setPositiveListener(pos);

        // 画面遷移
        Intent intent = new Intent(instance, OKActivity.class);
        intent.putExtra("title_OKActivity", title);
        intent.putExtra("message_OKActivity", message);
        instance.startActivityForResult(intent, 1);

        // Activityのアニメーション設定
        //instance.overridePendingTransition(R.anim.in_anim, R.anim.out_anim);

    }

    // テキストダイアログを表示します。
    public static void showTextDialog(String title, UIListener pos){


        // 画面遷移
        Intent intent = new Intent(instance, EditTextDialogActivity.class);

        // タイトルを送信
        intent.putExtra("title", title);
        instance.startActivityForResult(intent, 1);
    }

    // テキストダイアログを表示します。
    public static void showTextDialog(String title, UIListener pos, UIListener neg){

        // 登録
        EditAlertListenerManager.setPositiveListener(pos);
        EditAlertListenerManager.setNegativeListener(neg);

        if (instance.ad == null) {

            instance.ad = new AlertDialog.Builder(instance)
            .setTitle(title)
                    .setView(instance.editText)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // クリック伝達
                            EditAlertListenerManager.getPositiveListener().onClick(instance.editText);

                            //入力した文字をトースト出力する
                            Toast.makeText(instance,
                                    instance.editText.getText().toString(),
                                    Toast.LENGTH_LONG).show();

                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // クリック伝達
                            EditAlertListenerManager.getNegativeListener().onClick(instance.editText);
                        }
                    }).create();
            //instance.ad.create();
        }

        // 表示
        instance.ad.setTitle(title);
        instance.ad.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // テクスチャリロード
        //MainActivity.getGlView().loadTexAll();
        Log.d("MainActivity", "return");
    }

}
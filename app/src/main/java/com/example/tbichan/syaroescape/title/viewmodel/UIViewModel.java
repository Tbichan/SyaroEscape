package com.example.tbichan.syaroescape.title.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.ui.UIListener;

import javax.microedition.khronos.opengles.GL10;

/**
 * UI用のビューです。
 * Created by tbichan on 2017/10/16.
 */

public class UIViewModel extends GlViewModel {

    private final String registUrl = "http://192.168.3.130/android/syaro_escape/sql/regist/regist.py";
    private final String showUrl = "http://192.168.3.130/android/syaro_escape/sql/show/show.py";

    public UIViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    int cnt = 0;

    @Override
    public void awake() {
        // TODO 自動生成されたメソッド・スタブ

        // スタートボタン
        GlButton startBtn = new GlButton(this, "StartButton"){

            @Override
            public void start() {
                super.start();
                setTexture(R.drawable.sentaku);
                //setTexWidth(getTexWidth() / 2);
                Log.d("hoge", "startImg");

                // 大きさを指定
                setSize(800, 225);
            }

            // クリック時
            @Override
            public void onClick(){
                System.out.println("idou");
                //SceneManager.getInstance().setNextScene(new GameScene(getContext()));

                MainActivity.showTextDialog("名前をおしえてね" + cnt, new UIListener() {

                    // OKを押したとき
                    @Override
                    public void onClick(View view) {

                        // 文字列を取得
                        String name = ((EditText)view).getText().toString();
                        Log.d("名前", name);

                        // 通信で名前登録
                        MyHttp myHttp = new MyHttp(registUrl + "?name=" + name) {

                            // 接続成功時
                            @Override
                            public void success() {
                                // 表示
                                try {
                                    Log.d("net", result());
                                } catch (Exception e) {

                                }
                            }

                            // 接続失敗時
                            @Override
                            public void fail(Exception e) {
                                System.out.println("接続エラー:" + e.toString());
                            }

                        };
                        myHttp.connect();
                    }
                }, new UIListener() {

                    // キャンセルを押したとき
                    @Override
                    public void onClick(View view) {
                        // 通信で名前表示
                        MyHttp myHttp = new MyHttp(showUrl) {

                            // 接続成功時
                            @Override
                            public void success() {
                                // 表示
                                try {
                                    Log.d("net", result());
                                } catch (Exception e) {

                                }
                            }

                            // 接続失敗時
                            @Override
                            public void fail(Exception e) {
                                System.out.println("接続エラー:" + e.toString());
                            }

                        };
                        myHttp.connect();
                    }
                });
                cnt++;
                //MainActivity.showText();
            }
        };

        startBtn.setPosition(640*2, 100);
        // 追加
        addModel(startBtn);
    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl){
        super.update(gl);
    }

    // タップイベント
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return true;
    }
}



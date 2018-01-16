package com.example.tbichan.syaroescape.title.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.common.model.MoveModel;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.StringViewModel;
import com.example.tbichan.syaroescape.menu.MenuScene;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.store.StoreManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.sqlite.DataBaseHelper;
import com.example.tbichan.syaroescape.title.*;
import com.example.tbichan.syaroescape.title.TitleScene;
import com.example.tbichan.syaroescape.ui.UIListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 5516096h on 2017/12/11.
 */

public class TitleViewModel extends GlViewModel {

    GlModel titlebutton;

    // タップした時のカウンタ
    private int tapCnt = -1;

    // fadeViewModel
    private FadeViewModel fadeViewModel;

    public void setFadeViewModel(FadeViewModel fadeViewModel) {
        this.fadeViewModel = fadeViewModel;
    }

    public TitleViewModel(GlView glView, SceneBase sceneBase, String name) {
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        titlebutton = new GlModel(this, "タイトルボタン") {

            @Override
            public void start() {

            }
            @Override
            public void update() {

            }
        };
        titlebutton.setTextureId(R.drawable.title_logo);

        titlebutton.setPosition(GlView.VIEW_WIDTH * 0.5f - 750, GlView.VIEW_HEIGHT * 0.5f - 200 - 325 - 100);
        titlebutton.setSize(1500, 400);

        addModel(titlebutton);

    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

        titlebutton.setPosition(GlView.VIEW_WIDTH * 0.5f - 750, GlView.VIEW_HEIGHT * 0.5f - 200 - 325 - 100 + 20 * (float) Math.sin(getCnt()
                * 0.025));

        if (tapCnt != -1 && getCnt() - tapCnt == 60) {

            // 名前決定
            MainActivity.showNameDialog(new UIListener() {
                @Override
                public void onClick(View view) {
                    // プレイヤー名を描画する
                    String text = ((EditText)view).getText().toString();
                    // 空白で補完
                    text = String.format("%-6s", text);

                    // 名前をmysqlに保存
                    DataBaseHelper.getDataBaseHelper().write(DataBaseHelper.PLAYER_NAME, text);

                    //StoreManager.save("menu_playername", text);
                    String encode = "";
                    try {
                        encode = URLEncoder.encode(text, "UTF-8");
                    } catch (UnsupportedEncodingException e) {

                    }

                    // ネット接続
                    MyHttp myHttp = new MyHttp(NetWorkManager.DOMAIN + "/sql/regist/regist.py?name=" + encode) {

                        // 接続成功時
                        @Override
                        public void success() {
                            // 表示
                            try {
                                Log.d("net", result());

                                int network_id = Integer.parseInt(result().replace("\n", ""));

                                Log.d("net", network_id + "");

                                // networkidをsqliteに保存
                                DataBaseHelper.getDataBaseHelper().write(DataBaseHelper.NETWORK_ID, String.valueOf(network_id));

                            } catch (Exception e) {

                            }
                        }

                        // 接続失敗時
                        @Override
                        public void fail(Exception e) {
                            Log.d("net", "接続エラー:" + e.toString());

                        }

                    }.setSecondUrl(NetWorkManager.DOMAIN_SECOND + "/sql/regist/regist.py?name=" + encode);

                    myHttp.connect();

                    // IDをランダムで生成
                    StoreManager.save("player_id", (int)(Math.random() * 25535));

                    fadeViewModel.startFadeOut();
                    //SceneManager.getInstance().setNextScene(new MenuScene());
                }
            });

            //fadeViewModel.startFadeOut();
        }

    }

    // タップイベント
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);

        if (ev.getAction() == MotionEvent.ACTION_DOWN && isVisible() && getCnt() >= 300) {
            if (tapCnt == -1) tapCnt = getCnt();

        }

        return true;
    }
}

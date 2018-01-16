package com.example.tbichan.syaroescape.title.viewmodel;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.store.StoreManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.sound.SEManager;
import com.example.tbichan.syaroescape.sqlite.DataBaseHelper;
import com.example.tbichan.syaroescape.title.model.TitleLogoModel;
import com.example.tbichan.syaroescape.ui.UIListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 5516096h on 2017/12/11.
 */

public class TitleViewModel extends GlViewModel {

    // タップした時のカウンタ
    private int tapCnt = -1;

    private TitleLogoModel gameName, titleLogo;

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

        gameName = new TitleLogoModel(this, "ゲーム名");
        gameName.setTextureId(R.drawable.game_name);

        gameName.setSize(2000, 500);
        gameName.setPosition(GlView.VIEW_WIDTH * 0.5f - gameName.getWidth() * 0.5f, 800);


        titleLogo = new TitleLogoModel(this, "タイトルロゴ");
        titleLogo.setTextureId(R.drawable.title_logo);

        titleLogo.setPosition(GlView.VIEW_WIDTH * 0.5f - 750, GlView.VIEW_HEIGHT * 0.5f - 200 - 325 - 100);
        titleLogo.setSize(1500, 400);

        addModel(gameName);
        addModel(titleLogo);

    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);


        if (tapCnt != -1 && getCnt() - tapCnt == 60) {

            // プレイヤー名をよみこみ
            String playerName = "";
            try {
                playerName = DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.PLAYER_NAME);
            } catch (Exception e) {

            }

            // IDをよみこみ
            String playerIdString = "";
            try {
                playerIdString = DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.NETWORK_ID);
            } catch (Exception e) {

            }

            if (playerName.length() >= 2 && playerIdString.length() >= 2) {
                // IDをランダムで生成
                StoreManager.save("player_id", (int) (Math.random() * 25535));

                fadeViewModel.startFadeOut();
            }else {

                // 名前決定
                MainActivity.showNameDialog(new UIListener() {
                    @Override
                    public void onClick(View view) {
                        // プレイヤー名を描画する
                        String text = ((EditText) view).getText().toString();
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
                        StoreManager.save("player_id", (int) (Math.random() * 25535));

                        fadeViewModel.startFadeOut();
                        //SceneManager.getInstance().setNextScene(new MenuScene());
                    }
                });
            }

            //fadeViewModel.startFadeOut();
        }

    }

    // タップイベント
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);

        if (ev.getAction() == MotionEvent.ACTION_DOWN && isVisible() && getCnt() >= 300) {
            if (tapCnt == -1) {
                tapCnt = getCnt();

                gameName.flash();
                titleLogo.flash();

                // 効果音再生
                SEManager.getInstance().playSE(R.raw.decision);
            }

        }

        return true;
    }
}

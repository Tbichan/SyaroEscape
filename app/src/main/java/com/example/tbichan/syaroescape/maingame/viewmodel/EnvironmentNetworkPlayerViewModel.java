package com.example.tbichan.syaroescape.maingame.viewmodel;

import android.util.Log;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.maingame.NetworkGameScene;
import com.example.tbichan.syaroescape.maingame.model.Carrot;
import com.example.tbichan.syaroescape.maingame.model.Cup;
import com.example.tbichan.syaroescape.maingame.model.EnableFloor;
import com.example.tbichan.syaroescape.maingame.model.EnvSprite;
import com.example.tbichan.syaroescape.maingame.model.Environment;
import com.example.tbichan.syaroescape.maingame.model.Player;
import com.example.tbichan.syaroescape.maingame.model.PowerEffect;
import com.example.tbichan.syaroescape.maingame.model.Rabbit;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;

import java.util.ArrayList;
import java.util.HashSet;

import static com.example.tbichan.syaroescape.maingame.model.Environment.CREATE_RABBIT;
import static com.example.tbichan.syaroescape.maingame.model.Environment.MAP_SIZE;
import static com.example.tbichan.syaroescape.maingame.model.Environment.MOVE_DESK;
import static com.example.tbichan.syaroescape.maingame.model.Environment.MOVE_RABBIT;
import static com.example.tbichan.syaroescape.maingame.model.Environment.PARAM_ADD_CUP;
import static com.example.tbichan.syaroescape.maingame.model.Environment.PARAM_END;
import static com.example.tbichan.syaroescape.maingame.model.Environment.PARAM_HIT;

/**
 * Created by tbichan on 2017/12/10.
 */

public class EnvironmentNetworkPlayerViewModel extends EnvironmentViewModel implements GlObservable {

    // Observserable
    private ArrayList<GlObservable> glObservableList = new ArrayList<>();

    public EnvironmentNetworkPlayerViewModel(GlView glView, SceneBase sceneBase, String name, int id, int level) {
        super(glView, sceneBase, name, id, level);

    }

    // 環境にクエリとして送ります。(ネットワーク送信)
    @Override
    public void queryEnv(String query, boolean net) {
        super.queryEnv(query, net);

        if (net == true) {

            // リプレイファイル取得
            String fileName = ((NetworkGameScene) getScene()).getFileName();

            // サーバに送る
            MyHttp myHttp = new MyHttp(NetWorkManager.DOMAIN + "sql/send/send.py?query=" + query + "&id=" + getId() + "&filename=" + fileName) {

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
                    Log.d("net", "接続エラーss:" + e.toString());

                }

            }.setSecondUrl(NetWorkManager.DOMAIN_SECOND + "sql/send/send.py?query=" + query + "&id=" + getId() + "&filename=" + fileName);

            myHttp.connect();
        }
    }


}

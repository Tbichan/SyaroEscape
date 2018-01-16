package com.example.tbichan.syaroescape.menu.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.store.StoreManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.sqlite.DataBaseHelper;
import com.example.tbichan.syaroescape.ui.UIListener;

import javax.microedition.khronos.opengles.GL10;

/**
 * プレイヤー情報を出力するViewModelです。
 * Created by tbichan on 2017/12/12.
 */

public class PlayerDetailViewModel extends GlViewModel {

    // 名前のModel
    private GlModel playerNameModel;

    public PlayerDetailViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        playerNameModel = new GlModel(this, "PlayerName") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };

        //if (StoreManager.containsKey("menu_playername")) {

        // ID、プレイヤー名をよみこみ
        String playerName = "";
        try {
            playerName = "ID:" + DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.NETWORK_ID) + "　" + DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.PLAYER_NAME);
        } catch (Exception e) {

        }

        Log.d("name", playerName);

        //Bitmap strBit = BitMapManager.createStrImage(playerName, "uzura", 50, Color.YELLOW);
        playerNameModel.setTextureText(playerName);
        playerNameModel.setVisible(false);

        playerNameModel.setPosition(1800, -350);
        playerNameModel.setSize(1800, 500);
        //playerNameModel.setSize(150 * 2, 75 * 2);

        //} else {

        //    playerNameModel.setPosition(2000, 50);
        //    playerNameModel.setSize(150 * 2, 75 * 2);
        //    playerNameModel.setVisible(false);
        //}

        addModel(playerNameModel);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

        // 名前を指定させる。
        /*
        if (getCnt() == 2 && !StoreManager.containsKey("menu_playername")) {
            MainActivity.showNameDialog(new UIListener() {
                @Override
                public void onClick(View view) {
                    // プレイヤー名を描画する
                    String text = ((EditText)view).getText().toString();
                    // 空白で補完
                    text = String.format("%-6s", text);
                    Bitmap strBit = BitMapManager.createStrImage(text, 50, Color.YELLOW);
                    playerNameModel.setOutsideBitmapTexture(strBit);
                    playerNameModel.setVisible(true);

                    // 名前を保存
                    StoreManager.save("menu_playername", text);

                    // IDをランダムで生成
                    StoreManager.save("player_id", (int)(Math.random() * 25535));
                }
            });
        }
        */
    }
}

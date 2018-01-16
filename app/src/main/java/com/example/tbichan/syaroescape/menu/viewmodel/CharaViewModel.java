package com.example.tbichan.syaroescape.menu.viewmodel;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.menu.model.CharaModel;
import com.example.tbichan.syaroescape.menu.model.Fukidashi;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/12/09.
 */

public class CharaViewModel extends GlViewModel {

    private Fukidashi fukidashi = null;

    // 文字列のModel
    private GlModel strModel;

    // 表示する文字
    private final String[][] strs = {
            {"一人で遊びます。　　", "みんなで遊びます！　", "対戦履歴です。　　　", "設定です。　　　　　"},
            {"一人で遊ぶモードよ　", "お友達と遊ぶモードよ", "対戦履歴よ　　　　　", "設定よっ!!　　　　　"}};

    private CharaModel charaModel;

    // 表示するキャラid
    private int charaId = -1;

    // 退場フラグ
    private boolean retireFlg = false;

    // 退場したカウンタ
    private int retireCnt = -1;

    public CharaViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {
        // 吹き出し作成
        fukidashi = new Fukidashi(this, "fukidashi");

        fukidashi.setTextureId(R.drawable.fukidashi);
        fukidashi.setSize(800, 525); // 300->700
        fukidashi.setPosition(0.0f, 900.0f);

        // キャラ作成

        charaId = (int)(Math.random()*2.0); // キャラをランダムに決定

        final String[] names = {"chino", "syaro"};
        final int[] imgIds = {R.drawable.chino_menu, R.drawable.syaro_menu};

        charaModel = new CharaModel(this, names[charaId]);
        charaModel.setTextureId(imgIds[charaId]);
        charaModel.setSize(510, 1000);
        charaModel.setY(-12f);
        //charaModel.setAngle(45f);

        strModel = new GlModel(this, "moji") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };
        strModel.setPosition(50, 1330);


        addModel(charaModel);
        addModel(fukidashi);
        addModel(strModel);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

        if (!retireFlg) {
            float nextVMX = getX() + 70.0f;
            if (nextVMX > 200.0f) nextVMX = 200.0f;
            setX(nextVMX);

            // 移動
            float y = 12f * (float) (Math.sin(getCnt() * 0.025f) - 1.0f);
            setY(y);
        } else {
            // 退場時
            setX(getX() - 100.0f);
        }

    }

    // 吹き出しを出現させます。
    public void appearFukidashi(int id) {
        // 吹き出しを初期位置に
        setX(-1000.0f);

        // 文字作成
        Bitmap strBit = BitMapManager.createStrImage(strs[charaId][id], 50, Color.BLACK);
        strModel.setOutsideBitmapTexture(strBit);
        strModel.setSize(450, 60);

    }

    // 退場します。
    public void retire() {
        retireFlg = true;
        retireCnt = getCnt();
    }

}

package com.example.tbichan.syaroescape.maingamebak.viewmodel;

import android.content.Context;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.maingamebak.model.COM;
import com.example.tbichan.syaroescape.maingamebak.model.Cup;
import com.example.tbichan.syaroescape.maingamebak.model.Enemy;
import com.example.tbichan.syaroescape.maingamebak.model.StageModel;
import com.example.tbichan.syaroescape.maingamebak.model.Table;
import com.example.tbichan.syaroescape.scene.SceneBase;

import javax.microedition.khronos.opengles.GL10;

/**
 * 相手用のビューです。（右側）
 * Created by tbichan on 2017/10/16.
 */

public class OpponentViewModel extends GlViewModel {

    private COM com;

    // ステージモデル
    private StageModel stageModel;

    // 敵の数
    private int enemyCnt = 0;

    // カップの数
    private int cupCnt = 0;

    public OpponentViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);


    }

    // 読み込み
    @Override
    public void awake() {

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {
        // モデル読み込み
        stageModel = ((StageModel)getGlView().findModel("stagemodel"));

        int size = 16;

        // table
        for (int i = 0; i < size*size; i++) {
            if (stageModel.getMapID(this, i % size, i / size) == 1) {
                Table table = new Table(this, "Table" + i);
                table.setLPosition(i % size, i / size);
                addModel(table);
            } if (stageModel.isCup(this, i % size, i / size)) {
                Cup cup = new Cup(this, "Cup" + cupCnt);
                cup.setLPosition(i % size, i / size);
                addModel(cup);
                cupCnt++;
            }

            if (stageModel.getMapID(this, i % size, i / size) == StageModel.ENEMY) {
                Enemy enemy = new Enemy(this, "Enemy" + enemyCnt);
                enemy.setLPosition(i % size, i / size);
                addModel(enemy);
                enemyCnt++;
            }
        }

        // Player
        com = new COM(this, "COM");
        com.setLPosition(1, 1);
        // 追加
        addModel(com);
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

    // 敵追加
    public void addEnemy() {

        Enemy enemy = new Enemy(this, "Enemy" + enemyCnt);
        enemy.setLPosition(14, 14);
        addModel(enemy);
        enemyCnt++;

    }

    // ランダムにカップ作成
    public void addCup() {
        Cup cup = new Cup(this, "Cup" + cupCnt);
        cup.setLPosition((int)(Math.random() * 14) + 1, (int)(Math.random() * 14) + 1);
        addModel(cup);
        cupCnt++;
    }
}



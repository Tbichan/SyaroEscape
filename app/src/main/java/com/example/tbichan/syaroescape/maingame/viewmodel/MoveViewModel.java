package com.example.tbichan.syaroescape.maingame.viewmodel;

import android.content.Context;

import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * 動作つきのVMです。
 * Created by tbichan on 2017/12/16.
 */

public class MoveViewModel extends GlViewModel {

    private float oldX, oldY;

    private float newX, newY;

    // リスト
    private ArrayList<Float> newXList = new ArrayList<>();
    private ArrayList<Float> newYList = new ArrayList<>();
    private ArrayList<Integer> intervalList = new ArrayList<>();

    private int interval = 60;

    // 移動しきってからendMoveを呼ぶまでのカウンタ
    private int endInterval = 30;

    private int moveStartCnt = -1;

    public MoveViewModel(GlView glView, SceneBase sceneBase, String name) {
        super(glView, sceneBase, name);

    }

    // 隱ｭ縺ｿ霎ｼ縺ｿ
    @Override
    public void awake() {

    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

        if (isMove()) {

            // 次の移動
            float nextX = oldX + (newX - oldX) * 0.5f * (float)(-(Math.cos((float)(getCnt() - moveStartCnt) / interval * Math.PI)) + 1.0f);
            float nextY = oldY + (newY - oldY) * 0.5f * (float)(-(Math.cos((float)(getCnt() - moveStartCnt) / interval * Math.PI)) + 1.0f);

            // 速度を求める。
            //float nextX = oldX + (newX - oldX) * (float)(getCnt() - moveStartCnt) / interval;
            //float nextY = oldY + (newY - oldY) * (float)(getCnt() - moveStartCnt) / interval;

            setX(nextX);
            setY(nextY);
            //setY(getY() + vy);

            // 動きを止める。
            if (getCnt() - moveStartCnt >= interval) {
                setX(newX);
                setY(newY);
                if (getCnt() - moveStartCnt >= interval + endInterval) {
                    moveStartCnt = -1;
                    endMove();

                    // 予約が残っていたら
                    if (newXList.size() > 0) {
                        final float tmpNewX = newXList.get(0);
                        newXList.remove(0);
                        final float tmpNewY = newYList.get(0);
                        newYList.remove(0);
                        final int tmpInterval = intervalList.get(0);
                        intervalList.remove(0);
                        move(tmpNewX, tmpNewY, tmpInterval);
                    }
                }
            }
        }
    }

    // sin関数で移動します。
    public void move(float newX, float newY, int interval) {
        move(newX, newY, interval, false);
    }

    // sin関数で移動します。
    public void move(float newX, float newY, int interval, boolean overwrite) {

        if (!overwrite) {
            if (isMove()) {
                // 予約する
                newXList.add(newX);
                newYList.add(newY);
                intervalList.add(interval);
                return;
            }
        }

        oldX = getX();
        oldY = getY();
        this.newX = newX;
        this.newY = newY;
        this.interval = interval;
        moveStartCnt = getCnt();
    }

    public boolean isMove() {
        return moveStartCnt != -1;
    }

    public void endMove() {

    }
}

package com.example.tbichan.syaroescape.common.model;

import android.util.Log;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.R;

/**
 * Created by tbichan on 2017/10/17.
 */

public abstract class GlButton extends GlModel {

    // クリックしているかどうか
    private boolean push = false;

    // クリックカウンタ
    private int clickCnt = -1;

    public GlButton(GlViewModel glViewModel, String name) {
        super(glViewModel, name);
        setTexture(R.drawable.button);
        setSize(GlView.TILE_SIZE * 1.5f * 1.618033988749895f, GlView.TILE_SIZE * 1.5f);   // 黄金比
        // 離したときのテクスチャ変更
        upTex();
        //setTexWidth(getTexWidth() / 2);
    }

    @Override
    public void awake() {

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

    }

    @Override
    public void update(){
        if (push) press();

    }

    // タップイベント
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        float x, y;

        GlView glView = MainActivity.getGlView();

        // ボタンにかーそるが当たっているか
        boolean cursorBtn = false;

        if (ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_MOVE) {
            // タッチした座標を取得
            x = ev.getX() * glView.getViewWidth() / glView.getWidth();
            y = ev.getY() * glView.getViewHeight() / glView.getHeight();

            // y軸だけ座標系が違う
            y = MainActivity.getGlView().getViewHeight() - y;

            // 実際のボタンの座標
            float rx = getX() + getGlViewModel().getX();
            float ry = getY() + getGlViewModel().getY();

            if (x >= rx && x <= (rx + getWidth())) {
                if (y >= ry && y <= (ry + getHeight())) {

                    // 焦点
                    onCursor();
                    cursorBtn = true;
                }
            }
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // タッチした座標を取得
            x = ev.getX() * glView.getViewWidth() / glView.getWidth();
            y = ev.getY() * glView.getViewHeight() / glView.getHeight();

            // y軸だけ座標系が違う
            y = MainActivity.getGlView().getViewHeight() - y;

            // 実際のボタンの座標
            float rx = getX() + getGlViewModel().getX();
            float ry = getY() + getGlViewModel().getY();

            if (x >= rx && x <= (rx + getWidth()) && y >= ry && y <= (ry + getHeight())) {

                // クリック実行
                onClick();

                // テクスチャ変更
                onTex();

                clickCnt = getCnt();

                push = true;
                cursorBtn = true;
            } else {
                // 別クリック
                outClick();
            }

        }

        else if (ev.getAction() == MotionEvent.ACTION_UP) {

            if (push) {

                // タッチした座標を取得
                x = ev.getX() * glView.getViewWidth() / glView.getWidth();
                y = ev.getY() * glView.getViewHeight() / glView.getHeight();

                // y軸だけ座標系が違う
                y = MainActivity.getGlView().getViewHeight() - y;

                // 実際のボタンの座標
                float rx = getX() + getGlViewModel().getX();
                float ry = getY() + getGlViewModel().getY();

                if (x >= rx && x <= (rx + getWidth()) && y >= ry && y <= (ry + getHeight())) {

                    // すぐはなしたらタップに
                    if (clickCnt != -1 && getCnt() - clickCnt < 10) onTap();

                }

                // クリック実行
                upClick();

                push = false;
                clickCnt = -1;


                // テクスチャ変更
                upTex();
            }

        }

        if (!cursorBtn) {
            notCursor();
        }

        return true;
    }

    // ボタンクリック時
    public abstract void onClick();

    // 別クリック時
    public void outClick() {

    }

    // プレス時
    public void press() {

    }

    // ボタンに焦点が当たっているとき
    public void onCursor(){

    }

    // ボタンに焦点が当たっていないとき
    public void notCursor(){

    }

    // ボタンを離すとき
    public void upClick(){

    }

    // ボタンをタップしたとき
    public void onTap(){

    }

    // 押したときのテクスチャ変更
    public void onTex() {
        setU1(0.5f);
        setU2(1.0f);
    }

    // 離したときのテクスチャ変更
    public void upTex() {
        // テクスチャ変更
        setU1(0.0f);
        setU2(0.5f);
    }
}

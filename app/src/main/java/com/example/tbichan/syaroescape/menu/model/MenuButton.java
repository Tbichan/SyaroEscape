package com.example.tbichan.syaroescape.menu.model;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * Created by tbichan on 2017/12/09.
 */

public abstract class MenuButton extends GlButton {

    public float defaultX;
    public float defaultY;

    // カーソルが当たった瞬間のカウンタ
    private int cursorCnt = -1;

    // ボタンid
    private int id = -1;

    // 入場フラグ
    private boolean admissionFlg = true;

    // 退場フラグ
    private boolean retireFlg = false;

    // アニメーションの速さ
    private float animSpeed = 1.0f;

    public final void setDefultPosition(float x, float y) {
        defaultX = x;
        defaultY = y;
    }

    public MenuButton(GlViewModel glViewModel, String name, int id) {
        super(glViewModel, name);

        this.id = id;

    }

    @Override
    public void update() {

        // どこを描画するか
        int num = (int) (((Math.sin(getCnt() / 5) + 1.0) * 0.5 * animSpeed) * (5.0 - 0.001));
        //num = (getCnt()/20) % 5;
        setUV(0.0f, 0.2f * num, 1.0f, 0.2f * (num + 1.0f));

        if (admissionFlg) {
            // 移動量
            float nextX = getX() - 80.0f;

            if (nextX < defaultX) {
                nextX = defaultX;
                admissionFlg = false;
            }

            setX(nextX);
        }
        else if (!retireFlg) {

            if (cursorCnt != -1) {
                // 移動量
                float move = 20.0f * (getCnt() - cursorCnt);

                if (move > 150f) move = 150f;

                setX(defaultX - move);

            } else {
                setX(defaultX);
            }
        } else {
            // 退場中
            setX(getX() + 100.0f);
        }
    }

    public final void setAnimSpeed(float animSpeed) {
        this.animSpeed = animSpeed;
    }

    // ボタンに焦点が当たっているとき
    @Override
    public void onCursor(){
        if (!admissionFlg && !retireFlg) startMove();

    }

    // ボタンに焦点が当たっていないとき
    @Override
    public void notCursor(){
        //returnPosition();
    }

    // 押し出す
    public void startMove() {
        if (cursorCnt == -1) cursorCnt = getCnt();
    }

    // ひっこめる
    public void returnPosition() {
        cursorCnt = -1;
    }

    // 押し出し切っているか
    public boolean isMoveFinal()
    {
        return cursorCnt != -1 && (getCnt() - cursorCnt) > 10;
    }

    // 入場中かどうか
    public boolean isAdmission() {
        return admissionFlg;
    }

    // 退場します。
    public void retire() {
        retireFlg = true;
    }

    // id取得
    public final int getId() {
        return id;
    }

    // アニメーション停止
    public final void animStop() {
        animSpeed = 0.0f;
    }

    // 押したときのテクスチャ変更
    @Override
    public void onTex() {
    }

    // 離したときのテクスチャ変更
    @Override
    public void upTex() {
    }

}

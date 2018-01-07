package com.example.tbichan.syaroescape.common.model;

import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * キャラの立ち絵用
 * Created by tbichan on 2017/12/09.
 */

public class TalkCharaModel extends GlModel {

    // アクションID
    public static final int NONE = 0;
    public static final int APPEAR_LEFT = 1;
    public static final int APPEAR_RIGHT = 2;
    public static final int JUMP = 3;
    private int actId = 0;

    // アクション開始カウンタ
    public int actCnt = -1;

    // アクション開始時座標
    private float actInitX, actInitY;

    public TalkCharaModel(GlViewModel glViewModel, String name) {
        super(glViewModel, name);

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {

        int t = 0;
        switch (actId){
            case APPEAR_LEFT:
                // 左から出現
                t = getCnt() - actCnt;
                if (t < 2) {
                    setX(-300f);
                } else {
                    // 右に移動
                    setX(getX() + 60f);
                    if (getX() >= actInitX) endAction();
                }
                break;

            case APPEAR_RIGHT:
                // 右から出現
                t = getCnt() - actCnt;
                if (t < 2) {
                    setX(3000f);
                } else {
                    // 右に移動
                    setX(getX() - 60f);
                    if (getX() <= actInitX) endAction();
                }
                break;

            case JUMP:
                // ジャンプ
                t = getCnt() - actCnt;
                if (t < 30) {
                    setY(actInitY + 25f * (float)(Math.sin(t / 30f * Math.PI)));
                } else if (t == 30) endAction();
                break;

            default:
                break;
        }
    }

    public void action(int actId) {
        if (actCnt != -1) endAction();
        this.actId = actId;
        actCnt = getCnt();
        actInitX = getX();
        actInitY = getY();
    }

    public void endAction() {
        actId = -1;
        actCnt = -1;
        setPosition(actInitX, actInitY);
    }

}
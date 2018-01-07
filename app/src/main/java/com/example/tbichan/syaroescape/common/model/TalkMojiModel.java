package com.example.tbichan.syaroescape.common.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * 会話文用モデルです。
 * Created by tbichan on 2018/01/07.
 */

public class TalkMojiModel extends GlModel {

    private String text = "ほげー";

    private int strWidth = 0;

    private int numShowNum = 0;

    private int allShowCnt = -1;

    private boolean stop = false;

    // 表示間隔
    private int interval = 2;

    public TalkMojiModel(GlViewModel glViewModel, String name, String text) {
        super(glViewModel, name);
        this.text = text;
    }

    /**
     * テクスチャ読み込みが終わると実行されます。
     */
    @Override
    public void endTexLoad() {
        getBitmap().recycle();
        Log.d("outbitmap", "recycle");
    }

    @Override
    public void awake() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap strBit = BitMapManager.createStrImage(text, 80, 2048, 1024, Color.GRAY);

                // 文字列の幅を取得
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setTextSize(80);
                strWidth = (int) paint.measureText(text);

                setOutsideBitmapTexture(strBit);
            }
        }).start();

        setSize(2048, 1024);
        setVisible(false);
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {

        int textNum = text.length();

        if (getCnt() % interval == 0 && numShowNum != -1 && !stop) {
            int num = numShowNum;

            if (num >= textNum) num = textNum - 1;

            setSize((num + 1.0f) / textNum * (strWidth / 2048f) * 2048f, getHeight());
            setUV((num) / textNum * (strWidth / 2048f), 0f, (num + 1.0f) / textNum * (strWidth / 2048f), 1.0f);
            numShowNum++;
            if (numShowNum == textNum) {
                allShowCnt = getCnt();
                allShowBefore();

            }
            setVisible(true);
        }

        if (allShowCnt != -1 && allShowCnt + interval == getCnt()) {
            allShow();
        }
    }

    // タップイベント
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (allShowCnt == -1) {
                allShowBefore();
                setVisible(true);
            }
        }
        return true;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public final void allShowBefore() {
        int textNum = text.length();
        int num = textNum - 1;
        setSize((num + 1.0f) / textNum * (strWidth / 2048f) * 2048f, getHeight());
        setUV((num) / textNum * (strWidth / 2048f), 0f, (num + 1.0f) / textNum * (strWidth / 2048f), 1.0f);
        numShowNum = -1;
        allShowCnt = getCnt();
    }

    public void allShow() {

    }

    public boolean isAllShow() {
        return allShowCnt != -1 && getCnt() > allShowCnt + interval;
    }
}

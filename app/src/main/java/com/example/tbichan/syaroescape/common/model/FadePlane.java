package com.example.tbichan.syaroescape.common.model;

import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.renderer.GlRenderer;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/11/28.
 */

public class FadePlane extends GlModel {

    private final FloatBuffer mVertexBuffer;

    // フェードインの時間
    private int fadeInCnt = 180;

    // フェードアウトの時間
    private int fadeOutCnt = 180;

    // フェードアウトを始めた時間
    private int fadeOutStartCnt = -1;

    public FadePlane(GlViewModel glViewModel, String name) {
        super(glViewModel, name);

        setTag("fade");

        float size= 3.0f;

        float vertices[] = {
                // 前
                -size, -size, 0.1f,
                size, -size, 0.1f,
                -size, size, 0.1f,
                size, size, 0.1f,
                /*
                // 後
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,

                // 左
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,

                // 右
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,

                // 上
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,

                // 底
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f
                */
        };

        ByteBuffer vbb =
                ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
    }



    @Override
    public void start(){
        //setTextureId(R.drawable.black);
        //setSize(GlView.VTEW_WIDTH, GlView.VTEW_HEIGHT);
    }

    @Override
    public void update(){

    }

    // フェードインの時間を指定します。
    public void setFadeInCount(int fadeInCnt) {
        this.fadeInCnt = fadeInCnt;
    }

    // フェードアウトの時間を指定します。
    public void setFadeOutCount(int fadeOutCnt) {
        this.fadeOutCnt = fadeOutCnt;
    }

    // フェードアウトを開始します。
    public void startFadeOut() {
        fadeOutStartCnt = getCnt();
    }

    // フェードインが終了したかを判定します。
    public boolean isFadeInEnd() {
        return fadeInCnt <= getCnt();
    }

    // フェードアウトが開始したかを判定します。
    public boolean isStartFadeOut() {
        return fadeOutStartCnt != -1;
    }
    // フェードアウトが終了したかを判定します。
    public boolean isFadeOutEnd() {
        return fadeOutStartCnt != -1 && getCnt() - fadeOutStartCnt >= fadeOutCnt;
    }

    @Override
    public void draw(final GlRenderer glRenderer) {

        /*
        float alpha = 0.0f;
        // フェードインの時
        if (!isFadeInEnd()) {
            alpha = 0.5f * (float) (Math.cos(getCnt() * Math.PI / fadeInCnt) + 1.0f);

        }

        // フェードアウトの時
        if (isStartFadeOut() && !isFadeOutEnd()) {
            alpha = 0.5f * (float) (Math.cos(Math.PI - getCnt() * Math.PI / fadeInCnt) + 1.0f);

        }

        // 黒色にする。
        gl.glColor4f(0.0f, 0.0f, 0.0f, alpha);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

        // Front
        gl.glNormal3f(0, 0, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

        // 色（変更を戻す）
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        /*
        // Back
        gl.glNormal3f(0, 0, -1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);

        // Left
        gl.glNormal3f(-1.0f, 0, 0);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);

        // Right
        gl.glNormal3f(1.0f, 0, 0);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);

        // Top
        gl.glNormal3f(0, 1.0f, 0);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);

        // Right
        gl.glNormal3f(0, -1.0f, 0);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
        */
    }
}

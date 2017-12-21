package com.example.tbichan.syaroescape.opengl.model;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.renderer.GlRenderer;
import com.example.tbichan.syaroescape.opengl.shader.ShaderSource;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 3DモデルVer
 * Created by tbichan on 2017/10/15.
 */

public abstract class GlModel3DCube extends GlModel {

    private float[] color = new float[]{1.0f,1.0f,1.0f,1.0f};

    float[] worldMatrix = new float[16];


    public GlModel3DCube(GlViewModel glViewModel, String name){
        super(glViewModel, name);
    }

    // テクスチャをロード
    @Override
    public void loadTexture(GL10 gl) {
        // GLES 2.0
        //ここからシェーダーを使うっていう、定形
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, ShaderSource.VERTEX_SHADER_CODE_NORMAL);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderSource.FRAGMENT_SHADER_CODE_NORMAL);
        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);

        Matrix.setIdentityM(worldMatrix, 0);

        setLoadTex(true);
    }

    @Override
    public void draw(final GlRenderer glRenderer) {

        if (isLoadTex() == false) return;

        // verticesに座標を代入
        GlView glView = MainActivity.getGlView();
        GlViewModel glViewModel = getGlViewModel();

        float left = ((x + glViewModel.getX()) / (float) glView.getViewWidth()) * glView.getWidth();
        float right = left + getWidth() / (float) glView.getViewWidth() * glView.getWidth();
        float bottom = ((y + glViewModel.getY()) / (float) glView.getViewHeight()) * glView.getHeight();
        float top = bottom + getHeight() / (float) glView.getViewHeight() * glView.getHeight();

        left = left / glView.getWidth() * 2.0f - 1.0f;
        right = right / glView.getWidth() * 2.0f - 1.0f;
        bottom = bottom / glView.getHeight() * 2.0f - 1.0f;
        top = top / glView.getHeight() * 2.0f - 1.0f;

        final float zBuffer = glRenderer.getZBuffer();

        /*
        final float[] vertices = {
                right, top, zBuffer,    // 右上
                right, bottom, zBuffer,   // 右下
                left, bottom, zBuffer,   // 左下
                left, top, zBuffer   // 左上
        };*/



        final float vertices[] = {

                // back
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,

                // top
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,

                // front
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,

                // bottom
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,

                // left
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,

                // right
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,

        };

        /*
        float vertices[] = {
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                0.0f,  1.0f, 0.0f,
        };*/

        // 法線
        final float normal[] = {

                // back
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,

                // top
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,

                // front
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                // top
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,

                // left
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                // right
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
        };


        // Vertex
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        /*
        // 法線
        float normal[] = {
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
        };*/
        ByteBuffer nb = ByteBuffer.allocateDirect(normal.length * 4);
        nb.order(ByteOrder.nativeOrder());
        normalBuffer = nb.asFloatBuffer();
        normalBuffer.put(normal);
        normalBuffer.position(0);


        GLES20.glUseProgram(shaderProgram);

        // シェーダーの準備(図形側)
        int mPositionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        int mNormalHandle = GLES20.glGetAttribLocation(shaderProgram, "vertexNormal");
        // シェーダー:ON
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // 行列
        int matLoc1 = GLES20.glGetUniformLocation(shaderProgram, "wMatrix");
        int matLoc2 = GLES20.glGetUniformLocation(shaderProgram, "normalMatrix");

        //Matrix.rotateM(worldMatrix, 0, worldMatrix, 0, 1f, 0, 1, 0);
        Matrix.rotateM(worldMatrix, 0, worldMatrix, 0, 10f*(float)(Math.random()-0.5), 1, 0, 0);
        Matrix.rotateM(worldMatrix, 0, worldMatrix, 0, 10f*(float)(Math.random()-0.5), 0, 1, 0);
        Matrix.rotateM(worldMatrix, 0, worldMatrix, 0, 10f*(float)(Math.random()-0.5), 0, 0, 1);

        GLES20.glUniformMatrix4fv(matLoc1, 1, false, worldMatrix, 0);
        GLES20.glUniformMatrix4fv(matLoc2, 1, false, worldMatrix, 0);

        //////////////////////////////////START
        // vertexBufferをデータ列から頂点データという解釈に変換
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);

        // ユニフォーム変数にアクセスの準備
        int coloruniform = GLES20.glGetUniformLocation(shaderProgram, "vColor");
        // 色情報をユニフォーム変数に変換

        float col = 0.5f;

        float[] color = new float[]{col, col, col,1.0f};
        GLES20.glUniform4fv(coloruniform, 1, color, 0);

        for (int i = 0; i < 6; i++) {

            // 描画する。何で描くのかは、「関数内で登録してある」という暗黙の了解的な。
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 4*i, 4);
            //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 4*i, 4);
        }

        //////////////////////////////////END
        // シェーダー:OFF
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}

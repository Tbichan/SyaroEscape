package com.example.tbichan.syaroescape.opengl.model;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.renderer.GlRenderer;
import com.example.tbichan.syaroescape.opengl.shader.ShaderSource;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 5515012o on 2017/12/13.
 */

public class GlModelColor extends GlModel {

    public GlModelColor(GlViewModel glViewModel, String name) {
        super(glViewModel, name);
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {

    }

    private float[] color = new float[]{0.0f,0.0f,0.0f,1.0f};

    // テクスチャをロード
    @Override
    public void loadTexture(GL10 gl) {

        //x = 0;
        //y = 0;
        //if (getWidth() == 0) width = bitmap.getWidth();
        //if (getHeight() == 0) height = bitmap.getHeight();

        //if (imgWidth == 0) imgWidth = bitmap.getWidth();
        //if (imgHeight == 0) imgHeight = bitmap.getHeight();

        // GLES 2.0
        //ここからシェーダーを使うっていう、定形
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, ShaderSource.VERTEX_SHADER_CODE_COLOR);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderSource.FRAGMENT_SHADER_CODE_COLOR);
        shaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(shaderProgram, vertexShader);
        GLES20.glAttachShader(shaderProgram, fragmentShader);
        GLES20.glLinkProgram(shaderProgram);

        setLoadTex(true);
    }

    @Override
    public void draw(final GlRenderer glRenderer) {

        if (isLoadTex() == false) return;

        // verticesに座標を代入
        GlView glView = MainActivity.getGlView();
        GlViewModel glViewModel = getGlViewModel();

        float left = ((x + glViewModel.getX()) /  (float)glView.getViewWidth()) * glView.getWidth();
        float right = left + getWidth() / (float)glView.getViewWidth() * glView.getWidth();
        float bottom = ((y + glViewModel.getY()) / (float)glView.getViewHeight()) * glView.getHeight();
        float top = bottom + getHeight() / (float)glView.getViewHeight() * glView.getHeight();

        left = left / glView.getWidth() * 2.0f - 1.0f;
        right = right / glView.getWidth() * 2.0f - 1.0f;
        bottom = bottom / glView.getHeight() * 2.0f - 1.0f;
        top = top / glView.getHeight() * 2.0f - 1.0f;

        final float zBuffer = glRenderer.getZBuffer();

        final float[] vertices = {
                right, top, zBuffer,    // 右上
                right, bottom, zBuffer,   // 右下
                left, bottom, zBuffer,   // 左下
                left, top, zBuffer   // 左上
        };

        /*
        final float vertices[] = {
                0.0f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f
        };*/

        // Vertex
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // 拡大縮小の時のフィルターの設定
        //GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
        //GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

        GLES20.glUseProgram(shaderProgram);
        // シェーダーの準備(図形側)
        int mPositionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        // シェーダー:ON
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 行列
        int matLoc1 = GLES20.glGetUniformLocation(shaderProgram, "wMatrix");

        float[] worldMatrix = new float[16];
        Matrix.setIdentityM(worldMatrix, 0);

        GLES20.glUniformMatrix4fv(matLoc1, 1, false, worldMatrix, 0);

        //////////////////////////////////START
        // vertexBufferをデータ列から頂点データという解釈に変換
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        //GLES20.glVertexAttribPointer(mPositionHandle,vertices.length, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // ユニフォーム変数にアクセスの準備
        int coloruniform = GLES20.glGetUniformLocation(shaderProgram, "vColor");
        // 色情報をユニフォーム変数に変換

        GLES20.glUniform4fv(coloruniform, 1, color, 0);

        // 透明度
        //GLES20.glUniform1f(GLES20.glGetUniformLocation(shaderProgram, "Opacity"), getAlpha());

        // 画像の明るさ
        //GLES20.glUniform1f(GLES20.glGetUniformLocation(shaderProgram, "Brightness"), bright);


        // 描画に利用をする画像のデータをする
        //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNo);

        // 描画する。何で描くのかは、「関数内で登録してある」という暗黙の了解的な。
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

        //////////////////////////////////END
        // シェーダー:OFF
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    // 色を設定します。
    public void setRGBA(float r, float g, float b, float a) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
        color[3] = alpha;
    }

}

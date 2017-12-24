package com.example.tbichan.syaroescape.opengl.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.renderer.GlRenderer;
import com.example.tbichan.syaroescape.opengl.shader.ShaderSource;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Created by tbichan on 2017/10/15.
 */

public abstract class GlModel {

    // 名前
    private String name = "";

    // タグ
    private String tag = "unknown";

    // 座標
    protected float x, y;

    // カウンタ
    private int cnt = 0;

    // 表示中かどうか
    private boolean visible = true;

    // 画像ID
    private int imgId = -1;

    //テクスチャNo
    public int textureNo;

    // テクスチャをロードしたかどうか
    private boolean loadTex = false;

    // 外部画像
    private Bitmap outImg = null;

    public boolean isLoadTex() {return loadTex; }

    // 描画中かどうか
    private boolean drawFlg = false;

    public boolean isDraw() {
        return drawFlg;
    }

    public void setDrawFlg(boolean drawFlg) {
        this.drawFlg = drawFlg;
    }

    // 描画が終わるまで待つ
    public void waitDraw() {
        while (drawFlg) {

        }
    }

    //テクスチャ（画像）の位置とサイズ
    private int texX;
    private int texY;
    private int texWidth;
    private int texHeight;
    //配置する時の幅と高さ
    private float width = 0.0f;
    private float height = 0.0f;

    //　画像サイズ
    private int imgWidth = 0;
    private int imgHeight = 0;

    // UV座標
    private float u1 = .0f;
    private float v1 = .0f;

    private float u2 = 1.0f;
    private float v2 = 1.0f;

    // ビュー
    private GlView glView;

    // ビューモデル
    private GlViewModel glViewModel;

    public FloatBuffer vertexBuffer;
    public FloatBuffer normalBuffer;
    public FloatBuffer uvBuffer;
    public ShortBuffer indexBuffer;

    public GlViewModel getGlViewModel(){
        return glViewModel;
    }

    // 画像
    Bitmap bitmap;

    int shaderProgram;

    // テクスチャを繰り返すかどうか
    private boolean repeatTex = false;

    // 透明度
    private float alpha = 1.0f;

    // 画像の明るさ
    private float bright = 0.0f;

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getBright() {
        return bright;
    }

    public void setBright(float bright) {
        this.bright = bright;
    }


    public void setGlAngle(float angle) {

        // verticesに座標を代入
        if (glView == null) glView = MainActivity.getGlView();
        float left = ((x + glViewModel.getX()) / (float) glView.getViewWidth()) * glView.getWidth();
        float right = left + width / (float) glView.getViewWidth() * glView.getWidth();
        float bottom = ((y + glViewModel.getY()) / (float) glView.getViewHeight()) * glView.getHeight();
        float top = bottom + height / (float) glView.getViewHeight() * glView.getHeight();

        left = left / glView.getWidth() * 2.0f - 1.0f;
        right = right / glView.getWidth() * 2.0f - 1.0f;
        bottom = bottom / glView.getHeight() * 2.0f - 1.0f;
        top = top / glView.getHeight() * 2.0f - 1.0f;

        Matrix.translateM(worldMatrix, 0, worldMatrix, 0, (left + right) * 0.5f, (bottom + top) * 0.5f, 0f);
        Matrix.rotateM(worldMatrix, 0, worldMatrix, 0, angle, 0, 0, 1f);
        Matrix.translateM(worldMatrix, 0, worldMatrix, 0, -(left + right) * 0.5f, -(bottom + top) * 0.5f, 0f);
    }

    public void setRepeatTex(boolean repeatTex) {
        this.repeatTex = repeatTex;
    }

    // 行列
    float[] worldMatrix = new float[16];

    public GlModel(GlViewModel glViewModel, String name){
        this.glViewModel = glViewModel;
        this.name = name;
        Matrix.setIdentityM(worldMatrix, 0);
    }

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    // テクスチャをセット
    public void setTexture(int id){
        this.imgId = id;

        loadTex = false;

    }

    // 外部画像をセット
    public void setOutsideBitmapTexture(Bitmap b){

        outImg = b;
        loadTex = false;
    }

    // テクスチャをロード
    public void loadTexture(GL10 gl) {

        // 一度読み込んだ画像を取得
        if (outImg == null) {
            bitmap = BitMapManager.getBitmap(imgId);
        } else {
            bitmap = outImg;
        }

        if (bitmap != null) {

            texX = 0;
            texY = bitmap.getHeight();
            texWidth = bitmap.getWidth();
            texHeight = -bitmap.getHeight();
            //x = 0;
            //y = 0;
            if (width == 0) width = bitmap.getWidth();
            if (height == 0) height = bitmap.getHeight();

            if (imgWidth == 0) imgWidth = bitmap.getWidth();
            if (imgHeight == 0) imgHeight = bitmap.getHeight();

            /*

            gl.glEnable(GL10.GL_ALPHA_TEST);
            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
            //テクスチャIDを割り当てる
            int[] textureID = new int[1];
            gl.glGenTextures(1, textureID, 0);
            textureNo = textureID[0];

            //テクスチャIDのバインド
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureNo);
            //OpenGL ES用のメモリ領域に画像データを渡す。上でバインドされたテクスチャIDと結び付けられる。
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            //テクスチャ座標が1.0fを超えたときの、テクスチャを繰り返す設定
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
            //テクスチャを元のサイズから拡大、縮小して使用したときの色の使い方を設定
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);

            */

            // GLES 2.0

            setupImage(bitmap);

            //頂点座標をバッファーに変換
            /*
            ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(vertices);
            vertexBuffer.position(0);*/

            //ここからシェーダーを使うっていう、定形
            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, ShaderSource.VERTEX_SHADER_CODE_TEX);
            int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderSource.FRAGMENT_SHADER_CODE_TEX);
            shaderProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(shaderProgram, vertexShader);
            GLES20.glAttachShader(shaderProgram, fragmentShader);
            GLES20.glLinkProgram(shaderProgram);

            loadTex = true;
        }
    }

    // テクスチャアンロード
    public void unLoadTex() {
        loadTex = false;
    }

    // 読み込み
    public void awake(){

    }

    // 初期処理
    public void startBefore(){}
    public abstract void start();

    // 事前更新
    public void updateBefore() {

    }

    // 更新
    public abstract void update();

    public void draw(final GlRenderer glRenderer) {

        if (loadTex == false) return;

        // verticesに座標を代入
        if (glView == null) glView = MainActivity.getGlView();
        float left = ((x + glViewModel.getX()) /  (float)glView.getViewWidth()) * glView.getWidth();
        float right = left + width / (float)glView.getViewWidth() * glView.getWidth();
        float bottom = ((y + glViewModel.getY()) / (float)glView.getViewHeight()) * glView.getHeight();
        float top = bottom + height / (float)glView.getViewHeight() * glView.getHeight();

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

        //画像の方の座標
        final float uvs[] = new float[] {
                u2, 1.0f-v2,
                u2, 1.0f-v1,
                u1, 1.0f-v1,
                u1, 1.0f-v2
        };

        // Vertex
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // UV
        //画像側の頂点座標をバッファーに変換
        bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        // 拡大縮小の時のフィルターの設定
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

        // テクスチャの繰り返し設定
        if (repeatTex) {
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        } else {
            //GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            //GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        }

        GLES20.glUseProgram(shaderProgram);
        // シェーダーの準備(図形側)
        int mPositionHandle = GLES20.glGetAttribLocation(shaderProgram, "vPosition");
        // シェーダー:ON
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //シェーダーの準備（テクスチャ側）
        int mTexCoordLoc = GLES20.glGetAttribLocation(shaderProgram,"a_texCoord");
        // シェーダー:ON
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        // 行列
        int matLoc1 = GLES20.glGetUniformLocation(shaderProgram, "wMatrix");

        // 平行移動
        //Matrix.translateM(worldMatrix, 0, worldMatrix, 0, (left+right)*0.5f, (top+bottom)*0.5f, 0f);

        // 拡大
        //Matrix.scaleM(worldMatrix, 0, worldMatrix, 0, 1.0f, 1.0f, 1f);

        // 回転
        //Matrix.rotateM(worldMatrix, 0, worldMatrix, 0, angle, 0, 0, 1f);

        // 平行移動
        //Matrix.translateM(worldMatrix, 0, worldMatrix, 0, -(left+right)*0.5f, -(top+bottom)*0.5f, 0f);

        // 回転
        //Matrix.setRotateM(worldMatrix, 0, getCnt()*0.5f, 0, 0, 1f);

        // 平行移動
        //Matrix.translateM(worldMatrix, 0, -(left+right)*0.5f, -(top+bottom)*0.5f, 0f);
        // 拡大
        //Matrix.scaleM(worldMatrix, 0, worldMatrix, 0, 1.2f, f, 1f);
        // 回転
        //Matrix.rotateM(worldMatrix, 0, worldMatrix, 0,  getCnt()*0.5f, 0, 0, 1f);
        // 平行移動
        //Matrix.translateM(worldMatrix, 0, worldMatrix, 0, (left+right)*0.5f, (top+bottom)*0.5f, 0f);



        GLES20.glUniformMatrix4fv(matLoc1, 1, false, worldMatrix, 0);

        //////////////////////////////////START
        //vertexBufferをデータ列から頂点データという解釈に変換
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        //uvBufferをデータ列から頂点データという解釈に変換
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);

        // 透明度
        GLES20.glUniform1f(GLES20.glGetUniformLocation(shaderProgram, "Opacity"), alpha);

        // 画像の明るさ
        GLES20.glUniform1f(GLES20.glGetUniformLocation(shaderProgram, "Brightness"), bright);

        // 描画に利用をする画像のデータをする
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNo);

        // 描画する。何で描くのかは、「関数内で登録してある」という暗黙の了解的な。
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

        //////////////////////////////////END
        // シェーダー:OFF
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

        /* 1.0

        gl.glDisable(GL10.GL_DEPTH_TEST);
        // 背景色を白色で塗りつぶし
        gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
        // テクスチャ0番をアクティブにする
        gl.glActiveTexture(GL10.GL_TEXTURE0);
        // テクスチャIDに対応するテクスチャをバインドする
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureNo);
        //テクスチャの座標と幅と高さを指定
        int rect[] = {(int)(imgWidth * u1), (int)(imgHeight * (1.0f - v1)), (int)(imgWidth * (u2 - u1))-1, -(int)(imgHeight * (v2 - v1))+1};
        // int rect[] = {texX, texY, texWidth, texHeight};
        // テクスチャ画像のどの部分を使うかを指定
        ((GL11)gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);

        if (glView == null) glView = MainActivity.getGlView();
        // 描画
        ((GL11Ext)gl).glDrawTexfOES(
                ((x + glViewModel.getX()) /  (float)glView.getViewWidth()) * glView.getWidth(),
                ((y + glViewModel.getY()) / (float)glView.getViewHeight()) * glView.getHeight(),
                0,
                width / (float)glView.getViewWidth() * glView.getWidth(),
                height / (float)glView.getViewHeight() * glView.getHeight());

        gl.glEnable(GL10.GL_DEPTH_TEST);
        */
    }

    // 削除
    public void delete(){
        glViewModel.deleteModel(this);
    }

    // タップイベント
    public boolean onTouchEvent(MotionEvent ev) {

        return true;
    }

    // タップしたところのX座標を取得
    public float getTouchX(MotionEvent ev) {
        // タッチした座標を取得
        float tx = ev.getX() * glView.getViewWidth() / glView.getWidth();

        return tx;
    }

    // タップしたところのX座標を取得
    public float getTouchY(MotionEvent ev) {
        // タッチした座標を取得
        float ty = ev.getY() * glView.getViewHeight() / glView.getHeight();

        // y軸だけ座標系が違う
        ty = MainActivity.getGlView().getViewHeight() - ty;

        return ty;
    }

    // カウントアップ
    public final void addCnt(){
        cnt++;
    }

    public final int getCnt(){
        return cnt;
    }

    public final void setVisible(boolean visible) {
        this.visible = visible;
    }

    public final boolean isVisible() {
        return visible;
    }

    public final String getName() {
        return name;
    }

    public final void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public final float getX() {
        return x;
    }

    public final float getY() {
        return y;
    }

    public final float getWidth(){
        return width;
    }

    public final float getHeight(){
        return height;
    }

    public final void setSize(float w, float h) {
        width = w;
        height = h;
    }

    public final void setX(float x) {
        this.x = x;
    }

    public final void setY(float y) {
        this.y = y;
    }

    public final float getU1() {
        return u1;
    }

    public final void setU1(float u1) {
        this.u1 = u1;
    }

    public final float getV1() {
        return v1;
    }

    public final void setV1(float v1) {
        this.v1 = v1;
    }

    public final float getU2() {
        return u2;
    }

    public final void setU2(float u2) {
        this.u2 = u2;
    }

    public final float getV2() {
        return v2;
    }

    public final void setV2(float v2) {
        this.v2 = v2;
    }

    public final void setUV(float u1, float v1, float u2, float v2){
        this.u1 = u1; this.v1 = v1;
        this.u2 = u2; this.v2 = v2;
    }

    public final void setTexPosition(int texX, int texY, int texWidth, int texHeight) {
        this.texX = texX;
        this.texY = texY;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
    }

    public final void setTexX(int texX) {
        this.texX = texX;

    }
    public final void setTexY(int texY) {
        this.texY = texY;

    }

    public final void setTexWidth(int texWidth) {
        this.texWidth = texWidth;

    }

    public final void setTexHeight(int texHeight) {
        this.texHeight = texHeight;
    }

    public final void setLoadTex(boolean loadTex) {
        this.loadTex = loadTex;
    }

    public final int getTexX() {
        return texX;
    }

    public final int getTexY() {
        return texY;
    }

    public final int getTexWidth() {
        return texWidth;
    }

    public final int getTexHeight() {
        return texHeight;
    }

    public final String getTag() {
        return tag;
    }

    public final void setTag(String tag) {
        glViewModel.changeTag(this, this.tag, tag);
        this.tag = tag;

    }

    int[] texture;
    private void setupImage(Bitmap bitmap){

        texture = new int[1];
        //Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.getContext().getResources(), R.drawable.chino_menu);

        // Bind texture to texturename
        GLES20.glGenTextures(1,texture,0);
        //GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);

        // 画像をテクスチャに登録
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        //bitmap.recycle();

        // テクスチャNo
        textureNo = texture[0];
        //Log.e("CHECLK", "ID:" + texture[0]);
        //Log.e("CHECLK", "GLES20.GL_TEXTURE2:" + GLES20.GL_TEXTURE2);
    }

    // 拡大、縮小を行います。
    public void setGlScale(float sx, float sy) {

        // verticesに座標を代入
        if (glView == null) glView = MainActivity.getGlView();
        float left = ((x + glViewModel.getX()) / (float) glView.getViewWidth()) * glView.getWidth();
        float right = left + width / (float) glView.getViewWidth() * glView.getWidth();
        float bottom = ((y + glViewModel.getY()) / (float) glView.getViewHeight()) * glView.getHeight();
        float top = bottom + height / (float) glView.getViewHeight() * glView.getHeight();

        left = left / glView.getWidth() * 2.0f - 1.0f;
        right = right / glView.getWidth() * 2.0f - 1.0f;
        bottom = bottom / glView.getHeight() * 2.0f - 1.0f;
        top = top / glView.getHeight() * 2.0f - 1.0f;

        Matrix.translateM(worldMatrix, 0, worldMatrix, 0, (left + right) * 0.5f, (bottom + top) * 0.5f, 0f);
        Matrix.scaleM(worldMatrix, 0, worldMatrix, 0, sx, sy, 1f);
        Matrix.translateM(worldMatrix, 0, worldMatrix, 0, -(left + right) * 0.5f, -(bottom + top) * 0.5f, 0f);
    }

}

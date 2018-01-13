package com.example.tbichan.syaroescape.opengl.textures;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * テクスチャ保管用
 * Created by tbichan on 2018/01/13.
 */

public class TextureManager {

    private static TextureManager instance = new TextureManager();

    public static TextureManager getInstance() {
        return instance;
    }

    // 画像とテクスチャIDを紐図けるマップ
    private HashMap<Bitmap, int[]> texIdMap = new HashMap<>();


    private TextureManager(){

    }

    // bitmapから読み込み
    public int loadTexture(Bitmap bitmap) {
        int[] texture = new int[1];

        // テクスチャNo
        int textureNo = 0;

        // Bind texture to texturename
        GLES20.glGenTextures(1, texture, 0);
        //GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);

        // 画像をテクスチャに登録
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        textureNo = texture[0];

        // マップに追加
        texIdMap.put(bitmap, texture);

        bitmap.recycle();

        Log.d("draw", textureNo+"");

        return textureNo;
    }

    /**
     * テクスチャID取得、なければ-1
     */
    public int getTextureId(Bitmap bitmap) {
        if (!texIdMap.containsKey(bitmap)) return -1;

        return texIdMap.get(bitmap)[0];
    }

    /**
     * テクスチャ削除
     */
    public void deleteAll() {

        for (Bitmap bitmap: texIdMap.keySet()) {
            deleteTextureId(bitmap);
        }
    }

    /**
     * テクスチャ削除
     */
    public void deleteTextureId(Bitmap bitmap) {

        int[] textures = texIdMap.get(bitmap);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glDeleteTextures(1, textures, 0);
    }
}

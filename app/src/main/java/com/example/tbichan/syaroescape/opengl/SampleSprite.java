package com.example.tbichan.syaroescape.opengl;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class SampleSprite {
    //テクスチャNo
    public int textureNo;
    //表示位置
    public float  pos_x;
    public float  pos_y;
    public float  pos_z;
    //テクスチャ（画像）の位置とサイズ
    int    texX;
    int    texY;
    int    texWidth;
    int    texHeight;
    //配置する時の幅と高さ
    float  width;
    float  height;

    public void setTexture( GL10 gl, Resources res, int id ){
        InputStream is = res.openRawResource(id);
        Bitmap bitmap;
        try{
            bitmap = BitmapFactory.decodeStream(is);
        }
        finally{
            try{
                is.close();
            }
            catch(IOException e){   }
        }
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
        gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT );
        gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT );
        //テクスチャを元のサイズから拡大、縮小して使用したときの色の使い方を設定
        gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
        gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );

        texX      = 0;
        texY      = bitmap.getHeight();
        texWidth  = bitmap.getWidth();
        texHeight = -bitmap.getHeight();
        pos_x     = 0;
        pos_y     = 0;
        pos_z     = 0;
        width     = bitmap.getWidth();
        height    = bitmap.getHeight();
    }

    public void draw(GL10 gl){
        gl.glDisable(GL10.GL_DEPTH_TEST);
        //背景色を白色で塗りつぶし
        gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
        //テクスチャ0番をアクティブにする
        gl.glActiveTexture(GL10.GL_TEXTURE0);
        //テクスチャIDに対応するテクスチャをバインドする
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureNo);
        //テクスチャの座標と幅と高さを指定
        int rect[] = { texX,  texY,  texWidth, texHeight};
        //テクスチャ画像のどの部分を使うかを指定
        ((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D,GL11Ext.GL_TEXTURE_CROP_RECT_OES, rect, 0);
        //描画
        ((GL11Ext) gl).glDrawTexfOES( pos_x, pos_y, pos_z, width, height);

        gl.glEnable(GL10.GL_DEPTH_TEST);
    }
}

package com.example.tbichan.syaroescape.opengl.bitmapnmanager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import com.example.tbichan.syaroescape.activity.MainActivity;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by tbichan on 2017/12/07.
 */

public class BitMapManager {

    // インスタンス
    private static BitMapManager instance = new BitMapManager();

    public static BitMapManager getInstance() {
        return instance;
    }

    private BitMapManager(){

    }

    // 画像のリスト
    private HashMap<Integer, Bitmap> bitHashMap = new HashMap<>();

    // 文字型画像のリスト
    private HashMap<String, Bitmap> stringBitHashMap = new HashMap<>();

    // 外部画像個数
    private static int outBitMapNum = -1;

    public void addBitMap(int key, Bitmap bitmap){
        bitHashMap.put(key, bitmap);
    }

    public Bitmap getBitmap(int key) {
        return bitHashMap.get(key);
    }

    public void addStringBitMap(String key, Bitmap bitmap){
        stringBitHashMap.put(key, bitmap);
    }

    public Bitmap getStringBitmap(String key) {
        return stringBitHashMap.get(key);
    }

    public boolean isBitmap(int key) {
        return bitHashMap.containsKey(key);
    }

    public boolean isStringBitmap(String key) {
        return stringBitHashMap.containsKey(key);
    }

    // 文字列の画像を作成します。
    public static Bitmap createStrImage(String str, int fontSize, int width, int height, int color) {

        // ①文字列の情報を取得
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        // フォントをよみこむ
        try {
            Typeface typeface = Typeface.createFromAsset(
                    MainActivity.getContext().getAssets(), "uzura.ttf");
            paint.setTypeface( typeface );
        } catch( Exception e ) {
            paint.setTypeface( Typeface.DEFAULT );
            Log.e("font", "error");
        }

        paint.setTextSize(fontSize);

        Paint.FontMetrics fm = paint.getFontMetrics();

        // ②文字列が収まる大きさのBitmapを生成
        Bitmap bitmap = Bitmap.createBitmap(
                width, height, Bitmap.Config.ARGB_4444);

        // ③生成したBitmapにひも付けたCanvasを用意
        Canvas canvas = new Canvas(bitmap);

        // ④CanvasのdrawText()で、Bitmapに文字列を描画
        String[] lines = str.split("\n");
        for (int i = 0; i < lines.length; i++) {

            canvas.drawText(lines[i], 0, (i + 1) * -fm.top, paint);
        }

        instance.addBitMap(outBitMapNum, bitmap);

        outBitMapNum--;

        // ⑤Bitmapデータを返す
        return bitmap;
    }

    // 文字列の画像を作成します。
    public static Bitmap createStrImage(String str, int fontSize, int color) {

        // ①文字列の情報を取得
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        // フォントをよみこむ
        paint.setTypeface( Typeface.DEFAULT );

        paint.setTextSize(fontSize);

        Paint.FontMetrics fm = paint.getFontMetrics();

        int width = (int)paint.measureText(str);
        int height = (int)(-fm.top + fm.bottom);

        // ②文字列が収まる大きさのBitmapを生成
        Bitmap bitmap = Bitmap.createBitmap(
                width, height, Bitmap.Config.ARGB_4444);

        // ③生成したBitmapにひも付けたCanvasを用意
        Canvas canvas = new Canvas(bitmap);

        // ④CanvasのdrawText()で、Bitmapに文字列を描画
        canvas.drawText(str, 0, -fm.top, paint);

        // 画像サイズを2の累乗にする。
        int tmpW = 1;
        int tmpH = 1;

        for (; width >= tmpW; tmpW *= 2) {
        }
        for (; height >= tmpH; tmpH *= 2) {
        }

        Matrix matrix = new Matrix();
        // 比率をMatrixに設定
        matrix.postScale((float) tmpW / width, (float)(tmpH) / height);

        // リサイズ画像
        Bitmap bmpRsz = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        instance.addBitMap(outBitMapNum, bitmap);
        outBitMapNum--;

        // ⑤Bitmapデータを返す
        return bmpRsz;
    }

    // 文字列の画像を作成します。
    public static Bitmap createStrImage(String str, String fontName, int fontSize, int color) {

        // ①文字列の情報を取得
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

        // フォントをよみこむ
        try {
            Typeface typeface = Typeface.createFromAsset(
                    MainActivity.getContext().getAssets(), fontName.replace(".ttf", "") + ".ttf");
            paint.setTypeface( typeface );
        } catch( Exception e ) {
            paint.setTypeface( Typeface.DEFAULT );
        }
        paint.setTextSize(fontSize);

        Paint.FontMetrics fm = paint.getFontMetrics();

        int width = (int)paint.measureText(str);
        int height = (int)(-fm.top + fm.bottom);

        // ②文字列が収まる大きさのBitmapを生成
        Bitmap bitmap = Bitmap.createBitmap(
                width, height, Bitmap.Config.ARGB_4444);

        // ③生成したBitmapにひも付けたCanvasを用意
        Canvas canvas = new Canvas(bitmap);

        // ④CanvasのdrawText()で、Bitmapに文字列を描画
        canvas.drawText(str, 0, -fm.top, paint);

        // 画像サイズを2の累乗にする。
        int tmpW = 1;
        int tmpH = 1;

        for (; width >= tmpW; tmpW *= 2) {
        }
        for (; height >= tmpH; tmpH *= 2) {
        }

        Matrix matrix = new Matrix();
        // 比率をMatrixに設定
        matrix.postScale((float) tmpW / width, (float)(tmpH) / height);

        // リサイズ画像
        Bitmap bmpRsz = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        instance.addBitMap(outBitMapNum, bitmap);
        outBitMapNum--;

        // ⑤Bitmapデータを返す
        return bmpRsz;
    }

    // 画像をアンロードします。
    public void recycleAll() {

        final Set<Integer> keySet = bitHashMap.keySet();
        final Set<String> keySetString = stringBitHashMap.keySet();


        // 画像をアンロード
        try {
            for (int key : keySet) {
                Bitmap bitmap = bitHashMap.get(key);

                if (bitmap != null) {

                    bitmap.recycle();
                    bitmap = null;
                }
            }

            for (String key : keySetString) {
                Bitmap bitmap = stringBitHashMap.get(key);

                if (bitmap != null) {
                    bitmap.recycle();
                    bitmap = null;
                }
            }
        } catch(ConcurrentModificationException e) {
            Log.e("waring", "Bitmap recycle failed!!");
        }

        bitHashMap.clear();
        stringBitHashMap.clear();
    }
}

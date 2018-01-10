package com.example.tbichan.syaroescape.sound;

import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.example.tbichan.syaroescape.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tbichan on 2018/01/10.
 */

public class SEManager {

    // インスタンス
    private static SEManager instance = new SEManager();

    public static SEManager getInstance() {
        return instance;
    }

    // 効果音用
    private SoundPool soundPool = null;

    // 効果音IDリスト
    private HashMap<Integer, Integer> seMap = new HashMap<>();

    // 読み込み終了フラグ
    private boolean load = false;

    private SEManager() {

    }

    // 読み込み完了効果音数
    private int loadSe = 0;

    public void reset() {
        if (soundPool != null) soundPool.release();
        seMap.clear();
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        loadSe = 0;
        load = false;
    }

    /**
     * 効果音をすべてロードします。
     */
    public void loadAll(final ArrayList<Integer> idList) {

        reset();

        final int seNum = idList.size();

        if (seNum == 0) {
            loadEndAll();
        }

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

                loadSe++;

                if (loadSe == seNum) loadEndAll();
                /*
                if (0 == status) {
                    soundPool.play(sampleId,1.0F, 1.0F, 0, 0, 1.0F);
                }*/
            }
        });

        for (int resId: idList) {

            int seId = soundPool.load(MainActivity.getContext().getApplicationContext(), resId, 1);
            seMap.put(resId, seId);
        }
    }

    /**
     * 効果音を読み込み終えた時の処理
     */
    public void loadEndAll() {
        load = true;
        Log.d("sound","ok");

    }


    /**
     * 効果音を再生します。
     */
    public void playSE(int resId) {
        int seId = seMap.get(resId);
        while (!load) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
        }
        soundPool.play(seId, 1.0f, 1.0f, 0, 0, 1.0f);

    }
}

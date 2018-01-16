package com.example.tbichan.syaroescape.sound;

import java.util.ArrayList;
import java.util.HashSet;

import android.media.MediaPlayer;
/**
 * 音再生クラスです。
 * @author 5515012o
 *
 */
public class BGMManager {

	private static BGMManager instance = new BGMManager();
	
	public static BGMManager getInstance() {
		return instance;
	}
	
	/**
	 * 効果音リスト
	 */
	private HashSet<MediaPlayer> seList = new HashSet<MediaPlayer>();
	
	/**
	 * 効果音作成待リスト
	 */
	private ArrayList<MyBGM> seWaitList = new ArrayList<>();
	
	private BGMManager() {
		
	}
	
	/**
	 * 効果音リストに追加します。
	 */
	public void addSE(MyBGM mySE) {
		seWaitList.add(mySE);
	}
	
	/**
	 * 再生待ちのMySEを更新します。
	 */
	public void update() {
		for (int i = 0; i < seWaitList.size(); i++) {
			MyBGM se = seWaitList.get(i);
			if (!se.update()) {
				// 削除
				se.release();
				seWaitList.remove(i);
				i--;
			}
		}
	}

    /**
     * 再生中のBGMをすべて解放します。
     */
    public void releaseAll() {
        for (int i = 0; i < seWaitList.size(); i++) {
            MyBGM se = seWaitList.get(i);

            // 削除
            se.release();
        }

        seWaitList.clear();
    }

	/**
	 * 再生中のBGMを止めます
	 */
	public void pause() {

		for (int i = 0; i < seWaitList.size(); i++) {
			MyBGM se = seWaitList.get(i);

			// 削除
			se.pause();
		}
	}

	/**
	 * とまっているBGMを再生させます。
	 */
	public void resume() {

		for (int i = 0; i < seWaitList.size(); i++) {
			MyBGM se = seWaitList.get(i);

			// 削除
			se.resume();
		}
	}

}

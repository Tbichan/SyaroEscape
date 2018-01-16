package com.example.tbichan.syaroescape.sound;

import android.media.MediaPlayer;
import android.util.Log;

public class MyBGM implements MediaPlayer.OnCompletionListener {
	
	// 音楽データ
	private MediaPlayer player = null;
	
	// 再生されているか
	private boolean nowPlaying = false;

	// ループ再生かどうか
	private boolean loop = true;
	
	// 終了したかどうか
	private boolean endFlg = false;
	
	public MyBGM(MediaPlayer player) {
		this.player = player;
	}
	
	public boolean update() {
		
		if (endFlg) return false;
		
		if (!nowPlaying) {
			player.setOnCompletionListener(this);
			player.seekTo(0);
			player.start();
			nowPlaying = true;

		}
		
		return true;
	}
	
	/**
	 * 再生終了時に呼ばれます。
	 */
	public final void onCompletion(MediaPlayer mediaPlayer) {
		
		// ループ有効かどうか
		if (loop) {
			player.seekTo(0);
			player.start();
		} else {

			endSound();
			endFlg = true;
		}
	}
	
	/**
	 * 再生終了時に呼ばれます。
	 */
	public void endSound() {
		
	}

	/**
	 * 一時停止を行います。
	 */
	public void pause() {
		if (player != null) player.pause();
	}

	/**
	 * 一時停止を解除します。
	 */
	public void resume() {
		if (player != null) player.start();
	}
	
	/**
	 * 開放
	 */
	public final void release() {
		player.release();
		player = null;
		Log.d("sound", "release");
	}


	public void setLoop(boolean loop) {
		this.loop = loop;
	}
}

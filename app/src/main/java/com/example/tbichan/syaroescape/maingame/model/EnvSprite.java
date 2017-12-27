package com.example.tbichan.syaroescape.maingame.model;


import android.util.Log;

import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentViewModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

import java.util.ArrayList;

public class EnvSprite extends GlButton {

	// 幅、高さ
	public static final float WIDTH = GlView.VIEW_WIDTH / 16.0f;
	public static final float HEIGHT = GlView.VIEW_WIDTH / 16.0f;
	
	// 環境VM
	private EnvironmentViewModel environmentViewModel;

	// 直前の位置
	private ArrayList<Integer> nextMapXList = new ArrayList<>();
	private ArrayList<Integer> nextMapYList = new ArrayList<>();
	private int preX, preY;
	
	// マップ上の座標
	private int mapX, mapY;

	// インターバル
	private int interval = 30;
	private ArrayList<Integer> nextIntervalList = new ArrayList<>();

	// 移動開始カウンタ
	private int startMoveCnt = -1;

	// 消失インターバル
	private int hideInterval = 30;
	private int startHideCnt = -1;

	public EnvSprite(GlViewModel glViewModel, String name) {
		super(glViewModel, name);

	}
	
	/**
	 * 環境を追加
	 */
	public void setEnviromentViewModel(EnvironmentViewModel environmentViewModel) {
		this.environmentViewModel = environmentViewModel;
	}
	
	/**
	 * 環境を取得
	 */
	protected EnvironmentViewModel getEnvironmentViewModel() {
		return environmentViewModel;
	}

	@Override
	public void start() {
		// TODO 自動生成されたメソッド・スタブ
		setSize(WIDTH, HEIGHT);

	}

	@Override
	public void update() {
		// 移動中ならば更新
		if (isMove()) {

			// 移動してからの時間(0～1)
			float time = ((float)getCnt() - startMoveCnt) / interval;
			if (time < 1) {

				float hogeMapX = (1 - time) * preX + time * mapX;
				float hogeMapY = (1 - time) * preY + time * mapY;

				setPosition(WIDTH * hogeMapX, GlView.VIEW_HEIGHT - (hogeMapY + 1) * HEIGHT);
			} else {
				// 移動終了
				setPosition(WIDTH * mapX, GlView.VIEW_HEIGHT - (mapY + 1) * HEIGHT);
				startMoveCnt = -1;
				endMove();

				// リストにあれば再度移動開始
				if (nextMapXList.size() > 0) {
					int nextMapX = nextMapXList.get(0); nextMapXList.remove(0);
					int nextMapY = nextMapYList.get(0); nextMapYList.remove(0);
					int nextInterval = nextIntervalList.get(0); nextIntervalList.remove(0);
					move(nextMapY, nextMapX, nextInterval);
				}
			}
		}

		// 消失時
		if (startHideCnt != -1) {
			if (getCnt() - startHideCnt >= hideInterval) {
				setVisible(true);
				startHideCnt = -1;

			}
		}

	}

	// 移動します。
	public void move(int mapY, int mapX) {
		preX = this.mapX;
		preY = this.mapY;
		this.mapX = mapX;
		this.mapY = mapY;
		setPosition(WIDTH * mapX, GlView.VIEW_HEIGHT - (mapY + 1) * HEIGHT);
	}

	/**
	 * 時間をかけて移動します。
	 */
	public void move(int mapY, int mapX, int interval) {
		if (!isMove()) {

			preX = this.mapX;
			preY = this.mapY;
			this.mapX = mapX;
			this.mapY = mapY;
			this.interval = interval;
			startMoveCnt = getCnt();
		}
		// 移動中ならば保留
		else {
			nextMapXList.add(mapX);
			nextMapYList.add(mapY);
			nextIntervalList.add(interval);

		}
	}

	public static float parseX(int index) {
		return WIDTH * (index % Environment.MAP_SIZE);
	}

	public static float parseY(int index) {
		return GlView.VIEW_HEIGHT - ((index / Environment.MAP_SIZE) + 1) * HEIGHT;
	}
	
	public final int getMapX() {
		return mapX;
	}
	
	public final int getMapY() {
		return mapY;
	}

	public final int getMapIndex() {
		return mapY * Environment.MAP_SIZE + mapX;
	}

	public final boolean isMove() {
		return startMoveCnt != -1;
	}

	public final void hide(int hideInterval) {
		setVisible(false);
		this.hideInterval = hideInterval;
		startHideCnt = getCnt();
	}

	public void endMove() {

	}

	@Override
	public void onClick() {
		
		
	}
	
	@Override
    public void onTex() {
    	
    }

	@Override
    public void upTex() {
        
    }

}

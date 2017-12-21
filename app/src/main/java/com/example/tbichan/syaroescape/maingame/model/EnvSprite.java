package com.example.tbichan.syaroescape.maingame.model;


import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentViewModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

public class EnvSprite extends GlButton {

	// 幅、高さ
	public static final float WIDTH = GlView.VIEW_WIDTH / 16.0f;
	public static final float HEIGHT = GlView.VIEW_WIDTH / 16.0f;
	
	// 環境VM
	private EnvironmentViewModel environmentViewModel;
	
	// マップ上の座標
	private int mapX, mapY;

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
		// TODO 自動生成されたメソッド・スタブ

	}

	// 移動します。
	public void move(int mapY, int mapX) {
		this.mapX = mapX;
		this.mapY = mapY;
		setPosition(WIDTH * mapX, GlView.VIEW_HEIGHT - (mapY + 1) * HEIGHT);
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

package com.example.tbichan.syaroescape.maingame.model;

import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

public class EnableFloor extends EnvSprite {

	// プレイヤー
	private Player player;

	public EnableFloor(GlViewModel glViewModel, String name) {
		super(glViewModel, name);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}
	
	@Override
    public void update() {
    	setAlpha(0.15f * ((float)Math.sin(getCnt()*0.08f)+1.0f)+0.25f);
    }
	
	@Override
	public void onClick() {
		getEnvironmentViewModel().notify(this);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}

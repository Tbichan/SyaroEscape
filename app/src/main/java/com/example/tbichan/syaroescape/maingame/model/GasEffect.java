package com.example.tbichan.syaroescape.maingame.model;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * エフェクト
 */
public class GasEffect extends GlModel {

	// 出現時カウンタ
	private int actCnt = -1;

	// 枚数
	private final int NUM = 8;

	public GasEffect(GlViewModel glViewModel, String name) {
		super(glViewModel, name);
	}

	@Override
	public void awake() {
		setTextureId(R.drawable.gas);
		setVisible(false);
	}

	@Override
	public void start() {
		setSize(EnvSprite.WIDTH, EnvSprite.HEIGHT);
		anim(0);
	}
	
	@Override
    public void update() {
		if (actCnt != -1) {
			int t = ((getCnt() - actCnt) / 2) % NUM;
			anim(t);

		}
    }


    public void appear() {
		anim(0);
		setVisible(true);
		actCnt = getCnt();
	}

	private void anim(int t) {
		int tx = t % 8;

		// UV
		setU1(tx / 8.0f);
		setU2((tx + 1) / 8.0f);
	}
}

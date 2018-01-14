package com.example.tbichan.syaroescape.maingame.model;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * エフェクト
 */
public class PowerEffect extends GlModel {

	// 出現時カウンタ
	private int actCnt = -1;

	// 枚数
	private final int NUM = 15;

	public PowerEffect(GlViewModel glViewModel, String name) {
		super(glViewModel, name);
	}

	@Override
	public void awake() {
		setTextureId(R.drawable.powerup);
		setVisible(false);
	}

	@Override
	public void start() {
		setSize(EnvSprite.WIDTH, EnvSprite.HEIGHT * 1.5f);
		anim(0);
	}
	
	@Override
    public void update() {
		if (actCnt != -1) {
			int t = (getCnt() - actCnt) / 1;
			anim(t);
			if (t >= NUM) {
				setVisible(false);
			}

		}
    }


    public void appear() {
		anim(0);
		setVisible(true);
		actCnt = getCnt();
	}

	private void anim(int t) {
		int tx = t % 5;
		int ty = t / 5;

		// UV
		setU1(tx / 5.0f);
		setU2((tx + 1) / 5.0f);
		setV1(1.0f - (ty + 1) / 3.0f);
		setV2(1.0f - ty / 3.0f);
	}
}

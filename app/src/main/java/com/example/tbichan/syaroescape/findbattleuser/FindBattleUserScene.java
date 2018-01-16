package com.example.tbichan.syaroescape.findbattleuser;


import android.content.Context;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.viewmodel.NowLoadViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.ParticleViewModel;
import com.example.tbichan.syaroescape.findbattleuser.viewmodel.BGViewModel;
import com.example.tbichan.syaroescape.findbattleuser.viewmodel.TrampViewModel;
import com.example.tbichan.syaroescape.findbattleuser.viewmodel.ViewModel3D;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;


public class FindBattleUserScene extends SceneBase {

	// パーティクル
	ParticleViewModel particleViewModel;

	@Override
	public void load(GlView glView) {

		addBitmap(R.drawable.load_bg);
		addBitmap(R.drawable.load_str);
		addBitmap(R.drawable.bar_frame);
		addBitmap(R.drawable.bar);
		addBitmap(R.drawable.particle);

		addBitmap(R.drawable.tushin);
		addBitmap(R.drawable.tushin_err);
		addBitmap(R.drawable.trump);

		NowLoadViewModel nowLoadViewModel = new NowLoadViewModel(glView, this, "NowLoadViewModel");
		nowLoadViewModel.setSceneImgLoadedDraw(false);

		particleViewModel = new ParticleViewModel(glView, this, "ParticleModel");
		particleViewModel.setSceneImgLoadedDraw(false);

		glView.addViewModel(nowLoadViewModel);
		glView.addViewModel(particleViewModel);
	}

	@Override
	public void update() {


	}

	// 画像読み込み終了時の処理
	public void imgLoadEnd(GlView glView) {
		super.imgLoadEnd(glView);

		BGViewModel bgViewModel = new BGViewModel(glView, this, "BGViewModel");

		glView.addViewModel(bgViewModel);
		glView.addViewModel(new TrampViewModel(glView, this, "TrampViewModel"));

		// パーティクルを最前面に
		glView.moveFrontViewModel(particleViewModel);

	}

}

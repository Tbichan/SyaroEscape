package com.example.tbichan.syaroescape.findbattleuser.viewmodel;

import android.util.Log;
import android.view.View;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.maingame.NetworkGameScene;
import com.example.tbichan.syaroescape.menu.MenuScene;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.store.StoreManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.sqlite.DataBaseHelper;
import com.example.tbichan.syaroescape.ui.UIListener;

public class TrampViewModel extends GlViewModel {

	GlModel bgStrErr;

	public TrampViewModel(GlView glView, SceneBase sceneBase, String name) {
		super(glView, sceneBase, name);

	}

	@Override
	public void awake() {

		// 表示する文字
		GlModel bgStr = new GlModel(this, "bgStr_wait") {

			private final float MAX_WIDTH = 432f;

			private float initX;

			@Override
			public void start() {
				initX = getX();


			}

			@Override
			public void update() {

				float spd = (float)(Math.sin(getCnt() / 200f) * 12.6f * Math.PI);

				float nextWidth = (float)(Math.cos(spd) * MAX_WIDTH);

				setX(initX + MAX_WIDTH * 0.5f - nextWidth * 0.5f);
				setSize(nextWidth, getHeight());

				if (nextWidth > 0) {
					setU1(0.0f);
					setU2(0.5f);
				} else {
					setU1(0.5f);
					setU2(1.0f);
				}
			}
		};

		bgStr.setTextureId(R.drawable.trump);
		bgStr.setSize(432f, 700f);

		// ���S�ɏo��
		bgStr.setPosition(GlView.VIEW_WIDTH*0.5f-bgStr.getWidth() * 0.5f, GlView.VIEW_HEIGHT*0.5f-bgStr.getHeight() * 0.5f);

		addModel(bgStr);
	}

	@Override
	public void start() {

	}

}

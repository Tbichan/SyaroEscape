package com.example.tbichan.syaroescape.experience.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.tbichan.syaroescape.experience.model.TextModel;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

import javax.microedition.khronos.opengles.GL10;

public class MatsuokaViewModel extends GlViewModel {

	TextModel textModel2;
	float win = 0.5f;


	public MatsuokaViewModel(GlView glView, SceneBase sceneBase, String name) {
		super(glView, sceneBase, name);

	}

	@Override
	public void awake() {

		//MatsuokaModel matsuokaModel = new MatsuokaModel(this,"matsuoka");


		//matsuokaModel.setPosition(0, 500);
		//matsuokaModel.setText

		//addModel(matsuokaModel);
		TextModel textModel = new TextModel(this, "test");
		textModel2 = new TextModel(this, "text");
		textModel2.setPosition(0, 1000);
		textModel.setPosition(0, 500);
		textModel.setText("プレイヤー名：きやま");
		textModel2.setText("CPUとの勝率：" + win * 100 + "％");
		addModel(textModel);
		addModel(textModel2);

	}

	@Override
	public void start() {


	}

	@Override
	public void update(GL10 gl){
		super.update(gl);


		if (getCnt()%60==0) {
			win = (float)Math.random();
			//textModel2.setText("CPUとの勝率：" + (int)(win * 100) + "％");
		}
	}

}

package com.example.tbichan.syaroescape.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * ビューの基底クラス
 */

public abstract class GlViewBase extends GLSurfaceView {

    // レンダラー
    protected GlRendererBase mRenderer;

    public GlViewBase(Context context) {
        super(context);
        // レンダラー作成
        mRenderer = createRenderer(context);
        //renderer = new GlRendererBase(context);

        // 作成したレンダラーをセット
        setRenderer(mRenderer);
    }

    // レンダラー作成
    protected abstract GlRendererBase createRenderer(Context context);

}

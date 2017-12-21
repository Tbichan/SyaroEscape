package com.example.tbichan.syaroescape.opengl;

import android.content.Context;

public class SampleGlView extends GlViewBase {


    public SampleGlView(Context context) {
        super(context);

    }

    // レンダラー作成
    @Override
    protected GlRendererBase createRenderer(Context context) {
        SampleRenderer sampleRenderer = new SampleRenderer(context);

        return sampleRenderer;
    }

}

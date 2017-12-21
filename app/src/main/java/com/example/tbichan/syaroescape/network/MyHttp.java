package com.example.tbichan.syaroescape.network;

public abstract class MyHttp {
	
	// スレッド
	private Thread t;
	
	// Runnable
	private MyHttpRunnable r;
	
	/**
	 * コンストラクタです。
	 */
	public MyHttp(String url, String... params) {
		
		r = new MyHttpRunnable(this, url, params);
		
		t = new Thread(r);
		
		
	}
	
	/**
	 * 通信を行います。
	 */
	public final void connect(){
		t.start();

		/*
		try {
			// レスポンスが帰るまでループ
			while (!isResult()) {

				Thread.sleep(10);
			}

			// 接続成功
			success();

		} catch (Exception e) {
			// 失敗
			fail(e);
		}*/

	}

	// 通信成功
	public abstract void success();

	// 通信失敗
	public abstract void fail(Exception e);
	
	/**
	 * 結果がかえって来たかを取得します。
	 */
	public final boolean isResult() throws Exception {
		return r.isResult();
	}
	
	/**
	 * 結果を取得します。
	 */
	public final String result() throws Exception {
		return r.result();
	}

	/**
	 * セカンダリURLを指定します。
	 */
	public final MyHttp setSecondUrl(String secondUrl) {
		r.setSecondUrl(secondUrl);
		return this;
	}
}

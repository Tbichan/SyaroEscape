package com.example.tbichan.syaroescape.maingame.model;



import com.example.tbichan.syaroescape.opengl.GlObservable;

import java.util.HashSet;

/**
 * 環境モデルです。
 * Created by tbichan on 2017/10/16.
 */

public class Environment implements GlObservable {

	// プレイヤー
	public static final int PLAYER_ID = 1;

	// 机
	public static final int DESK_ID = 2;

	// 机
	public static final int CUP_ID = 3;

	// マップサイズ
	public static final int MAP_SIZE = 12;

	// カップ追加時のパラメータ
	public static final String PARAM_ADD_CUP = "add cup";

	// ターン終了
	public static final String PARAM_END = "end";

	// ステージマップ(プレイヤー...1、机の位置...2)
	private int[][] playerMap = {
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 1, 2, 2, 0, 0, 0, 0, 0, 2, 2, 0},
			{2, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0},
			{2, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 2},
			{0, 0, 2, 0, 0, 2, 0, 0, 0, 2, 0, 0},
			{2, 0, 0, 0, 0, 2, 0, 2, 0, 0, 2, 0},
			{0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 2},
			{0, 2, 0, 0, 2, 0, 2, 2, 2, 2, 0, 0},
			{2, 0, 0, 0, 2, 2, 0, 0, 0, 2, 0, 2},
			{0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 2},
			{0, 0, 0, 0, 2, 0, 2, 0, 0, 2, 0, 0},
			{0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0},


	};

	// ステージマップ(カップ...3)
	private int[][] cupMap = {
			{0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0},
			{0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0},
			{0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0},
			{3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},


	};

	// Observserable
	private HashSet<GlObservable> observableHashSet = new HashSet<>();

	// 獲得したカップ数
	private int getCupCnt = 0;


	public Environment(String name) {

	}


	// プレイヤーの位置(index)を取得します。
	public int getPlayerMapPlyaerIndex() {
		for (int y = 0; y < MAP_SIZE; y++) {
			for (int x = 0; x < MAP_SIZE; x++) {
				if (playerMap[y][x] == PLAYER_ID) return y * MAP_SIZE + x;
			}
		}

		return -1;
	}

	// プレイヤーマップのindexのIDを取得します。
	public int getPlayerMapID(int y, int x) {
		return playerMap[y][x];

	}

	// カップマップのindexのIDを取得します。
	public int getCupMapID(int y, int x) {
		return cupMap[y][x];

	}

	// プレイヤーを移動します。
	public void movePlayer(int oldX, int oldY, int newX, int newY) {

		// パラメータ用
		String[] params = null;

		// プレイヤーを移動
		playerMap[oldY][oldX] = 0;

		// プレイヤー位置更新
		if (playerMap[newY][newX] == DESK_ID) {
			// プレイヤーが机を押すとき
			// 移動量
			int dy = newY - oldY;
			int dx = newX - oldX;

			// 机移動
			playerMap[newY + dy][newX + dx] = DESK_ID;

		}
		playerMap[newY][newX] = PLAYER_ID;

		// 移動先にカップがあるならば削除
		if (cupMap[newY][newX] == CUP_ID) {
			cupMap[newY][newX] = 0;

			// 獲得
			getCupCnt++;

			params = new String[1];
			params[0] = PARAM_ADD_CUP;

		}

		// 敵が通れる位置を取得
		moveEnemys();

		// 変更を通知
		for (GlObservable glObservable: observableHashSet) {
			glObservable.notify(this, params);
		}
	}

	/**
	 * 敵が移動します。
	 */
	public void moveEnemys() {

		// 配列のコピー
		int[][] hogeMap = new int[MAP_SIZE][MAP_SIZE];
		for (int y = 0; y < hogeMap.length; y++) {
			for (int x = 0; x < hogeMap[y].length; x++) {
				int map = playerMap[y][x];
				hogeMap[y][x] = map;
			}
		}
		hogeMap[MAP_SIZE - 1][MAP_SIZE - 1] = 3;

		// A-starアルゴリズムによる最短経路探索
		AStar aStar = new AStar(hogeMap);
		aStar.setStartId(1);
		aStar.setSpaseId(0);
		aStar.setGoalId(3);

		aStar.loadMap();
		aStar.calc();
		aStar.print();
	}

	/**
	 * カップを作成します。
	 * @return
	 */
	public void createCup(int cupX, int cupY) {
		cupMap[cupY][cupX] = CUP_ID;
	}

	public int getGetCupCnt() {
		return getCupCnt;
	}

	// 初期化
    public void init() {
        // 変更を通知
        for (GlObservable glObservable: observableHashSet) {
            glObservable.notify(this);
        }
    }

	// 応答を受け取ります。
    @Override
	public void notify(Object o, String... param) {

		if (o instanceof String) {

			// クエリ
			String query = (String)o;
			if (query.startsWith("move:")) {
				String[] hoges = query.replace("move:", "").split(",");

				final int playerIndex = Integer.parseInt(hoges[0]);
				final int nextIndex = Integer.parseInt(hoges[1]);

				// 移動
				movePlayer(playerIndex % MAP_SIZE, playerIndex / MAP_SIZE, nextIndex % MAP_SIZE, nextIndex / MAP_SIZE);
			} else if (query.startsWith("cup:")) {
				final int cupIndex = Integer.parseInt(query.replace("cup:", ""));
				createCup(cupIndex % MAP_SIZE, cupIndex / MAP_SIZE);
			} else if (query.startsWith(PARAM_END)) {
				// 変更を通知
				for (GlObservable glObservable: observableHashSet) {
					glObservable.notify(this, PARAM_END);
				}
			}
		}

	}

	// 移動可能か判定
	public boolean isMove(int oldX, int oldY, int newX, int newY) {

		// 移動量
		int dy = newY - oldY;
		int dx = newX - oldX;

		// 移動先に机があるとき
		if (playerMap[newY][newX] == DESK_ID) {

			// 端ならfalse
			if (newY + dy < 0 || newY + dy >= MAP_SIZE) return false;
			if (newX + dx < 0 || newX + dx >= MAP_SIZE) return false;

			// 次の次が机ならfalse
			if (playerMap[newY + dy][newX + dx] == DESK_ID) return false;

			return true;
		}

		return true;
	}

	public void addGlObservable(GlObservable glObservable) {
		observableHashSet.add(glObservable);
	}

}

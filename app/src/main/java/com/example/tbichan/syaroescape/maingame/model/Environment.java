package com.example.tbichan.syaroescape.maingame.model;



import android.util.Log;

import com.example.tbichan.syaroescape.opengl.GlObservable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * 環境モデルです。
 * Created by tbichan on 2017/10/16.
 */

public class Environment implements GlObservable {

	// プレイヤー
	public static final int PLAYER_ID = 1;

	// 机
	public static final int DESK_ID = 2;

	// ウサギ
	public static final int RABBIT_ID = 4;

	// カップ
	public static final int CUP_ID = 8;

	// カップ
	public static final int CARROT_ID = 16;

	// マップサイズ
	public static final int MAP_SIZE = 12;

	// 机移動時のパラメータ
	public static final String MOVE_DESK = "move desk";

	// ウサギ移動時のパラメータ
	public static final String MOVE_RABBIT = "move rabbit";

	// ウサギ追加時のパラメータ
	public static final String CREATE_RABBIT = "create rabbit";

	// カップ追加時のパラメータ
	public static final String PARAM_ADD_CUP = "add cup";

	// 衝突時のパラメータ
	public static final String PARAM_HIT = "hit";

	// ターン終了
	public static final String PARAM_END = "end";

	// ステージマップ(プレイヤー...1、机の位置...2、ウサギ...4)
	private int[][] playerMap = {
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4},
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
			{4, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 4},


	};

	// ステージマップ(カップ...8、ニンジン...16)
	private int[][] cupMap = {
			{0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0},
			{0, 0, 0, 0, 8, 0, 0, 0, 0, 0, 8, 0},
			{0, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0},
			{0, 0, 0, 8, 0, 0, 0, 0, 0, 8, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 8, 0, 0, 0, 0, 0, 8, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 0},
			{8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

	};



	// 乱数設定用
	private Random r;

	// Observserable
	private HashSet<GlObservable> observableHashSet = new HashSet<>();

	// 行動回数
	private int actionCnt = 0;

	// 中断した際の行動回数
	private int lastActionCnt = 0;

	// 獲得したカップ数
	private int getCupCnt = 0;

	// 敵の行動間隔
	private int rabbitInterval = 2;

	// ひとつ前のプレイヤーインデックス
	private int prePlayerIndex = 0;


	public Environment(String name) {

		prePlayerIndex = getPlayerMapPlyaerIndex();
	}


	// プレイヤーの位置(index)を取得します。
	public int getPlayerMapPlyaerIndex() {
		for (int y = 0; y < MAP_SIZE; y++) {
			for (int x = 0; x < MAP_SIZE; x++) {
				if (isMapID(y, x, PLAYER_ID)) return y * MAP_SIZE + x;
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
		ArrayList<String> paramList = new ArrayList<>();
		paramList.add("move");

		// プレイヤーを移動
		prePlayerIndex = getPlayerMapPlyaerIndex();
		playerMap[oldY][oldX] = 0;

		// プレイヤー位置更新
		if (playerMap[newY][newX] == DESK_ID) {
			// プレイヤーが机を押すとき
			// 移動量
			int dy = newY - oldY;
			int dx = newX - oldX;

			// 机移動
			playerMap[newY + dy][newX + dx] = DESK_ID;

			// パラメータに設定
			String param = MOVE_DESK + ":" + ((newY) * MAP_SIZE + (newX)) + "," + ((newY + dy) * MAP_SIZE + (newX + dx));
			paramList.add(param);

		}
		playerMap[newY][newX] = PLAYER_ID;

		// 移動先にカップがあるならば削除
		if (cupMap[newY][newX] == CUP_ID) {
			cupMap[newY][newX] = 0;

			// 獲得
			getCupCnt++;

			// シード値に応じてカップ作成
			int nextCupX = r.nextInt(MAP_SIZE);
			int nextCupY = r.nextInt(MAP_SIZE);

			// プレイヤーの位置かカップと被ったらやり直し
			while (playerMap[nextCupY][nextCupX] == PLAYER_ID || cupMap[nextCupY][nextCupX] == CUP_ID) {
				nextCupX = r.nextInt(MAP_SIZE);
				nextCupY = r.nextInt(MAP_SIZE);
			}

			createCup(nextCupX, nextCupY);

			// パラメータに設定
			String param = PARAM_ADD_CUP + ":" + (newY * MAP_SIZE + newX) + "," + (nextCupY * MAP_SIZE + nextCupX);
			paramList.add(param);
		}

		// 行動回数増加
		actionCnt++;

		// 敵が通れる位置を取得
		if ((actionCnt - lastActionCnt) % rabbitInterval == 0) moveEnemys(paramList);

		// 敵を追加
		//createRabbit(5, paramList);

		// 変更を通知
		for (GlObservable glObservable: observableHashSet) {
			glObservable.notify(this, (String[])paramList.toArray(new String[paramList.size()]));
		}
	}

	/**
	 * 敵が一番近いプレイヤーまたはニンジンを取得します。
	 */
	public int getNearPlayerOrCarrot(int rabbitIndex) {

		// 配列のコピー
		int[][] hogeMap = new int[MAP_SIZE][MAP_SIZE];

		// 机位置のみを取得
		for (int y = 0; y < hogeMap.length; y++) {
			for (int x = 0; x < hogeMap[y].length; x++) {
				int map = playerMap[y][x];
				if (map != DESK_ID) map = 0;
				hogeMap[y][x] = map;
			}
		}

		// プレイヤーニンジンリスト
		ArrayList<Integer> carrotIndexList = new ArrayList<>();

		// プレイヤーインデックス追加
		//carrotIndexList.add(getPlayerMapPlyaerIndex());

		// ニンジン位置のみを取得
		for (int y = 0; y < hogeMap.length; y++) {
			for (int x = 0; x < hogeMap[y].length; x++) {
				if (isMapID(cupMap, y, x, CARROT_ID)) carrotIndexList.add(y * MAP_SIZE + x);
			}
		}

		// ニンジンがなければプレイヤー追跡
		if (carrotIndexList.size() == 0) return getPlayerMapPlyaerIndex();

		int minCost = Integer.MAX_VALUE;
		int minIndex = -1;

		// ニンジンをループで回す。
		for (final int carrotIndex : carrotIndexList) {

			if (carrotIndex == rabbitIndex) {
				minIndex = rabbitIndex;
				continue;
			}

			// スタート配置
			hogeMap[carrotIndex/MAP_SIZE][carrotIndex%MAP_SIZE] = 100;

			// ウサギ設置
			hogeMap[rabbitIndex/MAP_SIZE][rabbitIndex%MAP_SIZE] = 200;

			// A-starアルゴリズムによる最短経路探索
			AStar aStar = new AStar(hogeMap);
			aStar.setStartId(100);
			aStar.setSpaseId(0);
			aStar.setGoalId(200);

			aStar.loadMap();
			System.out.println(hogeMap);
			aStar.calc();

			// コスト計算
			int cost = aStar.getCost();


			Log.d("near", carrotIndex + "," + cost + "");

			if (cost > 0 && cost < minCost) {
				minIndex = carrotIndex;
				minCost = cost;
			}

			// スタート配置戻す
			hogeMap[carrotIndex/MAP_SIZE][carrotIndex%MAP_SIZE] = 0;
		}
		return minIndex;
	}

	/**
	 * 敵が移動します。
	 */
	public void moveEnemys(ArrayList<String> params) {

		// プレイヤーの位置
		final int playerIndex = getPlayerMapPlyaerIndex();

		// 動いたウサギ管理用
		boolean[][] moveMap = new boolean[MAP_SIZE][MAP_SIZE];

		// 配列のコピー（固定）
		int[][] preMap = new int[MAP_SIZE][MAP_SIZE];

		for (int y = 0; y < preMap.length; y++) {
			for (int x = 0; x < preMap[y].length; x++) {
				int map = playerMap[y][x];
				if (isMapID(y, x, PLAYER_ID)) map = 0;
				preMap[y][x] = map;
				moveMap[y][x] = false;
			}
		}

		// 配列のコピー
		int[][] hogeMap = new int[MAP_SIZE][MAP_SIZE];

		while (true) {

			// 一匹でも移動したかどうかのフラグ
			boolean moveFlg = false;

			for (int i = 0; ; i++) {


				// 机位置を取得
				for (int y = 0; y < hogeMap.length; y++) {
					for (int x = 0; x < hogeMap[y].length; x++) {
						int map = playerMap[y][x];
						if (map == RABBIT_ID) map = 0;
						hogeMap[y][x] = map;
					}
				}

				// i番目の敵インデックス取得
				int rabbitIndex = -1;

				int num = 0;

				boolean flg = true;

				// 固定マップからウサギを取得
				for (int y = 0; y < preMap.length; y++) {
					for (int x = 0; x < preMap[y].length; x++) {
						int map = preMap[y][x];

						// ウサギがいたら
						if (isMapID(preMap, y, x, RABBIT_ID)) {

							if (num == i) {
								rabbitIndex = y * MAP_SIZE + x;
								flg = false;
								break;
							}
							num++;
						}
					}
					if (!flg) break;
				}

				// なければ抜ける
				if (rabbitIndex == -1) break;

				// 移動済なら抜ける
				if (moveMap[rabbitIndex/MAP_SIZE][(rabbitIndex) % MAP_SIZE] == true) {
					continue;
				}

				// 一番近いプレイヤーまたはニンジンを取得
				int nearPlayerOrCarrotIndex = getNearPlayerOrCarrot(rabbitIndex);

				// 同位置なら移動しない
				if (nearPlayerOrCarrotIndex != rabbitIndex) {

					// プレイヤー
					hogeMap[nearPlayerOrCarrotIndex / MAP_SIZE][nearPlayerOrCarrotIndex % MAP_SIZE] = 100;

					// ゴールに設定
					hogeMap[rabbitIndex / MAP_SIZE][rabbitIndex % MAP_SIZE] = 200;

					// A-starアルゴリズムによる最短経路探索
					AStar aStar = new AStar(hogeMap);
					aStar.setStartId(100);
					aStar.setSpaseId(0);
					aStar.setGoalId(200);

					aStar.loadMap();
					aStar.calc();
					//aStar.print();

					// 次の敵インデックス取得
					int nextIndex = aStar.next();

					// ルートがなければ静止
					if (nextIndex != -1) {

						// 移動できれば移動
						if (isMapID(nextIndex / MAP_SIZE, (nextIndex) % MAP_SIZE, 0) || isMapID(nextIndex / MAP_SIZE, (nextIndex) % MAP_SIZE, PLAYER_ID)) {
							// 移動
							moveRabbit(rabbitIndex, nextIndex, params);
							moveMap[rabbitIndex/MAP_SIZE][rabbitIndex % MAP_SIZE] = true;
							moveFlg = true;
						}

					} else {
						// プレイヤーインデックス
						//final int playerIndex = getPlayerMapPlyaerIndex();

						// 横方向からプレイヤーに近づけるなら接近
						int dx = (int) Math.signum(nearPlayerOrCarrotIndex % MAP_SIZE - rabbitIndex % MAP_SIZE);
						int dy = (int) Math.signum(nearPlayerOrCarrotIndex / MAP_SIZE - rabbitIndex / MAP_SIZE);
						if (dx != 0 && (isMapID(rabbitIndex / MAP_SIZE, (rabbitIndex + dx) % MAP_SIZE, 0) || isMapID(rabbitIndex / MAP_SIZE, (rabbitIndex + dx) % MAP_SIZE, PLAYER_ID))) {

							// 移動
							moveRabbit(rabbitIndex, (rabbitIndex / MAP_SIZE) * MAP_SIZE + (rabbitIndex + dx) % MAP_SIZE, params);
							moveMap[rabbitIndex/MAP_SIZE][rabbitIndex % MAP_SIZE] = true;
							moveFlg = true;
						} else if (dy != 0 && (isMapID((rabbitIndex + dy) / MAP_SIZE, rabbitIndex % MAP_SIZE, 0) || isMapID((rabbitIndex + dy) / MAP_SIZE, rabbitIndex % MAP_SIZE, PLAYER_ID))) {
							// 移動
							moveRabbit(rabbitIndex, ((rabbitIndex + dy) / MAP_SIZE) * MAP_SIZE + rabbitIndex % MAP_SIZE, params);
							moveMap[rabbitIndex/MAP_SIZE][rabbitIndex % MAP_SIZE] = true;
							moveFlg = true;
						}

					}

					// もとに戻す
					hogeMap[rabbitIndex / MAP_SIZE][rabbitIndex % MAP_SIZE] = RABBIT_ID;
				}
			}

			// 誰も動かなかったら抜ける
			if (!moveFlg) break;
		}

	}

	public void moveRabbit(int rabbitIndex, int nextIndex, ArrayList<String> params) {

		if (playerMap[nextIndex / MAP_SIZE][nextIndex % MAP_SIZE] == PLAYER_ID) {
			// 衝突
			//MainActivity.showOKDialog(null);
			// パラメータに追加
			if (params != null) params.add(PARAM_HIT);
		}

		// 消す
		playerMap[rabbitIndex / MAP_SIZE][rabbitIndex % MAP_SIZE] = 0;

		//playerMap[nextIndex / MAP_SIZE][nextIndex % MAP_SIZE] = RABBIT_ID;
		setMapPostion(nextIndex / MAP_SIZE, nextIndex % MAP_SIZE, RABBIT_ID);

		// パラメータに追加
		if (params != null) params.add(MOVE_RABBIT + ":" + rabbitIndex + "," + nextIndex);
	}

	/**
	 * ウサギを追加します。
	 */
	public int createRabbit(int minCost, ArrayList<String> params) {

		// プレイヤー
		final int playerIndex = getPlayerMapPlyaerIndex();

		// 机位置取得
		int[][] deskAndPlyerMap = new int[MAP_SIZE][MAP_SIZE];
		createDeskMap(deskAndPlyerMap);

		// プレイヤー
		deskAndPlyerMap[playerIndex / MAP_SIZE][playerIndex % MAP_SIZE] = 100;

		int tmpMinCost = Integer.MAX_VALUE;
		int minIndex = -1;

		// コストが一番かかるところに追加
		for (int y = 0; y < playerMap.length; y++) {
			for (int x = 0; x < playerMap[y].length; x++) {

				// 空白に追加
				if (isMapID(y, x, 0)) {

					// ウサギインデックス
					final int rabbitIndex = y * MAP_SIZE + x;

					// ゴールに設定
					deskAndPlyerMap[rabbitIndex / MAP_SIZE][rabbitIndex % MAP_SIZE] = 200;

					// A-starアルゴリズムによる最短経路探索
					AStar aStar = new AStar(deskAndPlyerMap);
					aStar.setStartId(100);
					aStar.setSpaseId(0);
					aStar.setGoalId(200);

					aStar.loadMap();
					aStar.calc();

					// 最大コスト更新
					int cost = aStar.getCost();
					if (cost > 0 && cost >= minCost && tmpMinCost > cost) {
						tmpMinCost = cost;
						minIndex = rabbitIndex;
					}

				}
			}
		}

		// 新たにウサギに設定
		if (minIndex != -1) {
			playerMap[minIndex / MAP_SIZE][minIndex % MAP_SIZE] = RABBIT_ID;

			if (params != null) params.add(CREATE_RABBIT + ":" + minIndex);

			return minIndex;
		}

		return -1;
	}

	/**
	 * ウサギを遠くに追加します。
	 */
	public boolean createRabbitFar(ArrayList<String> params) {

		// プレイヤー
		final int playerIndex = getPlayerMapPlyaerIndex();

		// 机位置取得
		int[][] deskAndPlyerMap = new int[MAP_SIZE][MAP_SIZE];
		createDeskMap(deskAndPlyerMap);

		// プレイヤー
		deskAndPlyerMap[playerIndex / MAP_SIZE][playerIndex % MAP_SIZE] = 100;

		int maxCost = -1;
		int maxIndex = -1;

		// コストが一番かかるところに追加
		for (int y = 0; y < playerMap.length; y++) {
			for (int x = 0; x < playerMap[y].length; x++) {

				// 空白に追加
				if (isMapID(y, x, 0)) {

					// ウサギインデックス
					final int rabbitIndex = y * MAP_SIZE + x;

					// ゴールに設定
					deskAndPlyerMap[rabbitIndex / MAP_SIZE][rabbitIndex % MAP_SIZE] = 200;

					// A-starアルゴリズムによる最短経路探索
					AStar aStar = new AStar(deskAndPlyerMap);
					aStar.setStartId(100);
					aStar.setSpaseId(0);
					aStar.setGoalId(200);

					aStar.loadMap();
					aStar.calc();

					// 最大コスト更新
					int cost = aStar.getCost();
					if (cost > 0 && maxCost < cost) {
						maxCost = cost;
						maxIndex = rabbitIndex;
					}

				}
			}
		}

		// 新たにウサギに設定
		if (maxIndex != -1) {
			playerMap[maxIndex / MAP_SIZE][maxIndex % MAP_SIZE] = RABBIT_ID;

			if (params != null) params.add(CREATE_RABBIT + ":" + maxIndex);

			return true;
		}

		return false;
	}

	/**
	 * 机の位置マップを作成します。
	 */
	public void createDeskMap(int[][] maps) {
		// 机位置を取得
		for (int y = 0; y < maps.length; y++) {
			for (int x = 0; x < maps[y].length; x++) {
				int map = playerMap[y][x];
				if (map != DESK_ID) map = 0;
				maps[y][x] = map;
			}
		}
	}

	/**
	 * i-1番目の敵インデックスを取得します。
	 */
	public int getRabbitIndex(final int i){

		int num = 0;

		for (int y = 0; y < playerMap.length; y++) {
			for (int x = 0; x < playerMap[y].length; x++) {
				int map = playerMap[y][x];

				// ウサギがいれば
				if (isMapID(y, x, RABBIT_ID)) {
					if (num == i) return y * MAP_SIZE + x;
					num++;
				}
			}
		}
		return -1;
	}

	/**
	 * カップを作成します。
	 * @return
	 */
	public void createCup(int cupX, int cupY) {
		cupMap[cupY][cupX] = CUP_ID;
	}

	public int getCupCnt() {
		return getCupCnt;
	}

	// 応答を受け取ります。
    @Override
	public void notify(Object o, String... params) {

		if (o instanceof String) {

			// クエリ
			String query = (String)o;
			if (query.startsWith("move:")) {
				String[] hoges = query.replace("move:", "").split(",");

				final int playerIndex = Integer.parseInt(hoges[0]);
				final int nextIndex = Integer.parseInt(hoges[1]);

				// 移動
				movePlayer(playerIndex % MAP_SIZE, playerIndex / MAP_SIZE, nextIndex % MAP_SIZE, nextIndex / MAP_SIZE);
			} else if (query.startsWith(PARAM_END)) {

				// 新たに送るパラメータ設定
				ArrayList<String> nextParams = new ArrayList<>();
				nextParams.add(PARAM_END);

				// プレイヤー位置保存
				prePlayerIndex = getPlayerMapPlyaerIndex();

				// 敵移動
				moveEnemys(nextParams);
				lastActionCnt = actionCnt;

				// 変更を通知
				for (GlObservable glObservable: observableHashSet) {
					glObservable.notify(this, (String[])nextParams.toArray(new String[nextParams.size()]));
				}
			} else if (query.startsWith("addEnemy")) {
				// 敵を追加
				//createRabbit(5, paramList);
			} else if (query.startsWith("seed")) {
				int seed = Integer.parseInt(query.replace("seed:", ""));
				// シード値を設定
				r = new Random(seed);
			}
		}

	}

	// 移動可能か判定
	public boolean isPlayerMove(int oldX, int oldY, int newX, int newY) {

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
			// 次の次がウサギならfalse
			if (playerMap[newY + dy][newX + dx] == RABBIT_ID) return false;

			return true;
		}

		// 次にウサギがいればfalse
		if (playerMap[newY][newX] == RABBIT_ID) return false;

		return true;
	}

	public void addGlObservable(GlObservable glObservable) {
		observableHashSet.add(glObservable);
	}

	public int getActionCnt() {
		return actionCnt;
	}

	/**
	 * IDがあるか確認します。
	 * @param id
	 * @return
	 */
	public boolean isMapID(final int y, final int x, final int id) {
		if (id != 0) {
			return (playerMap[y][x] & id) != 0;
		} else {
			return playerMap[y][x] == 0;
		}
	}

	/**
	 * IDがあるか確認します。
	 * @param id
	 * @return
	 */
	public boolean isMapID(final int[][] map, final int y, final int x, final int id) {
		if (id != 0) {
			return (map[y][x] & id) != 0;
		} else {
			return map[y][x] == 0;
		}
	}

	/**
	 * IDをセットします。
	 */
	public void setMapPostion(final int y, final int x, final int id) {
		playerMap[y][x] |= id;
	}
}

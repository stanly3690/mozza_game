package com.board.stann.myapplication.db;

import android.provider.BaseColumns;

/**
 * Created by User on 15/01/2015.
 */
public abstract class PlayerStatistics implements BaseColumns {
    public static final String TABLE_NAME = "player_statistics_mossa";
    public static final String PLAYER_NAME = "player_name";
    public static final String PLAYER_WINS = "player_wins";
    public static final String PLAYER_LOSSES = "player_lost";
    public static final String LAST_GAME_TIME = "last_game_time";
    public static final String LAST_MOVEMENTS_NUM = "last_movements_num";
    public static final String PLAYER_NULLABLE_COL = "player_nullable";
}

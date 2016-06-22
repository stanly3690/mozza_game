package com.board.stann.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 15/01/2015.
 */
public class PlayerStatisticsHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Game.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String NULL = " NULL";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + PlayerStatistics.TABLE_NAME + " (" +
                    PlayerStatistics._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PlayerStatistics.PLAYER_NAME + TEXT_TYPE + COMMA_SEP +
                    PlayerStatistics.PLAYER_WINS + INTEGER_TYPE + COMMA_SEP +
                    PlayerStatistics.PLAYER_LOSSES + INTEGER_TYPE + COMMA_SEP +
                    PlayerStatistics.LAST_GAME_TIME + TEXT_TYPE + COMMA_SEP +
                    PlayerStatistics.LAST_MOVEMENTS_NUM + INTEGER_TYPE + COMMA_SEP +
                    PlayerStatistics.PLAYER_NULLABLE_COL + TEXT_TYPE + NULL +
            " )";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + PlayerStatistics.TABLE_NAME;

    public PlayerStatisticsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    public long insertPlayerStatistics(PlayerStatisticsHelper helper, String name, int wins, int losses, int movementsNum, String elapsedTime) {
        // Gets the data repository in write mode
        SQLiteDatabase db = helper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PlayerStatistics.PLAYER_NAME, name);
        values.put(PlayerStatistics.PLAYER_WINS, wins);
        values.put(PlayerStatistics.PLAYER_LOSSES, losses);
        values.put(PlayerStatistics.LAST_MOVEMENTS_NUM, movementsNum);
        values.put(PlayerStatistics.LAST_GAME_TIME, elapsedTime);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                PlayerStatistics.TABLE_NAME,
                PlayerStatistics.PLAYER_NULLABLE_COL,
                values);

        return newRowId;
    }

    public long updatePlayerStatistics(PlayerStatisticsHelper helper, String name, long wins, long losses, long movementsNum, String elapsedTime) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PlayerStatistics.PLAYER_WINS, wins);
        values.put(PlayerStatistics.PLAYER_LOSSES, losses);
        values.put(PlayerStatistics.LAST_MOVEMENTS_NUM, movementsNum);
        values.put(PlayerStatistics.LAST_GAME_TIME, elapsedTime);

        long newRowId;
        newRowId = db.update(
                PlayerStatistics.TABLE_NAME,
                values,
                PlayerStatistics.PLAYER_NAME + "=?",
                new String[]{name});

        return newRowId;
    }

    public List<Player> readData(PlayerStatisticsHelper helper) {
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PlayerStatistics._ID,
                PlayerStatistics.PLAYER_NAME,
                PlayerStatistics.PLAYER_WINS,
                PlayerStatistics.PLAYER_LOSSES,
                PlayerStatistics.LAST_GAME_TIME,
                PlayerStatistics.LAST_MOVEMENTS_NUM
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = PlayerStatistics._ID + " ASC";

        Cursor cursor = db.query(
                PlayerStatistics.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder,                                 // The sort order
                "10"
        );

        List<Player> playerList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(PlayerStatistics.PLAYER_NAME));
            long wins = cursor.getLong(cursor.getColumnIndexOrThrow(PlayerStatistics.PLAYER_WINS));
            long losses = cursor.getLong(cursor.getColumnIndexOrThrow(PlayerStatistics.PLAYER_LOSSES));
            long movementsNum = cursor.getLong(cursor.getColumnIndexOrThrow(PlayerStatistics.LAST_MOVEMENTS_NUM));
            String elapsedTime = cursor.getString(cursor.getColumnIndexOrThrow(PlayerStatistics.LAST_GAME_TIME));
            //Log.i("ROWS ----------> ", id + " - " + name + " - " + wins + " - " + losses + " - " + movementsNum + " - " + elapsedTime);
            Player player = new Player(name, wins, losses, movementsNum, elapsedTime);
            playerList.add(player);
        }
        return playerList;
    }

    public List<Player> readTopScores(PlayerStatisticsHelper helper) {
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PlayerStatistics._ID,
                PlayerStatistics.PLAYER_NAME,
                PlayerStatistics.PLAYER_WINS,
                PlayerStatistics.PLAYER_LOSSES,
                PlayerStatistics.LAST_GAME_TIME,
                PlayerStatistics.LAST_MOVEMENTS_NUM
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = PlayerStatistics.PLAYER_WINS + " DESC";

        Cursor cursor = db.query(
                PlayerStatistics.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder,                                 // The sort order
                "5"
        );

        List<Player> playerList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(PlayerStatistics.PLAYER_NAME));
            long wins = cursor.getLong(cursor.getColumnIndexOrThrow(PlayerStatistics.PLAYER_WINS));
            long losses = cursor.getLong(cursor.getColumnIndexOrThrow(PlayerStatistics.PLAYER_LOSSES));
            long movementsNum = cursor.getLong(cursor.getColumnIndexOrThrow(PlayerStatistics.LAST_MOVEMENTS_NUM));
            String elapsedTime = cursor.getString(cursor.getColumnIndexOrThrow(PlayerStatistics.LAST_GAME_TIME));
            //Log.i("ROWS ----------> ", id + " - " + name + " - " + wins + " - " + losses + " - " + movementsNum + " - " + elapsedTime);
            Player player = new Player(name, wins, losses, movementsNum, elapsedTime);
            playerList.add(player);
        }
        return playerList;
    }

    public boolean checkIfPlayerExists(PlayerStatisticsHelper helper, String name) {
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PlayerStatistics.PLAYER_NAME
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = PlayerStatistics.PLAYER_NAME + " DESC";

        Cursor cursor = db.query(
                PlayerStatistics.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                PlayerStatistics.PLAYER_NAME + "=?",                                // The columns for the WHERE clause
                new String[]{name},                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasRow = cursor.moveToFirst();
        return hasRow;
    }

    public Player getPlayerStatisticsByName(PlayerStatisticsHelper helper, String name) throws SQLDataException {
        SQLiteDatabase db = helper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PlayerStatistics.PLAYER_NAME,
                PlayerStatistics.PLAYER_WINS,
                PlayerStatistics.PLAYER_LOSSES,
                PlayerStatistics.LAST_MOVEMENTS_NUM,
                PlayerStatistics.LAST_GAME_TIME,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = PlayerStatistics.PLAYER_NAME + " DESC";

        Cursor cursor = db.query(
                PlayerStatistics.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                "player_name=?",                                // The columns for the WHERE clause
                new String[]{name},                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasRow = cursor.moveToFirst();
        if (hasRow) {
            String playerName = cursor.getString(cursor.getColumnIndexOrThrow(PlayerStatistics.PLAYER_NAME));
            long wins = cursor.getLong(cursor.getColumnIndexOrThrow(PlayerStatistics.PLAYER_WINS));
            long losses = cursor.getLong(cursor.getColumnIndexOrThrow(PlayerStatistics.PLAYER_LOSSES));
            long movementsNum = cursor.getLong(cursor.getColumnIndexOrThrow(PlayerStatistics.LAST_MOVEMENTS_NUM));
            String elapsedTime = cursor.getString(cursor.getColumnIndexOrThrow(PlayerStatistics.LAST_GAME_TIME));
            Player player = new Player(playerName, wins, losses, movementsNum, elapsedTime);
            return player;
        } else {
            throw new SQLDataException("Empty result set");
        }
    }
}
package com.board.stann.myapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.board.stann.myapplication.db.Player;
import com.board.stann.myapplication.db.PlayerStatisticsHelper;

import java.util.List;

public class GameStatistics extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamestatitics);
        test();
        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void test() {
        TableLayout tableView = (TableLayout) findViewById(R.id.tableview);
        tableView = (TableLayout) findViewById(R.id.tableview);
        tableView.setPadding(15, 3, 15, 3);
        PlayerStatisticsHelper playerStatistics = new PlayerStatisticsHelper(getApplicationContext());
        List<Player> playerList = playerStatistics.readData(playerStatistics);

        for (int i = 0; i < playerList.size(); i++) {

            TableRow row = new TableRow(this);

            TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            row.setPadding(15, 3, 15, 3);

            Player player = playerList.get(i);

            TextView name = new TextView(this);

            name.setPadding(15, 0, 15, 0);
            name.setGravity(Gravity.CENTER);
            name.setTextSize(25.0f);
            name.setTextColor(Color.parseColor("#FFFFFF"));
            //name.setTypeface(null, Typeface.BOLD);
            name.setText(player.getName());
            row.addView(name);

            TextView wins = new TextView(this);
            wins.setPadding(15, 0, 15, 0);
            wins.setGravity(Gravity.CENTER);
            wins.setTextSize(25.0f);
            wins.setTextColor(Color.parseColor("#FFFFFF"));
            // wins.setTypeface(null, Typeface.BOLD);
            wins.setText(player.getWins() + "");
            row.addView(wins);

            TextView losses = new TextView(this);
            losses.setPadding(15, 0, 15, 0);
            losses.setGravity(Gravity.CENTER);
            losses.setTextSize(25.0f);
            losses.setTextColor(Color.parseColor("#FFFFFF"));
            //losses.setTypeface(null, Typeface.BOLD);
            losses.setText(player.getLosses() + "");
            row.addView(losses);

            TextView lastMovements = new TextView(this);
            lastMovements.setPadding(15, 0, 15, 0);
            lastMovements.setGravity(Gravity.CENTER);
            lastMovements.setTextSize(25.0f);
            lastMovements.setTextColor(Color.parseColor("#FFFFFF"));
            //lastMovements.setTypeface(null, Typeface.BOLD);
            lastMovements.setText(player.getLastNumMovements() + "");
            row.addView(lastMovements);

            TextView lastTimeElapsed = new TextView(this);
            lastTimeElapsed.setPadding(15, 0, 15, 0);
            lastTimeElapsed.setGravity(Gravity.CENTER);
            lastTimeElapsed.setTextSize(25.0f);
            lastTimeElapsed.setTextColor(Color.parseColor("#FFFFFF"));
            // lastTimeElapsed.setTypeface(null, Typeface.BOLD);
            lastTimeElapsed.setText(player.getLastElapsedTime() + "");
            row.addView(lastTimeElapsed);


            tableView.addView(row);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gamestatitics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

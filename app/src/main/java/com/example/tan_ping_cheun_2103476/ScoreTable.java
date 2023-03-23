package com.example.tan_ping_cheun_2103476;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;

public class ScoreTable extends AppCompatActivity {

    private ScoreDatabase scoreDb;
    private Button bckBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);
        scoreDb = new ScoreDatabase(this);

        ArrayList<ScoreInfo> top25Table = scoreDb.getTop25();
        TableLayout nameList = findViewById(R.id.score_table);

        TableRow header = new TableRow(this);

        TextView hRank = new TextView(this);
        TextView hName = new TextView(this);
        TextView hScore = new TextView(this);

        hRank.setText("Rank");
        hRank.setTypeface(null, Typeface.BOLD);
        hRank.setTextSize(0,105);
        hName.setText("Name");
        hName.setTypeface(null, Typeface.BOLD);
        hName.setTextSize(0,105);
        hScore.setText("Score");
        hScore.setTypeface(null, Typeface.BOLD);
        hScore.setTextSize(0,105);

        header.addView(hRank);
        header.addView(hName);
        header.addView(hScore);

        nameList.addView(header);

        for(int i=0; i < top25Table.size(); i++){
            int color = Color.rgb((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
            ScoreInfo score = top25Table.get(i);
            TableRow tableRow = new TableRow(this);
            TextView ranking = new TextView(this);
            ranking.setText(String.valueOf(i+1));
            ranking.setTextSize(0, 100);
            ranking.setTypeface(null, Typeface.BOLD);
            ranking.setTextColor(Color.RED);
            tableRow.addView(ranking);

            TextView disName = new TextView(this);
            disName.setText(score.getName());
            disName.setTextSize(0, 80);
            tableRow.addView(disName);

            TextView disScore = new TextView(this);
            disScore.setText(String.valueOf(score.getScore()));
            disScore.setTextSize(0, 80);
            tableRow.addView(disScore);
            tableRow.setBackgroundColor(color);

            nameList.addView(tableRow);
        }
        bckBtn = findViewById(R.id.back_btn);
        bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreTable.super.onBackPressed();
            }
        });

    }
}

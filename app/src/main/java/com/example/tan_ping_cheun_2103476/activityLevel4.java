package com.example.tan_ping_cheun_2103476;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class activityLevel4 extends AppCompatActivity {

    private Button startGameBtn;
    private Button resetGameBtn;
    private Button scoreBtn;
    private TextView timeText;
    private TextView scoreText;
    private ArrayList<View> views;
    private CountDownTimer timer;
    private int remainingTime;
    private int totalScore;
    private int numViews;
    private int level = 1;
    private Random random;
    private ArrayList<View> highlightedViews = new ArrayList<>();
    private int numClicked;
    private int finalizeScore;
    private ScoreDatabase scoreDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level4);

        scoreDb = new ScoreDatabase(this);

        startGameBtn = findViewById(R.id.start_button);
        resetGameBtn = findViewById(R.id.reset_button);
        scoreBtn = findViewById(R.id.score_button);
        timeText = findViewById(R.id.time_text);
        scoreText = findViewById(R.id.score_text);
        views = new ArrayList<>();
        GridLayout parentView = findViewById(R.id.game_play_area);
        numViews = parentView.getChildCount();
        for (int i = 0; i < numViews; i++) {
            int viewId = getResources().getIdentifier("view_" + i, "id", getPackageName());
            views.add(findViewById(viewId));
        }
        random = new Random();

        startGameBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetGame();
                startGame();
            }
        });
        resetGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizeScore = 0;
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("score", finalizeScore);
                startActivity(i);
            }
        });

        scoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ScoreTable.class);
                startActivity(i);
            }
        });
        Button exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }
    private void resetGame() {
        totalScore = getIntent().getIntExtra("score", 0);
        remainingTime = 5000;
        scoreText.setText("Score: " + totalScore);
        for (View view : views) {
            view.setOnClickListener(null);
            view.setBackgroundResource(R.drawable.def_bg);
        }
        highlightedViews.clear();
        numClicked = 0; // Reset numClicked variable
        updateTimer(); // Reset the timer text view
    }

    private void startGame(){
        timer = new CountDownTimer(remainingTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = (int) millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                endGame();
            }
        };
        timer.start();
        playGame();
    }

    private void playGame() {
        if (remainingTime > 0) {
            int randomIndex = random.nextInt(numViews);
            View highlightView = views.get(randomIndex);

            // check if the view has already been highlighted in this game
            while (highlightedViews.contains(highlightView)) {
                randomIndex = random.nextInt(numViews);
                highlightView = views.get(randomIndex);
            }

            highlightedViews.add(highlightView);
            boolean isTouchView = true;
            if (isTouchView) {
                highlightView.setBackgroundResource(R.drawable.touch_bg);
            } else {
                highlightView.setBackgroundResource(R.drawable.highlight_bg);
            }
            highlightView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!highlightedViews.contains(v)) {
                        return; // don't count clicks on non-highlighted views
                    }
                    totalScore++;
                    scoreText.setText("Score: " + totalScore);
                    finalizeScore = totalScore;
                    v.setBackgroundResource(R.drawable.highlight_bg);
                    v.setOnClickListener(null); // disable click on this view
                    numClicked++;
                    if (numClicked == numViews) {
                        Intent i = new Intent(activityLevel4.this, activityLevel5.class);
                        i.putExtra("score", finalizeScore);
                        startActivity(i);
                    } else {
                        playGame();
                    }
                }
            });
        } else {
            endGame();
        }
    }

    private void endGame(){
        timer.cancel();
        for (View view : views) {
            view.setOnClickListener(null);
        }
        ArrayList<ScoreInfo> top25 = scoreDb.getTop25();
        boolean trueTop25 = false;
        if(top25.size() < 25){
            trueTop25 = true;
        }
        for(ScoreInfo scoreInfo : top25){
            if(finalizeScore > scoreInfo.getScore()){
                trueTop25 = true;
                break;
            }
        }
        if(trueTop25){
            showEnterName();
        }else{
            showTryAgain();
        }
    }
    private void showEnterName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText enterName = new EditText(this);
        enterName.setHint("Enter name");

        TextView textViewScore = new TextView(this);
        textViewScore.setTextSize(24);
        textViewScore.setTypeface(Typeface.DEFAULT_BOLD);
        textViewScore.setText("Total Score: " + finalizeScore);
        textViewScore.setGravity(Gravity.CENTER_HORIZONTAL);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(textViewScore);
        layout.addView(enterName);
        builder.setView(layout);

        builder.setTitle("Congratulation!!\nYou are the TOP 25. ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playerName = enterName.getText().toString();

                ScoreInfo newScore = new ScoreInfo(playerName, finalizeScore);
                scoreDb.addScore(newScore);
                Intent i = new Intent(activityLevel4.this, ScoreTable.class);
                startActivity(i);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showTryAgain(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Try Again, Gambateh~" );
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void updateTimer(){
        int sec = remainingTime / 1000;
        timeText.setText("Time: " + sec);
    }
}

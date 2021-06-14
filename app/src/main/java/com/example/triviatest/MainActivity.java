package com.example.triviatest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviatest.controller.AppController;
import com.example.triviatest.data.AnswerListAsyncResponse;
import com.example.triviatest.data.QuestionBank;
import com.example.triviatest.model.Question;
import com.example.triviatest.model.Score;
import com.example.triviatest.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextView;
    private TextView trivia;
    private Button trueButton;
    private Button falseButton;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private int currentQuestionIndex =0 ;
    private List<Question> questionList;
    private TextView counterCount;
    private  int scoreCounter=0;
    private Score score;
    private TextView scoreTextView;
    private TextView highestScore;
    private Prefs prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score = new Score();
        prefs = new Prefs(MainActivity.this);
        scoreTextView = findViewById(R.id.score_text);
        trivia=findViewById(R.id.textName);
        nextButton = findViewById(R.id.next);
        prevButton = findViewById(R.id.prev);
        trueButton = findViewById(R.id.sahi);
        falseButton = findViewById(R.id.galat);
        questionTextView = findViewById(R.id.questiontext);
        counterCount = findViewById(R.id.counterQuestions);
        highestScore = findViewById(R.id.highest_score);



        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        scoreTextView.setText(String.valueOf(score.getScore()));
        highestScore.setText(MessageFormat.format("highest score{0}", String.valueOf(prefs.getHighScore())));
        currentQuestionIndex = prefs.getState();
       // Log.d("second", "onCreate: "+ prefs.getHighScore());


        questionList=new QuestionBank().getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                counterCount.setText(currentQuestionIndex + "/" + questionArrayList.size());
                
            }
        });
        Log.d("json2", "onCreate: "+questionList);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prev:
                if(currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    updateQuestion();
                }
                break;
            case R.id.next:


                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
                break;
            case R.id.sahi:
                checkAnswer(true);

                break;
            case R.id.galat:
                checkAnswer(false);

                break;

        }
    }

    private void checkAnswer(boolean userchoice) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageId=0;
        if(userchoice == answerIsTrue) {
            toastMessageId = R.string.corect_answer;
            addPoints();
        }else{
            shakeAnimation();

                toastMessageId = R.string.wrong_answer;
                deductPoints();

            }
        Toast.makeText(MainActivity.this,toastMessageId,Toast.LENGTH_SHORT).show();

    }

    private void updateQuestion() {
        String question= questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        counterCount.setText(currentQuestionIndex + "/" + questionList.size());

    }

    public void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake_animation);
        CardView cardVew = findViewById(R.id.cardView);
        cardVew.setAnimation(shake);
        cardVew.startAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardVew.setCardBackgroundColor(Color.RED);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardVew.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void addPoints(){
        scoreCounter += 100;
        score.setScore(scoreCounter);
        scoreTextView.setText(String.valueOf(score.getScore()));

        Log.d("scoreponits", "addPoints: " +score.getScore());
    }
    private void deductPoints(){
        scoreCounter -= 100;
        if(scoreCounter > 0){
            score.setScore(scoreCounter);
            scoreTextView.setText(String.valueOf(score.getScore()));}
        else{
            scoreCounter=0;
            score.setScore(scoreCounter);
            Log.d("bad score", "deductPoints: "+score.getScore());
            scoreTextView.setText(String.valueOf(score.getScore()));

        }

        Log.d("scoreponits", "deductPoints: " +score.getScore());
    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }
}
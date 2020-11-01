package com.areeb.braintrainer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button goButton;
    int score = 0;
    String timeLeft = "";
    Random random;
    int numberOfQuestions = 0;
    int locationOfCorrectAnswer;
    ConstraintLayout gameLayout;
    ArrayList<Integer> answers = new ArrayList<>();
    TextView resultTextView, scoreTextView, sumTextView, timerTextView;
    Button answer0Button, answer1Button, answer2Button, answer3Button, playAGainButton;
    String[] symbols = {"+", "-", "*", "/"};
    String currentSymbol = "+";
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing xml views
        goButton = findViewById(R.id.goButton);
        sumTextView = findViewById(R.id.sumTextView);
        answer0Button  = findViewById(R.id.answer0Button);
        answer1Button  = findViewById(R.id.answer1Button);
        answer2Button  = findViewById(R.id.answer2Button);
        answer3Button  = findViewById(R.id.answer3Button);
        resultTextView = findViewById(R.id.resultTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        playAGainButton = findViewById(R.id.playAgainButton);
        gameLayout = findViewById(R.id.gameLayout);

        //initialize random object
        random = new Random();

        //set initial visibility
        gameLayout.setVisibility(View.INVISIBLE);
        goButton.setVisibility(View.VISIBLE);
    }

    //go button on click method
    public void start(View view)
    {
        goButton.setVisibility(View.INVISIBLE);
        gameLayout.setVisibility(View.VISIBLE);
        playAgain(playAGainButton);
    }

    //answer button onclick method
    public void chooseAnswer(View view)
    {
        if (locationOfCorrectAnswer == Integer.parseInt(view.getTag().toString()))
        {
            resultTextView.setText(R.string.correct);
            resultTextView.setTextColor(getResources().getColor(R.color.correct_answer));
            score++;
        }
        else {
            resultTextView.setText(R.string.wrong);
            resultTextView.setTextColor(getResources().getColor(R.color.wrong_answer));
        }

        numberOfQuestions++;
        String scoreString = score + "/" + numberOfQuestions;
        scoreTextView.setText(scoreString);
        newQuestion();
    }

    //generate new question
    public void newQuestion()
    {
        //generate random numbers
        int number1 = random.nextInt(21);
        int number2 = random.nextInt(21);

        //set question to TextView
        String expression = number1 + currentSymbol + number2;
        sumTextView.setText(expression);

        //generate random location for correct answer
        locationOfCorrectAnswer = random.nextInt(4);

        //clear answers array list
        answers.clear();

        int correctAnswer = 0;
        int maxValue = 1;
        switch (currentSymbol) {
            case "+":
                correctAnswer = number1 + number2;
                maxValue = 41;
                break;
            case "-":
                correctAnswer = number1 - number2;
                maxValue = 41;
                break;
            case "*":
                correctAnswer = number1 * number2;
                maxValue = 401;
                break;
            case "/":
                correctAnswer = number1 / number2;
                maxValue = 41;
                break;
        }

        //set correct and wrong answer
        for (int i = 0; i < 4; i++)
        {
            if (i == locationOfCorrectAnswer)
                answers.add(correctAnswer);
            else {
                int wrongAnswer = random.nextInt(maxValue);

                while (wrongAnswer == correctAnswer)
                {
                    wrongAnswer = random.nextInt(maxValue);
                }

                answers.add(wrongAnswer);
            }
        }

        //set answers to button
        answer0Button.setText(String.format(Locale.US,"%d", answers.get(0)));
        answer1Button.setText(String.format(Locale.US, "%d", answers.get(1)));
        answer2Button.setText(String.format(Locale.US, "%d", answers.get(2)));
        answer3Button.setText(String.format(Locale.US, "%d", answers.get(3)));
    }

    //play again button onclick method
    public void playAgain(View view)
    {
        //reset all the values of game
        score = 0;
        numberOfQuestions = 0;
        timerTextView.setText(R.string.basic_time);
        String scoreString = score + "/" + numberOfQuestions;
        scoreTextView.setText(scoreString);
        resultTextView.setText("");
        setButtonEnabled(true);
        newQuestion();
        playAGainButton.setVisibility(View.INVISIBLE);

        //start count down timer up to 30 seconds
        countDownTimer = new CountDownTimer(30100, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (millisUntilFinished / 1000) + "s";
                timerTextView.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                resultTextView.setText(getString(R.string.done));
                resultTextView.setTextColor(getResources().getColor(R.color.black));
                playAGainButton.setVisibility(View.VISIBLE);
                setButtonEnabled(false);
            }
        }.start();
    }

    //enable or disable answers button
    public void setButtonEnabled(Boolean buttonEnabled)
    {
        answer0Button.setClickable(buttonEnabled);
        answer1Button.setClickable(buttonEnabled);
        answer2Button.setClickable(buttonEnabled);
        answer3Button.setClickable(buttonEnabled);
    }

    //sumTextView on click method
    public void changeSymbol(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change symbol");
        builder.setItems(symbols, (dialog, which) -> {
            currentSymbol = symbols[which];
            countDownTimer.cancel();
            playAgain(playAGainButton);
        });
        builder.show();
    }
}
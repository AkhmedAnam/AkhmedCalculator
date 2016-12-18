package com.example.akhmedanamcalculator;

import android.content.DialogInterface;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String statement;
    int bracksCounter;
    boolean isHereLeftBrack = false, isHereRightBrack = false,
            isHereOperator = false, isHereDot = false,
            isHereDigit = false, isThereAnyOperations = false,
            isHereSquareRoot = false;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = (TextView)findViewById(R.id.txt);
    }



    public void clearClick(View v){
        txt.setText("");
        statement = "";
        bracksCounter = 0;
        isHereDigit = false;
        isHereDot = false;
        isHereLeftBrack = false;
        isHereOperator = false;
        isHereRightBrack = false;
    }

    public void digitClick(View v){
        if (isHereDigit || isHereLeftBrack || isHereOperator || txt.getText().toString().equals("") || isHereDot || isHereSquareRoot)
        {
            addNewTextAndUpdateStatement(v);
            isHereOperator = false;
            isHereLeftBrack = false;
            isHereRightBrack = false;
            isHereDot = false;

            isHereDigit = true;
        }
    }

    public void operationClick(View v){
        if (isHereDigit || isHereRightBrack)
        {
            addNewTextAndUpdateStatement(v);
            isHereLeftBrack = false;
            isHereRightBrack = false;
            isHereDot = false;
            isHereDigit = false;
            isHereSquareRoot = false;

            isHereOperator = true;
            isThereAnyOperations = true;
        }
    }

    public void bracketClick(View v){
        if(v.getId() == R.id.leftBr){
            if (isHereOperator || isHereLeftBrack || isHereSquareRoot)
            {
                addNewTextAndUpdateStatement(v);

                bracksCounter++;

                isHereOperator = false;
                isHereRightBrack = false;
                isHereDot = false;
                isHereDigit = false;
                isHereSquareRoot = false;

                isHereLeftBrack = true;

            }
        }
        else{//Правая скобка
            if (isHereDigit || isHereRightBrack)
            {
                addNewTextAndUpdateStatement(v);

                bracksCounter--;

                isHereLeftBrack = false;
                isHereDot = false;
                isHereDigit = false;
                isHereOperator = false;
                isHereSquareRoot = false;

                isHereRightBrack = true;
            }
        }
    }

    public void dotClick(View v){

        if (isHereDigit)
        {
            addNewTextAndUpdateStatement(v);

            isHereDigit = false;
            isHereLeftBrack = false;
            isHereOperator = false;
            isHereRightBrack = false;
            isHereSquareRoot = false;

            isHereDot = true;
        }
    }

    public void rootClick(View v){

        if (isHereLeftBrack || isHereOperator || isHereSquareRoot || txt.getText().toString().equals(""))
        {
            addNewTextAndUpdateStatement(v);

            isHereRightBrack = false;
            isHereOperator = false;
            isHereLeftBrack = false;
            isHereDot = false;
            isHereDigit = false;

            isHereSquareRoot = true;
            isThereAnyOperations = true;

        }

    }

    public void equalClick(View v){

        if (statement == null || statement.equals(""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Ошибка в вычисляемом выражении!")
                    .setMessage("Вы не ввели выражение! Необходимо ввести выражение.")
                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            builder.show();
        }

        else if (bracksCounter != 0){

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Ошибка в вычисляемом выражении!")
                    .setMessage("В введённом выражении не все скобки закрыты!")
                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            builder.show();
        }

        else if (!isThereAnyOperations){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Ошибка в вычисляемом выражении!")
                    .setMessage("В введённом выражении отсутствуют арифметические операторы.")
                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            builder.show();
        }

        else
        {
            StatementParser parse = new StatementParser(statement);

            txt.setText("");
            String answer = String.valueOf(parse.getAnswer());
            txt.setText(answer);
            statement = "";
        }
    }

    private void addNewTextAndUpdateStatement(View v){
        TextView tv = (TextView)v;
        String tt = tv.getText().toString();
        txt.setText(txt.getText().toString() + tt);
        statement = txt.getText().toString();
    }
}


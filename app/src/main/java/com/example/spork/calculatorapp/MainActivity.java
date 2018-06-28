package com.example.spork.calculatorapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getName(); // give you class name for console log


    private ArrayList<Button> buttonArrayList = new ArrayList<>();
    private TextView currentExpression;
    private StringBuilder expression;
    private LocalBroadcastManager localBroadcastManager;
    private CalculatorBroadcastReceiver calculatorBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button0 = findViewById(R.id.button0);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);
        Button buttonDot = findViewById(R.id.buttondot);
        Button buttonAdd = findViewById(R.id.buttonadd);
        Button buttonSub = findViewById(R.id.buttonminus);
        Button buttonDivide = findViewById(R.id.buttondivide);
        Button buttonMultiply = findViewById(R.id.buttonmultiply);
        Button buttonEval = findViewById(R.id.buttonequal);

        buttonArrayList.add(button0);
        buttonArrayList.add(button1);
        buttonArrayList.add(button2);
        buttonArrayList.add(button3);
        buttonArrayList.add(button4);
        buttonArrayList.add(button5);
        buttonArrayList.add(button6);
        buttonArrayList.add(button7);
        buttonArrayList.add(button8);
        buttonArrayList.add(button9);
        buttonArrayList.add(buttonDot);
        buttonArrayList.add(buttonAdd);
        buttonArrayList.add(buttonSub);
        buttonArrayList.add(buttonDivide);
        buttonArrayList.add(buttonMultiply);
        buttonArrayList.add(buttonEval);

        for (Button button: buttonArrayList) {
            button.setOnClickListener(this);
        }

        currentExpression = findViewById(R.id.editText);

        expression = new StringBuilder();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        calculatorBroadcastReceiver = new CalculatorBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        localBroadcastManager.registerReceiver(calculatorBroadcastReceiver, new IntentFilter(CalculatorService.CALC_SERVICE_INTENT_FILTER_SUCCESS));
        super.onResume();
    }

    @Override
    protected void onPause() {
        localBroadcastManager.unregisterReceiver(calculatorBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Button button = findViewById(id);
        switch(id) {
            case R.id.buttonequal:
                // still update the expression on the text field
                String eval = expression.toString();

                Intent intent = new Intent(this, CalculatorService.class);
                intent.putExtra(CalculatorService.CALC_SERVICE_INTENT_FILTER_EVALULATE, eval);
                this.startService(intent);
                break;
            case R.id.buttonclear:
                expression.setLength(0); //clear stringbuilder
                currentExpression.setText(expression.toString());
                break;
            default:
                updateExpression(button);
        }
    }

    private void updateExpression(Button button) {
        expression.append(button.getText());
        currentExpression.setText(expression.toString());
        Log.wtf(TAG, "button pressed: " + button.getText());
    }

    private class CalculatorBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String result = String.valueOf(intent.getDoubleExtra(CalculatorService.CALC_SERVICE_INTENT_FILTER_SUCCESS, 0));

            TextView viewResult = findViewById(R.id.result);
            viewResult.setText(result);

            Toast.makeText(context, "CalculatorBroadCastReceiver" + result, Toast.LENGTH_SHORT ).show();
        }
    }

}

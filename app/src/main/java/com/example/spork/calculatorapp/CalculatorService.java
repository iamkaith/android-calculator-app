package com.example.spork.calculatorapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorService extends IntentService {

    public static final String CALC_SERVICE_INTENT_FILTER_EVALULATE = "evaluate";
    public static final String CALC_SERVICE_INTENT_FILTER_SUCCESS = "success";

    private Handler handler;
    private LocalBroadcastManager localBroadcastManager;

    public CalculatorService() {
        super("CalculatorService");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        handler = new Handler();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null ) {
            final String toEvaluate = intent.getStringExtra(CalculatorService.CALC_SERVICE_INTENT_FILTER_EVALULATE);


            // calculate
            Expression calc = new ExpressionBuilder(toEvaluate).build();
            final double result;
            result = calc.evaluate();
            Log.wtf("CalculatorService ", "result is: " + result);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        //Toast.makeText(getApplicationContext(), String.valueOf(result), Toast.LENGTH_SHORT).show();
                        sendBroadCast(result);

                    } catch (ArithmeticException e ) {
                        sendBroadcast(e.getMessage());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
    }

    private void sendBroadCast(double result) {

        Intent intent = new Intent(CalculatorService.CALC_SERVICE_INTENT_FILTER_SUCCESS);
        intent.putExtra(CalculatorService.CALC_SERVICE_INTENT_FILTER_SUCCESS, result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendBroadcast(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }
}

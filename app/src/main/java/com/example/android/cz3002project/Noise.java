package com.example.android.cz3002project;

import android.app.Activity;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;


public class Noise extends Activity {

    TextView mStatusView;
    MediaRecorder mRecorder;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    final Runnable updater = new Runnable(){

        public void run(){
            updateTv();
        };
    };
    final Handler mHandler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mStatusView = (TextView) findViewById(R.id.status);


        if (runner == null)
        {
            runner = new Thread(){
                public void run()
                {
                    while (runner != null)
                    {
                        try
                        {
                            Thread.sleep(1000);
                            Log.i("Noise", "Tock");
                        } catch (InterruptedException e) { };
                        mHandler.post(updater);
                    }
                }
            };
            runner.start();
            Log.d("Noise", "start runner()");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void onResume()
    {
        super.onResume();
        startRecorder();
    }

    public void onPause()
    {
        super.onPause();
        stopRecorder();
    }

    public void startRecorder(){
        if (mRecorder == null)
        {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try
            {
                mRecorder.prepare();
            }catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " + android.util.Log.getStackTraceString(ioe));

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }
            try
            {
                mRecorder.start();
            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " + android.util.Log.getStackTraceString(e));
            }

            //mEMA = 0.0;
        }

    }
    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void updateTv(){
        mStatusView.setText(Double.toString((getAmplitudeEMA())) + " dB");
    }
    public double soundDb(double ampl){
        return  20 * Math.log10(getAmplitudeEMA() / ampl);
    }
    public double getAmplitude() {
        if (mRecorder != null) {

            return  (mRecorder.getMaxAmplitude());}
        else
            return 0;

    }
    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

}
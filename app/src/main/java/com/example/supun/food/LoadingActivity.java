package com.example.supun.food;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Menu(getBaseContext()).updateCategories(getBaseContext());
        new DownloadFilesTask().execute();


    }


    private class DownloadFilesTask extends AsyncTask<Integer,Integer,Long> {
        protected Long doInBackground(Integer... v) {

            while(GlobalState.loadingImageCount>0){
                Log.d("loadingActivity","waiting");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {
            Intent intent = new Intent(getBaseContext(), OrderActivity.class);

            startActivity(intent);
            finish();
        }
    }



}

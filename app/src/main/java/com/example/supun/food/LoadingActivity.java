package com.example.supun.food;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
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

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String username = sharedPref.getString(getString(R.string.username_key), "");
            Log.d("loadingActivty","username: "+username);
            if(username.equals("")){
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);

                startActivity(intent);

            }
            else{
                GlobalState.setCurrentUsername(username);
                GlobalState.setPhoneNumber(sharedPref.getString(getString(R.string.phone_number_key), ""));
                Intent intent = new Intent(getBaseContext(), OrderActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }



}

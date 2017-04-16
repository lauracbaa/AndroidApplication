package com.example.markohare.apitest2.loginregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.markohare.apitest2.MainActivity;
import com.example.markohare.apitest2.R;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
        Thread time = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent mainIntent = new Intent(Home.this, MainActivity.class);
                    Home.this.startActivity(mainIntent);
                }
            }
        };
        time.start();
    }
}


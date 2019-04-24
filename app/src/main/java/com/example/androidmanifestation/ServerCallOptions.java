package com.example.androidmanifestation;

import android.databinding.DataBinderMapper;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.androidmanifestation.databinding.ActivityServerCallOptionsBinding;

public class ServerCallOptions extends AppCompatActivity implements View.OnClickListener {

    ActivityServerCallOptionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_server_call_options);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNoLibrary: {
                break;
            }
            case R.id.btnLoopj: {
                break;
            }
            case R.id.btnVolley: {
                break;
            }
            case R.id.btnOkhttp: {
                break;
            }
            case R.id.btnRetrofit: {
                break;
            }
        }
    }
}

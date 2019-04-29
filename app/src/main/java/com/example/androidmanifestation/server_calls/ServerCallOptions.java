package com.example.androidmanifestation.server_calls;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.androidmanifestation.R;
import com.example.androidmanifestation.databinding.ActivityServerCallOptionsBinding;
import com.example.androidmanifestation.server_calls.LoopjCallActivity;
import com.example.androidmanifestation.server_calls.NoLibraryCallActivity;
import com.example.androidmanifestation.server_calls.OkhttpCallActivity;
import com.example.androidmanifestation.server_calls.VolleyServerCall;
import com.example.androidmanifestation.server_calls.retro_call.RetroCallActivity;

public class ServerCallOptions extends AppCompatActivity implements View.OnClickListener {

    ActivityServerCallOptionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_server_call_options);

        binding.btnNoLibrary.setOnClickListener(this);
        binding.btnLoopj.setOnClickListener(this);
        binding.btnVolley.setOnClickListener(this);
        binding.btnOkhttp.setOnClickListener(this);
        binding.btnRetrofit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNoLibrary: {
                Intent intent = new Intent(this, NoLibraryCallActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btnLoopj: {
                Intent intent = new Intent(this, LoopjCallActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btnVolley: {
                Intent intent = new Intent(this, VolleyServerCall.class);
                startActivity(intent);
                break;
            }
            case R.id.btnOkhttp: {
                Intent intent = new Intent(this, OkhttpCallActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btnRetrofit: {
                Intent intent = new Intent(this, RetroCallActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}

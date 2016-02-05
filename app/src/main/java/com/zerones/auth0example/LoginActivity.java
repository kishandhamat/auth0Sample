package com.zerones.auth0example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.auth0.core.Token;
import com.auth0.core.UserProfile;
import com.auth0.lock.Lock;
import com.auth0.lock.LockActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    LocalBroadcastManager broadcastManager;


    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceHelper = new PreferenceHelper(this);
        if (!TextUtils.isEmpty(preferenceHelper.getUserId())) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        findViewById(R.id.btnLogin).setOnClickListener(this);

        broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                UserProfile profile = intent.getParcelableExtra(Lock.AUTHENTICATION_ACTION_PROFILE_PARAMETER);
                preferenceHelper.putUserId(profile.getId());
                Token token = intent.getParcelableExtra(Lock.AUTHENTICATION_ACTION_TOKEN_PARAMETER);
                //Your code to handle login information.
                // Toast.makeText(LoginActivity.this, "Id : " + profile.getId() + " Token :" + token.getIdToken(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

            }
        }, new IntentFilter(Lock.AUTHENTICATION_ACTION));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                Intent lockIntent = new Intent(this, LockActivity.class);
                startActivity(lockIntent);
                break;
            default:
                break;
        }
    }
}

package com.example.android.notepad;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.notepad.lockview.LockView;

public class LoginActivity extends Activity {

    LockView mLockView;
    public static SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(LoginActivity.this, NotesList.class);
        startActivity(intent);
        LoginActivity.this.finish();
   /*     setContentView(R.layout.activity_login);

        mLockView = (LockView) findViewById(R.id.lock_view);
        mLockView.setOnLockChangeListeners(new LockView.OnLockChangeListener() {
            @Override
            public void setOnLockSuccessed(StringBuilder passward) {
                SharedPreferences pref = getSharedPreferences("keys", MODE_PRIVATE);
                String key = pref.getString("passward", null);
                if (key == null) {
                    Toast.makeText(LoginActivity.this, "设置初始密码", Toast.LENGTH_SHORT).show();
                    mEditor = getSharedPreferences("keys", MODE_PRIVATE).edit();
                    mEditor.putString("passward", passward.toString());
                    mEditor.apply();
                } else if (key.equals(passward.toString())) {
                    Intent intent = new Intent(LoginActivity.this, NotesList.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                } else {
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void setOnLockError() {
                Toast.makeText(LoginActivity.this, "失败", Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}

package cn.leo.magicmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.leo.messenger.MagicMessenger;
import cn.leo.messenger.MessageCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvTest;
    private Runnable mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvTest = findViewById(R.id.tvTest);
        mTvTest.setOnClickListener(this);

        MagicMessenger.subscribe("activity1", new MessageCallback() {
            @Override
            public void onMsgCallBack(Bundle data) {
                String test = data.getString("test");
                String text = "11111111" + test;
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                Log.e("111111111", "onMsgCallBack: " + text);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MagicMessenger.unsubscribe("activity1");
    }
}

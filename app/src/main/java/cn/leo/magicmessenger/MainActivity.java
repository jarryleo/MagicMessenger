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
                String text = "activity1 接收到消息" + test;
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                Log.e("activity1 ", "onMsgCallBack: " + text);
            }
        });
        Intent intent = new Intent(this, TestService.class);
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);

        Bundle bundle = new Bundle();
        bundle.putString("test", "activity1 发送消息到服务");
        MagicMessenger.post("service", bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MagicMessenger.unsubscribe("activity1");
        Intent intent = new Intent(this, TestService.class);
        stopService(intent);
    }
}

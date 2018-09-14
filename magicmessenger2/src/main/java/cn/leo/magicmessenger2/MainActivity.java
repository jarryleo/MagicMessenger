package cn.leo.magicmessenger2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.leo.messenger.MagicMessenger;
import cn.leo.messenger.MessageCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvTest = findViewById(R.id.tvTest);
        mTvTest.setOnClickListener(this);
        MagicMessenger.subscribe("app2", new MessageCallback() {
            @Override
            public void onMsgCallBack(Bundle data) {
                String test = data.getString("test");
                String text = "app2 接收到消息" + test;
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                mTvTest.setText(test);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("test", "发送消息到另一个app成功");
        MagicMessenger.post("activity1", bundle);
    }
}

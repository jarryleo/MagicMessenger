package cn.leo.magicmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.leo.magic_messenger.MagicMessenger;
import cn.leo.magic_messenger.MessageCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvTest;
    private Runnable mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvTest = findViewById(R.id.tvTest);
        mTvTest.setOnClickListener(this);

        MagicMessenger.subscribe(this, new MessageCallback() {
            @Override
            public void onMsgCallBack(Bundle data) {
                String test = data.getString("test");
                String text = "11111111" + test;
                //Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                Log.e("111111111", "onMsgCallBack: " + text);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
        mAction = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString("test", "测试跨进程第一个页面");
                MagicMessenger.post(bundle);
               //mTvTest.postDelayed(mAction,1000);
            }
        };
        mTvTest.postDelayed(mAction,5000);
    }

}

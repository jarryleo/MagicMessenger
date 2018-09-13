package cn.leo.magicmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.leo.messenger.MagicMessenger;
import cn.leo.messenger.MessageCallback;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("test", "测试跨进程第二个页面");
                MagicMessenger.post("activity1", bundle);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(Main2Activity.this, Main3Activity.class));
            }
        });
        MagicMessenger.subscribe("activity2", new MessageCallback() {
            @Override
            public void onMsgCallBack(Bundle data) {
                String test = data.getString("test");
                String text = "222222222" + test;
                Toast.makeText(Main2Activity.this, text, Toast.LENGTH_SHORT).show();
                Log.e("22222222", "onMsgCallBack: " + text);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MagicMessenger.unsubscribe("activity2");
    }
}

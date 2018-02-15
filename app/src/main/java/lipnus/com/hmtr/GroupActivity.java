package lipnus.com.hmtr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import lipnus.com.hmtr.chatting.ChatActivity;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    }


    public void onClick_intro(View v){
        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
        startActivity(intent);
    }
}

package lipnus.com.hmtr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import lipnus.com.hmtr.chatting.ChatActivity;
import lipnus.com.hmtr.chatting.ChatActivity3;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //스플래쉬 스크린 역할

        //버전체크 & 설문중이었는지 확인

    }

    public void onClick_intro_splash(View v){

        Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
        startActivity(intent);
    }

    public void onClick_intro_group(View v){

        Intent intent = new Intent(getApplicationContext(),GroupActivity.class);
        startActivity(intent);
    }


    public void onClick_intro_welcome(View v){

        Intent intent = new Intent(getApplicationContext(),WelcomeActivity.class);
        startActivity(intent);
    }


    public void onClick_intro_chat(View v){

        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        startActivity(intent);
    }

    public void onClick_intro_chat3(View v){

        GlobalApplication.category="aptitude";
        Intent intent = new Intent(getApplicationContext(), ChatActivity3.class);
        startActivity(intent);
    }

    public void onClick_intro_chat4(View v){

        GlobalApplication.category="balance";
        GlobalApplication.sequence=1;
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        startActivity(intent);
    }

}

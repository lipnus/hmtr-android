package lipnus.com.hmtr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //스플래쉬 스크린 역할

        //버전체크 & 설문중이었는지 확인

    }

    public void onClick_intro(View v){

        Intent intent = new Intent(getApplicationContext(),GroupActivity.class);
        startActivity(intent);
    }
}

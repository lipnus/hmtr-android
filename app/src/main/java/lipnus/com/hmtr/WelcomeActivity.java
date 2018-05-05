package lipnus.com.hmtr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.hmtr.chatting.ChatActivity;

public class WelcomeActivity extends AppCompatActivity {

    int scriptIndex; //몇번째 대사인지(1~3)

    int imgPath= 0;
    String script= "";


    @BindView(R.id.welcome_script_tv)
    TextView scriptTv;

    @BindView(R.id.welcome_charactor_iv)
    ImageView charactorIv;

    @BindView(R.id.welcome_charactor2_iv)
    ImageView charactor2Iv;

    static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //액티비티 화면 전환효과
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        Glide.with(getApplicationContext())
                .load( R.drawable.tory )
                .into( charactorIv );
        charactorIv.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(getApplicationContext())
                .load( R.drawable.many )
                .into( charactor2Iv );
        charactor2Iv.setScaleType(ImageView.ScaleType.FIT_XY);

        scriptIndex=1;
        setScreen();
    }


    public void setScreen(){

        switch (scriptIndex){
            case 1:
                script = "안녕하세요. \n저희는 토리와 매니입니다";
                break;
            case 2:
                script= "에듀벨 진단검사(EBTI)는 학교생활에 대한 꼼꼼한 분석을 토대로 맞춤형 솔루션을 제공하여 성공적인 학교생활을 할 수 있도록 도와주는 검사입니다.";
                break;
            case 3:
                script= " 지금부터 저희와 함께 시작해볼까요?";
                break;
            case 4:
                //다음으로 이동
                break;

        }

        YoYo.with(Techniques.FadeOut)
                .duration(500)
                .playOn(scriptTv);

        //0.5초후 실행
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mHandler.sendEmptyMessage(0);	// 실행이 끝난후 알림
            }
        }, 500);

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if(scriptIndex==4){
                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    scriptTv.setText( script );
                    YoYo.with(Techniques.FadeIn)
                            .duration(500)
                            .playOn(scriptTv);
                }
            }
        };

    }

    //화면을 터치했을 때
    public void onClick_welcome(View v){
        scriptIndex++;
        setScreen();
    }

    public void onClick_welcome_img(View v){

        scriptIndex++;
        setScreen();

        YoYo.with(Techniques.RubberBand)
                .duration(800)
                .playOn(charactorIv);
    }

    public void onClick_welcome_img2(View v){

        scriptIndex++;
        setScreen();

        YoYo.with(Techniques.RubberBand)
                .duration(800)
                .playOn(charactor2Iv);
    }
}
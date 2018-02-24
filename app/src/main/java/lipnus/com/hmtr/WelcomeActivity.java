package lipnus.com.hmtr;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    int scriptIndex; //몇번째 대사인지(1~3)

    int imgPath= 0;
    String script= "";


    @BindView(R.id.welcome_script_tv)
    TextView scriptTv;

    @BindView(R.id.welcome_charactor_iv)
    ImageView charactorIv;

    @BindView(R.id.welcome_btn)
    Button nextBtn;

    static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        setScreen();
    }


    public void setScreen(){

        switch (scriptIndex){
            case 1:
                script = "안녕하세요. 저는 토리입니다";
                imgPath = R.drawable.tory_1;
                break;
            case 2:
                script= "MKBT는 현재 자신의 상황 및 수준을 파악해 균형있는 학습,진로,진학을 계획하여 성공적인 학교생활을 할 수 있도록 도와주는 검사입니다.";
                imgPath = R.drawable.tory_2;
                break;
            case 3:
                script= "저와함께 지금부터 시작해볼까요?";
                imgPath = R.drawable.tory_3;
                break;
        }


        YoYo.with(Techniques.FadeOutUp)
                .duration(600)
                .playOn(scriptTv);

        YoYo.with(Techniques.FadeOut)
                .duration(1000)
                .playOn(charactorIv);

        YoYo.with(Techniques.FadeOut)
                .duration(600)
                .playOn(nextBtn);


        //1초후 실행
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mHandler.sendEmptyMessage(0);	// 실행이 끝난후 알림
            }
        }, 1000);

        mHandler = new Handler() {
            public void handleMessage(Message msg) {

                // 실행이 끝난후 실행
                nextBtn.setVisibility(View.VISIBLE);

                scriptTv.setText( script );
                Glide.with(getApplicationContext())
                        .load( imgPath )
                        .into( charactorIv );
                charactorIv.setScaleType(ImageView.ScaleType.FIT_XY);

                YoYo.with(Techniques.SlideInDown)
                        .duration(1500)
                        .playOn(scriptTv);

                YoYo.with(Techniques.FadeIn)
                        .duration(600)
                        .playOn(charactorIv);

                YoYo.with(Techniques.FadeIn)
                        .duration(1000)
                        .playOn(nextBtn);

            }
        };













    }

    public void onClick_welcome(View v){
        scriptIndex++;
        setScreen();
    }
}

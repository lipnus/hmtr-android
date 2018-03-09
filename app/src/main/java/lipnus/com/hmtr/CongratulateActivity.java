package lipnus.com.hmtr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CongratulateActivity extends AppCompatActivity {

    @BindView(R.id.congratulate_script_tv)
    TextView scriptTv;

    @BindView(R.id.congratulate_charactor_iv)
    ImageView charactorIv;

    @BindView(R.id.congratulate_btn)
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulate);
        ButterKnife.bind(this);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //액티비티 화면 전환효과
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                setScreen();
            }
        }, 500);


    }


    public void setScreen(){

        String script = "모든 검사를 마쳤어요!";
        int imgPath = R.drawable.tory_3;
        nextBtn.setVisibility(View.VISIBLE);

        scriptTv.setText(script);
        Glide.with(getApplicationContext())
                .load( imgPath )
                .into( charactorIv );
        charactorIv.setScaleType(ImageView.ScaleType.FIT_XY);


        YoYo.with(Techniques.SlideInUp)
                .duration(1300)
                .playOn(scriptTv);

        YoYo.with(Techniques.Bounce)
                .duration(500)
                .repeat(5)
                .playOn(charactorIv);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(nextBtn);

    }

    public void onClick_intro_congratulate(View v){
        Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
        startActivity(intent);
    }

    public void onClick_welcome_img(View v){
        YoYo.with(Techniques.RubberBand)
                .duration(800)
                .playOn(charactorIv);
    }
}
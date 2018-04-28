package lipnus.com.hmtr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.hmtr.retro.RetroCallback;
import lipnus.com.hmtr.retro.RetroClient;

public class CongratulateActivity extends AppCompatActivity {

    @BindView(R.id.congratulate_script_tv)
    TextView scriptTv;

    @BindView(R.id.congratulate_charactor_iv)
    ImageView charactorIv;

    @BindView(R.id.congratulate_charactor2_iv)
    ImageView charactor2Iv;

    RetroClient retroClient;
    String LOG = "CCGG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulate);
        ButterKnife.bind(this);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //액티비티 화면 전환효과
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //레트로핏
        retroClient = RetroClient.getInstance(this).createBaseApi();



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

        scriptTv.setText(script);

        //토리메리
        Glide.with(getApplicationContext())
                .load( R.drawable.tory )
                .into( charactorIv );
        charactorIv.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(getApplicationContext())
                .load( R.drawable.many )
                .into( charactor2Iv );
        charactor2Iv.setScaleType(ImageView.ScaleType.FIT_XY);


        YoYo.with(Techniques.SlideInUp)
                .duration(1300)
                .playOn(scriptTv);

        YoYo.with(Techniques.Bounce)
                .duration(500)
                .repeat(5)
                .playOn(charactorIv);

        YoYo.with(Techniques.Bounce)
                .duration(400)
                .repeat(5)
                .playOn(charactor2Iv);
    }

    public void onClick_intro_congratulate(View v){
        postCaldata(GlobalApplication.userinfo_pk);
    }

    public void onClick_welcome_img(View v){
        YoYo.with(Techniques.RubberBand)
                .duration(800)
                .playOn(charactorIv);
    }

    public void postCaldata(int userinfo_pk){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("userinfo_pk", userinfo_pk);

        retroClient.postCaldata(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, "Error: " + t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {

                Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int code) {

                Log.e(LOG, "Failure: " + String.valueOf(code));
            }
        });
    }

}
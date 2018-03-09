package lipnus.com.hmtr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportActivity extends AppCompatActivity {

    @BindView(R.id.report_title_iv)
    ImageView titleIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //액티비티 화면 전환효과
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setTitle();
    }

    public void setTitle(){
        Glide.with(getApplicationContext())
                .load( R.drawable.report_title )
                .into( titleIv );
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}



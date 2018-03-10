package lipnus.com.hmtr;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportActivity extends AppCompatActivity {

    @BindView(R.id.report_title_iv)
    ImageView titleIv;


    @BindView(R.id.report_line_chart)
    LineChart lineChart;

    @BindView(R.id.report_bar_chart)
    HorizontalBarChart barChart;

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
        setChart();
    }

    public void setChart(){

        List<Entry> entries = new ArrayList<Entry>();

        entries.add( new Entry(1,20) );
        entries.add( new Entry(2,30) );
        entries.add( new Entry(3,10) );
        entries.add( new Entry(4,50) );

        LineDataSet lineDataSet = new LineDataSet(entries, "ㅋㅋㅋ");
        lineDataSet.setColor( Color.parseColor("#604a7b") );

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData( lineData );
        lineChart.invalidate();




        List<BarEntry> entries2 = new ArrayList<>();
        entries2.add(new BarEntry(0, 30f));
        entries2.add(new BarEntry(1f, 80f));
        entries2.add(new BarEntry(2f, 60f));
        entries2.add(new BarEntry(3f, 30f));


        BarDataSet set = new BarDataSet(entries2, "행동유형척도");
        BarData data = new BarData(set);

        data.setBarWidth(0.5f); // set custom bar width


        barChart.setData(data);
        barChart.setVisibleYRange(0,100f, YAxis.AxisDependency.RIGHT);
        barChart.set

        barChart.invalidate(); // refresh




    }

    public void setTitle(){
        Glide.with(getApplicationContext())
                .load( R.drawable.report_title )
                .into( titleIv );
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}



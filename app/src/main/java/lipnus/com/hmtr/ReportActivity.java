package lipnus.com.hmtr;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportActivity extends AppCompatActivity {

    @BindView(R.id.report_title_iv)
    ImageView titleIv;


    @BindView(R.id.report_radar_chart)
    RadarChart radarChart;

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
        setRadarChart();
        setBarChar();
    }

    public void setRadarChart(){

        radarChart.setBackgroundColor(Color.rgb(60, 65, 82));
        radarChart.getDescription().setEnabled(false);

        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.LTGRAY);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(Color.LTGRAY);
        radarChart.setWebAlpha(100);
        setData();

        radarChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"채움형", "세움형", "키움형", "돋움형"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.WHITE);




        YAxis yAxis = radarChart.getYAxis();
        yAxis.setLabelCount(4, false);
        yAxis.setTextSize(10f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setDrawLabels(false);

        radarChart.getDescription().setEnabled(false);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.LTGRAY);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(Color.LTGRAY);
        radarChart.setWebAlpha(100);


        radarChart.invalidate();
    }

    public void setData() {
        int count = 4;

        ArrayList<Integer> result;
        Intent get_intent = getIntent();
        result = (ArrayList<Integer>) get_intent.getSerializableExtra("result");

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();



        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
            entries1.add(new RadarEntry(i*20+10));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "DAESUNG ACADEMY");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        radarChart.setData(data);
        radarChart.invalidate();
    }




    public void setBarChar(){
        List<BarEntry> entries2 = new ArrayList<>();
        entries2.add(new BarEntry(0, 10f));
        entries2.add(new BarEntry(1f, 20f));
        entries2.add(new BarEntry(2f, 60f));
        entries2.add(new BarEntry(3f, 30f));


        BarDataSet dataSet = new BarDataSet(entries2, "행동유형척도");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor( Color.parseColor("#604a7b"));

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f); // set custom bar width


        barChart.setData(data);
        barChart.setDoubleTapToZoomEnabled(false);

        //기준열
        XAxis xAxis = barChart.getXAxis();
        xAxis.setEnabled(false);

        //위쪽
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setEnabled(false);

        //아래쪽
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setAxisMinimum(0);
        rightAxis.setAxisMaximum(100);

        barChart.invalidate(); // refresh
    }

    public void setTitle(){
        Glide.with(getApplicationContext())
                .load( R.drawable.report_title )
                .into( titleIv );
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}



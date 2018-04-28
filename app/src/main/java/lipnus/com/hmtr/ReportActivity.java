package lipnus.com.hmtr;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.hmtr.retro.Response.ReportData;
import lipnus.com.hmtr.retro.RetroCallback;
import lipnus.com.hmtr.retro.RetroClient;

public class ReportActivity extends AppCompatActivity {

    @BindView(R.id.report_title_iv)
    ImageView titleIv;

    @BindView(R.id.report_basic_name) TextView basic_nameTv;
    @BindView(R.id.report_basic_testdate) TextView basic_testdateTv;
    @BindView(R.id.report_basic_birth) TextView basic_birthTv;
    @BindView(R.id.report_basic_sex) TextView basic_sexTv;
    @BindView(R.id.report_basic_school) TextView basic_schoolTv;
    @BindView(R.id.report_basic_grade) TextView basic_gradeTv;

    @BindView(R.id.report_cw_raw) TextView cw_rawTv;
    @BindView(R.id.report_cw_standard) TextView cw_standardTv;
    @BindView(R.id.report_sw_raw) TextView sw_rawTv;
    @BindView(R.id.report_sw_standard) TextView sw_standardTv;
    @BindView(R.id.report_kw_raw) TextView kw_rawTv;
    @BindView(R.id.report_kw_standard) TextView kw_standardTv;
    @BindView(R.id.report_dw_raw) TextView dw_rawTv;
    @BindView(R.id.report_dw_standard) TextView dw_standardTv;

    @BindView(R.id.report_learning_tv) TextView learningTv;
    @BindView(R.id.report_course_tv) TextView courseTv;
    @BindView(R.id.report_enterance_tv) TextView enteranceTv;
    @BindView(R.id.report_learning_grade_tv) TextView learningGradeTv;
    @BindView(R.id.report_course_grade_tv) TextView courseGradeTv;
    @BindView(R.id.report_enterance_grade_tv) TextView enteranceGradeTv;
    @BindView(R.id.report_balance_sum_tv) TextView balanceSumTv;

    @BindView(R.id.report_behavior_comment_tv) TextView behaviorCommentTv;
    @BindView(R.id.report_learning_comment_tv) TextView leanringCommentTv;
    @BindView(R.id.report_course_comment_tv) TextView courseCommentTv;
    @BindView(R.id.report_entrance_comment_tv) TextView entranceCommentTv;

    @BindView(R.id.report_scroll)
    ScrollView scrollView;





    @BindView(R.id.report_behavior_radar_chart)
    RadarChart radarBehaviorChart;

    @BindView(R.id.report_balance_radar_chart)
    RadarChart radarBalanceChart;

    @BindView(R.id.report_bar_chart)
    HorizontalBarChart barBehaviorChart;

    @BindView(R.id.report_aptitude_bar_chart)
    HorizontalBarChart barAptitudeChart;

    RetroClient retroClient;
    String LOG = "RRPP";

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //액티비티 화면 전환효과
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        retroClient = RetroClient.getInstance(this).createBaseApi();

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("USERDATA", 0);
        editor= setting.edit();

        postReportData( GlobalApplication.userinfo_pk );

        setTitle();

        setBehavior_Radar_Chart();
        setBalance_Radar_Chart();

    }


    public void postReportData(int userinfo_pk){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("userinfo_pk", userinfo_pk);

        retroClient.postReportData(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, "Error: " + t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                ReportData data = (ReportData)receivedData;
                Log.e(LOG, "Success: " + String.valueOf(code));

                setBehavior_Radar_Data(data);
                setBehavior_Bar_Char(data);
                setAptitude_Bar_Char(data);
                setBalanceRadarData(data);

                setBasic(data);
                setBehavior(data);
                setBalance(data);
            }

            @Override
            public void onFailure(int code) {

                Log.e(LOG, "Failure: " + String.valueOf(code));
            }
        });

    }

    public void setBasic(ReportData reportData){
        basic_testdateTv.setText( reportData.date );
        basic_nameTv.setText( reportData.name );
        basic_birthTv.setText( reportData.birth );
        basic_sexTv.setText( reportData.gender );
        basic_schoolTv.setText( reportData.group_name );
        basic_gradeTv.setText( reportData.grade );
    }
    public void setBehavior(ReportData reportData){

        cw_rawTv.setText( reportData.cw_raw_score + ""  );
        cw_standardTv.setText( (int)reportData.cw_standard_score + "%" );
        sw_rawTv.setText( reportData.sw_raw_score + "" );
        sw_standardTv.setText( (int)reportData.sw_standard_score + "%" );
        kw_rawTv.setText( reportData.kw_raw_score + "" );
        kw_standardTv.setText( (int)reportData.kw_standard_score + "%" );
        dw_rawTv.setText( reportData.dw_raw_score + "" );
        dw_standardTv.setText( (int)reportData.dw_standard_score + "%" );

        behaviorCommentTv.setText(reportData.behavior_comment);
    }
    public void setBalance(ReportData reportData){
        learningTv.setText(reportData.learning_score + "");
        courseTv.setText(reportData.course_score + "");
        enteranceTv.setText(reportData.entrance_score + "");
        learningGradeTv.setText(reportData.learning_grade + "");
        courseGradeTv.setText(reportData.course_grade + "");
        enteranceGradeTv.setText(reportData.entrance_grade + "");

        balanceSumTv.setText(reportData.total_score_lce + "");

        //코멘트
        leanringCommentTv.setText(reportData.learning_comment);
        courseCommentTv.setText(reportData.course_comment);
        entranceCommentTv.setText(reportData.entrance_comment);

    }

    public void setBehavior_Radar_Chart(){

        radarBehaviorChart.getDescription().setEnabled(false);

        radarBehaviorChart.setRotationEnabled(false);
        radarBehaviorChart.setTouchEnabled(false);

        radarBehaviorChart.setWebLineWidth(1f);
        radarBehaviorChart.setWebColor( Color.parseColor("#909090")  );
        radarBehaviorChart.setWebLineWidthInner(1f);
        radarBehaviorChart.setWebColorInner(Color.parseColor("#909090") );
        radarBehaviorChart.setWebAlpha(100);
//        setBehavior_Radar_Data(reportData);

        radarBehaviorChart.animateXY(
                2000, 2000,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = radarBehaviorChart.getXAxis();
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
        xAxis.setTextColor(Color.parseColor("#000000") );




        YAxis yAxis = radarBehaviorChart.getYAxis();
        yAxis.setLabelCount(8, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setDrawLabels(false);

        Legend legend = radarBehaviorChart.getLegend();
        legend.setEnabled(false);

    }
    public void setBehavior_Radar_Data(ReportData reportData) {

        ArrayList<RadarEntry> entries = new ArrayList<>();

        //데이터입력
        entries.add(new RadarEntry((int)reportData.cw_standard_score));
        entries.add(new RadarEntry((int)reportData.sw_standard_score));
        entries.add(new RadarEntry((int)reportData.kw_standard_score));
        entries.add(new RadarEntry((int)reportData.dw_standard_score));

        RadarDataSet set1 = new RadarDataSet(entries, "행동유형분석");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor( Color.parseColor("#604a7b") );
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(1f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(true);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);


        radarBehaviorChart.setData(data);
        radarBehaviorChart.invalidate();
    }
    public void setBehavior_Bar_Char(ReportData data){

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, (float)data.dw_standard_score));
        entries.add(new BarEntry(1, (float)data.kw_standard_score));
        entries.add(new BarEntry(2, (float)data.sw_standard_score));
        entries.add(new BarEntry(3, (float)data.cw_standard_score));


        BarDataSet dataSet = new BarDataSet(entries, "행동유형척도");
        dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSet.setColor( Color.parseColor("#604a7b"));


        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f); // set custom bar width


        barBehaviorChart.setData(barData);
        barBehaviorChart.setDoubleTapToZoomEnabled(false);
        barBehaviorChart.getDescription().setEnabled(false);
        barBehaviorChart.setTouchEnabled(false);


        Legend legend = barBehaviorChart.getLegend();
        legend.setEnabled(false);


        //기준열
        XAxis xAxis = barBehaviorChart.getXAxis();
        xAxis.setEnabled(false);

        //위쪽
        YAxis leftAxis = barBehaviorChart.getAxisLeft();
        leftAxis.setEnabled(false);

        //아래쪽
        YAxis rightAxis = barBehaviorChart.getAxisRight();
        rightAxis.setAxisMinimum(0);
        rightAxis.setAxisMaximum(100);

        barBehaviorChart.invalidate(); // refresh


        barBehaviorChart.animateXY(
                3000, 3000,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);
    }



    public void setAptitude_Bar_Char(ReportData reportData){
        List<BarEntry> entries2 = new ArrayList<>();
        entries2.add(new BarEntry(0, reportData.pnc_F*10)); //관심
        entries2.add(new BarEntry(1f, reportData.pnc_E*10)); //창의
        entries2.add(new BarEntry(2f, reportData.pnc_D*10)); //계획
        entries2.add(new BarEntry(3f, reportData.pnc_C*10)); //열정
        entries2.add(new BarEntry(4f, reportData.pnc_B*10)); //주도
        entries2.add(new BarEntry(5f, reportData.pnc_A*10)); //실천


        //X축
        XAxis xAxis = barAptitudeChart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"관심형", "창의형", "계획형", "열정형", "주도형", "실천형"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.parseColor("#909090") );

        //Y축
        YAxis rightAxis = barAptitudeChart.getAxisRight();
        rightAxis.setAxisMinimum(0);
        rightAxis.setAxisMaximum(100);
        YAxis leftAxis = barAptitudeChart.getAxisLeft();
        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(100);



        BarDataSet dataSet = new BarDataSet(entries2, "행동유형척도");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor( Color.parseColor("#604a7b"));


        BarData data = new BarData(dataSet);
        data.setBarWidth(0.3f); // set custom bar width

        barAptitudeChart.setData(data);
        barAptitudeChart.setDoubleTapToZoomEnabled(false);
        barAptitudeChart.getDescription().setEnabled(false);
        barAptitudeChart.setTouchEnabled(false);


        Legend legend = barAptitudeChart.getLegend();
        legend.setEnabled(false);


        barAptitudeChart.invalidate(); // refresh
    }



    public void setBalance_Radar_Chart(){

        radarBalanceChart.getDescription().setEnabled(false);

        radarBalanceChart.setWebLineWidth(1f);
        radarBalanceChart.setWebColor( Color.parseColor("#909090")  );
        radarBalanceChart.setWebLineWidthInner(1f);
        radarBalanceChart.setWebColorInner(Color.parseColor("#909090") );
        radarBalanceChart.setWebAlpha(100);
//        setBalanceRadarData(reportData);

        radarBalanceChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        radarBalanceChart.setTouchEnabled(false);

        XAxis xAxis = radarBalanceChart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"학습상황", "진로상황", "진학상황"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.parseColor("#000000") );




        YAxis yAxis = radarBalanceChart.getYAxis();
        yAxis.setLabelCount(3, false);
        yAxis.setTextSize(12f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setDrawLabels(false);

        Legend legend = radarBalanceChart.getLegend();
        legend.setEnabled(false);

    }

    public void setBalanceRadarData(ReportData reportData) {
        ArrayList<RadarEntry> entries = new ArrayList<RadarEntry>();

        //데이터입력
        entries.add(new RadarEntry( (float)reportData.learning_score) );
        entries.add(new RadarEntry((float)reportData.course_score) );
        entries.add(new RadarEntry((float)reportData.entrance_score) );

        RadarDataSet set1 = new RadarDataSet(entries, "밸런스진단");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor( Color.parseColor("#604a7b") );
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(1f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(true);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);

        radarBalanceChart.setData(data);
        radarBalanceChart.invalidate();
    }

    public void setTitle(){
        Glide.with(getApplicationContext())
                .load( R.drawable.report_title )
                .into( titleIv );
        titleIv.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    public void onClick_report_delete(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("정말 초기화 하시겠습니까?");

        builder.setPositiveButton("네",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //프레퍼런스의 유저정보를 0으로 리셋
                        editor.clear();
                        editor.commit();


                        //앱 재실행
                        Intent iT = new Intent(getApplicationContext(), SplashActivity.class);
                        startActivity(iT);
                        finish();

                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //무반응

                    }
                });
        builder.show();


    }

    public void onClick_report_save(View v){

        //퍼미션 리스너
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                print();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("화면저장 권한요청")
                .setDeniedMessage("권한요청이 거부되었습니다 거부")
                .setPermissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();

    }


    private void print(){
//        ProgressDialog dialog = new ProgressDialog(getApplicationContext());
//        dialog.setMessage("Saving...");
//        dialog.show();

        Bitmap bitmap = getBitmapFromView(scrollView,scrollView.getChildAt(0).getHeight(),scrollView.getChildAt(0).getWidth());
        try {
            File defaultFile = new File(Environment.getExternalStorageDirectory()+File.separator+"DCIM/Camera/");
            if (!defaultFile.exists())
                defaultFile.mkdirs();

            String filename = "EBTI.jpg";
            File file = new File(defaultFile,filename);
            if (file.exists()) {
                file.delete();
                file = new File(defaultFile,filename);
            }

            FileOutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();

//            dialog.dismiss();

            Toast.makeText(getApplication(), "보고서를 갤러리에 저장", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log
                    .e("EEEE", "에러: " + e);
//            dialog.dismiss();
            Toast.makeText(getApplication(), "보고서 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }

    //create bitmap from the view
    private Bitmap getBitmapFromView(View view,int height,int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

}



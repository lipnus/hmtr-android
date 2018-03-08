package lipnus.com.hmtr;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.hmtr.chatting.ChatActivity;
import lipnus.com.hmtr.chatting.ChatActivity3;
import lipnus.com.hmtr.retro.Response.DeleteAptitude;
import lipnus.com.hmtr.retro.Response.ServerInfo;
import lipnus.com.hmtr.retro.RetroCallback;
import lipnus.com.hmtr.retro.RetroClient;

public class SplashActivity extends AppCompatActivity {

    RetroClient retroClient;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @BindView(R.id.splash_logo_iv)
    ImageView logoIv;

    String LOG = "SSPP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi();

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("USERDATA", 0);
        editor= setting.edit();

        Glide.with(getApplicationContext())
                .load( R.drawable.logo )
                .into( logoIv );
        logoIv.setScaleType(ImageView.ScaleType.FIT_XY);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                postServerinfo();
            }
        }, 800);

    }

    public void postServerinfo(){
        int version = getVersion();


        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("version", version);

        retroClient.postServerInfo(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, "Error: " + t.toString());
                Toast.makeText(getApplicationContext(), "접속실패", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                ServerInfo data = (ServerInfo) receivedData;
                Log.e(LOG, "Success: " + String.valueOf(code) + ", " + String.valueOf(data.nickname));
                setAppInit(data);
            }

            @Override
            public void onFailure(int code) {

                Log.e(LOG, "Failure: " + String.valueOf(code));
                Toast.makeText(getApplicationContext(), "접속실패", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void postDeleteAptitude(int userinfo_pk){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("userinfo_pk", userinfo_pk);

        retroClient.postDeleteAptitude(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, "Error: " + t.toString());
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                DeleteAptitude data = (DeleteAptitude) receivedData;

                Intent intent = new Intent(getApplicationContext(), ChatActivity3.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int code) {

                Log.e(LOG, "Failure: " + String.valueOf(code));
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }



    public void setAppInit(ServerInfo data){

        Log.d(LOG, "서버: "+ data.version + " / 앱: " + getVersion());

        //업데이트 체크
        if( getVersion() < data.version ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("최신버전이 아닙니다. 업데이트가 필요합니다");

            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        finish();
                        }
                    });
            builder.show();

        }

        //NPC정보설정
        GlobalApplication.npcName = data.nickname;
        GlobalApplication.facePath = data.imgpath;

        //연속성 관리
        GlobalApplication.sequence = setting.getFloat("sequence", 0);
        GlobalApplication.category = setting.getString("category", "basic");
        GlobalApplication.userinfo_pk = setting.getInt("userinfo_pk", 0);

        Log.e(LOG, "sequence: " + GlobalApplication.sequence +
                " / category: " + GlobalApplication.category +
                " / userinfo_pk: " + GlobalApplication.userinfo_pk);

        if(GlobalApplication.userinfo_pk==0){

            Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
            startActivity(intent);
        }
        else if(GlobalApplication.category.equals("basic")){

            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
        }else if(GlobalApplication.category.equals("behavior")){

            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);

        }else if(GlobalApplication.category.equals("aptitude")){

            GlobalApplication.sequence = 0;

            //3에서 멈추면 3의 첫번째부터 다시 시작
            postDeleteAptitude( GlobalApplication.userinfo_pk );

        }else if(GlobalApplication.category.equals("balance")){

            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
        }
        else if(GlobalApplication.category.equals("report")){

            Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
            startActivity(intent);
        }

        finish();


    }


    public int getVersion(){
        int version=0;
        try {
            PackageInfo i = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = i.versionCode;
        } catch(PackageManager.NameNotFoundException e) { }

        return version;
    }
}

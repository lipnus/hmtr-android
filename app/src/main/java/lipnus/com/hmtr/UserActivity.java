package lipnus.com.hmtr;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.hmtr.retro.Response.User;
import lipnus.com.hmtr.retro.RetroCallback;
import lipnus.com.hmtr.retro.RetroClient;

public class UserActivity extends AppCompatActivity {

    String groupName; //그룹명
    int postCount; //몇번째 등록인지

    @BindView(R.id.profile_name_et)
    EditText nameEt;

    @BindView(R.id.profile_birth_et)
    EditText birthEt;

    @BindView(R.id.profile_phone_et)
    EditText phoneEt;

    @BindView(R.id.profile_email_et)
    EditText emailEt;


    @BindView(R.id.profile_result_tv)
    TextView resultTv;

    @BindView(R.id.profile_agree_check)
    com.rey.material.widget.CheckBox agreeChk;

    RetroClient retroClient;
    String LOG = "UUSS";

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("USERDATA", 0);
        editor= setting.edit();

        //호출할 때 같이 보낸 값 받아옴
        Intent iT = getIntent();
        groupName = iT.getExtras().getString("group_name", "group불러오기 실패");

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //일단 최초가입으로 간주. 서버에서 확인 후 재전송한다
        postCount = 1;
        retroClient = RetroClient.getInstance(this).createBaseApi();
    }

    //검사시작
    public void onClick_profile_start(View v){

        //입력값이 올바른지 확인
        if(!inputCheck()){ return; }

        postUser();
    }

    //올바르게 입력했는지 체크
    public boolean inputCheck(){

        String resultStr=null;

        //조건충족 체크
        if(nameEt.getText().toString().equals("")){
            resultStr = "이름을 입력해주세요";
        }else if(birthEt.getText().toString().equals("")){
            resultStr = "생년월일을 입력해주세요";
        }else if(birthEt.getText().toString().length() != 6){
            resultStr = "생년월일 6자리를 입력하세요";
        }else if( Integer.parseInt  (birthEt.getText().toString().substring(2,4) ) > 12 ){
            resultStr = "생년월일 형식이 올바르지 않습니다(월)";
        }else if( Integer.parseInt (birthEt.getText().toString().substring(4,6) ) > 31){
            resultStr = "생년월일 형식이 올바르지 않습니다(일)";
        }else if(phoneEt.getText().toString().equals("")){
            resultStr = "전화번호를 입력해주세요";
        }else if(phoneEt.getText().toString().length() != 11){
            resultStr = "전화번호가 올바르지 않습니다";
        }else if(emailEt.getText().toString().equals("")){
            resultStr = "이메일을 입력해주세요";
        }else if(!emailEt.getText().toString().contains("@")){
            resultStr = "이메일 형식이 올바르지 않습니다";
        }else{
            if(!agreeChk.isChecked()){
                resultStr = "개인정보 수집방침에 동의해주세요";
            }
        }

        resultTv.setText(resultStr);


        if(resultStr==null){
            return true;
        }else{
            return false;
        }
    }

    public void postUser(){

        String group_name = groupName;
        String user_name = nameEt.getText().toString();
        String user_birth = birthEt.getText().toString();
        String user_phone = phoneEt.getText().toString();
        String user_email = emailEt.getText().toString();
        int user_count = postCount;


        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("user_name", user_name);
        parameters.put("user_birth", user_birth);
        parameters.put("user_phone", user_phone);
        parameters.put("user_email", user_email);
        parameters.put("group_name", group_name);
        parameters.put("user_count", user_count);

        retroClient.postUser(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, "Error: " + t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                User data = (User)receivedData;
                Log.e(LOG, "Success: " + String.valueOf(code) + ", " + String.valueOf(data.result));
                setUser(data);
            }

            @Override
            public void onFailure(int code) {

                Log.e(LOG, "Failure: " + String.valueOf(code));
            }
        });
    }

    public void setUser(final User mUser){
        Log.e(LOG, mUser.result + ", " + mUser.date + " " + mUser.count + ", " + mUser.userinfo_pk);

        if(mUser.result.equals("already")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("이미 " + mUser.count +
                    "번 검사한 적이 있으며 최근의 검사일은 " + mUser.date + "입니다");

            builder.setPositiveButton("맞아요",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            //count+1해서 재전송(DB의 raw_user은 기존것으로 두고, raw_userinfo만 등록)
                            postCount = mUser.count+1;
                            postUser();
                        }
                    });
            builder.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //무반응

                        }
                    });
            builder.show();
        }
        else{
            setPreferrence(mUser.userinfo_pk);
            Intent iT = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(iT);
            finish();
        }

    }


    //기본정보를 스마트폰 내부에 저장한다
    public void setPreferrence(int userinfo_pk){

        //어플리케이션
        GlobalApplication.userinfo_pk = userinfo_pk;

        //프레퍼런스
        editor.putInt("userinfo_pk", userinfo_pk);
        editor.commit();

    }
}
package lipnus.com.hmtr;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.hmtr.retro.ResponseBody.User;
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

    @BindView(R.id.profile_group_tv)
    TextView groupTv;

    @BindView(R.id.profile_result_tv)
    TextView resultTv;

    RetroClient retroClient;
    String LOG = "UUSS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        //호출할 때 같이 보낸 값 받아옴
        Intent iT = getIntent();
        groupName = iT.getExtras().getString("group_name", "group불러오기 실패");
        groupTv.setText(groupName);

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

        //비어있는지 체크
        if(nameEt.getText().toString().equals("")){
            resultStr = "이름을 입력해주세요";
        }else if(birthEt.getText().toString().equals("")){
            resultStr = "생년월일을 입력해주세요";
        }else if(phoneEt.getText().toString().equals("")){
            resultStr = "전화번호를 입력해주세요";
        }else if(emailEt.getText().toString().equals("")){
            resultStr = "이메일을 입력해주세요";
        }

        resultTv.setText(resultStr);

        if(resultStr==null){
            return true;
        }else{
            return false;
        }
    }

    public void postUser(){

        Toast.makeText(getApplicationContext(), "유저확인", Toast.LENGTH_LONG).show();

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
            setPreferrence();
            Intent iT = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(iT);
        }

    }


    //기본정보를 스마트폰 내부에 저장한다
    public void setPreferrence(){
        //프레퍼런스
    }
}

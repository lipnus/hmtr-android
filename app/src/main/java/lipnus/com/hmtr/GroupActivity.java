package lipnus.com.hmtr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.hmtr.retro.Response.GroupExist;
import lipnus.com.hmtr.retro.RetroCallback;
import lipnus.com.hmtr.retro.RetroClient;

public class GroupActivity extends AppCompatActivity {

    String LOG = "RREE";
    RetroClient retroClient;

    @BindView(R.id.group_et)
    EditText groupEt;

    @BindView(R.id.group_tv)
    TextView groupTv;

    @BindView(R.id.group_edit_lr)
    LinearLayout groupLr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        retroClient = RetroClient.getInstance(this).createBaseApi();

        setEditText();

        YoYo.with(Techniques.FadeIn)
                .duration(2000)
                .playOn(groupLr);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(groupTv);
    }



    public void setEditText(){

        groupEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String inputStr = groupEt.getText().toString();

                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE: //Done버튼 눌렀을 때
                        postGroup( inputStr );
                }
                return true;
            }



        });

    }

    public void postGroup(String group_code){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("group_code", group_code);

        retroClient.postGroup(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {

                Log.e(LOG, "Error: " + t.toString());
                groupCheck("error");

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                GroupExist data = (GroupExist)receivedData;
                Log.e(LOG, "Success: " + String.valueOf(code) + ", " + String.valueOf(data.result));
                groupCheck(String.valueOf(data.result));
            }

            @Override
            public void onFailure(int code) {

                Log.e(LOG, "Failure: " + String.valueOf(code));
                groupCheck("error");
            }
        });
    }

    public void groupCheck(String resultCode){

        switch (resultCode){
            case "close":
                groupTv.setText("그룹이 닫혀있습니다. 선생님에게 문의하세요.");
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .playOn(groupLr);
                break;

            case "none":
                groupTv.setText("존재하지 않는 코드예요.");
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .playOn(groupLr);
                break;

            case "error":
                groupTv.setText("서버가 열려있지 않습니다");
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .playOn(groupLr);
                break;


            default:
                groupTv.setText(resultCode + "그룹에 입장합니다");
                Intent iT = new Intent(this, UserActivity.class);
                iT.putExtra("group_name", resultCode);
                startActivity(iT);
                finish();
                break;
        }
    }
}

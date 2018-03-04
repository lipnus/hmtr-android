package lipnus.com.hmtr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi();
    }


    public void onClick_group(View v){
        Toast.makeText(this, "그룹유무 체크", Toast.LENGTH_SHORT).show();

        String group_code = groupEt.getText().toString();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("group_code", group_code);

        retroClient.postGroup(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, "Error: " + t.toString());
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
            }
        });
    }

    public void groupCheck(String resultCode){

        switch (resultCode){
            case "close":
                groupTv.setText("그룹이 닫혀있습니다. 선생님에게 문의하세요.");
                break;

            case "none":
                groupTv.setText("존재하지 않는 코드예요.");
                break;

            default:
                groupTv.setText(resultCode + "그룹에 입장합니다");
                Intent iT = new Intent(this, UserActivity.class);
                iT.putExtra("group_name", resultCode);
                startActivity(iT);
//                finish();
                break;
        }
    }
}

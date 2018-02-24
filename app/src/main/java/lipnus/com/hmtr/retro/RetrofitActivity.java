package lipnus.com.hmtr.retro;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import lipnus.com.hmtr.R;
import lipnus.com.hmtr.retro.ResponseBody.GroupExist;


public class RetrofitActivity extends AppCompatActivity {

    TextView resonseTv;

    String codeResult;
    String idResult;
    String titleResult;
    String useridResult;
    String bodyResult;

    String LOG = "SSSS";
    RetroClient retroClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        resonseTv = (TextView) findViewById(R.id.connect_response_tv);
        retroClient = RetroClient.getInstance(this).createBaseApi();
    }


    public void onClick_post(View v){
        Toast.makeText(this, "그룹유무 체크", Toast.LENGTH_SHORT).show();

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("name", "aa");

        retroClient.postFirst(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, t.toString());

                codeResult = "Error";
                idResult="Error";
                titleResult ="Error";
                useridResult="Error";
                bodyResult="Error";

                setTextView();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                GroupExist data = (GroupExist)receivedData;

                codeResult = String.valueOf(code);
                idResult = String.valueOf(data.result);

                setTextView();
            }

            @Override
            public void onFailure(int code) {

                codeResult = String.valueOf(code);
                idResult="Failure";
                titleResult ="Failure";
                useridResult="Failure";
                bodyResult="Failure";

                setTextView();
            }
        });
    }


    public void setTextView(){

        String resultText = "codeResult: " + codeResult  + "\n" +
                "result: " + idResult  + "\n"
//                +
//                "titleResult: " + titleResult  + "\n" +
//                "useridResult: " + useridResult  + "\n" +
//                "bodyResult: " + bodyResult
                ;

        resonseTv.setText( resultText );
    }
}

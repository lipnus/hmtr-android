package lipnus.com.hmtr.chatting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.hmtr.BusProvider;
import lipnus.com.hmtr.GlobalApplication;
import lipnus.com.hmtr.InformationEvent;
import lipnus.com.hmtr.R;
import lipnus.com.hmtr.retro.Response.ChattingBasic;
import lipnus.com.hmtr.retro.RetroCallback;
import lipnus.com.hmtr.retro.RetroClient;

public class ChatActivity extends AppCompatActivity {


    ChatListViewAdapter chat_adapter;
    AnswerListViewAdapter answer_adapter;

    @BindView(R.id.chat_listview)
    ListView chat_listview;

    @BindView(R.id.answer_listview)
    ListView answer_listview;

    @BindView(R.id.answer_inputchat_tv)
    TextView chatBoxTv;

    RetroClient retroClient;

    SharedPreferences setting;
    SharedPreferences.Editor editor;


    //=========================================================
    // onSuccess에서 값을 받은 직후에 여기에 할당
    //=========================================================
    int nowScriptPk; //현재의 질문
    double nowSequence; //현재의 시퀸스
    String customAnswer; //"0"이면 없는것

    int selectedChoicePk; //현재 선택된 답변의 pk

    String LOG = "BBCC";


    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi();
        BusProvider.getInstance().register(this);

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("USERDATA", 0);
        editor= setting.edit();

        nowSequence = GlobalApplication.sequence;

        //리스트뷰 설정
        initList();
        connectServer(0, nowSequence, "none");
    }






    public void initList(){

        //리스트뷰 Adapter 생성
        chat_adapter = new ChatListViewAdapter();
        answer_adapter = new AnswerListViewAdapter();

        //채팅리스트
        chat_listview.setAdapter(chat_adapter);

        //답변리스트
        answer_listview.setAdapter(answer_adapter);

        touchList();
    }

    public void touchList(){

        answer_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AnswerListViewItem mAnswer = (AnswerListViewItem)answer_adapter.getItem(position);
                chatBoxTv.setText( mAnswer.choice);

                customAnswer = mAnswer.custom;
                selectedChoicePk = mAnswer.choice_pk;

                Log.d("SBSB", "뭐지: " + mAnswer.information);
            }
        });
    }



    public void onClick_chat_send(View v){

        if(chatBoxTv.getText().toString().equals("")){

        }else{

            int delay = 1000;

            addUserScript(chatBoxTv.getText().toString());
            clearAnswer(); //입력창 초기화

            //custom답변
            if(!customAnswer.equals("0")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addNpcScript(customAnswer);
                    }
                }, 800);

                delay= 2000;
            }

            //서버로 다음 sciprt요청
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run(){
                    connectServer(nowScriptPk, nowSequence, Integer.toString(selectedChoicePk));
                }
            }, delay);
        }
    }



    public void connectServer(int scriptPk, double sequence, String answer){

        if(GlobalApplication.category.equals("basic")){
            postBasic(scriptPk, sequence, answer);
        }else if(GlobalApplication.category.equals("behavior")){
            postBehavior(scriptPk, sequence, answer);
        }
    }



    public void postBasic(int script_pk, double sequence, String answer){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("script_pk", script_pk);
        parameters.put("sequence", sequence);
        parameters.put("userinfo_pk", GlobalApplication.userinfo_pk);
        parameters.put("answer", answer);

        retroClient.postBasic(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, "Error: " + t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                ChattingBasic data = (ChattingBasic)receivedData;
                Log.e(LOG, "Success: " + String.valueOf(code) + ", " + String.valueOf(data.script));

                nowScriptPk = data.script_pk;
                nowSequence = data.sequence;

                if(! checkCategoryEnd(data.script)){
                    setChat(data);
                }

            }

            @Override
            public void onFailure(int code) {

                Log.e(LOG, "Failure: " + String.valueOf(code));
            }
        });
    }

    public void postBehavior(int script_pk, double sequence, String answer){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("script_pk", script_pk);
        parameters.put("sequence", sequence);
        parameters.put("userinfo_pk", GlobalApplication.userinfo_pk);
        parameters.put("answer", answer);

        retroClient.postBehavior(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, "Error: " + t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                ChattingBasic data = (ChattingBasic)receivedData;
                Log.e(LOG, "Success: " + String.valueOf(code) + ", " + String.valueOf(data.script));

                nowScriptPk = data.script_pk;
                nowSequence = data.sequence;

                if(! checkCategoryEnd(data.script)){
                    setChat(data);
                }
            }

            @Override
            public void onFailure(int code) {

                Log.e(LOG, "Failure: " + String.valueOf(code));
            }
        });
    }



    public void setChat(ChattingBasic data){

        Log.d("SSQQ", "sequence: " + setting.getFloat("sequence", 0) + " / " + nowSequence );

        addNpcScript(data.script);

        if(data.type.equals("question")){
            for(int i=0; i<data.answer.size(); i++){
                answer_adapter.addItem(data.answer.get(i).choice_pk, data.answer.get(i).choice, data.answer.get(i).custom, data.answer.get(i).information);
            }
            answer_adapter.notifyDataSetChanged();
        }
        else if(data.type.equals("script")){ //대사만 있는 경우 1초 뒤 다음 대사 호출

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run(){
                    connectServer(nowScriptPk, nowSequence, "none");
                }
            }, 1000);
        }
    }



    public void addNpcScript(String script){
        String npcName = GlobalApplication.npcName;
        String imgPath = GlobalApplication.facePath;

        chat_adapter.addItem(npcName, imgPath, script, null, "오후 11:40");
        chat_adapter.notifyDataSetChanged(); //리스트 새로고침
        chat_listview.setSelection(chat_adapter.getCount() - 1); //가장 아래쪽으로 스크롤다운
    }

    public void addUserScript(String script){
        chat_adapter.addItem(null, null, null, script, "오후 11:40");
        chat_adapter.notifyDataSetChanged(); //리스트 새로고침
        chat_listview.setSelection(chat_adapter.getCount() - 1); //가장 아래쪽으로 스크롤다운
    }

    public void setSequence(){

        //프레퍼런스에 저장(double은 저장안되서 float에..)
        editor.putFloat("sequence", (float)nowSequence );
        editor.commit();
    }

    public void setCategory(String category){

        //프레퍼런스에 저장
        editor.putString("category", category);
        editor.commit();

        //Application 업데이트
        GlobalApplication.category = category;
    }



    public boolean checkCategoryEnd(String script){

        if(script.equals("끝")){

            clearChat();
            clearAnswer();

            if(GlobalApplication.category.equals("basic")){
                setCategory("behavior");
            }
            else if(GlobalApplication.category.equals("behavior")){
                setCategory("aptitude");
            }

//            GlobalApplication.sequence = 0;
            nowSequence = 0;

            setSequence();
            connectServer(0,0,"none");

            Toast.makeText(getApplicationContext(), "카테고리 변경", Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }

    public void clearChat(){

        //채팅창 초기화
        chat_adapter.removeAllItem();
        chat_adapter.notifyDataSetChanged();
    }

    public void clearAnswer(){

        //입력창 초기화
        chatBoxTv.setText("");
        answer_adapter.removeAllItem();
        answer_adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void FinishLoad(InformationEvent mInfo) {

        if(mInfo.message.length() < 50){
            Toast.makeText(getApplication(), mInfo.message, Toast.LENGTH_LONG).show();
        }else{
            addNpcScript(mInfo.message);
        }
    }




}








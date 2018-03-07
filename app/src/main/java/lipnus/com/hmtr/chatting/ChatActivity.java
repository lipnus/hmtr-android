package lipnus.com.hmtr.chatting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.hmtr.BusProvider;
import lipnus.com.hmtr.GlobalApplication;
import lipnus.com.hmtr.InformationEvent;
import lipnus.com.hmtr.R;
import lipnus.com.hmtr.retro.Response.AnswerBasic;
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

    @BindView(R.id.chat_title_tv)
    TextView titleTv;

    @BindView(R.id.answer_send_iv)
    ImageView sendIv;

    RetroClient retroClient;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    int chatDelayTime = 1;

    //=========================================================
    // onSuccess에서 값을 받은 직후에 여기에 할당
    //=========================================================
    int nowScriptPk; //현재의 질문
    double nowSequence; //현재의 시퀸스
    String customAnswer; //"0"이면 없는것

    int nowRootSeq; //챕터4에서만 씀
    double nowNextSeq; //챕터4에서만 씀
    String nowResult; //챕터4에서만 씀(1~5)

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



        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sendBtnImg(false);
        setTitle();

        retroClient = RetroClient.getInstance(this).createBaseApi();
        BusProvider.getInstance().register(this);

        //액티비티 화면 전환효과
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("USERDATA", 0);
        editor= setting.edit();

        nowSequence = GlobalApplication.sequence;

        //임시로 해놓음
        nowRootSeq = (int)GlobalApplication.sequence;
        nowNextSeq = GlobalApplication.sequence;

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

                //챕터4는 다음 seq를 찍어줌
                if(GlobalApplication.category.equals("balance")){
                    nowNextSeq = mAnswer.next_seq;
                    nowRootSeq = mAnswer.root_seq;
                    nowResult = transAnswer( mAnswer.result );
                }

                //버튼에 불들어옴
                sendBtnImg(true);

                Log.d("SBSB", "뭐지: " + mAnswer.information);
            }
        });
    }



    public void onClick_chat_send(View v){

        if(chatBoxTv.getText().toString().equals("")){

        }else{

            int delay = chatDelayTime;

            sendBtnImg(false); //버튼에 불끔
            addUserScript(chatBoxTv.getText().toString());//유저대화
            clearAnswer(); //입력창 초기화


            //custom답변
            if(!customAnswer.equals("0")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addNpcScript(customAnswer);
                    }
                }, 500);

                delay= chatDelayTime;
            }

            //서버로 다음 sciprt요청
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run(){

                    if (GlobalApplication.category.equals("balance")) {
                        connectServer(nowScriptPk, nowSequence, nowResult);
                    }else{
                        connectServer(nowScriptPk, nowSequence, Integer.toString(selectedChoicePk));
                    }

                }
            }, delay);
        }
    }



    public void connectServer(int scriptPk, double sequence, String answer){

        //"aptitude'는 통일성이 너무 깨져서  ChatAcivity3에서 처리.

        if(GlobalApplication.category.equals("basic")){
            postBasic(scriptPk, sequence, answer);
        }else if(GlobalApplication.category.equals("behavior")){
            postBehavior(scriptPk, sequence, answer);
        }else if(GlobalApplication.category.equals("balance")) {
            postBalance( nowRootSeq, nowNextSeq, answer);
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

    public void postBalance(int rootSeq, double nextSeq, String answer){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("userinfo_pk", GlobalApplication.userinfo_pk);
        parameters.put("root_sequence", rootSeq);
        parameters.put("next_sequence", nextSeq);
        parameters.put("answer", answer);

        retroClient.postBalance(parameters, new RetroCallback() {

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
                    setChat4(data);
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

            nowNextSeq++;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run(){
                    connectServer(nowScriptPk, nowSequence, "none");
                }
            }, chatDelayTime);
        }
    }

    public void setChat4(ChattingBasic data){

        Log.d("SSQQ", "sequence: " + setting.getFloat("sequence", 0) + " / " + nowSequence );

        addNpcScript(data.script);

        if(data.type.equals("question")){
            for(int i=0; i<data.answer.size(); i++){

                AnswerBasic mAnswer = data.answer.get(i);

                answer_adapter.addItem(mAnswer.choice_pk, mAnswer.choice, mAnswer.custom, mAnswer.information, mAnswer.result, mAnswer.next_seq, mAnswer.root_seq);
            }
            answer_adapter.notifyDataSetChanged();
        }
        else if(data.type.equals("script")){ //대사만 있는 경우 1초 뒤 다음 대사 호출

            nowNextSeq = data.sequence+1;
            nowRootSeq = (int)data.sequence;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run(){
                    connectServer(nowScriptPk, nowSequence, "none");
                }
            }, chatDelayTime);
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

        GlobalApplication.sequence = nowSequence;
    }

    public void setCategory(String category){

        //프레퍼런스에 저장
        editor.putString("category", category);
        editor.commit();

        //Application 업데이트
        GlobalApplication.category = category;
    }

    public void setTitle(){

        if(GlobalApplication.category.equals("basic")){
            titleTv.setText("#1 기본 인적사항");
        }else if(GlobalApplication.category.equals("behavior")){
            titleTv.setText("#2 학습행동유형");
        }else if(GlobalApplication.category.equals("balance")){
            titleTv.setText("#4 밸런스 자가진단");
        }

    }



    public boolean checkCategoryEnd(String script){

        if(script.equals("끝")){

            clearChat();
            clearAnswer();

            nowSequence = 0;
            setSequence();

            if(GlobalApplication.category.equals("basic")){
                Toast.makeText(getApplicationContext(), "기본인적사항 완료!", Toast.LENGTH_LONG).show();
                setCategory("behavior");
                setTitle();
            }
            else if(GlobalApplication.category.equals("behavior")){
                Toast.makeText(getApplicationContext(), "학습행동유형 완료!", Toast.LENGTH_LONG).show();
                setCategory("aptitude");
                setTitle();

                //챕터3은 다른데서 하기 때문에 이렇게 해준다
                Intent intent = new Intent(getApplicationContext(), ChatActivity3.class);
                startActivity(intent);
                finish();


            }
            else if(GlobalApplication.category.equals("balance")){
                Toast.makeText(getApplicationContext(), "밸런스 자가진단 완료!", Toast.LENGTH_LONG).show();
                finish();
//                return true;
            }


            connectServer(0,0,"none"); //얘는 1~2넘어갈때만 호출될듯
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



    public String transAnswer(String answerStr){

        //챕터4의 선택지를 숫자로 바꿔줌

        String val = "none";

        if(answerStr.equals("매우우수")){
            val =  "5";
        }else if(answerStr.equals("우수")){
            val = "4";
        }else if(answerStr.equals("보통")){
            val = "3";
        }else if(answerStr.equals("미흡")){
            val = "2";
        }else if(answerStr.equals("매우미흡")){
            val = "1";
        }

        return val;
    }


    public void sendBtnImg(boolean light){

        if(light==true){
            Glide.with(getApplicationContext())
                    .load( R.drawable.send_on )
                    .into( sendIv );
            sendIv.setScaleType(ImageView.ScaleType.FIT_XY);

        }else if(light==false){
            Glide.with(getApplicationContext())
                    .load( R.drawable.send_off )
                    .into( sendIv );
            sendIv.setScaleType(ImageView.ScaleType.FIT_XY);


        }



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








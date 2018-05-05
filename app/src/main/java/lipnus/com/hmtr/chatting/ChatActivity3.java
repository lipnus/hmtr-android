package lipnus.com.hmtr.chatting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.hmtr.GlobalApplication;
import lipnus.com.hmtr.R;
import lipnus.com.hmtr.retro.Request.MultiChoice;
import lipnus.com.hmtr.retro.Response.ChattingBasic;
import lipnus.com.hmtr.retro.RetroCallback;
import lipnus.com.hmtr.retro.RetroClient;
import lipnus.com.hmtr.tool.BusProvider;
import lipnus.com.hmtr.tool.InformationEvent;

public class ChatActivity3 extends AppCompatActivity {

    ChatListViewAdapter chat_adapter;
    AnswerListViewAdapter answer_adapter;
    MultiAnswerListViewAdapter multi_answer_adapter; //복수응답일때만 이걸로 바뀜

    @BindView(R.id.chat_listview)
    ListView chat_listview;

    @BindView(R.id.answer_listview)
    ListView answer_listview;

    @BindView(R.id.answer_inputchat_tv)
    TextView chatBoxTv;

    @BindView(R.id.answer_send_iv)
    ImageView sendIv;

    @BindView(R.id.chat_title_tv)
    TextView titleTv;

    @BindView(R.id.chat_progressBar)
    ProgressBar progressBar;

    RetroClient retroClient;

    SharedPreferences setting;
    SharedPreferences.Editor editor;

    //=========================================================
    // onSuccess에서 값을 받은 직후에 여기에 할당
    //=========================================================
    int nowScriptPk; //현재의 질문
    double nowSequence; //현재의 시퀸스
    String nowAnswerType; //답변의 타입(single, multi_1, multi_2, multi_3) -> 챕터3에서만 사용

    int selectedChoicePk; //현재 선택된 답변의 pk(answer리스트에서 선택 시 할당)

    int chatDelayTime = GlobalApplication.delayTime;

    String customAnswer; //"0"이면 없는것
    String LOG = "BBCC";

    Boolean lastItemVisibleFlag = false; //바닥친거 확인

    Boolean chat_send_tocck_lock = false; //복수선택시 중복전송방지하기 위해서 잠금


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

        titleTv.setText("# Ⅲ_학습적용유형 검사");

        //툴바 없에기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sendBtnImg(false);

        //액티비티 화면 전환효과
        this.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        retroClient = RetroClient.getInstance(this).createBaseApi();
        BusProvider.getInstance().register(this);

        //Prefrence설정(0:읽기,쓰기가능)
        setting = getSharedPreferences("USERDATA", 0);
        editor= setting.edit();

        //리스트뷰 설정
        initList();

        nowAnswerType = GlobalApplication.answerType;
        nowSequence = GlobalApplication.sequence;

        connectServer(0, nowSequence, "single", "none");
    }






    public void initList(){

        //리스트뷰 Adapter 생성
        chat_adapter = new ChatListViewAdapter();
        answer_adapter = new AnswerListViewAdapter();
        multi_answer_adapter = new MultiAnswerListViewAdapter();

        //채팅리스트
        chat_listview.setAdapter(chat_adapter);

        //답변리스트
        answer_listview.setAdapter(answer_adapter);

        touchList();
        scrollList();
    }

    public void scrollList(){

        answer_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    //TODO 화면이 바닦에 닿을때 처리
                    Log.e(LOG, "상태: " + lastItemVisibleFlag);

                    //바닥치면(확인을 다 하면 불 켜준다
                    if(nowAnswerType.equals("multi_1")){
                        sendBtnImg(true);
                    }
                }
            }
        });
    }

    public void touchList(){

        answer_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AnswerListViewItem mAnswer = (AnswerListViewItem)answer_adapter.getItem(position);
                chatBoxTv.setText( mAnswer.choice);

                customAnswer = mAnswer.custom;
                selectedChoicePk = mAnswer.choice_pk;

                sendBtnImg(true);
                Log.d("SBSB", "뭐지: " + mAnswer.information);
            }
        });
    }

    public void touchMultiList(){

        answer_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(nowAnswerType.equals("multi_1")){
                    multi_answer_adapter.changeThreeColor(position);
                }
                else if(nowAnswerType.equals("multi_2") || nowAnswerType.equals("multi_3")){
                    multi_answer_adapter.changeTwoColor(position);
                }
                multi_answer_adapter.notifyDataSetChanged();
            }
        });
    }




    public void onClick_chat_send(View v){

        if(chat_send_tocck_lock){
            return;
        }

        if(chatBoxTv.getText().toString().equals("아래에서 선택해주세요") ||
                chatBoxTv.getText().toString().equals("")){
            return;
        }

        //복수답안 전송
        if(nowAnswerType.equals("multi_1") || nowAnswerType.equals("multi_2") || nowAnswerType.equals("multi_3")){
            chat_send_tocck_lock = true; //서버에서 onSucess하면 풀어줌
            sendBtnImg(false);

            int answerCount = multi_answer_adapter.getCount();
            List<MultiChoice> choiceList = new ArrayList<>();

            for(int i=0; i<answerCount; i++){
                MultiAnswerListViewItem mMultiAnswer = (MultiAnswerListViewItem)multi_answer_adapter.getItem(i);

                MultiChoice choiceObj = new MultiChoice(mMultiAnswer.choice_pk, mMultiAnswer.checkValue);
                choiceList.add(choiceObj);
            }

            Gson gson = new Gson();
            final String answerJson = gson.toJson(choiceList);
            Log.e("SSBB", "다중선택답안: " + answerJson);
            addUserScript("답안전송!");

            //답변리스트 되돌리기
            clearAnswer();
            answer_listview.setAdapter(answer_adapter);
            touchList();

            //약간의 딜레이 후 서버로 전송
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    connectServer(nowScriptPk, nowSequence, nowAnswerType, answerJson );
                }
            }, chatDelayTime);
        }

        //전송조건 미충족
        else if(chatBoxTv.getText().toString().equals("")){

        }

        //단일답안 전송
        else{
            chat_send_tocck_lock = true; //서버에서 onSucess하면 풀어줌
            int delay = chatDelayTime;

            sendBtnImg(false);
            addUserScript(chatBoxTv.getText().toString());
            clearAnswer(); //입력창 초기화

            //custom답변이 있으면 출력한 뒤에 잠깐 딜레이 주고 서버호출
            if(!customAnswer.equals("0")) {
                delay *= 2;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addNpcScript(customAnswer);
                    }
                }, chatDelayTime);
            }

            //서버로 다음 sciprt요청
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run(){
                    connectServer(nowScriptPk, nowSequence, nowAnswerType, Integer.toString(selectedChoicePk));
                }
            }, delay);
        }
    }

    public void connectServer(int scriptPk, double sequence, String answer_type, String answer){

        postAptitude(scriptPk, sequence, answer_type, answer);
    }

    public void postAptitude(int script_pk, final double sequence, String answer_type, String answer){

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("script_pk", script_pk);
        parameters.put("sequence", sequence);
        parameters.put("userinfo_pk", GlobalApplication.userinfo_pk);

        parameters.put("answer_type", answer_type); //챕터3에만 있는것

        parameters.put("answer", answer);

        retroClient.postAptitude(parameters, new RetroCallback() {

            @Override
            public void onError(Throwable t) {
                Log.e(LOG, "Error: " + t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                ChattingBasic data = (ChattingBasic)receivedData;
                Log.e(LOG, "Success: " + String.valueOf(code) + " / " + String.valueOf(data.script) + " / " + String.valueOf(data.sequence) + " / " + String.valueOf(data.type));

                nowScriptPk = data.script_pk;
                nowSequence = data.sequence;

                int percent = (int)(((double)nowSequence / 40) * 100);
                progressBar.setProgress(percent);

                String titleText = "# Ⅲ_학습적용유형 검사";
                titleTv.setText(titleText);

                chat_send_tocck_lock = false; //send버튼 잠금 풀어줌

                if(data.type.equals("script") || data.type.equals("question")){
                    nowAnswerType = "single";
                }else{
                    nowAnswerType = data.type;
                }


                //끝 체크
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

        if(data.type.equals("question")){ //단일선택 답변처리
            chatBoxTv.setText("아래에서 선택해주세요");
            addNpcScript(data.script);

            for(int i=0; i<data.answer.size(); i++){
                answer_adapter.addItem(data.answer.get(i).choice_pk, data.answer.get(i).choice, data.answer.get(i).custom, data.answer.get(i).information);
            }
            answer_adapter.notifyDataSetChanged();
        }
        else if(data.type.equals("script")){ //대사만 있는 경우 1초 뒤 다음 대사 호출

            addNpcScript(data.script);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run(){

                    connectServer(nowScriptPk, nowSequence, nowAnswerType, "none");
                }
            }, chatDelayTime);

        }

        else{ //복수선택 답변처리

            chatBoxTv.setText("모두 선택해주세요 (해당사항 없으면 선택X)");
            Log.e("TTDD", data.sequence + " type: " + data.type );
            Log.e("TTDD", data.sequence + " answer: " + data.answer );

            //복수선택 첫번째 처리
            //동일한 내용인데 type만 바꿔서 재전송
            if(data.answer.isEmpty() == true){
                connectServer(data.script_pk, data.sequence, nowAnswerType,"none");
            }else{

                addNpcScript(data.script);

                //어댑터 바꿔치기
                answer_listview.setAdapter(multi_answer_adapter);
                touchMultiList();

                for(int i=0; i<data.answer.size(); i++){
                    multi_answer_adapter.addItem(data.answer.get(i).choice_pk, data.answer.get(i).choice, data.answer.get(i).custom, data.answer.get(i).information);
                }
                multi_answer_adapter.notifyDataSetChanged();

                if(nowAnswerType.equals("multi_2") || nowAnswerType.equals("multi_3")){
                    sendBtnImg(true);
                }
            }
        }

    }

    public void addNpcScript(String script){
        String npcName = GlobalApplication.npcName;
        String imgPath = GlobalApplication.facePath;

        chat_adapter.addItem(npcName, imgPath, script, null, getTime() );
        chat_adapter.notifyDataSetChanged(); //리스트 새로고침
        chat_listview.setSelection(chat_adapter.getCount() - 1); //가장 아래쪽으로 스크롤다운
    }

    public void addUserScript(String script){
        chat_adapter.addItem(null, null, null, script, getTime() );
        chat_adapter.notifyDataSetChanged(); //리스트 새로고침
        chat_listview.setSelection(chat_adapter.getCount() - 1); //가장 아래쪽으로 스크롤다운
    }



    public void saveSequence(){

        //프레퍼런스에 저장(double은 저장안되서 float에..)
        editor.putFloat("sequence", (float)nowSequence );
        editor.commit();

        GlobalApplication.sequence = nowSequence;
    }

    public void saveCategory(String category){

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
            saveCategory("balance");

            nowSequence = 1;
            saveSequence();
            Toast.makeText(getApplicationContext(), "학습적성유형 완료!", Toast.LENGTH_LONG).show();

            //챕터3은 다른데서 하기 때문에 이렇게 해준다
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
            finish();
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

        multi_answer_adapter.removeAllItem();
        multi_answer_adapter.notifyDataSetChanged();
    }

    public String getTime(){

        String Time;

        // 시스템으로부터 현재시간(ms) 가져오기
        long now = System.currentTimeMillis();

        // Data 객체에 시간을 저장한다.
        Date date = new Date(now);

        // 각자 사용할 포맷을 정하고 문자열로 만든다.
        SimpleDateFormat sdfMin = new SimpleDateFormat("mm");
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH");

        int hour = Integer.parseInt( sdfHour.format(date) );
        String minute = sdfMin.format(date);

        if(hour <= 12) {
            Time = "오전 " + hour + ":" + minute;
        }else{
            hour = hour - 12;
            Time = "오후 " + hour + ":" + minute;
        }

        return  Time;

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
        //추가정보 눌렀을때
        addNpcScript(mInfo.message);
    }
}

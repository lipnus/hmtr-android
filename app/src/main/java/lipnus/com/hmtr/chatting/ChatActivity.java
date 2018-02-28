package lipnus.com.hmtr.chatting;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import lipnus.com.hmtr.GlobalApplication;
import lipnus.com.hmtr.R;
import lipnus.com.hmtr.retro.ResponseBody.ChattingBasic;
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


    boolean lastItemVisibleFlag = false;

    int user, npc = 0;
    RetroClient retroClient;
    int selectedChoicePk; //현재 선택된 답변의 pk
    int nowScriptPk; //현재의 질문
    String customAnswer; //"0"이면 없는것

    String LOG = "BBCC";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        retroClient = RetroClient.getInstance(this).createBaseApi();

        //리스트뷰 설정
        initList();

        postChat(0,GlobalApplication.sequence, "none");
    }

    public void onClick_chat(View v){
        String imgPath = "http://img.yonhapnews.co.kr/etc/inner/KR/2017/10/19/AKR20171019092300003_01_i.jpg";
        chat_adapter.addItem("공유", imgPath, "npc대사 " + npc++, null, "오후 11:40");

        chat_adapter.notifyDataSetChanged(); //리스트 새로고침
        chat_listview.setSelection(chat_adapter.getCount() - 1); //가장 아래쪽으로 스크롤다운
    }

    public void onClick_chat2(View v){
        chat_adapter.addItem(null, null, null, "유저대사 " + user++, "오후 11:40");
        chat_adapter.notifyDataSetChanged(); //리스트 새로고침
        chat_listview.setSelection(chat_adapter.getCount() - 1); //가장 아래쪽으로 스크롤다운
    }

    public void onClick_chat3(View v){
//        answer_adapter.addItem("나는 공부를 잘한다", "없음");
//        answer_adapter.addItem("나는 운동을 잘한다", "없음");
//        answer_adapter.addItem("나는 빡대가리이다", "없음");
//        answer_adapter.addItem("몰라", "없음");
//        answer_adapter.notifyDataSetChanged();
    }

    //리스트 초기화
    public void initList(){

        //리스트뷰 Adapter 생성
        chat_adapter = new ChatListViewAdapter();
        answer_adapter = new AnswerListViewAdapter();

        //채팅리스트
        chat_listview.setAdapter(chat_adapter);

        //답변리스트
        answer_listview.setAdapter(answer_adapter);


        //답변클릭 이벤트
        answer_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AnswerListViewItem mAnswer = (AnswerListViewItem)answer_adapter.getItem(position);
                chatBoxTv.setText( mAnswer.choice);

                customAnswer = mAnswer.custom;
                selectedChoicePk = mAnswer.choice_pk;

                if(!mAnswer.information.equals("0")){
                    Toast.makeText(getApplication(), mAnswer.information, Toast.LENGTH_LONG).show();
                }
            }
        });

//        //바닥에 닿는 것을 체크(지금은 일단 사용안함)
//        chat_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
//                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
//            }
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
//                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
//                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
//                    //TODO 화면이 바닦에 닿을때 처리
//                    Log.d("LILI", "인생바닥침");
//                }
//            }
//
//        });
    }



    public void onClick_chat_send(View v){

        if(chatBoxTv.getText().toString().equals("")){

        }else{

            final int sequence = ++GlobalApplication.sequence;

            Log.d("BBCC", "전송답안pk: " + selectedChoicePk);
            addUserScript(chatBoxTv.getText().toString());

            //입력칸 정리
            chatBoxTv.setText("");
            answer_adapter.removeAllItem();
            answer_adapter.notifyDataSetChanged();

            //0.8초 후 즉각적인 답변
            if(!customAnswer.equals("0")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run(){
                        addNpcScript(customAnswer);
                    }
                }, 800);

            }

            //1.5초 후 다음 대사 호출
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run(){
                    postChat(nowScriptPk, sequence, Integer.toString(selectedChoicePk) );
                }
            }, 1500);


        }
    }

    public void postChat(int script_pk, int sequence, String answer){
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
                setChat(data);
            }

            @Override
            public void onFailure(int code) {

                Log.e(LOG, "Failure: " + String.valueOf(code));
            }
        });
    }

    public void setChat(ChattingBasic data){

        addNpcScript(data.script);

        if(data.type.equals("question")){
            for(int i=0; i<data.answer.size(); i++){
                answer_adapter.addItem(data.answer.get(i).choice_pk, data.answer.get(i).choice, data.answer.get(i).custom, data.answer.get(i).information);
            }
            answer_adapter.notifyDataSetChanged();
        }
        else if(data.type.equals("script")){ //질문없이 대사만 있는 경우 1초 뒤 다음 대사 호출
            final int sequence = ++GlobalApplication.sequence;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run(){
                    postChat(nowScriptPk, sequence, "none");
                }
            }, 2000);

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



}








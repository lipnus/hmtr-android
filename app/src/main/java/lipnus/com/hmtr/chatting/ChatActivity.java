package lipnus.com.hmtr.chatting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import lipnus.com.hmtr.R;

public class ChatActivity extends AppCompatActivity {


    ListView listview;
    ChatListViewAdapter adapter;

    boolean lastItemVisibleFlag = false;

    int user, npc = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //리스트뷰 설정
        initList();


    }

    public void onClick_chat(View v){
        String imgPath = "http://img.yonhapnews.co.kr/etc/inner/KR/2017/10/19/AKR20171019092300003_01_i.jpg";
        adapter.addItem("공유", imgPath, "npc대사 " + npc++, null, "오후 11:40");

        adapter.notifyDataSetChanged(); //리스트 새로고침
        listview.setSelection(adapter.getCount() - 1); //가장 아래쪽으로 스크롤다운
    }

    public void onClick_chat2(View v){
        adapter.addItem(null, null, null, "유저대사 " + user++, "오후 11:40");

        adapter.notifyDataSetChanged(); //리스트 새로고침
       listview.setSelection(adapter.getCount() - 1); //가장 아래쪽으로 스크롤다운
    }

    //리스트 초기화
    public void initList(){

        //리스트뷰 Adapter 생성
        adapter = new ChatListViewAdapter();

        //알람를 표시할 리스트뷰와 컨트롤을 위한 어댑터
        listview = (ListView) findViewById(R.id.chat_listview);
        listview.setAdapter(adapter);

//        //바닥에 닿는 것을 체크(지금은 일단 사용안함)
//        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
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


    //상단바의 중간부분 타이틀을 터치하면 스크롤 위로 끌어올림
    public void onClicK_topmenu_title(View v){
        listview.smoothScrollToPosition( 0 );
    }



















}








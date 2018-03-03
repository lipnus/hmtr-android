package lipnus.com.hmtr.chatting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

import lipnus.com.hmtr.BusProvider;
import lipnus.com.hmtr.InformationEvent;
import lipnus.com.hmtr.R;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class AnswerListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<AnswerListViewItem> listViewItemList = new ArrayList<AnswerListViewItem>() ;


    // ListViewAdapter의 생성자
    public AnswerListViewAdapter() {

    }


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }


    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_answer, parent, false);
        }

        TextView answerTv = convertView.findViewById(R.id.answer_answer_tv);
        ImageView informationIv = convertView.findViewById(R.id.answer_information_iv);
        LinearLayout answerLr = convertView.findViewById(R.id.answer_lr);

        //------------------------------------------------------------------------------------------
        // 화면에 표시
        //------------------------------------------------------------------------------------------

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final AnswerListViewItem listViewItem = listViewItemList.get(position);
        answerTv.setText( listViewItem.choice);


        //추가정보가 있는경우
        if(!listViewItem.information.equals("0")){

            informationIv.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(R.drawable.question)
                    .into(informationIv);
            informationIv.setScaleType(ImageView.ScaleType.FIT_XY);

            //클릭 시 ChatActivity로 post
            informationIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BusProvider.getInstance().post(new InformationEvent(listViewItem.information));
                }
            });
        }else{
            informationIv.setVisibility(View.GONE);
        }

        //애니매이션
        YoYo.with(Techniques.FadeInLeft)
                .duration(800)
                .playOn(answerLr);






        //------------------------------------------------------------------------------------------
        // 터치 이벤트
        //------------------------------------------------------------------------------------------



        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }


    // 아이템 추가
    public void addItem(int choice_pk, String choice, String custom, String information) {

        Log.e("ADAD", "addItem()");

        AnswerListViewItem item = new AnswerListViewItem(choice_pk, choice, custom, information);
        listViewItemList.add(item);
    }

    // 모든 아이템 삭제
    public void removeAllItem(){
        listViewItemList.clear();
    }
//
//    // 해당 position의 객체 반환
//    public AnswerListViewItem getItem(int position){
//        AnswerListViewItem answerListViewItem = listViewItemList.get(position);
//        return answerListViewItem;
//    }
}
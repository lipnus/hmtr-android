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

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import lipnus.com.hmtr.R;

/**
 * Created by Sunpil on 2016-07-13.
 */
public class ChatListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ChatListViewItem> listViewItemList = new ArrayList<ChatListViewItem>() ;


    // ListViewAdapter의 생성자
    public ChatListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_chat, parent, false);
        }
        LinearLayout npcLr = convertView.findViewById(R.id.chat_npc_lr);
        LinearLayout userLr = convertView.findViewById(R.id.chat_user_lr);

        ImageView npcFaceIv = convertView.findViewById(R.id.chat_npc_profile_iv);
        TextView npcNameIv = convertView.findViewById(R.id.chat_npc_name_tv);
        TextView npcScriptTv = convertView.findViewById(R.id.chat_npc_script_tv);
        TextView npcDateTv = convertView.findViewById(R.id.chat_npc_date_tv);
        TextView userScriptTv = convertView.findViewById(R.id.chat_user_script_tv);
        TextView userDateTv = convertView.findViewById(R.id.chat_user_date_tv);

        //------------------------------------------------------------------------------------------
        // 화면에 표시
        //------------------------------------------------------------------------------------------

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ChatListViewItem listViewItem = listViewItemList.get(position);

        Log.e("ADAD",position + ", " + listViewItem.npcScript + ", " + listViewItem.userScript);

        //npc 차례
        if(listViewItem.npcScript != null){
            Log.e("CHCH", "NPC");

            npcLr.setVisibility(View.VISIBLE);
            userLr.setVisibility(View.GONE);

            Glide.with(context)
                    .load(listViewItem.npcFaceImg)
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(npcFaceIv);
            npcFaceIv.setScaleType(ImageView.ScaleType.FIT_XY);

            npcNameIv.setText(listViewItem.npcName);
            npcScriptTv.setText(listViewItem.npcScript);
            npcDateTv.setText(listViewItem.date);
        }

        //user 차례
        else if(listViewItem.userScript != null){
            Log.e("CHCH", "USER");
            npcLr.setVisibility(View.GONE);
            userLr.setVisibility(View.VISIBLE);

            userScriptTv.setText(listViewItem.userScript);
            userDateTv.setText(listViewItem.date);
        }



        //------------------------------------------------------------------------------------------
        // 데이터 정리
        //------------------------------------------------------------------------------------------



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
    public void addItem(String npcName, String npcFaceImg, String npcScript, String userScript, String date) {

        Log.e("ADAD", "addItem()");


        ChatListViewItem item = new ChatListViewItem(npcName, npcFaceImg, npcScript, userScript, date);
        listViewItemList.add(item);
    }

    // 모든 아이템 삭제
    public void removeAllItem(){
        listViewItemList.clear();
    }
}
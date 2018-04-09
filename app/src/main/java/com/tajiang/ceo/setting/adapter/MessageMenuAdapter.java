package com.tajiang.ceo.setting.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.BottomCallBackInterface;
import com.tajiang.ceo.model.MessageInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消息适配器
 * Created by Administrator on 2017-07-27.
 */
public class MessageMenuAdapter extends RecyclerView.Adapter<MessageMenuAdapter.MyViewHolder> {

    private Context context;
    private List<MessageInfo> dataList;

    private BottomCallBackInterface.OnBottomClickListener mListener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MessageMenuAdapter.MyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_messagemenu, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final MessageInfo info = dataList.get(position);

        holder.time.setText(info.getCreateTime());
        holder.content.setText(info.getContent());

        holder.layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(mListener != null){
                    mListener.onBottomClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public MessageMenuAdapter(Context context, List<MessageInfo> dataList, BottomCallBackInterface.OnBottomClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.mListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_message_menu_top)
        LinearLayout layout;

        @BindView(R.id.item_message_menu_content)
        TextView content;

        @BindView(R.id.item_message_menu_time)
        TextView time;

        public MyViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}

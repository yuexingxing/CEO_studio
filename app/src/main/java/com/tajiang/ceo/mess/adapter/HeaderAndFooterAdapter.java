package com.tajiang.ceo.mess.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QiLi Shen on 2016/10/13.
 */
public class HeaderAndFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int ITEM_TYPE_NORMAL           = 0;      //普通Item
    private static final int ITEM_TYPE_HEADER           = 1;      //RecyclerView头部
    private static final int ITEM_TYPE_FOOTER           = 2;      //RecyclerView尾部

    private static final int ITEM_STYLE_TYPE_SPAN_COUNT = 3;  //多个Item一行的样式
    private static final int ITEM_STYLE_TYPE_SINGLE_ROW = 4;  //单个Item一行的样式

    //校园经理权限
    private final  int [] mDatasCeo = {
            R.string.title,
            R.string.mess_management,R.string.deliver_schedule,
            R.string.dorm_management,R.string.contact_service,R.string.deliver_manage
    };
    private final  int [] mImgResCeo = {
            R.mipmap.ic_launcher,
            R.mipmap.icon_mess_management,R.mipmap.icon_deliver_schedule,
            R.mipmap.icon_dorm_management,R.mipmap.icon_contact_service,
            R.mipmap.icon_deliver_manage
    };
    //楼长权限
    private final int [] mDatasAdmin = {
            R.string.title,
            R.string.contact_service
    };
    private final int [] mImgResAdmin = {
            R.mipmap.ic_launcher,
            R.mipmap.icon_contact_service
    };
    private int[] mDatas;
    private int[] mImgRes;
    private int mItemSpanCount;
    private int mItemViewType;

    private Context mContext;
    private View mHeadView;
    private View mFootView;
    private View mItemView;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int ItemId);
    }

    /**
     * @param headView  Head头部布局对象
     * @param mContext  宿主activity上下文
     * @param roles     登录用户权限
     * @param spanCount 宿主RecyclerView 每一行Item数量
     */
    public HeaderAndFooterAdapter(Context mContext, View headView, View footView, int roles, int spanCount) {
        this.mContext = mContext;
        this.mHeadView = headView;
        this.mFootView = footView;
        this.mItemSpanCount = spanCount;
        initDate(roles);
    }

    private void initDate(int roles) {
        if (roles == TJApp.RULES_SCHOOL_CEO) {
            mDatas = mDatasCeo;
            mImgRes = mImgResCeo;
        } else {
            mDatas = mDatasAdmin;
            mImgRes = mImgResAdmin;
        }
        //包含一个Head头部占位？（头部使用）
        if (mDatas.length - 1 < 2) {
            mItemViewType = ITEM_STYLE_TYPE_SINGLE_ROW;
        } else {
            mItemViewType = ITEM_STYLE_TYPE_SPAN_COUNT;
        }
    }


    /**
     * 根据getItemViewType()方法返回的不同类型创建不同的ViewHolder
     * @param parent
     * @param viewType Item类型
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ITEM_TYPE_HEADER: //RecyclerView头部
                viewHolder =  new HeaderHolder(mHeadView);
                break;
            case ITEM_TYPE_NORMAL: //RecyclerView列表项, 单个Itemt，单行展示
                if (mItemViewType == ITEM_STYLE_TYPE_SINGLE_ROW) {        //单个Itemt，单行展示
                    mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_view_whole_row, null);
                } else {                                                  //多个Itemt使用九宫格样式展示
                    mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_view, null);
                }
                viewHolder = new ItemHolder(mItemView);
            break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
//            HeaderHolder headerHolder = (HeaderHolder) holder;
//            headerHolder.textViewHeader.setText(mDatas[position]);
        } else {
            ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.textViewItemName.setText(mContext.getString(mDatas[position]));
            itemHolder.imageView.setImageResource(mImgRes[position]);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.length;
    }

    /*根据位置来返回不同的item类型*/
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else
            return ITEM_TYPE_NORMAL;
    }

    /**
     * 自定义LayoutManager （单行展示的Item数量）
     * @return
     */
    public RecyclerView.LayoutManager getLayoutManager() {
        final GridLayoutManager layoutManager = new GridLayoutManager(mContext, mItemSpanCount);
        //调用以下方法让RecyclerView的第一个条目仅为1列
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //如果位置是0，是Header,这个Item将占用SpanCount()这么多的列数，再此也就是3(ITEM_SPAN_COUNT)
                //如果不是0，不是Header，占用1列
                return isHeader(position) || mItemViewType == ITEM_STYLE_TYPE_SINGLE_ROW ? layoutManager.getSpanCount() : 1;
            }
        });
        return layoutManager;
    }

    /**
     * 判断当前position是否处于第一个
     * @param position
     * @return
     */
    public boolean isHeader(int position) {
        return position == 0;
    }

    /*头部Item*/
    class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView textViewHeader;

        public HeaderHolder(View itemView) {
            super(itemView);
            textViewHeader = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    /*Item*/
    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView textViewItemName;
        private ImageView imageView;

        public ItemHolder(View itemView) {
            super(itemView);
            initItemRootView();
            initItemListener();
        }

        private void initItemRootView() {
            textViewItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);

            switch (mItemViewType) {
                case ITEM_STYLE_TYPE_SPAN_COUNT:
                    LinearLayout layout = (LinearLayout) itemView.findViewById(R.id.ll_item_root);

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
                    android.view.WindowManager wm = (android.view.WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    params.height = wm.getDefaultDisplay().getWidth() / mItemSpanCount;
                    layout.setLayoutParams(params);
                    break;

                case ITEM_STYLE_TYPE_SINGLE_ROW:
                    break;
            }
        }

        private void initItemListener() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClickListener(mDatas[getLayoutPosition()]);
                }
            });
        }
    }

}

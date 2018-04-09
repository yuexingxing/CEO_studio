package com.tajiang.ceo.setting.wallet.message;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.activity.BaseActivity;
import com.tajiang.ceo.common.utils.BottomCallBackInterface;
import com.tajiang.ceo.common.utils.PostDataTools;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.tajiang.ceo.model.MessageInfo;
import com.tajiang.ceo.model.Order;
import com.tajiang.ceo.model.Pager;
import com.tajiang.ceo.setting.adapter.MessageMenuAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 通知中心
 */
public class MessageMenuActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.swipe_to_load_message)
    SwipeToLoadLayout swipeToLoadLayout;

    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;

    private MessageMenuAdapter mAdapter;
    private List<MessageInfo> dataList = new ArrayList<MessageInfo>();

    private int pageNumber = 1;//当前页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
    }

    @Override
    protected void initTopBar() {
        setTitle("通知中心");
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_message_menu);
    }

    @Override
    protected void initData() {

        mAdapter = new MessageMenuAdapter(this, dataList, new BottomCallBackInterface.OnBottomClickListener() {

            @Override
            public void onBottomClick(View v, int position) {

                ToastUtils.showShort(position + "");
                MessageInfo recordInfo = dataList.get(position);
                if(v.getId() == R.id.item_message_menu_top){


                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.gray)).size(36).build());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {

        pageNumber = 1;
        PostDataTools.msg_user_list(this,pageNumber, swipeToLoadLayout, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                Pager<MessageInfo> pager = (Pager) object;
                List<MessageInfo> newList = pager.getList();

                dataList.clear();
                dataList.addAll(newList);
                mAdapter.notifyDataSetChanged();

                if (newList.size() >= Order.PAGE_SIZE){
                    pageNumber = 2;
                    swipeToLoadLayout.setLoadingMore(true);
                }else{
                    swipeToLoadLayout.setLoadingMore(false);
                }
            }
        });
    }

    @Override
    public void onLoadMore() {

        PostDataTools.msg_user_list(this,pageNumber, swipeToLoadLayout, new PostDataTools.DataCallback() {

            @Override
            public void callback(boolean flag, String message, Object object) {

                Pager pager = (Pager) object;
                List<MessageInfo> newList = pager.getList();

                dataList.addAll(newList);
                mAdapter.notifyDataSetChanged();

                if (newList.size() >= Order.PAGE_SIZE){
                    ++pageNumber;
                    swipeToLoadLayout.setLoadingMore(true);
                }
            }
        });
    }
}

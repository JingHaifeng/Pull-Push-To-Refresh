package com.jinghaifeng.ultra.refresh;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.footer.PtrClassicDefaultFooter;
import in.srain.cube.views.ptr.header.PtrClassicDefaultHeader;

public class MainActivity extends AppCompatActivity {

    private PtrFrameLayout mPtrFrameLayout;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.pull_up_down_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyAdapter());

        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(this);
        PtrClassicDefaultFooter footer = new PtrClassicDefaultFooter(this);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.setFooterView(footer);
        mPtrFrameLayout.addPtrUIHeaderHandler(header);
        mPtrFrameLayout.addPtrUIFooterHandler(footer);
        mPtrFrameLayout.setLoadingMinTime(0);
        mPtrFrameLayout.setPtrHandler(new PtrDefHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrameLayout.refreshComplete();
                    }
                },1500L);
            }
        });
    }

    private class MyHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;
        public MyHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        private List<String> mDataList;
        public MyAdapter() {
            mDataList = new ArrayList<>(10);
            for (int i = 0; i < 10; i++) {
                mDataList.add(i+":"+Character.forDigit(i,100));
            }
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new MyHolder(inflater.inflate(R.layout.my_item,parent,false));
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            final String data= mDataList.get(position);
            holder.mTextView.setText(data);
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }
}

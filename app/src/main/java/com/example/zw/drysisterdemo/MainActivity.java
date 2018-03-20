package com.example.zw.drysisterdemo;

import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.zw.drysisterdemo.API.SisterApi;
import com.example.zw.drysisterdemo.Model.Sister;
import com.example.zw.drysisterdemo.Utils.PictureLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button showBtn;
    private Button refreshBtn;
    private ImageView showImg;
    private ArrayList<Sister> data;
    private  int curPos = 0;  //当前显示的是哪一张
    private int page = 1;     //当前页数
    private PictureLoader loader;
    private SisterApi sisterApi;
    private SisterTask sisterTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sisterApi = new SisterApi();
        loader = new PictureLoader();
        initData();
        initUI();
    }

    private void initData(){
         data = new ArrayList<>();
         sisterTask = new SisterTask();
         sisterTask.execute();
    }

    private void initUI(){
        showBtn = (Button) findViewById(R.id.btn_show);
        refreshBtn = (Button) findViewById(R.id.btn_refresh);
        showImg = (ImageView) findViewById(R.id.img_show);
        showBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
    }

    private class SisterTask extends AsyncTask<Void,Void,ArrayList<Sister>>{
        public SisterTask( ){}

        @Override
        protected ArrayList<Sister> doInBackground(Void... voids) {
            return sisterApi.fetchSister(10,page);
        }

        @Override
        protected void onPostExecute(ArrayList<Sister> sisters) {
            super.onPostExecute(sisters);
            data.clear();
            data.addAll(sisters);
            // 模拟第一次点击
            showBtn.performClick();
            page++;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            sisterTask = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_show:
                if(data != null && !data.isEmpty()){
                    if(curPos > 9){
                        curPos = 0;
                    }
                    loader.load(showImg, data.get(curPos).getUrl());
                    curPos++;
                }
                break;
            case R.id.btn_refresh:
                sisterTask = new SisterTask();
                sisterTask.execute();
                curPos = 0;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sisterTask.cancel(true);
    }
}

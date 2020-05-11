package com.qr.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.qr.app.adapter.ListAdapter;
import com.qr.app.api.QrAPI;
import com.qr.app.conf.APIClient;
import com.qr.app.vo.ListVO;
import com.qr.app.vo.UserVO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private QrAPI qrApi;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qrApi = APIClient.getClient().create(QrAPI.class);//API SET

        Intent intent = getIntent();
        String clientNo = intent.getExtras().getString("clientNo");
        getCompanyList(clientNo);
    }

    public void getCompanyList(String clientNo){

        UserVO.Data data = new UserVO().new Data(clientNo);
        Call<ListVO> listData = qrApi.doPostNewCompanyList(data);
        listData.enqueue(new Callback<ListVO>() {
            @Override
            public void onResponse(Call<ListVO> call, Response<ListVO> response) {
               ListVO apiData = response.body();
               final List<ListVO.Data> newCompanyList =  apiData.data;
                  if(apiData.status){
                      listView = (ListView)findViewById(R.id.listView);
                      ListAdapter adapter = new ListAdapter((ArrayList)newCompanyList);
                      listView.setAdapter(adapter);
                      listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                          @Override
                          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                              Intent intent = new Intent(getApplicationContext(), CompanyDetailActivity.class);
                              String cnt = newCompanyList.get(position).cnt;
                              String name = newCompanyList.get(position).name;
                              String addr = newCompanyList.get(position).addr;
                              String companySrl = newCompanyList.get(position).companySrl;

                              intent.putExtra("cnt",cnt);
                              intent.putExtra("name",name);
                              intent.putExtra("addr",addr);
                              intent.putExtra("companySrl",companySrl);

                              startActivity(intent);
                          }
                      });
                  }
            }
            @Override
            public void onFailure(Call<ListVO> call, Throwable t) {
                call.cancel();
            }
        });
    }

}
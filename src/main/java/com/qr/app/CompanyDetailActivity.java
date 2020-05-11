package com.qr.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/*업체 상세정보및 카메라 열기*/
public class CompanyDetailActivity extends AppCompatActivity {

    private TextView t_cnt;
    private TextView t_name;
    private TextView t_addr;
    private Button cameraBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_detail);
        Intent intent = getIntent();

        String cnt = intent.getExtras().getString("cnt");
        String name = intent.getExtras().getString("name");
        String addr = intent.getExtras().getString("addr");
        final String companySrl = intent.getExtras().getString("companySrl");

        t_cnt = (TextView) findViewById(R.id.detailCnt);
        t_name = (TextView) findViewById(R.id.detailName);
        t_addr = (TextView) findViewById(R.id.detailAddr);

        /*텍스트 세팅*/
        t_cnt.setText(cnt);
        t_name.setText(name);
        t_addr.setText(addr);

        cameraBtn = (Button) findViewById(R.id.cameraBtn);
        /*카메라 열기*/
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*QR CODE scanner call*/
                Intent intent = new Intent(getApplicationContext(), QRDecodeActivity.class);
                intent.putExtra("companySrl",companySrl);
                startActivity(intent);
            }
        });
    }
}
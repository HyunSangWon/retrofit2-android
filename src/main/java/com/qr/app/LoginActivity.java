package com.qr.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.qr.app.api.QrAPI;
import com.qr.app.conf.APIClient;
import com.qr.app.vo.UserVO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText id;
    private EditText password;
    private Button loginBtn;
    QrAPI qrApi;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        qrApi = APIClient.getClient().create(QrAPI.class);//API SET

        id = (EditText) findViewById(R.id.idInput);
        password = (EditText) findViewById(R.id.passwordInput);
        loginBtn = (Button) findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Login API CALL
                UserVO.Data data = new UserVO().new Data(id.getText().toString().trim(),password.getText().toString().trim());
                Call<UserVO> userData = qrApi.login(data);
                userData.enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                        UserVO user = response.body();
                        UserVO.Data userInfo = user.data;
                        if(user.status){
                            Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();
                            String clientNo = userInfo.clientNo;
                            //화면 전환
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("clientNo",clientNo);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),"없는 회원정보입니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<UserVO> call, Throwable t) {
                            call.cancel();
                    }
                });
            }
        });
    }
}
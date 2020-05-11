package com.qr.app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.qr.app.activity.PhotoActivity;
import com.qr.app.api.QrAPI;
import com.qr.app.conf.APIClient;
import com.qr.app.vo.QrVO;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRDecodeActivity extends Activity {

    private int MY_PERMISSIONS_REQUEST_CAMERA = 1000;

    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector barcodeDetector;
    private QrAPI qrApi;
    private int cnt = 0;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_main);
        qrApi = APIClient.getClient().create(QrAPI.class);//API SET
        surfaceView = (SurfaceView)findViewById(R.id.cameraPreview);
        textView = (TextView)findViewById(R.id.qrcode);

        Intent intent = getIntent();
        final String companySrl = intent.getExtras().getString("companySrl");

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(640,480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ContextCompat.checkSelfPermission(QRDecodeActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    //권한이 부여되면 PERMISSION_GRANTED 거부되면 PERMISSION_DENIED 리턴
                    if(ActivityCompat.shouldShowRequestPermissionRationale(QRDecodeActivity.this,
                            Manifest.permission.CAMERA)){
                    }else{
                        ActivityCompat.requestPermissions(QRDecodeActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                    return;
                }
                try{
                    cameraSource.start(holder);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
            }

        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>()  {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode = detections.getDetectedItems();
                if(qrCode.size() != 0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            textView.setText(qrCode.valueAt(0).displayValue);
                            ++cnt;
                            if(cnt < 2){
                                Call<QrVO> qrData = qrApi.doPostQrDecode(new QrVO(qrCode.valueAt(0).displayValue));
                                qrData.enqueue(new Callback<QrVO>() {
                                    @Override
                                    public void onResponse(Call<QrVO> call, Response<QrVO> response) {
                                        QrVO qrInfo = response.body();
                                        if(qrInfo.data){
                                            Toast.makeText(getApplicationContext(),"등록완료",Toast.LENGTH_SHORT).show();
                                            /*
                                            * 1. 등록완료
                                            * 2. 담보물 외관 사진으로 이동
                                            * */
                                            Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
                                            intent.putExtra("companySrl",companySrl);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(getApplicationContext(),"없는 QR코드 입니다.",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<QrVO> call, Throwable t) {
                                        call.cancel();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000 : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),"카메라 권한 획득", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"카메라 권한 획득 실패", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
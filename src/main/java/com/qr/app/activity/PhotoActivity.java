package com.qr.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.qr.app.R;
import com.qr.app.conf.ShowCamera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/*담보물 외관촬영*/
public class PhotoActivity extends Activity {

    private FrameLayout frameLayout;
    private Button button;
    private ImageView imageView;
    private ShowCamera showCamera;
    private Camera camera;
    private TransferUtility transferUtility;
    private String bucketName = "qr-s3";

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_main);
        Intent intent = getIntent();
        final String companySrl = intent.getExtras().getString("companySrl");
        credentialsProvider();

        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.objPhoto);
        frameLayout = (FrameLayout) findViewById(R.id.cameraObjPreview);
        camera = Camera.open();
        showCamera = new ShowCamera(this,camera);
        frameLayout.addView(showCamera);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showCamera.capture(new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 20;
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        imageView.setImageBitmap(bitmap);
                        /* 갤러리함 저장
                        * 1. 우선 강제로 권한을 부여함 (파일 스토리지 WRITE 권한 필요)
                        * */
                        String imgSaveUri = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"사진 저장","저장되었습니다.");
                        Uri uri = Uri.parse(imgSaveUri);
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));

                        File uploadToS3 = new File(getPath(uri));
                        /*S3 경로
                        *
                        * 담보물 외관 bucket-name/OBJ/업체PK/날짜/objNo_date_random.jpg
                        * */
                        Date today = new Date();
                        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
                        String strDate = date.format(today);
                        String objNo = "1";
                        String imgName = companySrl+","+objNo+","+strDate+",pk"+".jpg";
                        String s3BucketKey = "images/"+companySrl+"/"+objNo+"/"+imgName;
                        /*갤러리에서 이미지를 가져와 AWS S3에 전송*/
                        setFileToUpload(uploadToS3,s3BucketKey);
                        //사진을 찍게 되면 미리보기가 중지된다. 다시 미리보기를 시작하려면...
                        camera.startPreview();
                    }
                });
            }
        });
    }
    // Amazon Cognito 인증 공급자를 초기화
    public void credentialsProvider(){
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "자격 증명 풀 ID", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );
        setTransferUtility(credentialsProvider);
    }
    // TransferUtility 객체 생성
    public void setTransferUtility(CognitoCachingCredentialsProvider credentialsProvider){
        transferUtility = TransferUtility.builder()
                .context(getApplicationContext())
                .defaultBucket(bucketName)
                .s3Client(new AmazonS3Client(credentialsProvider,Region.getRegion(Regions.AP_NORTHEAST_2)))
                .build();
    }

    public void setFileToUpload(File file,String s3Key){
        TransferObserver transferObserver = transferUtility.upload(
                bucketName,          /* The bucket to upload to */
                s3Key,/* The key for the uploaded object */
                file      /* The file where the data to upload exists */
        );
        transferObserverListener(transferObserver);
    }

    // 다운로드 과정을 알 수 있도록 Listener를 추가
    public void transferObserverListener(TransferObserver transferObserver){
        transferObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Toast.makeText(getApplicationContext(), "State Change" + state, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Toast.makeText(getApplicationContext(), "Progress in %" + percentage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
            }
        });
    }

    public String getPath(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

}
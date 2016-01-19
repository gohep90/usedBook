package com.example.min.usedbook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChoiceSell extends AppCompatActivity {

    // Button btn_photo;
    Button btn_esell;
    Button btn_csell;

    EditText edt_sbook;
    EditText edt_sauthor;
    EditText edt_spublisher;
    EditText edt_scost;
    EditText edt_sstate;
    EditText edt_sother;

    TextView txt_name;
    TextView txt_phone;

    ImageView img_book;

    Spinner spn_group;


    String encodedString = "";
    String photoUri = "";
    String photoName= "";
    String group="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_sell);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);

        // btn_photo = (Button)findViewById(R.id.btn_photo);
        btn_esell = (Button) findViewById(R.id.btn_esell);
        btn_csell = (Button) findViewById(R.id.btn_csell);

        edt_sbook = (EditText) findViewById(R.id.edt_sbook);
        edt_sauthor = (EditText) findViewById(R.id.edt_sauthor);
        edt_spublisher = (EditText) findViewById(R.id.edt_spublisher);
        edt_scost = (EditText) findViewById(R.id.edt_scost);
        edt_sstate = (EditText) findViewById(R.id.edt_sstate);
        edt_sother = (EditText) findViewById(R.id.edt_sother);

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_name.setText(sharedPreferences.getString("name", ""));

        txt_phone = (TextView) findViewById(R.id.txt_phone);
        txt_phone.setText(sharedPreferences.getString("tel", ""));

        img_book = (ImageView) findViewById(R.id.img_book);

        spn_group =  (Spinner)findViewById(R.id.spn_group);
        String[] group = {"전공","교양","토익"};

        ArrayAdapter<String>  list = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, group);

        spn_group.setAdapter(list);

        buttonClickListener();
    }


    private void buttonClickListener() {
        //  btn_photo.setOnClickListener(ClickListener);
        btn_esell.setOnClickListener(ClickListener);
        btn_csell.setOnClickListener(ClickListener);
        img_book.setOnClickListener(ClickListener);
    }

    View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_esell:
                    if (edt_sbook.getText().toString() == null || edt_sbook.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "도서명을 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if (edt_sauthor.getText().toString() == null || edt_sauthor.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "저자를 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if (edt_spublisher.getText().toString() == null || edt_spublisher.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "출판사를 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if (edt_scost.getText().toString() == null || edt_scost.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "가격을 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if (edt_sstate.getText().toString() == null || edt_sstate.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "상태를 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if (edt_sother.getText().toString() == null || edt_sother.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "기타를 입력하세요.", Toast.LENGTH_SHORT).show();
                    else {
                        //asyncTaskCall();
                        ImageAsyncTaskCall();

                        Toast.makeText(getApplicationContext(), "팝니다 물품이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    break;

                case R.id.img_book:
                    Intent intent = new Intent();
                    // Gallery 호출
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    // 잘라내기 셋팅
                    //                intent.putExtra("crop", "true");
                    //                intent.putExtra("aspectX", 1);
                    //                 intent.putExtra("aspectY", 1);
                    //   intent.putExtra("outputX", 256);
                    //    intent.putExtra("outputY", 256);
                    //    intent.putExtra("scale",true);
                    //    intent.putExtra("return-data", true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), 100);
                    break;

                case R.id.btn_csell:
                    finish();
                    break;
            }
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {

                try {
                    photoUri = getImageNameToUri(data.getData());  //이미지 절대경로 구하기!!

                    Toast.makeText(ChoiceSell.this, photoUri, Toast.LENGTH_SHORT).show();
                    //Log.i("이미지 :", absolutePath);

                    Bitmap image_bitmap = BitmapFactory.decodeFile(photoUri);
                    image_bitmap = Bitmap.createScaledBitmap(image_bitmap, 550, 600, true);

                    ExifInterface exif = new ExifInterface(photoUri);     //이미지 자동회전 방지!!
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);
                    image_bitmap = rotate(image_bitmap, exifDegree);

                    img_book.setImageBitmap(image_bitmap);
                    // ImageAsyncTaskCall();

                } catch (Exception e) {
                    e.printStackTrace();
                }

             /*   Bundle extras =data.getExtras();    이미지 자른 후
                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");
                    img_book.setImageBitmap(photo);
                }    */
            }
        }
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(Uri.parse(data.toString()), null, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        photoName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        return imgPath;
    }


    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }


 /*   public void asyncTaskCall() {
        new MyAsyncTask().execute();
    }

    public class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {  // 통신을 위한 Thread
            String result = SendSell();
            return result;
        }

        public String encodeString(Properties params) {  //한글 encoding??
            StringBuffer sb = new StringBuffer(256);
            Enumeration names = params.propertyNames();

            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String value = params.getProperty(name);
                sb.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));

                if (names.hasMoreElements()) sb.append("&");
            }
            return sb.toString();
        }

        private String SendSell() { //데이터 보내고 받아오기!!

            HttpURLConnection urlConnection = null;
            URL url = null;
            DataOutputStream out = null;
            BufferedInputStream buf = null;
            BufferedReader bufreader = null;

            Properties prop = new Properties();

            prop.setProperty("txt_name", txt_name.getText().toString());
            prop.setProperty("txt_photoName", edt_sbook.getText().toString());
            prop.setProperty("edt_sbook", edt_sbook.getText().toString());
            prop.setProperty("edt_sauthor", edt_sauthor.getText().toString());
            prop.setProperty("edt_spublisher", edt_spublisher.getText().toString());
            prop.setProperty("edt_scost", edt_scost.getText().toString());
            prop.setProperty("edt_sstate", edt_sstate.getText().toString());
            prop.setProperty("edt_sother", edt_sother.getText().toString());

            encodedString = encodeString(prop);

            try {
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                url=new URL("http://" + ip + ":8080/usedBook/inputSell.jsp");
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);

                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                out = new DataOutputStream(urlConnection.getOutputStream());

                out.writeBytes(encodedString);

                out.flush();    //서버로 버퍼의 내용 전송

                buf = new BufferedInputStream(urlConnection.getInputStream());
                bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8"));

                String line = null;
                String result = "";

                while ((line = bufreader.readLine()) != null) {
                    result += line;
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            } finally {
                urlConnection.disconnect();  //URL 연결해제
            }
        }
    }

*/

    public void ImageAsyncTaskCall(){
        new ImageAsyncTask().execute();
    }

    public class ImageAsyncTask extends AsyncTask<Void,Integer,String> {
        @Override
        protected String doInBackground(Void... params) {  // 통신을 위한 Thread
            doFileUpload();
            return null;
        }

        public void doFileUpload() {
            URL connectUrl = null;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;

            try {
                String boundary = "^******^";

                // 데이터 경계선
                String delimiter = "\r\n--" + boundary + "\r\n";

                Log.i("이미지 :", photoUri );
                FileInputStream fileInputStream = new FileInputStream(photoUri);
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                connectUrl=new URL("http://" + ip + ":8080/usedBook/saveImage.jsp");

                // open connection
                conn = (HttpURLConnection) connectUrl.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                Log.i("이미지 :", "연결??");
                // write data
                dos = new DataOutputStream(conn.getOutputStream());


                StringBuffer pd = new StringBuffer();
                pd.append(delimiter);
                pd.append(setText("id", sharedPreferences.getString("ID", "" )));
                pd.append(delimiter);
                pd.append(setText("edt_sbook", edt_sbook.getText().toString()));
                pd.append(delimiter);
                pd.append(setText("edt_sauthor", edt_sauthor.getText().toString()));
                pd.append(delimiter);
                pd.append(setText("edt_spublisher", edt_spublisher.getText().toString()));
                pd.append(delimiter);
                pd.append(setText("edt_scost", edt_scost.getText().toString()));
                pd.append(delimiter);
                pd.append(setText("edt_sstate", edt_sstate.getText().toString()));
                pd.append(delimiter);
                pd.append(setText("edt_sother", edt_sother.getText().toString()));

                pd.append(delimiter);
                pd.append(setImage("image", photoUri));
                pd.append("\r\n");

                dos.writeUTF(pd.toString());

                int bytesAvailable = fileInputStream.available();
                int maxBufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);

                byte[] buffer = new byte[bufferSize];
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0)
                {
                    dos.write(buffer, 0, bufferSize); //이미지 전송
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(delimiter);

                //이미지와 텍스트값을 전송할때와 다르게 뒤에 --가 더 붙으며 전송데이터 값의 끝
                fileInputStream.close();
                dos.flush(); //전송완료

                //전송을 완료하고 꼭 서버에서 전송된 결과값을 받아야 완벽하게 업로드된 텍스트
                //이미지가 저장된다. 중요하다 ..
                BufferedReader rd = null;
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line = null;
                while ((line = rd.readLine()) != null) {
                    Log.d("BufferedReader: ", line);
                }
                dos.close();

            } catch (MalformedURLException e) {
                Log.d("MalformedURLException", e.toString());
            }catch(Exception ex){
                Log.d("MalformedURLException", ex.toString());
            }

        }

        //텍스트값 처리
        private String setText(String tname, String text){
            return "Content-Disposition: form-data; name=\""+tname+"\"r\n\r\n"+text;
        }

        //이미지값 처리
        private String setImage(String iname, String image){
            return "Content-Disposition: form-data; name=\""+iname+"\";filename=\""+image+"\"\r\n";
        }

    }

    public void onBackPressed(){
        Intent intent1 = new Intent(getApplicationContext(), Main.class);
        startActivity(intent1);
        finish();
    }
}


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

public class SellUpdateBook extends AppCompatActivity {

    ImageView info_image;

    TextView txt_name;
    TextView txt_phone;

    EditText info_book;
    EditText info_author;
    EditText info_publisher;
    EditText info_cost;
    EditText info_state;
    EditText info_other;

    Button btn_cancel;
    Button btn_update;
            ;

    int index=0;
    String encodedString="";
    String photoUri = "";
    String photoName= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_update_book);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);

        info_image =(ImageView)findViewById(R.id.info_image);

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_name.setText(sharedPreferences.getString("name", ""));

        txt_phone = (TextView) findViewById(R.id.txt_phone);
        txt_phone.setText(sharedPreferences.getString("tel", ""));

        info_book       =   (EditText)findViewById(R.id.info_book);
        info_author     =   (EditText)findViewById(R.id.info_author);
        info_publisher  =   (EditText)findViewById(R.id.info_publisher);
        info_cost       =   (EditText)findViewById(R.id.info_cost);
        info_state      =   (EditText)findViewById(R.id.info_state);
        info_other      =   (EditText)findViewById(R.id.info_other);

        btn_cancel  =   (Button)findViewById(R.id.btn_cancel);
        btn_update  =   (Button)findViewById(R.id.btn_update);

        Intent i = getIntent();
        index = i.getExtras().getInt("index");

        Log.i("eeee", String.valueOf(index));

        asyncTaskCall();
        buttonClickListener();


    }

    private void buttonClickListener() {
        btn_cancel.setOnClickListener(ClickListener);
        btn_update.setOnClickListener(ClickListener);
        info_image.setOnClickListener(ClickListener);
    }

    View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_update:
                   if(info_book.getText().toString() == null || info_book.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "도서명을 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if(info_author.getText().toString() == null || info_author.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "저자를 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if(info_publisher.getText().toString() == null || info_publisher.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "출판사를 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if(info_cost.getText().toString() == null || info_cost.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "가격을 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if(info_state.getText().toString() == null || info_state.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "상태를 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if(info_other.getText().toString() == null || info_other.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "기타를 입력하세요.", Toast.LENGTH_SHORT).show();
                    else {
                        //SendUpdateBuy();
                       if(photoUri.equals("")){
                           NoImageasyncTaskCall();
                       }else {
                           UpdateAsyncTaskCall();
                       }
                       Intent i = new Intent(getApplicationContext(), Mysell.class);
                       startActivity(i);
                       Toast.makeText(getApplicationContext(), "팝니다 물품이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                       finish();
                    }
                    break;

                case R.id.info_image:
                    Intent intent = new Intent();
                    // Gallery 호출
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), 100);
                    break;

                case R.id.btn_cancel:
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

                    //Log.i("이미지 :", absolutePath);

                    Bitmap image_bitmap = BitmapFactory.decodeFile(photoUri);
                    image_bitmap = Bitmap.createScaledBitmap(image_bitmap, 550, 600, true);

                    ExifInterface exif = new ExifInterface(photoUri);     //이미지 자동회전 방지!!
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);
                    image_bitmap = rotate(image_bitmap, exifDegree);

                    info_image.setImageBitmap(image_bitmap);
                    // ImageAsyncTaskCall();

                } catch (Exception e) {
                    e.printStackTrace();
                }

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


    public void asyncTaskCall(){
        new MyAsyncTask().execute();
    }

    public class MyAsyncTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {  // 통신을 위한 Thread
            String result =recvList();
            return result;
        }

        public String encodeString(Properties params) {  //한글 encoding??
            StringBuffer sb = new StringBuffer(256);
            Enumeration names = params.propertyNames();

            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String value = params.getProperty(name);
                sb.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value) );

                if (names.hasMoreElements()) sb.append("&");
            }
            return sb.toString();
        }

        private String recvList() { //데이터 보내고 받아오기!!

            HttpURLConnection urlConnection=null;
            URL url =null;
            DataOutputStream out=null;
            BufferedInputStream buf=null;
            BufferedReader bufreader=null;

            Properties prop = new Properties();
            prop.setProperty("index", String.valueOf(index));

            encodedString = encodeString(prop);

            try{
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                url=new URL("http://" + ip + ":8080/usedBook/detailSell.jsp");
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);

                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                out = new DataOutputStream(urlConnection.getOutputStream());

                out.writeBytes(encodedString);

                out.flush();    //서버로 버퍼의 내용 전송

                buf = new BufferedInputStream(urlConnection.getInputStream());
                bufreader = new BufferedReader(new InputStreamReader(buf,"utf-8"));

                String line=null;
                String result="";

                while((line=bufreader.readLine())!=null){
                    result += line;
                }

                return result;

            }catch(Exception e){
                e.printStackTrace();
                return "";
            }finally{
                urlConnection.disconnect();  //URL 연결해제
            }
        }
        protected void onPostExecute(String result){  //Thread 이후 UI 처리 result는 Thread의 리턴값!!!
            jsonParserList(result);
        }
    }


    private void jsonParserList(String recv) {

        Log.i("서버에서 받은 전체 내용 : ", recv);

        try{
            JSONObject json=new JSONObject(recv);
            JSONArray jArr =json.getJSONArray("List");

            for(int i=0; i<jArr.length();i++){
                json=jArr.getJSONObject(i);

                info_book.setText(json.getString("info_book"));
                info_author.setText(json.getString("info_author"));
                info_publisher.setText(json.getString("info_publisher"));
                info_cost.setText(json.getString("info_cost"));
                info_state.setText(json.getString("info_state"));
                info_other.setText(json.getString("info_other"));
                info_image.setImageBitmap(imageAsyncTaskCall(json.getString("photo")));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public Bitmap imageAsyncTaskCall(String image){
        try {
            return new ImageAsyncTask().execute(image).get();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public class ImageAsyncTask extends AsyncTask<String,Integer,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... image) {  // 통신을 위한 Thread
            Bitmap bm=null;
            try {
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                URL url=new URL("http://" + ip + ":8080/usedBook/image/"+image[0]);
                URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        }
    }







    public void UpdateAsyncTaskCall(){
        new UpdateAsyncTask().execute();
    }

    public class UpdateAsyncTask extends AsyncTask<Void,Integer,String> {
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

                connectUrl=new URL("http://" + ip + ":8080/usedBook/updateSell.jsp");

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
                pd.append(setText("index", String.valueOf(index)));
                pd.append(delimiter);
                pd.append(setText("edt_sbook", info_book.getText().toString()));
                pd.append(delimiter);
                pd.append(setText("edt_sauthor", info_author.getText().toString()));
                pd.append(delimiter);
                pd.append(setText("edt_spublisher", info_publisher.getText().toString()));
                pd.append(delimiter);
                pd.append(setText("edt_scost", info_cost.getText().toString()));
                pd.append(delimiter);
                pd.append(setText("edt_sstate", info_state.getText().toString()));
                pd.append(delimiter);
                pd.append(setText("edt_sother", info_other.getText().toString()));

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







    public void NoImageasyncTaskCall(){
        new NoImageAsyncTask().execute();
    }

    public class NoImageAsyncTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {  // 통신을 위한 Thread
            String result =recvList();
            return result;
        }

        public String encodeString(Properties params) {  //한글 encoding??
            StringBuffer sb = new StringBuffer(256);
            Enumeration names = params.propertyNames();

            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String value = params.getProperty(name);
                sb.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value) );

                if (names.hasMoreElements()) sb.append("&");
            }
            return sb.toString();
        }

        private String recvList() { //데이터 보내고 받아오기!!

            HttpURLConnection urlConnection=null;
            URL url =null;
            DataOutputStream out=null;
            BufferedInputStream buf=null;
            BufferedReader bufreader=null;

            Properties prop = new Properties();
            prop.setProperty("index", String.valueOf(index));
            prop.setProperty("edt_sbook", info_book.getText().toString());
            prop.setProperty("edt_sauthor", info_author.getText().toString());
            prop.setProperty("edt_spublisher", info_publisher.getText().toString());
            prop.setProperty("edt_scost", info_cost.getText().toString());
            prop.setProperty("edt_sstate", info_state.getText().toString());
            prop.setProperty("edt_sother", info_other.getText().toString());

            encodedString = encodeString(prop);

            try{
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                url=new URL("http://" + ip + ":8080/usedBook/updateSellNoImage.jsp");
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);

                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                out = new DataOutputStream(urlConnection.getOutputStream());

                out.writeBytes(encodedString);

                out.flush();    //서버로 버퍼의 내용 전송

                buf = new BufferedInputStream(urlConnection.getInputStream());
                bufreader = new BufferedReader(new InputStreamReader(buf,"utf-8"));

                String line=null;
                String result="";

                while((line=bufreader.readLine())!=null){
                    result += line;
                }

                return result;

            }catch(Exception e){
                e.printStackTrace();
                return "";
            }finally{
                urlConnection.disconnect();  //URL 연결해제
            }
        }
    }



    public void onBackPressed(){
        Intent intent1 = new Intent(getApplicationContext(), Main.class);
        startActivity(intent1);
        finish();
    }

}

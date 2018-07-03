package com.example.acer.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {

    EditText name, code;
   static TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.editText);
        code = findViewById(R.id.editText3);
        textView = findViewById(R.id.textView2);


    }

    public static class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                String result = "";
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1){
                    char c = (char)data;
                    result += c;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
// [{"id":300,"main":"Drizzle","description":"light intensity drizzle","icon":"09d"}]
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String mess = "";
                JSONObject jsonObject = new JSONObject(s);
                String jsonString =jsonObject.getString("weather");
                JSONArray jsonArray = new JSONArray(jsonString);
                for(int i = 0; i<jsonArray.length(); i++){
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    Log.i("Main :", jsonPart.getString("main"));
                    Log.i("Descrition :", jsonPart.getString("description"));
                    mess = jsonPart.getString("main")+" : "+jsonPart.getString("description");
                }
                    textView.setText(mess);

            } catch (Exception e1) {
                e1.printStackTrace();
                textView.setText("Something went wrong, Please try again");
            }

        }
    }

    public void WhatsWeather(View view){
        DownloadTask task = new DownloadTask();
        String place = name.getText().toString();
        String cc = code.getText().toString();
        View v = this.getCurrentFocus();
        if(v != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            Log.i("Test Run", "window is closed");
        }


        textView.setText(" ");
        try {
            if(cc.equals("")){
                String s = task.execute("http://openweathermap.org/data/2.5/weather?q="+place+"&appid=b6907d289e10d714a6e88b30761fae22").get();
            }else{
                String s = task.execute("http://openweathermap.org/data/2.5/weather?q="+place+","+cc+"&appid=b6907d289e10d714a6e88b30761fae22").get();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

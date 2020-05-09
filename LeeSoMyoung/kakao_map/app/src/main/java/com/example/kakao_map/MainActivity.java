package com.example.kakao_map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    JSONArray jsonArray;
    ArrayList<String> arrayList,arrayList2,arrayList3;
    ArrayAdapter<String> arrayAdapter,arrayAdapter2;
    Background task;
    Background1 gu_task;
    Background2 gun_task;

    JSONObject city;
    JSONObject gu;
    JSONObject n_x,n_y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = new MapView(this);
        final Spinner SI=(Spinner) findViewById(R.id.si);
        final Spinner GU=(Spinner) findViewById(R.id.gu);
        final Spinner DONG=(Spinner) findViewById(R.id.dong);
        //기상청 위치 api
        final String url="http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt";
        //mapView.setCurrentLocationEventListener(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        task=new Background();
        city=new JSONObject();
        gu=new JSONObject();

        try{
            jsonArray=task.execute(url).get();
            arrayList=new ArrayList<>();
            int len=jsonArray.length();
            for(int i=0;i<len;++i){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                arrayList.add(jsonObject.getString("value"));
                city.put(jsonObject.getString("value"),jsonObject.getString("code"));
            }
            arrayAdapter=new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_dropdown_item,arrayList);

            SI.setAdapter(arrayAdapter);
        }
        catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
        } catch(JSONException e){
            e.printStackTrace();
        }

    /*    SI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            JSONArray Gu_jsonArray;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s=parent.getItemAtPosition(position).toString();
                System.out.println("####"+s);
                gu_task=new Background1();

                try{
                    Gu_jsonArray=gu_task.execute("http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl.11.json.txt"+city.getString(s)+".json.txt").get();
                    arrayList2=new ArrayList<>();

                    for(int j=0;j<Gu_jsonArray.length();++j){
                        JSONObject jsonObject=Gu_jsonArray.getJSONObject(j);
                        arrayList2.add(jsonObject.getString("value"));

                        gu.put(jsonObject.getString("value"),jsonObject.getString("code"));
                    }

                    arrayAdapter2=new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,arrayList2);

                    GU.setAdapter(arrayAdapter2);
                }
                catch(InterruptedException | ExecutionException e){
                    e.printStackTrace();
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
*/
    }

    public class Background extends AsyncTask<String, Void, JSONArray>{
        JSONArray b_jsonArray;

        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected JSONArray doInBackground(String... strUrl){
            try{
                URL url=new URL(strUrl[0]);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                InputStream in=new BufferedInputStream(conn.getInputStream());
                BufferedReader br=new BufferedReader(new InputStreamReader(in,"UTF-8"));
                StringBuffer builder=new StringBuffer();

                String inputString=null;
                while((inputString=br.readLine())!=null){
                    builder.append(inputString);
                }
                String s=builder.toString();
                b_jsonArray=new JSONArray(s);

                conn.disconnect();
                br.close();
                in.close();
            }
            catch(IOException e){
                e.printStackTrace();
            } catch(JSONException e){
                e.printStackTrace();
            }
            return b_jsonArray;
        }
    }
    public class Background1 extends AsyncTask<String, Void, JSONArray>{
        JSONArray gu_jsonArray;

        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected JSONArray doInBackground(String... strUrl){
            try{
                URL url=new URL(strUrl[0]);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                InputStream in=new BufferedInputStream(conn.getInputStream());
                BufferedReader br=new BufferedReader(new InputStreamReader(in,"UTF-8"));
                StringBuffer builder=new StringBuffer();

                String inputString=null;
                while((inputString=br.readLine())!=null){
                    builder.append(inputString);
                }
                String s=builder.toString();
                gu_jsonArray=new JSONArray(s);

                conn.disconnect();
                br.close();
                in.close();
            }
            catch(IOException e){
                e.printStackTrace();
            } catch(JSONException e){
                e.printStackTrace();
            }
            return gu_jsonArray;
        }
    }
    public class Background2 extends AsyncTask<String, Void, JSONArray>{
        JSONArray b_jsonArray;

        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected JSONArray doInBackground(String... strUrl){
            try{
                URL url=new URL(strUrl[0]);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                InputStream in=new BufferedInputStream(conn.getInputStream());
                BufferedReader br=new BufferedReader(new InputStreamReader(in,"UTF-8"));
                StringBuffer builder=new StringBuffer();

                String inputString=null;
                while((inputString=br.readLine())!=null){
                    builder.append(inputString);
                }
                String s=builder.toString();
                b_jsonArray=new JSONArray(s);

                conn.disconnect();
                br.close();
                in.close();
            }
            catch(IOException e){
                e.printStackTrace();
            } catch(JSONException e){
                e.printStackTrace();
            }
            return b_jsonArray;
        }
    }
}

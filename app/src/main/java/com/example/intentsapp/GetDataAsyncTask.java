package com.example.intentsapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class GetDataAsyncTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "GetDataAsyncTask";
    public AsyncResponse delegate;

    public GetDataAsyncTask (AsyncResponse delegate){
        this.delegate=delegate;
    }

    public interface AsyncResponse{
        void proccessFinish (String output);
    }

    @Override
    protected String doInBackground(String... strings){
        try {
            String login = strings[0];
            String pass = strings[1];
            String url_link = strings[2];

            Log.d(TAG, "doInBackground: " + url_link);
            String data = URLEncoder.encode("login", "UTF-8")+"="+URLEncoder.encode(login,"UTF-8");
            data+="&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");

            URL url=new URL (url_link);
            URLConnection conn=url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();
            BufferedReader reader= new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb= new StringBuilder();
            String line= null;
            while((line=reader.readLine())!=null){
                sb.append(line);
                //Log.d(TAG, "doInBackground: " + sb.append(line));
                break;
            }
            return sb.toString();

        }catch (Exception e){
            //e.printStackTrace();
            return new String("Exception: "+e.getMessage());
        }
        //return null;
    }


    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(s);
        if(result!=null && !result.equals("")){
            delegate.proccessFinish(result);
        }
    }
}

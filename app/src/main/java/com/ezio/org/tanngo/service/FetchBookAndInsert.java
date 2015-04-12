package com.ezio.org.tanngo.service;

import android.content.Context;
import android.os.AsyncTask;

import com.ezio.org.tanngo.data.Word;
import com.ezio.org.tanngo.data.WordsDbHelper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ezio on 2015/4/12.
 */
public class FetchBookAndInsert extends AsyncTask<String,Integer,Boolean> {

    private Context mContext;

    public FetchBookAndInsert(Context context) {
        super();
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {






        if (params!=null&&params.length==2) {

            BufferedReader readerJSON = null;



            String fileUrl = params[0];
            String fileName = params[1];

            WordsDbHelper mDbHelper = new WordsDbHelper(mContext);


            if (!mDbHelper.isTableExist(fileName)) {


                try {
                    URL url = new URL(fileUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);


                    readerJSON = new BufferedReader(new InputStreamReader(connection.getInputStream()));


                    Gson gson = new Gson();

                    List<Word> wordList = new ArrayList<Word>();

                    //TODO:handle json
                    gson.fromJson(readerJSON,Word.class);



                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (readerJSON != null)
                            readerJSON.close();

                    } catch (Exception e) {
                        throw new RuntimeException("读取关闭失败");
                    }


                }

            }

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }



    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}

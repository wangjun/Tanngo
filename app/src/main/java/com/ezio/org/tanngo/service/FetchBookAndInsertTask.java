package com.ezio.org.tanngo.service;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.ezio.org.tanngo.data.WordsContract;
import com.ezio.org.tanngo.data.WordsDbHelper;
import com.ezio.org.tanngo.utils.MyPreference;
import com.ezio.org.tanngo.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ezio on 2015/4/12.
 */
public class FetchBookAndInsertTask extends AsyncTask<String,Float,Boolean> {

    private Context mContext;
    private MyPreference myPref;
    ProgressDialog progressDialog;


    public final static Float ON_CONNECTION_CREATED =1000.0f;
    public final static Float ON_DOWNLOAD_FINISHED =4000.0f;
    public final static Float ON_JSON_TRANSIMTED =5000.0f;
    public final static Float ON_FINISHED =10000.0f;

    /**
     * this task will get words book JSON file from net , and insert words to SQL. when use execute()
     * you must put two args into ,  file url and file name , file name also used as SQL table name
     * @param context context
     * @author
     * */
    public FetchBookAndInsertTask(Context context) {
        super();
        mContext = context;
        myPref = new MyPreference(context);
    }


    @Override
    protected Boolean doInBackground(String... params) {




        boolean aBoolean=false;


        if (params!=null&&params.length==2) {

            BufferedReader readerJSON = null;
            HttpURLConnection mConnection = null;



            String fileUrl = params[0];
            String fileName = params[1];

            WordsDbHelper mDbHelper = new WordsDbHelper(mContext);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();


            if (!mDbHelper.isTableExist(fileName)) {


                try {

                    //network
                    mConnection = httpInit(fileUrl);




                    publishProgress(ON_CONNECTION_CREATED);

                    //stream
                    readerJSON = new BufferedReader(new InputStreamReader(mConnection.getInputStream()));

                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line=readerJSON.readLine())!=null){
                        response.append(line);
                    }


                    publishProgress(ON_DOWNLOAD_FINISHED);

                    //json
                    //Gson gson = new Gson();
                    //Word[] wordsList = gson.fromJson(response.toString(),Word[].class);

                    //List<Word> wordsList = gson.fromJson(response.toString(), new TypeToken<List<Word>>(){}.getType());

                    publishProgress(ON_JSON_TRANSIMTED);


                    //sql
                    //TODO:将filename点全部去掉？

                    String noExFileName=Utility.getFileNameNoEx(fileName);
                    noExFileName = "table"+noExFileName;
                    myPref.setDictName(noExFileName);
                    mDbHelper.creatTableIfNotExist(myPref.getDictName());

                    try{
                        JSONArray jsonArray = new JSONArray(response.toString());

                        Float temp = ON_JSON_TRANSIMTED;
                        int jsonLength = jsonArray.length();
                        Float diff = (ON_FINISHED-ON_JSON_TRANSIMTED)/jsonLength;


                        for (int i=0;i<jsonLength;i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String word = jsonObject.getString("word");
                            String kana = jsonObject.getString("kana");
                            String definition = jsonObject.getString("definition");
                            String example_sentence = jsonObject.getString("example_sentence");


                            ContentValues values = new ContentValues();

                            values.put(WordsContract.WordsEntry.COLUMN_WORD, word);
                            values.put(WordsContract.WordsEntry.COLUMN_KANA, kana);
                            values.put(WordsContract.WordsEntry.COLUMN_EXAMPLE_SENTENCE, definition);
                            values.put(WordsContract.WordsEntry.COLUMN_DEFINITION, example_sentence);

                            long newRowId = db.insert(noExFileName, null, values);
                            temp=temp+diff;
                            publishProgress(temp);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


//                    for (int i = 0; i<wordsList.size();i++){
//                        ContentValues values = new ContentValues();
//
//                        values.put(WordsContract.WordsEntry.COLUMN_WORD, wordsList.get(i).getWord());
//                        values.put(WordsContract.WordsEntry.COLUMN_KANA, wordsList.get(i).getKana());
//                        values.put(WordsContract.WordsEntry.COLUMN_EXAMPLE_SENTENCE, wordsList.get(i).getExample_sentence());
//                        values.put(WordsContract.WordsEntry.COLUMN_DEFINITION, wordsList.get(i).getDefinition());
//
//
//                        long newRowId = db.insert(myPref.getDictName(), null, values);
//
//                        temp=temp+diff;
//                        publishProgress(temp);
//                    }

                    aBoolean= true;



                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    try {
                        if (readerJSON != null)
                            readerJSON.close();

                    } catch (Exception e) {
                        throw new RuntimeException("读取关闭失败");
                    }

                    try{
                        if (mConnection!=null){
                            mConnection.disconnect();
                        }

                    }catch (Exception e){
                        throw new RuntimeException("网络流关闭失败");
                    }


                }

            }

        }
        return aBoolean;
    }

    private HttpURLConnection httpInit(String url) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(8000);
        return connection;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog =new ProgressDialog(mContext);
        progressDialog.setTitle("下载文件中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        progressDialog.setMax(10000);

        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Float... values) {
        super.onProgressUpdate(values);

        progressDialog.setProgress(values[0].intValue());


    }



    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if (!aBoolean){
            Utility.ShowToast("下载失败",mContext);
        }else {
            //TODO:回到HomeActivity？
            Utility.ShowToast("下载成功",mContext);
        }

        progressDialog.cancel();
    }

    public static void main(String[] args){


    }


}

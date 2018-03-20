package com.example.zw.drysisterdemo.API;

import com.example.zw.drysisterdemo.Model.Sister;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * 描述：网络请求处理相关类
 *
 * @author coder-pig： 2016/08/07 14:28
 */
public class SisterApi {
  private static final String TAG = "Network";
  private static final String BASE_URL   = "http://gank.io/api/data/福利/";

  /**
   * 查询妹子信息
   */
   public ArrayList<Sister> fetchSister(int count, int page) {
       String fetchUrl = BASE_URL + count + "/" + page;
       ArrayList<Sister> sisters = new ArrayList<>();
       try{
           URL url = new URL(fetchUrl);
           HttpURLConnection conn = (HttpURLConnection)url.openConnection();
           conn.setRequestMethod("GET");
           conn.setReadTimeout(5000);
           int code = conn.getResponseCode();
           if(code == 200){
               InputStream in = conn.getInputStream();
               byte[] data = readFromStream(in);
               String result = new String(data, "UTF-8");
               sisters = parseSister(result);
           }
       }catch (Exception e){
           e.printStackTrace();
       }
       return sisters;
   }

    /**
     * 解析返回Json数据的方法
     */
    public ArrayList<Sister>parseSister(String content) throws Exception{
        ArrayList<Sister>sisters = new ArrayList<>();
        JSONObject object = new JSONObject(content);
        JSONArray array = object.getJSONArray("results");
        for (int i = 0;i<array.length();i++){
            JSONObject results = (JSONObject)array.get(i);
            Sister sister = new Sister();
            sister.set_id(results.getString("_id"));
            sister.setCreatedAt(results.getString("createdAt"));
            sister.setDesc(results.getString("desc"));
            sister.setPublishedAt(results.getString("publishedAt"));
            sister.setSource(results.getString("source"));
            sister.setType(results.getString("type"));
            sister.setUrl(results.getString("url"));
            sister.setUsed(results.getBoolean("used"));
            sister.setWho(results.getString("who"));
            sisters.add(sister);
        }
        return sisters;
    }


    /**
     * 读取流中数据的方法
     */
    public byte[] readFromStream(InputStream inputStream) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0 ,len);
        }
        inputStream.close();
//        outputStream.close();
        return  outputStream.toByteArray();
    }
}

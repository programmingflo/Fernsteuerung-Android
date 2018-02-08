package programming146.fernsteuerung;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author Tim Grohmann fit to project by Florian Mansfeld
 *
 * Communication with server
 */
public class ServerConnection extends AsyncTask<String,Void,JSONObject> {
    @SuppressLint("StaticFieldLeak")
    private Context c;

    ServerConnection(Context context){
        this.c = context;
    }

    private String downloadFromUrl(String[] node) throws IOException{
        SharedPreferences sharedPreferences = this.c.getSharedPreferences("preferences",Context.MODE_PRIVATE);
        String urlString = this.c.getResources().getString(R.string.baseurl) + node[0];
        InputStream is = null;

        ArrayList<String> account = new ArrayList<>();
        account.add("username");
        account.add(sharedPreferences.getString("username","test"));

        ArrayList<String> password = new ArrayList<>();
        password.add("password");
        password.add(sharedPreferences.getString("password","test0815"));

        ArrayList<ArrayList<String>> request = new ArrayList<>();
        request.add(account);
        request.add(password);

        request.remove(0);

        StringBuilder requestString = new StringBuilder();

        for(ArrayList<String> object: request){
            if(object.size() == 2){
                requestString.append(URLEncoder.encode(object.get(0), "utf-8")).append("=").append(URLEncoder.encode(object.get(1), "utf-8")).append("&");
            }
        }
        if(requestString.charAt(requestString.length()-1)=='&' && requestString.length() > 0){
            requestString = new StringBuilder(requestString.substring(0, requestString.length() - 1));
        }

        byte[]  postData        = requestString.toString().getBytes(Charset.forName("utf-8"));
        //Log.v("146s","postData: "+postData);
        int     postDataLength  = postData.length;
        //Log.v("146s","postDataLength: "+postDataLength);
        String content = "";

        try{
            URL url = new URL(urlString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds*/);
            conn.setConnectTimeout(15000 /*milliseconds*/);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(postDataLength);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8")
            );
            writer.write(requestString.toString());
            writer.flush();
            writer.close();
            os.close();
            //Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("146s","response: "+response);
            is = conn.getInputStream();

            //Convert the InputStream into a String
            String contentAsString = convertStreamToString(is);
            Log.d("146s","content: "+contentAsString);
            content=contentAsString;
            return contentAsString;
        }catch (IOException e){
            Log.d("146s","content: "+content+" error: "+e);
        }finally {
            if(is != null){
                try{
                    is.close();
                }catch(IOException e){
                    e.printStackTrace();
                    Log.d("146s","content: "+content+" error: "+e);
                }
            }
        }
        return "";
    }

    private static String convertStreamToString(InputStream is){
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    protected final JSONObject doInBackground(String... request){
        try{
            return new JSONObject(downloadFromUrl(request));
        }catch(IOException e){
            Log.d("146s","Load failed");
            return null;
        }catch(JSONException e){
            Log.d("146s","Parsing failed");
            return null;
        }
    }
}

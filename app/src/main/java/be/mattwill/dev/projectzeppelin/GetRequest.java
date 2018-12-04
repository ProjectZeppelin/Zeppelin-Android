package be.mattwill.dev.projectzeppelin;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetRequest {
  private static final String TAG = GetRequest.class.getSimpleName();

  static String get(String url) {
    HttpURLConnection connection = null;
    BufferedReader reader = null;
    String result = null;
    try {
      URL requestURL = new URL(url);
      connection = (HttpURLConnection) requestURL.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
      InputStream inputStream = connection.getInputStream();
      reader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuilder builder = new StringBuilder();

      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line);
        builder.append("\n");
      }

      if (builder.length() == 0) {
        return null;
      }

      result = builder.toString();
    } catch (IOException e) {
      e.getStackTrace();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    if(result != null) {
      Log.d(TAG, result);
    }else{
      Log.d(TAG, "Could not get result");
    }
    return result;
  }
}

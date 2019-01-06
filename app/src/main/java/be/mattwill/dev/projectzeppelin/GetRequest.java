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

  // basic rest GET request this should be executed on a thread that is not the UI thread
  static String get(String url) {
    Log.i(TAG, "Get request to " + url);
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
      // read the incoming packet and append it to the string builder
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
      // always close the connection
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
    // return the data or null otherwise
    return result;
  }
}

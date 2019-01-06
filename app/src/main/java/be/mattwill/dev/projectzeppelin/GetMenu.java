package be.mattwill.dev.projectzeppelin;

import android.os.AsyncTask;
import android.view.Menu;
import android.widget.TextView;

import org.json.JSONArray;

import java.lang.ref.WeakReference;


public class GetMenu extends AsyncTask<String, Void, String> {

  private WeakReference<Menu> mList;

  GetMenu(Menu list) {
    this.mList = new WeakReference<>(list);
  }

  // Do a get request in another thread
  @Override
  protected String doInBackground(String... strings) {
    return GetRequest.get(strings[0]);
  }

  // return a menu object after the get request
  @Override
  protected void onPostExecute (String s) {
    super.onPostExecute(s);

    String result = "";

    try {
      JSONArray languages = new JSONArray(s);
      for (int i = 0; i < languages.length(); i++) {
        String val = languages.getJSONObject(i).getString("title");
        result += " ";
        result += val;
        mList.get().add(val);

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

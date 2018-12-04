package be.mattwill.dev.projectzeppelin;

import android.os.AsyncTask;
import android.view.Menu;
import android.widget.TextView;

import org.json.JSONArray;

import java.lang.ref.WeakReference;

public class GetMenu extends AsyncTask<String, Void, String> {

  private WeakReference<TextView> mTitleText;
  private WeakReference<Menu> mList;

  GetMenu(TextView titleView, Menu list) {
    this.mTitleText = new WeakReference<>(titleView);
    this.mList = new WeakReference<>(list);
  }

  @Override
  protected String doInBackground(String... strings) {
    return GetRequest.get(strings[0]);
  }

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

    mTitleText.get().setText(result);
  }
}

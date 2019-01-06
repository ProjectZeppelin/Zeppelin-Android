package be.mattwill.dev.projectzeppelin;

import android.os.AsyncTask;
import android.util.Base64;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.lang.ref.WeakReference;

import static android.provider.Settings.System.getString;


public class GetLanguage extends AsyncTask<String, Void, String> {

  private WeakReference<LinearLayout> mLayout;
  private WeakReference<MainActivity> mActivity;


  GetLanguage(LinearLayout layout, MainActivity activity) {
    this.mActivity = new WeakReference<>(activity);
    this.mLayout = new WeakReference<>(layout);
  }

  @Override
  protected String doInBackground(String... strings) {
    return GetRequest.get(strings[0]);
  }


  protected void displayError(){
    TextView Error = new TextView(mActivity.get());

    Error.setText(mActivity.get().getString(R.string.DataRetrieveError));


    mLayout.get().addView(Error);
  }

  // generate a new LinearLayout based on all the data from a new language
  @Override
  protected void onPostExecute (String s) {
    super.onPostExecute(s);

    String result = "";

    // convert our result to a JSON array
    try {
      JSONArray languages = new JSONArray(s);
      // generate a new code snippet for each part of our language
      for (int i = 0; i < languages.length(); i++) {
        String val = languages.getJSONObject(i).getString("data");

        JSONArray data = new JSONArray(val);
        result += " ";
        result += val;
        mLayout.get().removeAllViews();
        for (int j = 0; j < data.length(); j++) {
          String title = data.getJSONObject(j).getString("title");
          String subtitle = data.getJSONObject(j).getString("subtitle");
          String gist = data.getJSONObject(j).getString("gist");

          TextView Title = new TextView(mActivity.get());
          TextView Subtitle = new TextView(mActivity.get());
          WebView Gist = new WebView(mActivity.get());

          Title.setText(title);
          Title.setTextSize(25);
          Subtitle.setText(subtitle);
          Gist.getSettings().setJavaScriptEnabled(true);
          String script = "<html><body><script src=\"" + gist  + "\"></script><script>\n" +
                  "        document.querySelector(\".gist-meta\").style.display = \"none\";\n" +
                  "    </script></body></html>";
          Gist.loadData(script, "text/html", "UTF-8");
          mLayout.get().addView(Title);
          mLayout.get().addView(Subtitle);
          mLayout.get().addView(Gist);


        }

      }
    } catch (Exception e) {
      mLayout.get().removeAllViews();
      displayError();
      e.printStackTrace();
    }
  }
}

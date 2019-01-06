package be.mattwill.dev.projectzeppelin;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;

import java.lang.ref.WeakReference;

import static android.provider.Settings.System.getString;





public class GetLanguage extends AsyncTask<String, Void, String> {

  private WeakReference<LinearLayout> mLayout;
  private WeakReference<MainActivity> mActivity;
  private WeakReference<ProgressBar> mSpinner;

  private int amountFinsihed =0;
  private int totalToLoad = 1;

  // only draw our Webview if everything has loaded
  private class LoadingWebViewClient extends WebViewClient {
    private WeakReference<LinearLayout> mLayout;
    LoadingWebViewClient(WeakReference<LinearLayout> layout){
      mLayout = layout;
    }

    @Override
    public void onPageStarted(WebView webview, String url, Bitmap favicon) {
      mLayout.get().setVisibility(View.GONE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
      if(++amountFinsihed >= totalToLoad) {
        mSpinner.get().setVisibility(View.GONE);
        mLayout.get().setVisibility(View.VISIBLE);
      }
      super.onPageFinished(view, url);
    }

    // make our webview invisible if there was an error
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
      super.onReceivedError(view, request, error);
      mLayout.get().setVisibility(View.GONE);
    }

    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
      super.onReceivedHttpError(view, request, errorResponse);
      mLayout.get().setVisibility(View.GONE);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
      super.onReceivedSslError(view, handler, error);
      mLayout.get().setVisibility(View.GONE);
    }
  }


  GetLanguage(LinearLayout layout, MainActivity activity, ProgressBar spinner) {
    this.mActivity = new WeakReference<>(activity);
    this.mLayout = new WeakReference<>(layout);
    this.mSpinner = new WeakReference<>(spinner);
  }

  @Override
  protected String doInBackground(String... strings) {
    return GetRequest.get(strings[0]);
  }

  // if our get failed show an error
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
      Log.i("parsed", s);
      JSONArray languages = new JSONArray(s);
      Log.i("parsed", Integer.toString(languages.length()));

      mSpinner.get().setVisibility(View.VISIBLE);
      // generate a new code snippet for each part of our language
      for (int i = 0; i < languages.length(); i++) {
        String val = languages.getJSONObject(i).getString("data");

        JSONArray data = new JSONArray(val);
        result += " ";
        result += val;
        mLayout.get().removeAllViews();
        totalToLoad = data.length();
        for (int j = 0; j < data.length(); j++) {

          // get our string information
          String title = data.getJSONObject(j).getString("title");
          String subtitle = data.getJSONObject(j).getString("subtitle");
          String gist = data.getJSONObject(j).getString("gist");

          // create 3 new views
          TextView Title = new TextView(mActivity.get());
          TextView Subtitle = new TextView(mActivity.get());
          WebView Gist = new WebView(mActivity.get());
          Gist.setWebViewClient(new LoadingWebViewClient(mLayout));



          // alert the webview to use caching for extra speed
          Gist.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

          // make our title big
          Title.setText(title);
          Title.setTextSize(25);

          //make our subtitle
          Subtitle.setText(subtitle);
          Subtitle.setTextSize(15);


          // set some margins on our views
          setLayout(Title, 0,15,0,5);

          setLayout(Subtitle, 0,0,0,10);

          setLayout(Gist, 0,0,0,20);


          // enable use of javascript
          Gist.getSettings().setJavaScriptEnabled(true);
          Gist.setLayerType(View.LAYER_TYPE_HARDWARE, null);

          // load in the scripts from our server
          Gist.loadUrl(gist);

          // populate our linearlayout
          mLayout.get().addView(Title);
          mLayout.get().addView(Subtitle);
          mLayout.get().addView(Gist);


        }

      }
    } catch (Exception e) {
      mLayout.get().removeAllViews();
      displayError();
      e.printStackTrace();
      Log.i("parsed", e.getMessage());
    }
  }

  private void setLayout(View view, int left, int top, int right, int bottom){
      LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
      layout.setMargins(left,top,right,bottom);
      view.setLayoutParams(layout);
  }

}

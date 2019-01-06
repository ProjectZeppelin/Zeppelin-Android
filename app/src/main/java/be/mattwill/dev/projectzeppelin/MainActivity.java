package be.mattwill.dev.projectzeppelin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

  public static final String TAG = "MainActivity_mat";
  // the last url we used to list our application data
  private String m_url = "";
  // if our previous url is the same as the current then we don't have to load any data in
  private String m_prevurl = "";

  // loading bar when we are waiting for the webviews to load
  private ProgressBar spinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {


    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // make our drawer without any data
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    addMenuItemInNavMenuDrawer();

    if(savedInstanceState != null){
      String url = savedInstanceState.getString("url");
      Log.i(TAG, url);
      m_url = url;
      // only change the content when we have a valid url
      if(m_url != "")
        setContent(m_url);
    }

    spinner = (ProgressBar)findViewById(R.id.progressBar1);


  }

  // populate our navigation drawer based on a Get request
  private void addMenuItemInNavMenuDrawer() {
    NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

    Menu menu = navView.getMenu();
    Menu submenu = menu.addSubMenu("Cheat sheet");

    new GetMenu(menu).execute("http://" +  getString(R.string.IP) + ":" + getString(R.string.PORT) + "/menu");
    navView.invalidate();
  }

  // close our drawer if the user pressed the back button
  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }


  // generate our content based on the current language
  private void setContent(String url){
    Log.d(TAG, url);
    LinearLayout layout = findViewById(R.id.mainConstraint);

    new GetLanguage(layout, this, (ProgressBar) findViewById(R.id.progressBar1)).execute(m_url);
  }

  private String getlanguage(String language){
    // urls don't like special characters convert the nececery languages to a url friendly version
    if(language.equals("c#")) {
      return "csharp";
    }
    return language;
  }


  // we have pressed a item in our menu. Handle it and display the right content
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.

    String language = getlanguage(item.getTitle().toString().toLowerCase());

    m_url = "http://" +  getString(R.string.IP) + ":" + getString(R.string.PORT) + "/language/" + language;

    if(!m_prevurl.equals(m_url)) {
      setContent(m_url);
      m_prevurl = m_url;
    }


    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    savedInstanceState.putString("url", m_url);
  }
}

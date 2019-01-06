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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

  public static final String TAG = "MainActivity_mat";
  // the last url we used to list our application data
  private String m_url = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {


    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


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

  }

  private void addMenuItemInNavMenuDrawer() {
    NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

    Menu menu = navView.getMenu();
    Menu submenu = menu.addSubMenu("Cheat sheet");

    new GetMenu(menu).execute("http://" +  getString(R.string.IP) + ":" + getString(R.string.PORT) + "/menu");
    navView.invalidate();
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  private void setContent(String url){
    Log.d(TAG, url);
    LinearLayout layout = findViewById(R.id.mainConstraint);

    new GetLanguage(layout, this).execute(m_url);
  }


  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.

    m_url = "http://" +  getString(R.string.IP) + ":" + getString(R.string.PORT) +"/language/" + item.getTitle().toString().toLowerCase();

    setContent(m_url);


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

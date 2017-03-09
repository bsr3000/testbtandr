package ishchenko.a.testbt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

import ishchenko.a.testbt.Adapters.RecyclerClick;
import ishchenko.a.testbt.Adapters.UsersAdapter;
import ishchenko.a.testbt.SupportClasses.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private LinearLayoutManager layoutManager;
    private SharedPreferences sP;
    private API api;
    private User curUser;
    private TextView email;
    private TextView full_name;
    private static int ID;
    private ImageView myAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init_view();
    }

    private void init_view() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddActivity(0);
            }
        });
        sP = getSharedPreferences("prefs", 0);
        api = RestClient.getInstance(sP).getApiService();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.userlist);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        load_users();
        load_user();
    }

    private void load_users() {
        api.getUserList("id").enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                final UsersAdapter adapter = new UsersAdapter(response.body(), sP);
                recyclerView.setAdapter(adapter);
                recyclerView.addOnItemTouchListener(
                        new RecyclerClick(getApplicationContext(), new RecyclerClick.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
                                startAddActivity(adapter.getUserId(position));
                            }
                        })
                );
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void load_user() {
        String[] token = sP.getString("token", "default").split(" ");
        api.getCurrentUser(token[1]).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                curUser = response.body();
                TextView name = (TextView) navigationView.findViewById(R.id.name_my);
                TextView email = (TextView) navigationView.findViewById(R.id.email_my);
                name.setText(curUser.getFirstName() + " " + curUser.getLastName());
                email.setText(curUser.getEmail());
                myAvatar = (ImageView) navigationView.findViewById(R.id.myAvatar);
                if(curUser.getPhoto().length() > 0) {
                    DownloadImageTask dImage = new DownloadImageTask(myAvatar);
                    dImage.execute(RestClient.getInstance(sP).getBaseUrl() + curUser.getPhoto());
                }
                ID = curUser.getPk();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Snackbar.make(navigationView, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_my_profile) {
            startAddActivity(-1);
        } else if (id == R.id.nav_docs) {
            startDocsActivity();
        } else if (id == R.id.nav_group) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startDocsActivity() {
        Intent intent = new Intent(this, DocsActivity.class);
        if(curUser != null) {
            intent.putExtra("user", (Parcelable) curUser);
        }
        startActivity(intent);
    }

    public void startAboutActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void startAddActivity(int id) {
        Intent intent = new Intent(this, addUser.class);
        if(id==0) {
            intent.putExtra("user_id", 0);
        }
        else if (id!=-1) {
            intent.putExtra("user_id", id);
        } else {
            intent.putExtra("curUser", (Parcelable) curUser);
        }
        startActivity(intent);
        finish();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap img = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                img = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return img;
        }

        protected void onPostExecute(Bitmap result) {
            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, result.getWidth(), result.getHeight()), new RectF(0, 0, 150, 150), Matrix.ScaleToFit.CENTER);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), m, true);
            myAvatar.setImageBitmap(result);
        }
    }
}

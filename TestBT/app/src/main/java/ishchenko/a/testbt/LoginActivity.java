package ishchenko.a.testbt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

import ishchenko.a.testbt.SupportClasses.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText username_inp;
    private EditText password_inp;
    private EditText url_inp;
    private TextView errors;
    private Button login;
    private SharedPreferences sP;
    private API api = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init_view();
    }

    private void init_view() {
        username_inp = (EditText) findViewById(R.id.username);
        password_inp = (EditText) findViewById(R.id.password);
        url_inp = (EditText) findViewById(R.id.url);
        errors = (TextView) findViewById(R.id.login_error);
        login = (Button) findViewById(R.id.login_button);
        sP = getSharedPreferences("prefs", 0);
        checkSavedURL();
        url_inp.setOnFocusChangeListener(new urlFocusListener());
        login.setOnClickListener(new loginClickListener());
    }

    private boolean check_uname() {
        if(username_inp.getText().toString().length() > 0) {
            return true;
        }
        errors.setText(R.string.username_len_error);
        return false;
    }

    private boolean check_password() {
        if(password_inp.getText().toString().length() > 0) {
            return true;
        }
        errors.setText(R.string.password_len_error);
        return false;
    }

    public void checkSavedURL() {
        String url = sP.getString("url", "default");
        if (!url.equals("default")) {
            url_inp.setText(url);
        }
    }

    private class urlFocusListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
             if(!b && url_inp.getText().toString().length() > 0) {
                 boolean pinged = false;
                 CheckConnection check = new CheckConnection();
                 check.execute(url_inp.getText().toString());
             }
        }
    }

    public void startActivity(String token) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    private class loginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final View view1 = view;
            if(check_uname() && check_password()) {
                api = RestClient.getInstance(sP).getApiService();
                String username = username_inp.getText().toString();
                String pass = password_inp.getText().toString();
                Call<Token> tokenData = api.getToken(username, pass);
                tokenData.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        errors.setText("");
                        if (response.body() != null) {
                            String token = response.body().getToken();
                            sP.edit().putString("token", "token " + token).commit();
                            RestClient.getInstance(sP).rebuild();
                            api = RestClient.getInstance(sP).getApiService();
                            startActivity(token);
                        } else {
                            errors.setText("Username or password is incorrect");
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Snackbar.make(view1, t.getMessage(), Snackbar.LENGTH_LONG).show();
                        Log.d("MESSAGE", t.getMessage());
                    }
                });
            }
        }
    }
    class CheckConnection extends AsyncTask<String, Void, Boolean> {

        private Exception except;

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int code = connection.getResponseCode();
                return code == 404;
            } catch (Exception e) {
                this.except = e;
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(except != null) {
                errors.setText(except.getMessage());
            }
            else if(!result) {
                errors.setText("Host is unreachable");
            }
            else {
                errors.setText("Host is online");
                SharedPreferences.Editor editor = sP.edit();
                editor.putString("url", url_inp.getText().toString()).apply();
            }
        }
    }
}

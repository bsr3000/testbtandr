package ishchenko.a.testbt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import ishchenko.a.testbt.SupportClasses.Group;
import ishchenko.a.testbt.SupportClasses.Position;
import ishchenko.a.testbt.SupportClasses.User;
import ishchenko.a.testbt.Validators.InputValidator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addUser extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText fname;
    private EditText lname;
    private EditText salary;
    private Spinner positionDrop;
    private Spinner groupDrop;
    private SharedPreferences sP;
    private ConstraintLayout parent;
    private TextView add_label;
    private FloatingActionButton fab;
    private User thisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        init_view();
    }

    private void init_view() {
        sP = getSharedPreferences("prefs", 0);
        add_label = (TextView) findViewById(R.id.add_label);
        parent = (ConstraintLayout) findViewById(R.id.add_parent);
        username = (EditText) findViewById(R.id.username_add);
        email = (EditText) findViewById(R.id.email_add);
        fname = (EditText) findViewById(R.id.firstname_add);
        lname = (EditText) findViewById(R.id.lastname_add);
        salary = (EditText) findViewById(R.id.salary_add);
        positionDrop = (Spinner) findViewById(R.id.position_drop);
        groupDrop = (Spinner) findViewById(R.id.group_drop);
        fab = (FloatingActionButton) findViewById(R.id.fab_add);
        checkParcel();
        setValidators();
    }

    private void checkParcel() {
        Intent passedData = getIntent();
        if (passedData.getParcelableExtra("curUser") != null) {
            setFields((User) passedData.getParcelableExtra("curUser"));
            fab.setOnClickListener(new AddUser(true));
            add_label.setText("My Profile");
        } else if (passedData.getIntExtra("user_id", 0) != 0) {
            getUser(passedData.getIntExtra("user_id", 0));
            fab.setOnClickListener(new AddUser(true));
        } else {
            fab.setOnClickListener(new AddUser(false));
            getGrAndPos(null);
        }
    }

    private void setValidators() {
        username.addTextChangedListener(new InputValidator(username) {
            @Override
            public boolean validate(EditText editText, String text) {
                if (text.length() > 0) {
                    return true;
                } else {
                    editText.setError("This field can not be blank");
                    return false;
                }
            }
        });
        email.addTextChangedListener(new InputValidator(email) {
            @Override
            public boolean validate(EditText editText, String text) {
                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(text);
                if (text.length() > 0 && matcher.find()) {
                    return true;
                } else {
                    editText.setError("Email has errors");
                    return false;
                }
            }
        });
    }

    private void getUser(int pk) {
        RestClient.getInstance(sP).getApiService().getUser(pk).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    thisUser = response.body();
                    setFields(thisUser);
                    getGrAndPos(thisUser);
                } else {
                    returnToMain();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    protected void updateUser() {
        if (thisUser != null) {
            thisUser.setEmail(email.getText().toString());
            thisUser.setUsername(username.getText().toString());
            thisUser.setFirstName(fname.getText().toString());
            thisUser.setLastName(lname.getText().toString());
            thisUser.setSalary(Integer.parseInt(salary.getText().toString()));
            thisUser.setGroup(new Group(0, groupDrop.getSelectedItem().toString()));
            thisUser.setPosition(new Position(0, positionDrop.getSelectedItem().toString()));
        } else {
            thisUser = new User(0, username.getText().toString(), email.getText().toString(),
                    fname.getText().toString(), lname.getText().toString(), "",
                    new Position(0, positionDrop.getSelectedItem().toString()),
                    Integer.parseInt(salary.getText().toString()),
                    new Group(0, groupDrop.getSelectedItem().toString()));
        }
    }


    private void setFields(User user) {
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        fname.setText(user.getFirstName());
        lname.setText(user.getLastName());
        salary.setText(user.getSalary().toString());
        thisUser = user;
        getGrAndPos(thisUser);
    }

    @Override
    public void onBackPressed() {
        returnToMain();
    }

    private void getGrAndPos(@Nullable final User userPassed) {
        final ArrayList<String> arrayListPositions = new ArrayList<>();
        final ArrayList<String> arrayListGroups = new ArrayList<>();
        RestClient.getInstance(sP).getApiService().getPositions().enqueue(new Callback<List<Position>>() {
            @Override
            public void onResponse(Call<List<Position>> call, Response<List<Position>> response) {
                int code = response.code();
                if (code == 200) {
                    for (int i = 0; i < response.body().size(); i++) {
                        arrayListPositions.add(response.body().get(i).getPosName());
                        if (userPassed != null && response.body().get(i).getPosName().equals(userPassed.getPosition().getPosName())) {
                            positionDrop.setSelection(arrayListPositions.indexOf(response.body().get(i).getPosName()));
                        }
                    }
                    ArrayAdapter<String> posAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayListPositions);
                    positionDrop.setAdapter(posAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Position>> call, Throwable t) {
                Snackbar.make(parent, "No position Data", Snackbar.LENGTH_LONG).show();
            }
        });
        RestClient.getInstance(sP).getApiService().getGroupList().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                int code = response.code();
                if (code == 200) {
                    for (int i = 0; i < response.body().size(); i++) {
                        arrayListGroups.add(response.body().get(i).getGrName());
                        if (userPassed != null && response.body().get(i).getGrName().equals(userPassed.getPosition().getPosName())) {
                            groupDrop.setSelection(arrayListGroups.indexOf(response.body().get(i).getGrName()));
                        }
                    }
                    ArrayAdapter<String> grAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayListGroups);
                    groupDrop.setAdapter(grAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                Snackbar.make(parent, "No group Data", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    protected void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    class AddUser implements View.OnClickListener {

        private Boolean update;

        public AddUser(boolean update) {
            this.update = update;
        }

        @Override
        public void onClick(final View view) {
            updateUser();
            if (!update) {
                RestClient.getInstance(sP).getApiService().createUser(thisUser).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 201) {
                            returnToMain();
                        } else {
                            Snackbar.make(view, response.message(), Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
            } else {
                RestClient.getInstance(sP).getApiService().updateUser(thisUser.getPk(), thisUser).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            returnToMain();
                        } else {
                            Snackbar.make(view, response.message(), Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}

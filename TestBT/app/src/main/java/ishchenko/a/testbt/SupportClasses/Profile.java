package ishchenko.a.testbt.SupportClasses;

import android.content.SharedPreferences;

import ishchenko.a.testbt.RestClient;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by andrey on 08.03.17.
 */

public class Profile extends User {

    private static Profile instance = null;
    private SharedPreferences sp;
    private static User user;

    public static Profile getInstance(SharedPreferences sp) {
        if (instance == null) {
            instance = new Profile(sp);
        }
        return instance;
    }

    private Profile(SharedPreferences sp) {
        this.sp = sp;
        getUser();
    }

    private User getUser() {
        String[] token = sp.getString("token", "default").split(" ");
        RestClient.getInstance(sp).getApiService().getCurrentUser(token[1]).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                user = response.body();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        return user;
    }

    private void buildClient() {

    }

}

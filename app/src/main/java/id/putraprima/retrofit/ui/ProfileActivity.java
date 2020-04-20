package id.putraprima.retrofit.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.UserInfo;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    public static final String TOKEN_LOGIN = "login_token";
    TextView tvName, tvEmail;
    SharedPreferences preferences;
    String name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        getMe();
    }

    private void getMe() {
        ApiInterface services = ServiceGenerator.createService(ApiInterface.class);
        Call<Envelope<UserInfo>> call = services.me("bearer"+" "+preferences.getString(TOKEN_LOGIN, null));
        call.enqueue(new Callback<Envelope<UserInfo>>() {
            @Override
            public void onResponse(Call<Envelope<UserInfo>> call, Response<Envelope<UserInfo>> response) {
                name = response.body().getData().getName();
                email = response.body().getData().getEmail();
                tvName.setText(name);
                tvEmail.setText(email);
            }

            @Override
            public void onFailure(Call<Envelope<UserInfo>> call, Throwable t) {
                tvName.setText("Check your connection");
                tvEmail.setText("We can't load your information");
            }
        });
    }

    public void doEditProfile(View view) {
        Intent i = new Intent(this, UpdateProfile.class);
        i.putExtra("name", name);
        i.putExtra("email", email);
        startActivityForResult(i, 1);
    }

    public void doEditPassword(View view) {
        Intent i = new Intent(this, UpdatePassword.class);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){//request update profilel
                getMe();
            }
        }
    }

    public void Recipe(View view) {
        Intent i = new Intent(this, RecipeActivity.class);
        startActivity(i);
    }

    public void AddRecipe(View view) {
        Intent i = new Intent(this, UploadRecipe.class);
        startActivity(i);
    }
}

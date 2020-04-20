package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.putraprima.retrofit.ui.SplashActivity.KEY_APP_NAME;
import static id.putraprima.retrofit.ui.SplashActivity.KEY_APP_VERSION;

public class MainActivity extends AppCompatActivity {
    public static final String TOKEN_LOGIN = "login_token";
    TextView txtApp, txtVersion;
    EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtApp = findViewById(R.id.mainTxtAppName);
        txtVersion = findViewById(R.id.mainTxtAppVersion);
        etEmail = findViewById(R.id.edtEmail);
        etPassword = findViewById(R.id.edtPassword);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        txtApp.setText(preferences.getString(KEY_APP_NAME, null));
        txtVersion.setText(preferences.getString(KEY_APP_VERSION, null));
    }

    private void login() {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        LoginRequest loginRequest = new LoginRequest(etEmail.getText().toString().trim(),
                etPassword.getText().toString().trim());
        Call<LoginResponse> call = service.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putString(TOKEN_LOGIN,response.body().getToken());
                    editor.apply();
                    Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(i);
                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    if (error.getError().getEmail() != null){
                        showError(error.getError().getEmail().get(0));
                    }
                    if (error.getError().getPassword() != null){
                        showError(error.getError().getPassword().get(0));
                    }
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Koneksi Ke Server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login(View view) {
        login();
    }

    public void Register(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivityForResult(i, 1);
    }

    public void showError(String message){
        AlertDialog optionDialog = new AlertDialog.Builder(this).create();
        optionDialog.setTitle("Login Error");
        optionDialog.setMessage(message);
        optionDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Okay",
                (DialogInterface.OnClickListener) null);
        optionDialog.show();
    }

}

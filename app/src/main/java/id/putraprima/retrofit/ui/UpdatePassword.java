package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.UpdatePasswordRequest;
import id.putraprima.retrofit.api.models.UpdateRequest;
import id.putraprima.retrofit.api.models.UpdateResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePassword extends AppCompatActivity {
    private static final String TOKEN_LOGIN = "login_token";
    EditText etPass, etConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        etPass = findViewById(R.id.et_pass);
        etConfirm = findViewById(R.id.et_confirm);
    }

    public void DoUpdate(View view) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class,
                "bearer " + preference.getString(TOKEN_LOGIN, null));
        UpdatePasswordRequest updateRequest = new UpdatePasswordRequest(etPass.getText().toString().trim(),
                etConfirm.getText().toString().trim());
        Call<Envelope<UpdateResponse>> call = service.updatePassword(updateRequest);
        call.enqueue(new Callback<Envelope<UpdateResponse>>() {
            @Override
            public void onResponse(Call<Envelope<UpdateResponse>> call, Response<Envelope<UpdateResponse>> response) {
                if(response.isSuccessful()){
                    Toast.makeText(UpdatePassword.this, "Update Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent();
                    setResult(RESULT_OK, i);
                    finish();
                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    if(error.getError().getPassword() != null){
                        etPass.setError(error.getError().getPassword().get(0));
                        etConfirm.setError(error.getError().getPassword().get(0));
                    }
                }
            }
            @Override
            public void onFailure(Call<Envelope<UpdateResponse>> call, Throwable t) {
                Toast.makeText(UpdatePassword.this, "Gagal Koneksi Ke Server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

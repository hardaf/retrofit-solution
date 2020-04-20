package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText etName, etEmail, etPass, etConfirmation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPass  = findViewById(R.id.et_pass);
        etConfirmation = findViewById(R.id.et_confirm);
    }

    public void SubmitRegister(View view) {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        RegisterRequest request = new RegisterRequest(etName.getText().toString().trim(),
                etEmail.getText().toString().trim(),
                etPass.getText().toString().trim(),
                etConfirmation.getText().toString().trim());
        Call<Envelope<RegisterResponse>> call = service.register(request);
        call.enqueue(new Callback<Envelope<RegisterResponse>>() {
            @Override
            public void onResponse(Call<Envelope<RegisterResponse>> call, Response<Envelope<RegisterResponse>> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent();
                    setResult(RESULT_OK, i);
                }else{
                    ApiError error = ErrorUtils.parseError(response);
                    if (error.getError().getEmail() != null){
                        etEmail.setError(error.getError().getEmail().get(0));
                    }
                    if (error.getError().getPassword() != null){
                        etPass.setError(error.getError().getPassword().get(0));
                    }
                    if (error.getError().getName() != null){
                        etName.setError(error.getError().getName().get(0));
                    }
                }
            }

            @Override
            public void onFailure(Call<Envelope<RegisterResponse>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Gagal Koneksi Ke Server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

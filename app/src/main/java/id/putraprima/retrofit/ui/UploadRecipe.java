package id.putraprima.retrofit.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.livinglifetechway.quickpermissions.annotations.WithPermissions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.ApiError;
import id.putraprima.retrofit.api.models.ErrorUtils;
import id.putraprima.retrofit.api.services.ApiInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadRecipe extends AppCompatActivity {
    Uri imageUri;
    Bitmap bitmap;
    ImageView ivPreview;

    EditText etResep, etDeskripsi, etBahan, etCara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);
        ivPreview = findViewById(R.id.imgPreview);
        etResep = findViewById(R.id.etResep);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        etBahan = findViewById(R.id.etBahan);
        etCara = findViewById(R.id.etCara);
    }

    public void SubmitResep(View view) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String namaResep = etResep.getText().toString();
        String deskripsi = etDeskripsi.getText().toString();
        String bahan = etBahan.getText().toString();
        String cara = etCara.getText().toString();

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("fk_user", createPartFromString("1"));
        map.put("nama_resep", createPartFromString(namaResep));
        map.put("deskripsi", createPartFromString(deskripsi));
        map.put("bahan", createPartFromString(bahan));
        map.put("langkah_pembuatan", createPartFromString(cara));
        map.put("token", createPartFromString(preference.getString("login_token", null)));

        File file = createTempFile(bitmap);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);

        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<ResponseBody> call = service.doUpload(body, map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UploadRecipe.this, "Upload sukses", Toast.LENGTH_SHORT).show();
                } else {
                    ApiError error = ErrorUtils.parseError(response);
                    if(error.getError().getResep() != null){
                        etResep.setError(error.getError().getResep().get(0));
                    } else if (error.getError().getDeskripsi() != null) {
                        etDeskripsi.setError(error.getError().getDeskripsi().get(0));
                    } else if (error.getError().getBahan() != null) {
                        etBahan.setError(error.getError().getBahan().get(0));
                    } else if (error.getError().getCara() != null) {
                        etCara.setError(error.getError().getCara().get(0));
                    } else if (error.getError().getFoto() != null) {
                        Toast.makeText(UploadRecipe.this, "Gambar gagal diupload",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadRecipe.this, "Gagal Koneksi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //setting untuk mengisi hash
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    //setting untuk membuat gambar menjadi tersimpan secara temporary
    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() + "_image.png");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public void uploadCamera(View view) {
        openCamera();
    }

    public void uploadFiles(View view) {
        openFiles();
    }

    //melihat permission kamera
    @WithPermissions(
            permissions = {Manifest.permission.CAMERA},
            rationaleMessage = "We need to access your camera",
            permanentlyDeniedMessage = "You can't take picture from camera, grant the access first"
    )public void openCamera() {
        pickImage(1);
    }

    //melihat permission files
    @WithPermissions(
            permissions = {Manifest.permission.READ_EXTERNAL_STORAGE},
            rationaleMessage = "We need to access your file manager",
            permanentlyDeniedMessage = "You can't upload files from storage, grant the access first"
    )public void openFiles() {
        pickImage(2);
    }

    //decide to choose picking image or capture by camera
    private void pickImage(int info) {
        if(info == 1){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);
        }else if(info == 2){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }
    }

    //start for activity after picking
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            return;
        }
        if(requestCode == 1){
            if (data != null) {
                imageUri = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ivPreview.setImageBitmap(bitmap);
            }
        }else if(requestCode == 2){
            if (data != null) {
                imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivPreview.setImageBitmap(bitmap);
            }
        }
    }
}

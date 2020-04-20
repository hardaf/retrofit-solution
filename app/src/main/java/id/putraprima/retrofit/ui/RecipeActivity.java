package id.putraprima.retrofit.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.adapter.RecipeAdapter;
import id.putraprima.retrofit.api.helper.ServiceGenerator;
import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.Recipe;
import id.putraprima.retrofit.api.services.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity {
    ArrayList<Recipe> recipe = new ArrayList<>();
    RecyclerView rvRecipe;
    RecipeAdapter recipeAdapter;

    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        rvRecipe = findViewById(R.id.rv_recipe);

        RecyclerView recipeView = findViewById(R.id.rv_recipe);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recipeView.setLayoutManager(layoutManager);

        recipeAdapter = new RecipeAdapter(this, recipe);
        recipeView.setAdapter(recipeAdapter);
        page = 1;
        loadData();
    }

    public void LoadMore(View view) {
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Envelope<List<Recipe>>> call = service.LoadMoreRecipe(page);
        call.enqueue(new Callback<Envelope<List<Recipe>>>() {
            @Override
            public void onResponse(Call<Envelope<List<Recipe>>> call, Response<Envelope<List<Recipe>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getData().size() != 0){
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            int id = response.body().getData().get(i).getId();
                            String namaResep = response.body().getData().get(i).getNamaResep();
                            String deskripsi = response.body().getData().get(i).getDeskripsi();
                            String bahan = response.body().getData().get(i).getBahan();
                            String langkahPembuatan = response.body().getData().get(i).getLangkahPembuatan();
                            String foto = response.body().getData().get(i).getFoto();
                            recipe.add(new Recipe(id, namaResep, deskripsi, bahan, langkahPembuatan, foto));
                            recipeAdapter.notifyDataSetChanged();
                        }
                        //ganti page selanjutnya
                        page++;
                    }else{
                        showError("Data tidak dapat di load");
                    }
                }else{
                    showError("Periksa kembali koneksi anda!");
                }
            }

            @Override
            public void onFailure(Call<Envelope<List<Recipe>>> call, Throwable t) {
                showError("Koneksi ke Rest Error");
            }
        });
    }

    public void loadData(){
        ApiInterface service = ServiceGenerator.createService(ApiInterface.class);
        Call<Envelope<List<Recipe>>> call = service.LoadRecipe();
        call.enqueue(new Callback<Envelope<List<Recipe>>>() {
            @Override
            public void onResponse(Call<Envelope<List<Recipe>>> call, Response<Envelope<List<Recipe>>> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        int id = response.body().getData().get(i).getId();
                        String namaResep = response.body().getData().get(i).getNamaResep();
                        String deskripsi = response.body().getData().get(i).getDeskripsi();
                        String bahan = response.body().getData().get(i).getBahan();
                        String langkahPembuatan = response.body().getData().get(i).getLangkahPembuatan();
                        String foto = response.body().getData().get(i).getFoto();
                        recipe.add(new Recipe(id, namaResep, deskripsi, bahan, langkahPembuatan, foto));
                        recipeAdapter.notifyDataSetChanged();
                    }
                    page++;
                }else{
                    showError("Data Error!");
                }
            }

            @Override
            public void onFailure(Call<Envelope<List<Recipe>>> call, Throwable t) {
                showError("Koneksi ke Rest Error");
            }
        });
    }

    public void RefreshData(View view) {
        recipe.clear();
        loadData();
    }

    public void showError(String message){
        AlertDialog optionDialog = new AlertDialog.Builder(this).create();
        optionDialog.setTitle("Recycler View Error");
        optionDialog.setMessage(message);
        optionDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Okay",
                (DialogInterface.OnClickListener) null);
        optionDialog.show();
    }
}

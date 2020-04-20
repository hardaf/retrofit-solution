package id.putraprima.retrofit.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.putraprima.retrofit.R;
import id.putraprima.retrofit.api.models.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private Context context;
    private List<Recipe> items;

    public RecipeAdapter(Context context, List<Recipe> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = items.get(position);
        Picasso.get().load("https://mobile.putraprima.id/uploads/"+recipe.getFoto()).into(holder.foto); // internet path
        holder.id.setText(String.valueOf(recipe.getId()));
        holder.nama.setText(recipe.getNamaResep());
        holder.deskripsi.setText(recipe.getDeskripsi());
        holder.bahan.setText(recipe.getBahan());
        holder.langkah.setText(recipe.getLangkahPembuatan());
    }

    @Override
    public int getItemCount() { return (items != null) ? items.size() : 0; }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, nama, deskripsi, bahan, langkah;
        ImageView foto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tv_id);
            nama = itemView.findViewById(R.id.tv_resep);
            deskripsi = itemView.findViewById(R.id.tv_deskripsi);
            bahan = itemView.findViewById(R.id.tv_bahan);
            langkah = itemView.findViewById(R.id.tv_langkah);
            foto = itemView.findViewById(R.id.iv_foto);
        }
    }
}

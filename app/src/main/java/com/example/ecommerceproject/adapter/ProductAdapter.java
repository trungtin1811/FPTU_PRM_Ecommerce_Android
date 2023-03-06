package com.example.ecommerceproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceproject.R;
import com.example.ecommerceproject.dto.ProductModel;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mContext;
    private List<ProductModel> products;
    private OnItemClickListener mListener;


    public ProductAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ProductModel> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {


        ProductModel product = products.get(position);
        if (product == null) {
            return;
        }
        Glide.with(mContext).load(product.getImageUrl()).into(holder.imgImage);
        holder.txtName.setText(product.getName());
        holder.txtPrice.setText(String.format("%s $", product.getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.onClick(view);
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(ProductModel product);
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgImage;
        private TextView txtName;
        private TextView txtPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imgImage = itemView.findViewById(R.id.imgProductImg);
            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ProductModel data = products.get(getAdapterPosition());
            mListener.onItemClick(data);
        }
    }
}

package com.example.ecommerceproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceproject.R;
import com.example.ecommerceproject.database.AppDatabase;
import com.example.ecommerceproject.entities.OrderItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context mContext;
    private List<OrderItem> cartItems;
    private CartAdapter.OnItemClickListener mListener;
    private CartAdapter.UpdateFragmentListener updateFragmentListener;


    public CartAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<OrderItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    public void setListener(CartAdapter.OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setUpdateFragmentListener(UpdateFragmentListener updateFragmentListener) {
        this.updateFragmentListener = updateFragmentListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {


        OrderItem cartItem = cartItems.get(position);
        if (cartItem == null) {
            return;
        }

        Glide.with(mContext).load(AppDatabase.getInstance(mContext).productDao().getById(cartItem.getProductId()).getImageUrl()).into(holder.imgImage);
        holder.txtName.setText(AppDatabase.getInstance(mContext).productDao().getById(cartItem.getProductId()).getName());
        holder.txtPrice.setText(String.format("%s $", AppDatabase.getInstance(mContext).productDao().getById(cartItem.getProductId()).getPrice()));
        holder.txtQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartItem.getQuantity() - 1 <= 0) {
                    cartItems.remove(holder.getAdapterPosition());
                    AppDatabase.getInstance(mContext).orderItemDao().delete(cartItem);
                    notifyItemRemoved(holder.getAdapterPosition());
                    holder.updateCart();
                } else {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    AppDatabase.getInstance(mContext).orderItemDao().update(cartItem);
                    notifyDataSetChanged();
                    holder.updateCart();
                }
            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                AppDatabase.getInstance(mContext).orderItemDao().upsert(cartItem);
                notifyDataSetChanged();
                holder.updateCart();
            }
        });

        holder.btnCartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItems.remove(holder.getAdapterPosition());
                AppDatabase.getInstance(mContext).orderItemDao().delete(cartItem);
                notifyItemRemoved(holder.getAdapterPosition());
                holder.updateCart();
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.onClick(view);
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(OrderItem cartItem);
    }

    public interface UpdateFragmentListener {
        void updateCart();
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgImage;
        private TextView txtName;
        private TextView txtQuantity;
        private TextView txtPrice;
        private Button btnMinus;
        private Button btnPlus;
        private ImageView btnCartDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            imgImage = itemView.findViewById(R.id.imgCardProductImg);
            txtName = itemView.findViewById(R.id.txtCardProductName);
            txtQuantity = itemView.findViewById(R.id.txtCardProductQuantity);
            txtPrice = itemView.findViewById(R.id.txtCardProductPrice);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnCartDelete = itemView.findViewById(R.id.btnCartDelete);
        }

        @Override
        public void onClick(View view) {
            OrderItem cartItem = cartItems.get(getAdapterPosition());
            mListener.onItemClick(cartItem);
        }

        public void updateCart() {
            updateFragmentListener.updateCart();
        }
    }
}

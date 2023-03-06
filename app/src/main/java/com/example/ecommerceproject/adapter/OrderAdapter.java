package com.example.ecommerceproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.database.AppDatabase;
import com.example.ecommerceproject.entities.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.CartViewHolder> {

    private Context mContext;
    private List<Order> orderItems;


    public OrderAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Order> orderItems) {
        this.orderItems = orderItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {


        Order order = orderItems.get(position);
        if (order == null) {
            return;
        }
        holder.txtOrderTimeStamp.setText(order.getDateTime().toString());
        holder.txtOrderNumOfProducts.setText(String.format("%s items", AppDatabase.getInstance(mContext).orderDao().countItems(order.getId())));
        holder.txtOrderPrice.setText(String.format("%s $", AppDatabase.getInstance(mContext).orderDao().sumPriceOrder(order.getId())));

    }

    @Override
    public int getItemCount() {
        return orderItems != null ? orderItems.size() : 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView txtOrderTimeStamp;
        private TextView txtOrderNumOfProducts;
        private TextView txtOrderPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderTimeStamp = itemView.findViewById(R.id.txtOrderTimeStamp);
            txtOrderNumOfProducts = itemView.findViewById(R.id.txtOrderNumOfProducts);
            txtOrderPrice = itemView.findViewById(R.id.txtOrderPrice);
        }
    }
}

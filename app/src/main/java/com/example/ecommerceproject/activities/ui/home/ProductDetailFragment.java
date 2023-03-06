package com.example.ecommerceproject.activities.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ecommerceproject.R;
import com.example.ecommerceproject.database.AppDatabase;
import com.example.ecommerceproject.dto.AccountModel;
import com.example.ecommerceproject.dto.ProductModel;
import com.example.ecommerceproject.entities.Account;
import com.example.ecommerceproject.entities.Order;
import com.example.ecommerceproject.entities.OrderItem;
import com.example.ecommerceproject.enums.OrderType;
import com.example.ecommerceproject.util.CommonUtils;

public class ProductDetailFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detail, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String id = getActivity().getIntent().getExtras().getString("currentUserId");
        Account account = AppDatabase.getInstance(getContext()).accountDao().getById(id);
        if (account == null) {
            return;
        }
        ProductModel product = (ProductModel) getArguments().get("productDetail");
        if (product == null) {
            return;
        }
        TextView name = view.findViewById(R.id.txtProductDetailName);
        TextView desc = view.findViewById(R.id.txtProductDetailDesc);
        TextView price = view.findViewById(R.id.txtProductDetailPrice);
        ImageView image = view.findViewById(R.id.imgProductDetailImg);

        name.setText(product.getName());
        desc.setText(product.getDescription());
        price.setText(String.format("%s $", product.getPrice().toString()));
        Glide.with(getContext()).load(product.getImageUrl()).into(image);

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        ((Button) view.findViewById(R.id.btnAddToCart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Order cart = AppDatabase.getInstance(getContext()).orderDao().getCart(account.getId());
                    if (cart == null) {
                        cart = new Order().setAccountId(account.id).setType(OrderType.CART);
                        AppDatabase.getInstance(getContext()).orderDao().insert(cart);
                        cart = AppDatabase.getInstance(getContext()).orderDao().getCart(account.getId());
                    }
                    OrderItem orderItem = AppDatabase.getInstance(getContext()).orderItemDao().getItemCart(cart.getId(), product.getId());
                    if (orderItem == null) {
                        AppDatabase.getInstance(getContext()).orderItemDao().insert(new OrderItem().setProductId(product.getId()).setOrderId(cart.getId()).setQuantity(1));
                    } else {
                        int quantity = orderItem.getQuantity() + 1;
                        AppDatabase.getInstance(getContext()).orderItemDao().upsert(new OrderItem().setId(orderItem.getId()).setProductId(product.getId()).setOrderId(cart.getId()).setQuantity(quantity));
                    }
                    Toast.makeText(getContext(), "Add success", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
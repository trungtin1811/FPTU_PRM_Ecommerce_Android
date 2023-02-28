package com.example.ecommerceproject.activities.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.adapter.ProductAdapter;
import com.example.ecommerceproject.database.AppDatabase;
import com.example.ecommerceproject.databinding.FragmentHomeBinding;
import com.example.ecommerceproject.dto.ProductModel;
import com.example.ecommerceproject.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView rvProduct;
    private ProductAdapter productAdapter;
    private AppDatabase db;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = AppDatabase.getInstance(getContext());

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        rvProduct = view.findViewById(R.id.rvProduct);
        productAdapter = new ProductAdapter(getContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        rvProduct.setLayoutManager(gridLayoutManager);
        productAdapter.setData(getListProduct());
        rvProduct.setAdapter(productAdapter);

        super.onViewCreated(view, savedInstanceState);
    }

    private List<ProductModel> getListProduct() {
        List<ProductModel> productModels = new ArrayList<>();
        List<Product> products = db.productDao().getAll();
        if (products != null && products.size() > 0) {
            for (Product product : products) {
                productModels.add(new ProductModel(product));
            }
        }
        return productModels;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
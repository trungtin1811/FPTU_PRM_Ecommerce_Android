package com.example.ecommerceproject.activities.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

public class HomeFragment extends Fragment implements ProductAdapter.OnItemClickListener {

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
        super.onViewCreated(view, savedInstanceState);
        // init product
        initProduct();
        rvProduct = view.findViewById(R.id.rvProduct);
        productAdapter = new ProductAdapter(getContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        rvProduct.setLayoutManager(gridLayoutManager);
        productAdapter.setData(getListProduct());
        productAdapter.setListener(this);
        rvProduct.setAdapter(productAdapter);


        EditText searchKey = view.findViewById(R.id.txtSearchKey);
        searchKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productAdapter.setData(getListProductSearch(charSequence.toString()));
                rvProduct.setAdapter(productAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

    private List<ProductModel> getListProductSearch(String searchKey) {
        List<ProductModel> productModels = new ArrayList<>();
        List<Product> products = db.productDao().getAll("%" + searchKey + "%");
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

    @Override
    public void onItemClick(ProductModel product) {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("productDetail", product);
        productDetailFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_dashboard, productDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void initProduct() {
        if (AppDatabase.getInstance(getContext()).productDao().getAll().size() > 0) {
            return;
        }
        AppDatabase.getInstance(getContext()).productDao().insertAll(
                new Product()
                        .setName("Apple Watch")
                        .setDescription("Available when you purchase any new iPhone, iPad, iPod Touch, Mac or Apple TV, £4.99/month after free trial.")
                        .setPrice(1000.0)
                        .setQuantity(10L)
                        .setImageUrl("https://i0.wp.com/danzis.org/wp-content/uploads/2020/04/black-apple-watch.png?ssl=1"),
                new Product()
                        .setName("IPhone 14")
                        .setDescription("iPhone 14 Pro. Capture impressive details with the 48MP Main Camera. Experience iPhone in a whole new way with Dynamic Island and the Always On display. Collision Detection, a new safety feature, calls for help when needed.")
                        .setPrice(2000.0)
                        .setQuantity(20L)
                        .setImageUrl("https://media.croma.com/image/upload/v1662703724/Croma%20Assets/Communication/Mobiles/Images/261934_qgssvy.png"),
                new Product()
                        .setName("Macbook M2")
                        .setDescription("M2 is the next generation of Apple silicon. Its 8-core CPU lets you zip through everyday tasks like creating documents and presentations, or take on more intensive workflows like developing in Xcode or mixing tracks in Logic Pro.")
                        .setPrice(1000.0)
                        .setQuantity(10L)
                        .setImageUrl("https://vuatao.vn/wp-content/uploads/2021/10/macbook-pro-16-m1-pro-2021-xam-650x650-2.png"),
                new Product()
                        .setName("Air Pod 2")
                        .setDescription("AirPods 2 Lightning Charge Apple MV7N2 Bluetooth headset - dubbed a national legendary AirPods that is very popular with apple fans.")
                        .setPrice(3000.0)
                        .setQuantity(10L)
                        .setImageUrl("https://assets.stickpng.com/images/60b79e8771a1fd000411f6be.png"),
                new Product()
                        .setName("Air Tag")
                        .setDescription("Airtag is a small device with integrated Bluetooth technology used to find lost objects and equipment. Although there are many similar products, Apple's smart home accessories promise to integrate technology more deeply, allowing users to experience even more wonderful activities of the device.")
                        .setPrice(1000.0)
                        .setQuantity(10L)
                        .setImageUrl("https://www.uwalumnistore.com/storeimages/294-1693423-3_hi.png")
        );
    }
}
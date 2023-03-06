package com.example.ecommerceproject.activities.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.activities.ui.home.ProductDetailFragment;
import com.example.ecommerceproject.adapter.CartAdapter;
import com.example.ecommerceproject.database.AppDatabase;
import com.example.ecommerceproject.databinding.FragmentCartBinding;
import com.example.ecommerceproject.dto.ProductModel;
import com.example.ecommerceproject.entities.Account;
import com.example.ecommerceproject.entities.OrderItem;
import com.example.ecommerceproject.entities.Product;

import java.util.List;
import java.util.Objects;


public class CartFragment extends Fragment implements CartAdapter.OnItemClickListener, CartAdapter.UpdateFragmentListener {

    private FragmentCartBinding binding;
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCart = view.findViewById(R.id.rvCart);
        cartAdapter = new CartAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvCart.setLayoutManager(linearLayoutManager);
        cartAdapter.setListener(this);
        cartAdapter.setUpdateFragmentListener(this);

        String id = getActivity().getIntent().getExtras().getString("currentUserId");
        Account account = AppDatabase.getInstance(getContext()).accountDao().getById(id);
        if (account == null) {
            return;
        }
        List<OrderItem> cartList = getListCart(account.getId());
        if (cartList == null || cartList.size() == 0) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_dashboard, new EmptyCartFragment())
                    .addToBackStack(null)
                    .commit();
        }
        cartAdapter.setData(cartList);
        rvCart.setAdapter(cartAdapter);
        ((TextView) view.findViewById(R.id.txtCartTotalPrice)).setText(String.format("%s $", Objects.requireNonNullElse(AppDatabase.getInstance(getContext()).orderDao().sumPrice(account.getId()), "")));

        ((Button) view.findViewById(R.id.btnCheckOut)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Order cart = AppDatabase.getInstance(getContext()).orderDao().getCart(account.getId());
//                cart.setType(OrderType.ORDER);
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//                cart.setDateTime(LocalDateTime.now().format(formatter));
//                AppDatabase.getInstance(getContext()).orderDao().update(cart);
//                Toast.makeText(getActivity(), "Checkout success", Toast.LENGTH_LONG).show();
//
//                getParentFragmentManager().beginTransaction()
//                        .replace(R.id.nav_host_fragment_activity_dashboard, new HomeFragment())
//                        .addToBackStack(null)
//                        .commit();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_dashboard, new ConfirmCartFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<OrderItem> getListCart(String accountId) {
        return AppDatabase.getInstance(getContext()).orderItemDao().getAllCartItems(accountId);
    }


    @Override
    public void onItemClick(OrderItem cartItem) {
        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        Product product = AppDatabase.getInstance(getContext()).productDao().getById(cartItem.getProductId());
        if (product != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("productDetail", new ProductModel(product));
            productDetailFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_dashboard, productDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void updateCart() {
        String id = getActivity().getIntent().getExtras().getString("currentUserId");
        Account account = AppDatabase.getInstance(getContext()).accountDao().getById(id);
        if (account == null) {
            return;
        }
        ((TextView) getView().findViewById(R.id.txtCartTotalPrice)).setText(String.format("%s $", Objects.requireNonNullElse(AppDatabase.getInstance(getContext()).orderDao().sumPrice(account.getId()), "")));
        List<OrderItem> cartList = getListCart(account.getId());
        if (cartList == null || cartList.size() == 0) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_dashboard, new EmptyCartFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }
}
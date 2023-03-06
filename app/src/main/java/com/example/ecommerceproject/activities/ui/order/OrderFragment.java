package com.example.ecommerceproject.activities.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.adapter.CartAdapter;
import com.example.ecommerceproject.adapter.OrderAdapter;
import com.example.ecommerceproject.database.AppDatabase;
import com.example.ecommerceproject.databinding.FragmentOrderBinding;
import com.example.ecommerceproject.entities.Account;
import com.example.ecommerceproject.entities.Order;
import com.example.ecommerceproject.entities.OrderItem;

import java.util.List;
import java.util.Objects;

public class OrderFragment extends Fragment {

    private FragmentOrderBinding binding;
    private RecyclerView rvOrder;
    private OrderAdapter orderAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvOrder = view.findViewById(R.id.rvOrder);
        orderAdapter = new OrderAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvOrder.setLayoutManager(linearLayoutManager);

        String id = getActivity().getIntent().getExtras().getString("currentUserId");
        Account account = AppDatabase.getInstance(getContext()).accountDao().getById(id);
        if (account == null) {
            return;
        }
        orderAdapter.setData(getListOrder(account.getId()));
        rvOrder.setAdapter(orderAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<Order> getListOrder(String accountId) {
        return AppDatabase.getInstance(getContext()).orderDao().getAllOrder(accountId);
    }
}
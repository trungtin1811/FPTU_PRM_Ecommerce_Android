package com.example.ecommerceproject.activities.ui.cart;

import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.activities.ui.home.HomeFragment;
import com.example.ecommerceproject.database.AppDatabase;
import com.example.ecommerceproject.entities.Account;
import com.example.ecommerceproject.entities.Order;
import com.example.ecommerceproject.enums.OrderType;
import com.google.android.gms.common.util.Strings;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConfirmCartFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String id = getActivity().getIntent().getExtras().getString("currentUserId");
        Account account = AppDatabase.getInstance(getContext()).accountDao().getById(id);
        if (account == null) {
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        EditText name = view.findViewById(R.id.txtCartUpdateName);
        EditText phone = view.findViewById(R.id.txtCartUpdatePhone);
        EditText address = view.findViewById(R.id.txtCartUpdateAddress);
        TextView nameError = view.findViewById(R.id.txtCartUpdateErrorName);
        TextView phoneError = view.findViewById(R.id.txtCartUpdateErrorPhone);
        TextView addressError = view.findViewById(R.id.txtCartUpdateErrorAddress);

        Order cart = AppDatabase.getInstance(getContext()).orderDao().getCart(account.getId());
        ((TextView) view.findViewById(R.id.txtOrderTimeStamp)).setText(LocalDateTime.now().format(formatter));
        ((TextView) view.findViewById(R.id.txtOrderNumOfProducts)).setText(String.format("%s items", AppDatabase.getInstance(getContext()).orderDao().countItems(cart.getId())));
        ((TextView) view.findViewById(R.id.txtOrderPrice)).setText(String.format("%s $", AppDatabase.getInstance(getContext()).orderDao().sumPriceOrder(cart.getId())));

        name.setText(account.getName());
        if (account.getPhone() != null) {
            phone.setText(account.getPhone());
        }
        if (account.getAddress() != null) {
            address.setText(account.getAddress());
        }
        ((Button) view.findViewById(R.id.btnConfirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = true;

                if (Strings.isEmptyOrWhitespace(name.getText().toString())) {
                    nameError.setText("Please input Full name.");
                    result = false;
                }
                if (Strings.isEmptyOrWhitespace(address.getText().toString())) {
                    addressError.setText("Please input Address.");
                    result = false;
                }
                if (Strings.isEmptyOrWhitespace(phone.getText().toString())) {
                    phoneError.setText("Please input Phone Number.");
                    result = false;
                }
                if (result) {
                    Order cart = AppDatabase.getInstance(getContext()).orderDao().getCart(account.getId());
                    cart.setType(OrderType.ORDER);
                    cart.setDateTime(LocalDateTime.now().format(formatter));
                    cart.setName(name.getText().toString());
                    cart.setAddress(address.getText().toString());
                    cart.setPhone(phone.getText().toString());
                    AppDatabase.getInstance(getContext()).orderDao().update(cart);
                    Toast.makeText(getActivity(), "Checkout success", Toast.LENGTH_LONG).show();

                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_dashboard, new HomeFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }
}
package com.example.ecommerceproject.activities.ui.userinformation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.activities.LoginActivity;
import com.example.ecommerceproject.databinding.FragmentUserInformationBinding;
import com.example.ecommerceproject.dto.AccountModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class UserInformationFragment extends Fragment {

    private GoogleSignInClient mGoogleSignInClient;

    private FragmentUserInformationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUserInformationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


        //initialize your view here for use view.findViewById("your view id")
        view.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        AccountModel accountModel = (AccountModel) getActivity().getIntent().getSerializableExtra("currentUser");
        System.out.println(accountModel.toString());
        ((TextView) view.findViewById(R.id.txtAccountName)).setText(accountModel.getName());
        ((TextView) view.findViewById(R.id.txtAccountEmail)).setText(accountModel.getEmail());
        ((TextView) view.findViewById(R.id.txtAccountPhone)).setText("0705 365 185");

    }

}
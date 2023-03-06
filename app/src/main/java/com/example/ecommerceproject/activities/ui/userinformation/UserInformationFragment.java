package com.example.ecommerceproject.activities.ui.userinformation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ecommerceproject.R;
import com.example.ecommerceproject.activities.LoginActivity;
import com.example.ecommerceproject.database.AppDatabase;
import com.example.ecommerceproject.databinding.FragmentUserInformationBinding;
import com.example.ecommerceproject.entities.Account;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

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
        String id = getActivity().getIntent().getExtras().getString("currentUserId");
        Account account = AppDatabase.getInstance(getContext()).accountDao().getById(id);
        if (account == null) {
            return;
        }
        ((TextView) view.findViewById(R.id.txtAccountName)).setText(account.getName());
        ((TextView) view.findViewById(R.id.txtAccountEmail)).setText(account.getEmail());
        ((TextView) view.findViewById(R.id.txtAccountPhone)).setText(account.getPhone() != null ? account.getPhone() : "");
        ((TextView) view.findViewById(R.id.txtAccountAddress)).setText(account.getAddress() != null ? account.getAddress() : "");
        if (account.getImageUrl() != null) {
            Glide.with(getContext()).load(account.getImageUrl()).into((CircleImageView) view.findViewById(R.id.imageAvatar));

        }
        view.findViewById(R.id.btnUpdateProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_dashboard, new UpdateProfileFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });


    }

}
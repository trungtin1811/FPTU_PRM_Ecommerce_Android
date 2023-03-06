package com.example.ecommerceproject.activities.ui.userinformation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ecommerceproject.R;
import com.example.ecommerceproject.database.AppDatabase;
import com.example.ecommerceproject.entities.Account;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileFragment extends Fragment {

    private final int GALLERY_REQ_CODE = 1000;
    private CircleImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String id = getActivity().getIntent().getExtras().getString("currentUserId");
        Account account = AppDatabase.getInstance(getContext()).accountDao().getById(id);
        if (account == null) {
            return;
        }

        imageView = view.findViewById(R.id.imgUpdate);
        if (account.getImageUrl() != null) {
            Glide.with(getContext()).load(account.getImageUrl()).into(imageView);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        TextView name = view.findViewById(R.id.txtUpdateName);
        TextView phone = view.findViewById(R.id.txtUpdatePhone);
        TextView address = view.findViewById(R.id.txtUpdateAddress);
        TextView nameError = view.findViewById(R.id.txtUpdateErrorName);
        TextView phoneError = view.findViewById(R.id.txtUpdateErrorPhone);
        name.setText(account.getName());
        if (account.getPhone() != null) {
            phone.setText(account.getPhone());
        }
        if (account.getAddress() != null) {
            address.setText(account.getAddress());
        }
        ((Button) view.findViewById(R.id.btnUpdate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = true;

                if (Strings.isEmptyOrWhitespace(name.getText().toString())) {
                    nameError.setText("Please input Full name.");
                    result = false;
                }
                if (phone != null && Strings.isEmptyOrWhitespace(phone.getText().toString())) {
                    phoneError.setText("Please input Phone.");
                    result = false;
                }

                if (result) {
                    if (!"".equals(name.getText().toString())) {
                        account.setName(name.getText().toString());
                    }
                    if (!"".equals(phone.getText().toString())) {
                        account.setPhone(phone.getText().toString());
                    }

                    account.setAddress(address.getText().toString());
                    AppDatabase.getInstance(getContext()).accountDao().updateUsers(account);
                    Toast.makeText(getActivity(), "Update success", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_dashboard, new UserInformationFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://android-ecommerce-aac12.appspot.com");
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child(String.format("images/%s.jpg", UUID.randomUUID()));

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
//                imageView.setImageURI(data.getData());
                UploadTask uploadTask = imagesRef.putFile(data.getData());

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return imagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String id = getActivity().getIntent().getExtras().getString("currentUserId");
                            Account account = AppDatabase.getInstance(getContext()).accountDao().getById(id);
                            if (account == null) {
                                return;
                            }
                            if (account != null) {
                                account.setImageUrl(downloadUri.toString());
                            }
                            if (account.imageUrl != null) {
                                Glide.with(getContext()).load(account.getImageUrl()).into(imageView);
                            }

                            AppDatabase.appDatabase.accountDao().updateUsers(account);
                        } else {
                        }
                    }
                });

            }
        }

    }
}
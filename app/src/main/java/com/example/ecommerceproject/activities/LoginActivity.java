package com.example.ecommerceproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.database.AppDatabase;
import com.example.ecommerceproject.dto.AccountModel;
import com.example.ecommerceproject.entities.Account;
import com.example.ecommerceproject.entities.Product;
import com.example.ecommerceproject.enums.Role;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.Serializable;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private AppDatabase db;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Account account = db.accountDao().getByEmail(user.getEmail());
            if (account != null) {
                handleSuccess(new AccountModel(account));
            }
        }
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = AppDatabase.getInstance(getApplicationContext());
        //Init default product
        initProducts();
        getSupportActionBar().hide();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnGoogleSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = findViewById(R.id.txtEmail);
                EditText password = findViewById(R.id.txtPassword);

                if (isValid(username.getText().toString(), password.getText().toString())) {
                    login(username.getText().toString(), password.getText().toString());
                }
            }
        });


        findViewById(R.id.txtRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterAccountActivity.class));
                finish();
            }
        });
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Account account = db.accountDao().getByEmail(email);
                            if (account != null) {
                                handleSuccess(new AccountModel(account));
                            }
                        } else {
                            ((TextView) findViewById(R.id.txtError)).setText("Invalid email or password.");
                        }
                    }
                });

    }

    private boolean isValid(String email, String password) {
        boolean result = true;

        if (Strings.isEmptyOrWhitespace(email) || Strings.isEmptyOrWhitespace(password)) {
            ((TextView) findViewById(R.id.txtError)).setText("Please input email and password.");
            result = false;
        }

        return result;
    }
//

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Account account = db.accountDao().getByEmail(user.getEmail());
                                if (account == null) {
                                    account = new Account().setName(user.getDisplayName())
                                            .setEmail(user.getEmail())
                                            .setId(user.getUid())
                                            .setRole(Role.USER);
                                    db.accountDao().insertAll(account);
                                }
                                handleSuccess(new AccountModel(account));

                            }
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
    // [END auth_with_google]'

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void handleSuccess(AccountModel accountModel) {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.putExtra("currentUser", (Serializable) accountModel);
        startActivity(intent);
        finish();
    }

    private void initProducts() {
        if (db.productDao().getAll().size() > 0) {
            return;
        }
        db.productDao().insertAll(
                new Product()
                        .setName("Apple Watch")
                        .setDescription("Available when you purchase any new iPhone, iPad, iPod Touch, Mac or Apple TV, £4.99/month after free trial.")
                        .setPrice(1000.0)
                        .setQuantity(10L)
                        .setImageUrl("https://images.fpt.shop/unsafe/fit-in/800x800/filters:quality(5):fill(white)/fptshop.com.vn/Uploads/Originals/2022/9/12/637985935050489552_apple-watch-series-8-gps-41mm-do-dd.jpg"),
                new Product()
                        .setName("IPhone 14")
                        .setDescription("iPhone 14 Pro. Capture impressive details with the 48MP Main Camera. Experience iPhone in a whole new way with Dynamic Island and the Always On display. Collision Detection, a new safety feature, calls for help when needed.")
                        .setPrice(2000.0)
                        .setQuantity(20L)
                        .setImageUrl("https://shopdunk.com/images/thumbs/0008771_iphone-14-pro-256gb.png"),
                new Product()
                        .setName("Macbook M2")
                        .setDescription("M2 is the next generation of Apple silicon. Its 8-core CPU lets you zip through everyday tasks like creating documents and presentations, or take on more intensive workflows like developing in Xcode or mixing tracks in Logic Pro.")
                        .setPrice(1000.0)
                        .setQuantity(10L)
                        .setImageUrl("https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/mbp-spacegray-select-202206?wid=904&hei=840&fmt=jpeg&qlt=90&.v=1664497359481"),
                new Product()
                        .setName("Air Pod 2")
                        .setDescription("AirPods 2 Lightning Charge Apple MV7N2 Bluetooth headset - dubbed a national legendary AirPods that is very popular with apple fans.")
                        .setPrice(3000.0)
                        .setQuantity(10L)
                        .setImageUrl("https://cdn.tgdd.vn/Products/Images/54/236016/bluetooth-airpods-2-apple-mv7n2-imei-1-org.jpg"),
                new Product()
                        .setName("Air Tag")
                        .setDescription("Airtag is a small device with integrated Bluetooth technology used to find lost objects and equipment. Although there are many similar products, Apple's smart home accessories promise to integrate technology more deeply, allowing users to experience even more wonderful activities of the device.")
                        .setPrice(1000.0)
                        .setQuantity(10L)
                        .setImageUrl("https://cdn2.cellphones.com.vn/358x358,webp,q100/media/catalog/product/a/i/airtag-3.png")
        );
    }
}
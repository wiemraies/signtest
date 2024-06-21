package com.example.signtest;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.kaopiz.kprogresshud.KProgressHUD;

public class SignUpFragment extends Fragment {

    private EditText edName, edEmail, edNumber, edPassword;
    private AppCompatButton signUpButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private KProgressHUD kProgressHUD;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        edName = view.findViewById(R.id.name);
        edEmail = view.findViewById(R.id.email);
        edNumber = view.findViewById(R.id.mobileNumber);
        edPassword = view.findViewById(R.id.password);
        signUpButton = view.findViewById(R.id.SignUp);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });

        return view;
    }

    private void showProgressBar() {
        kProgressHUD = KProgressHUD.create(requireContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setMaxProgress(100)
                .setWindowColor(R.color.light_green)
                .show();
        kProgressHUD.setProgress(90);
    }

    private void signUpUser() {
        String name = edName.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String number = edNumber.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(number) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(),"please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        showProgressBar();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        DocumentReference userInfo = firestore.collection("Users").document(userId);
                        UserModel model = new UserModel(name, email, number, password, userId);
                        userInfo.set(model, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        kProgressHUD.dismiss();
                                        Toast.makeText(getContext(), "user registered successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        kProgressHUD.dismiss();
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        kProgressHUD.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

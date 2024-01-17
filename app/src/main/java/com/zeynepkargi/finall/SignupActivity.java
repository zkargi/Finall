package com.zeynepkargi.finall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zeynepkargi.finall.databinding.ActivityMainBinding;
import com.zeynepkargi.finall.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private FirebaseAuth auth;
    Button btn_login,btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btn_login=findViewById(R.id.btnlogin);
        btn_signup=findViewById(R.id.btnkaydet);
        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isim = binding.etisim.getText().toString();
                String soyisim = binding.etsoyisim.getText().toString();
                String email = binding.etemail.getText().toString();
                String password = binding.etsifre.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Lütfen epostayı ya da şifreyi doğru girin", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(SignupActivity.this, "Kullanıcı oluşturuldu", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignupActivity.this, SignupActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        });
            }

        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i̇ntent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i̇ntent);
                finish();
            }
        });






 }
}
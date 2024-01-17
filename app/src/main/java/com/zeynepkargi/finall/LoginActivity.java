package com.zeynepkargi.finall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.zeynepkargi.finall.databinding.ActivityLoginBinding;
import com.zeynepkargi.finall.ui.label.AddLabelFragment;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    Button btnlogin,btnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnlogin=findViewById(R.id.btnnlogin);
        btnsignup=findViewById(R.id.btnnsignup);
        binding =ActivityLoginBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();

        auth=FirebaseAuth.getInstance();
         btnlogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String email=binding.ettemail.getText().toString();
                 String password=binding.ettpassword.getText().toString();
                 if(email.isEmpty()||password.isEmpty()){
                     Toast.makeText(LoginActivity.this,"Lütfen email ya da şifreyi boş bırakmayın",Toast.LENGTH_LONG).show();
                     return;
                 }
                 if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                     Toast.makeText(LoginActivity.this,"Geçerli bir eposta adresi girin. ",Toast.LENGTH_LONG).show();
                     return;
                 }

                 auth.signInWithEmailAndPassword(email,password)
                         .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                     @Override
                     public void onSuccess(AuthResult authResult){
                         Intent intent =new Intent(LoginActivity.this, AddLabelFragment.class);
                         startActivity(intent);
                         finish();
                     }
                 });
             }
         });

    }
}
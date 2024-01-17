package com.zeynepkargi.finall.ui.label;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.DescriptorProtos;
import com.zeynepkargi.finall.databinding.FragmentAddlabelBinding;


public class AddLabelFragment extends Fragment {

    private FragmentAddlabelBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private LinearLayout ll;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Button eklenen=binding.btnekle;

        binding = FragmentAddlabelBinding.inflate(inflater, container, false);
        EditText labeltxt=binding.etlabel;
        View root = binding.getRoot();
        firebaseFirestore=FirebaseFirestore.getInstance();
        collectionReference=firebaseFirestore.collection("label");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> gorev) {
                if(gorev.isSuccessful()){
                    for(QueryDocumentSnapshot document:gorev.getResult()){
                        Label etiket = document.toObject(Label.class);
                        CheckBox checkBox=new CheckBox((getActivity()));
                        checkBox.setText(etiket.getLabelText());
                        ll.addView(checkBox);
                    }
                }else{
                    Log.d("TAG","Dosya alınırken Hata Oluştu",gorev.getException());
                }
            }
        });

        eklenen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String labelet=labeltxt.getText().toString();
                collectionReference.whereEqualTo("Label text",labelet).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        collectionReference.add(new Label(labelet)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getActivity(),"Label oluşturuldu. ",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),"Label oluşturulamadı.",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
        return root;
    }
    public class Label{
        private String lbl_text;
        public Label(String lbl_text){
            this.lbl_text=lbl_text;
        }
        public String getLabelText(){
            return lbl_text;
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.zeynepkargi.finall.ui.photo;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.CloseGuard;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zeynepkargi.finall.databinding.FragmentAddphotoBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddPhotoFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private int Foto=1;
    List<String> firebaseLabelList=new ArrayList<>();

    private FragmentAddphotoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddphotoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        Button btnsec=binding.btnfotosec;
        btnsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,Foto);
            }
        });

        return root;
    }
    private List<String> getSelectedLAbels(){
        RadioGroup radioGroup=binding.radioGroup;
        List<String> secililabel=new ArrayList<>();
        for(int i=0;i<radioGroup.getChildCount();i++){
            View view=radioGroup.getChildAt(i);
            if(view instanceof CheckBox){
                CheckBox checkBox=(CheckBox) view;
                if(checkBox.isChecked()){
                    secililabel.add(checkBox.getText().toString());
                }
            }
        }
        return secililabel;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent veri){
        super.onActivityResult(requestCode,resultCode,veri);
        if(requestCode==Foto && resultCode==RESULT_OK && veri!=null){
            Uri secilifoto=veri.getData();
            String[] dosyayolu={MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(secilifoto,dosyayolu, null,null,null);
            cursor.moveToFirst();


            int columnindex=cursor.getColumnIndex(dosyayolu[0]);
            String fotoyolu=cursor.getString(columnindex);
            cursor.close();
            ImageView imageview=binding.imgview;
            imageview.setImageBitmap(BitmapFactory.decodeFile(fotoyolu));
            Button paylas=binding.btnyukle;
            paylas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StorageReference photoreference=storageReference.child("gönderiler/"+ UUID.randomUUID().toString());
                    UploadTask uploadTask=photoreference.putFile(secilifoto);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> taskuri=taskSnapshot.getStorage().getDownloadUrl();
                            while(!taskuri.isSuccessful());
                            Uri download_Url = taskuri.getResult();

                            DocumentReference reference=firebaseFirestore.collection("kullanıcı").document(firebaseAuth.getUid());

                            reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentsnapshot) {
                                    String name=documentsnapshot.getString("name");
                                    Map<String,Object> gonderi = new HashMap<>();
                                    gonderi.put("imageUrl",download_Url.toString());
                                    gonderi.put("name", name);

                                    firebaseFirestore.collection("Gönderi").document().set(gonderi).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG,"yüklendi");
                                            Toast.makeText(getActivity(),"başarılı yükleme", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            }



                     });

                    }

                });

            }
        });
     }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.explorev3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.explorev3.pojo.PostItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

public class AddPostActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 0;
    //
    private Button btnUploadPost, btnChooseImg;
    private EditText postName, postAddress, postDesc;
    private TextView tvImgName;
    private ImageView imgContainer;
    private ProgressBar progressBar;

    private FirebaseUser user;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask uploadPostTask;
    private Uri mImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        postName = findViewById(R.id.edt_name_post);
        postAddress = findViewById(R.id.edt_address_post);
        postDesc = findViewById(R.id.edt_desc_post);
        btnUploadPost = findViewById(R.id.btn_upload_post);
        btnChooseImg = findViewById(R.id.btn_choose_img_post);
        imgContainer = findViewById(R.id.img_container_post);
        progressBar = findViewById(R.id.upload_progress_bar_post);

        mStorageRef = FirebaseStorage.getInstance().getReference("post");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("post");

        btnChooseImg.setOnClickListener(this);
        btnUploadPost.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choose_img_post:
                openFileChooser();
                break;
            case R.id.btn_upload_post:
                if (uploadPostTask != null && uploadPostTask.isInProgress())
                    Toast.makeText(this, "Upload still in progress", Toast.LENGTH_SHORT).show();
                else
                    uploadFile();

                break;
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=  null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(imgContainer);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {

        String name, address, desc;
        name = postName.getText().toString().trim();
        address = postAddress.getText().toString().trim();
        desc = postDesc.getText().toString().trim();

        if (name.isEmpty()) {
            postName.setError("Place name is required");
            postName.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            postAddress.setError("Address is required");
            postAddress.requestFocus();
            return;
        }

        if (desc.isEmpty()) {
            postDesc.setError("Description is required");
            postDesc.requestFocus();
            return;
        }

        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

            uploadPostTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            progressBar.setProgress(0);
                                        }
                                    }, 500);

                                    Toast.makeText(AddPostActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();

                                    Map<String, Object> childUpload = new HashMap<>();
                                    childUpload.put("uid", user.getUid());
                                    childUpload.put("name", name);
                                    childUpload.put("address", address);
                                    childUpload.put("desc", desc);
                                    childUpload.put("imgUrl", uri.toString());
                                    childUpload.put("vote", 0);

                                    String postId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(postId).setValue(childUpload)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("edit user", "onFailure: " + e);
                                                    Toast.makeText(AddPostActivity.this, "Error " + e, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddPostActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}
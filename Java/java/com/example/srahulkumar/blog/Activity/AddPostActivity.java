package com.example.srahulkumar.blog.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.srahulkumar.blog.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
   private ImageButton mPostImage;
   private EditText mPostTitle,mPostDesc;
   private Button mSubmitButton;
   private ImageView camera;

   private DatabaseReference mPostReference;
   private FirebaseAuth mAuth;
   private FirebaseUser mUser;
   private  StorageReference mStorageRef;

   private ProgressDialog mProgress;
   private static final int galleryCode=1;
   private Uri mImageUri;

private static final int REQUEST_IMAGE_CAPTURE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        mProgress=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mPostReference= FirebaseDatabase.getInstance().getReference().child("mBlog");

        mStorageRef= FirebaseStorage.getInstance().getReference();

//.....................................................................................id of UI

        mPostImage=findViewById(R.id.imageButton);
        mPostTitle=findViewById(R.id.titleAddPostId);
        mPostDesc=findViewById(R.id.addDiscriptionPostId);
        mSubmitButton=findViewById(R.id.addPostButton);
        camera=findViewById(R.id.camera);


        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,galleryCode);
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //posting to database..
                  startPosting();
            }
        });

        //................................................................use camera to take  pic

//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }
//            }
//        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode==galleryCode && resultCode==RESULT_OK){
             mImageUri=data.getData();
             mPostImage.setImageURI(mImageUri);
       }
    }

    public void startPosting(){
        mProgress.setMessage("Posting to blog...");
        mProgress.show();
          final String titleVal=mPostTitle.getText().toString();
          final String descVal=mPostDesc.getText().toString();

          if (!TextUtils.isEmpty(mPostTitle.getText().toString()) && !TextUtils.isEmpty(mPostDesc.getText().toString()) && mImageUri!=null){
             StorageReference filepath=mStorageRef.child("mBlog_Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 Uri downloadUrl=taskSnapshot.getDownloadUrl();
                 DatabaseReference newPost=mPostReference.push();

                    Map<String ,String> dataToSave =new HashMap<String, String>();
                    dataToSave.put("title",titleVal);
                    dataToSave.put("desc",descVal);
                    dataToSave.put("image",downloadUrl.toString());
                    dataToSave.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    dataToSave.put("userId",mUser.getUid());

                    newPost.setValue(dataToSave);
                    mProgress.dismiss();
                    startActivity(new Intent(AddPostActivity.this,PostListActivity.class));
                }
            });




          }
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.back){
            startActivity(new Intent(AddPostActivity.this,PostListActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu2,menu);

        return super.onCreateOptionsMenu(menu);
    }
}

  package com.example.friendsapp;
  import android.app.Activity;
  import android.app.ProgressDialog;
  import android.content.ContentResolver;
  import android.content.Context;
  import android.content.Intent;
  import android.database.Cursor;
  import android.net.Uri;
  import android.os.Bundle;
  import android.provider.ContactsContract;
  import android.view.View;
  import android.widget.Button;
  import android.widget.ImageView;
  import android.widget.TextView;
  import android.widget.Toast;

  import androidx.annotation.NonNull;
  import androidx.appcompat.app.AppCompatActivity;

  import com.google.android.gms.tasks.OnCompleteListener;
  import com.google.android.gms.tasks.OnSuccessListener;
  import com.google.android.gms.tasks.Task;
  import com.google.firebase.database.DataSnapshot;
  import com.google.firebase.database.DatabaseError;
  import com.google.firebase.database.DatabaseReference;
  import com.google.firebase.database.FirebaseDatabase;
  import com.google.firebase.database.ValueEventListener;
  import com.google.firebase.storage.FirebaseStorage;
  import com.google.firebase.storage.StorageReference;
  import com.google.firebase.storage.UploadTask;
  import com.squareup.picasso.Picasso;
  import com.theartofdev.edmodo.cropper.CropImage;
  import com.theartofdev.edmodo.cropper.CropImageView;

  import java.util.ArrayList;

  import de.hdodenhof.circleimageview.CircleImageView;


  public class MainActivity extends AppCompatActivity {
      Button friend;
      /********************FOR USER PROFILE*****************************/
      final static int Gallery_Pick = 1;
      private CircleImageView frnd_image;
      //private ImageView pica_img;
      private StorageReference ProfileStorageRef;
      private DatabaseReference UserRef;
      ProgressDialog pd;

      /**********************************************/
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          friend = (Button) findViewById(R.id.friendbtn);
/***********************************************************************/
     /*   Context mContext=MainActivity.this;
        ArrayList<String> alContacts = null;
        ContentResolver cr = mContext.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cursor.moveToFirst())
        {
            alContacts = new ArrayList<String>();
            do
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                    while (pCur.moveToNext())
                    {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        alContacts.add(contactNumber);
                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext()) ;
        }
        
            System.out.println(alContacts);*/
          /*******************************************************************/
          friend.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent i = new Intent(MainActivity.this, friendhome.class);
                  startActivity(i);
              }
          });


          /**************************************************************************/


          ProfileStorageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
          frnd_image = (CircleImageView) findViewById(R.id.set_profile_image);
         // pica_img=(ImageView)findViewById(R.id.picassoImageView);
          UserRef = FirebaseDatabase.getInstance().getReference("user");
          pd = new ProgressDialog(this);
          pd.setMessage("Uploading....");

          frnd_image.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  CropImage.activity()
                          .setGuidelines(CropImageView.Guidelines.ON)
                          .setAspectRatio(1, 1)
                          .start(MainActivity.this);
              }
          });
          UserRef.child("nikhar").addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists())
                 {
                        String image =dataSnapshot.child("profile_img").getValue().toString();
                        Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.profile_image).error(R.mipmap.ic_launcher).into(frnd_image);
                        //"http://i.imgur.com/DvpvklR.png"
                 }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
      }

      @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);
          if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
              CropImage.ActivityResult result = CropImage.getActivityResult(data);
              if (resultCode == RESULT_OK) {
                  Uri resulturi = result.getUri();
                  pd.show();
                  final StorageReference filepath = ProfileStorageRef.child("nikhar" + ".jpg");/*current_user_id*/
                  filepath.putFile(resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                          if (task.isSuccessful()) {
                              Toast.makeText(MainActivity.this, "Profile image stored in storage", Toast.LENGTH_SHORT).show();

                              filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                  @Override
                                  public void onSuccess(Uri uri) {
                                      Uri DownloadUrl = uri;
                                     String downloadurl = DownloadUrl.toString();

                                      UserRef.child("nikhar").child("profile_img").setValue(downloadurl)
                                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task) {

                                                      if (task.isSuccessful()) {
                                                          Intent selfIntent = new Intent(MainActivity.this, MainActivity.class);
                                                          startActivity(selfIntent);

                                                          Toast.makeText(MainActivity.this, "Profile image url saved in database", Toast.LENGTH_SHORT).show();
                                                          pd.dismiss();
                                                      } else {
                                                          String message = task.getException().getMessage();
                                                          Toast.makeText(MainActivity.this, "Error is " + message, Toast.LENGTH_SHORT);
                                                          pd.dismiss();

                                                      }
                                                  }
                                              });


                                  }
                              });




                          } else {
                              System.out.println("Image cannot be added Error occured");
                              Toast.makeText(MainActivity.this, "Image cannot be added Error occured", Toast.LENGTH_SHORT);
                              pd.dismiss();
                          }
                      }
                  });
              } else {
                  Toast.makeText(MainActivity.this, "Image cannot be cropped Error occured", Toast.LENGTH_SHORT);
                  pd.dismiss();
              }
          }


          /****************************************************************************/
      }
  }


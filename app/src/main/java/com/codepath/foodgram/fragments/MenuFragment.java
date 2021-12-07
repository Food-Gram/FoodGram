package com.codepath.foodgram.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.foodgram.R;
import com.codepath.foodgram.models.StoreMenu;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class MenuFragment extends Fragment {

    public static final String TAG = "MenuFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    //private static final int RESULT_LOAD_IMAGE = 1;
    private EditText foodName;
    private EditText prices;
    private ImageView takephote;
    //private ImageView selectphote;
    private ImageView foodImage;
    private ProgressBar pb;
    private TextView msg;
    private Button upload;
    private File photoFile;
    public String photoFileName = "photo.jpg";


    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        foodName = view.findViewById(R.id.etFoodName);
        prices = view.findViewById(R.id.etPrice);
        takephote = view.findViewById(R.id.ibTakePhoto);
       // selectphote = view.findViewById(R.id.ibSelectPhoto);
        foodImage = view.findViewById(R.id.ivFoodImage);
        pb = view.findViewById(R.id.pbLoading);
        upload = view.findViewById(R.id.bnMenuUpload);
        msg = view.findViewById(R.id.tvMenuMsg);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(ProgressBar.VISIBLE);
                String name = foodName.getText().toString();
                String price = prices.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Food name cannot be empty", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(ProgressBar.INVISIBLE);
                    return;
                }
                if (price.isEmpty()) {
                    Toast.makeText(getContext(), "price cannot be empty", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(ProgressBar.INVISIBLE);
                    return;
                }

                if(photoFile == null || foodImage.getDrawable() == null){
                    Toast.makeText(getContext(), "There is no image!", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(ProgressBar.INVISIBLE);
                    return;
                }

                try {
                    double Price = (Math.round(Double.valueOf(price) * 100.0) / 100.0);
                    saveMenu(name, Price, photoFile);
                }
                catch(IllegalArgumentException e){
                    Toast.makeText(getContext(), "Price you entered is not valid", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(ProgressBar.INVISIBLE);
                    return;
                }

            }
        });

        takephote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        /*
        selectphote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri imageUri = createImageUri(getContext());
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent, RESULT_LOAD_IMAGE );



                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
                //photoFile = getPhotoFileUri(photoFileName);


            }
        });
*/
    }


    private void saveMenu(String name, Double price, File photoFile) {
        System.out.println(photoFile.toString());
        StoreMenu menu = new StoreMenu();
        menu.setStore(ParseUser.getCurrentUser());
        menu.setFoodName(name);
        menu.setPrice(price);
        menu.setImage(new ParseFile(photoFile));
        menu.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                }
                Log.i(TAG, "Menu save was successful!!");
                // after post saved, clear the text.
                msg.setVisibility(TextView.VISIBLE);
                foodName.setText("");
                prices.setText("");
                foodImage.setImageResource(0);
                pb.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                foodImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        /*
        if(requestCode == RESULT_LOAD_IMAGE ){
            if (resultCode == RESULT_OK && data != null) {

                Uri selectedImage = data.getData();
                foodImage.setImageURI(selectedImage);
                System.out.println("1--------");

                Uri uri = data.getData();
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor actualimagecursor = getContext().getContentResolver().query(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                String img_path = actualimagecursor.getString(actual_image_column_index);
                photoFile = new File(img_path);
                ParseFile file = new ParseFile(photoFile);


                //photoFile = uriToFile(selectedImage);
                System.out.println(file+"----");

            }
        }
         */
    }

    /*
    private static Uri createImageUri(Context context){
        String name = "takePhoto" +System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,name);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, name + "jpeg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        return uri;
    }
    */


    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

}
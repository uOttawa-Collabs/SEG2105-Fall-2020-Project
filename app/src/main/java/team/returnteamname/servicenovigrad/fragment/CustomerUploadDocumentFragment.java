package team.returnteamname.servicenovigrad.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.CustomerAccount;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;


public class CustomerUploadDocumentFragment extends Fragment
{
    private        final FirebaseDatabase firebaseDatabase      = FirebaseDatabase.getInstance();
    private static final int              CAMERA_REQUEST        = 10;
    private static final int              IMAGE_GALLERY_REQUEST = 20;
    ImageView preview;
    Uri imageUri;
    Bitmap previewImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_customer_upload_document,
                                     container, false);
        Bundle        bundle        = getArguments();

        Button buttonUploadPic = view.findViewById(R.id.buttonUploadPicture);
        Button buttonTakePic = view.findViewById(R.id.buttonTakePicture);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);
        Button buttonNext = view.findViewById(R.id.buttonNext);
        preview = view.findViewById(R.id.imageViewDocument);

        if(bundle != null)
        {
            CustomerAccount customerAccount = (CustomerAccount) bundle.getSerializable("account");

            try
            {
                buttonUploadPic.setOnClickListener(
                    v->
                    {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        String pictureDirectoryPath = pictureDirectory.getPath();
                        Uri data = Uri.parse(pictureDirectoryPath);
                        photoPickerIntent.setDataAndType(data, "image/*");
                        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
                    });

                buttonTakePic.setOnClickListener(
                    v->
                    {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    });

                buttonSubmit.setOnClickListener(
                    v->
                    {
                        DatabaseReference databaseReference = firebaseDatabase.getReference();
                        String imageStr = bitmapToBase64(previewImage);
                        databaseReference.child("documents").child("Jinemployee").child(customerAccount.getUsername()).setValue(imageStr);
                        Toast.makeText(getContext(), "Submit successfully", Toast.LENGTH_SHORT).show();
                    });

                /*buttonNext.setOnClickListener(
                    v ->
                    {
                       Intent intentLayout = new Intent(view.getContext(), CustomerRatingBranchFragment.class);
                       view.getContext().startActivity(intentLayout);
                    });*/
            }
            catch (Exception e)
            {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        else
            throw new IllegalArgumentException("Invalid argument");

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                previewImage = (Bitmap) data.getExtras().get("data");
                preview.setImageBitmap(previewImage);
            }
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                imageUri = data.getData();
                InputStream inputStream;

                try {
                    ContentResolver contentResolver = getContext().getContentResolver();
                    inputStream = contentResolver.openInputStream(imageUri);
                    previewImage = BitmapFactory.decodeStream(inputStream);
                    preview.setImageBitmap(previewImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }

        }
    }

    public static String bitmapToBase64(Bitmap bitmap){
        String                result = null;
        ByteArrayOutputStream output = null;

        try{
            if(bitmap != null)
            {
                output = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

                output.flush();
                output.close();

                byte[] bitmapBytes = output.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        finally
        {
            try
            {
                if(output != null){
                    output.flush();
                    output.close();
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return result;
    }
}

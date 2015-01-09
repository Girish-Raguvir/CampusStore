package com.example.android.io2014;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.view.View;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import java.io.File;
import android.os.Environment;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.widget.Toast;
import android.graphics.BitmapFactory;
import java.io.InputStream;



public class CreateActivity extends Activity {

    private static final int REQUEST_CODE = 1;
    private static final int CAMERA_PIC_REQUEST = 1337;
    private Bitmap bitmap;
    private ImageView imageView;
    private Button save, capture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        imageView = (ImageView) findViewById(R.id.imageView);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    File sdCardDirectory = Environment.getExternalStorageDirectory();
                    File image = new File(sdCardDirectory, "test.png");
                    boolean success = false;

                    // Encode the file as a PNG image.
                    FileOutputStream outStream;
                    try {

                        outStream = new FileOutputStream(image);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            /* 100 to keep full quality of the image */

                        outStream.flush();
                        outStream.close();
                        success = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (success) {
                        Toast.makeText(getApplicationContext(),
                                "Image saved with success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error during image saving", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        capture = (Button) findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View v){
                {
                    captureImage();
                }
            }
            });
    }


    public void captureImage() {
        // Capture image from camera

        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CAMERA_PIC_REQUEST);

    }

    public void pickImage() {
        // To pick a image from file system
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                try {
                    // We need to recycle unused bitmaps
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                    InputStream stream = getContentResolver().openInputStream(
                            data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    stream.close();
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_PIC_REQUEST) {

                if (bitmap != null) {
                    bitmap.recycle();
                }
                bitmap = (Bitmap) data.getExtras().get("data");
                if (data.getExtras().get("data") == null)
                    Toast.makeText(getApplicationContext(),
                            "No image returned", Toast.LENGTH_LONG).show();
                else
                    imageView.setImageBitmap(bitmap);



            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == save) {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            File sdCardDirectory = Environment.getExternalStorageDirectory();
            File image = new File(sdCardDirectory, "test.png");
            boolean success = false;

            // Encode the file as a PNG image.
            FileOutputStream outStream;
            try {

                outStream = new FileOutputStream(image);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            /* 100 to keep full quality of the image */

                outStream.flush();
                outStream.close();
                success = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (success) {
                Toast.makeText(getApplicationContext(),
                        "Image saved with success", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Error during image saving", Toast.LENGTH_LONG).show();
            }
        } else if (v == capture) {
            captureImage();
        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

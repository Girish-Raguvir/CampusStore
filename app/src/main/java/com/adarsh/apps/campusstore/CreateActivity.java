package com.adarsh.apps.campusstore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class CreateActivity extends Activity {

    private static final int REQUEST_CODE = 1;
    private static final int CAMERA_PIC_REQUEST = 1337;
    private Bitmap bitmap;
    private ImageView imageView;
    private Button save, capture,submit;
    private EditText et1,et2,et3;
    private Spinner spinner1;
    private ParseFile imagefile;
    private CheckBox neg;
    ItemInfo olditem=null;
    static String cat;
   // private ItemInfo item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        et1=(EditText)findViewById(R.id.editText4);
        et2=(EditText)findViewById(R.id.editText3);
        et3=(EditText)findViewById(R.id.editText5);
        neg=(CheckBox)findViewById(R.id.checkBox);
        imageView = (ImageView) findViewById(R.id.imageView);
        save = (Button) findViewById(R.id.save);
        spinner1 = (Spinner) findViewById(R.id.spinner);
        spinner1.setOnItemSelectedListener(new OnCategorySelected());
        Intent intent=getIntent();
        if(intent.getExtras()!=null)
        { final String titletext = intent.getStringExtra("key");
        final String desctext = intent.getStringExtra("key3");
        final String pricetext=intent.getStringExtra("key4");
            final String nametext=intent.getStringExtra("key2");

            Log.d("test","Olditem found");
        et1.setText(titletext);
        et2.setText(desctext);
        et3.setText(pricetext);
        imageView.setImageBitmap(CommonResources.bmp);
            olditem=new ItemInfo(intent.getStringExtra("noteId"),titletext,nametext,desctext,new BitmapDrawable(getResources(), CommonResources.bmp),pricetext,cat);
        }
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
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                {   BitmapDrawable drawable = (BitmapDrawable)imageView.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                    byte[] byteArray = bs.toByteArray();
                    imagefile = new ParseFile("image.png", byteArray);

                    saveitem();
                    Intent i = new Intent(CreateActivity.this,MainActivity.class);
                    i.putExtra("name1", et1.getText().toString());
                    i.putExtra("desc1", et2.getText().toString());


                    startActivity(i);

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
    private void saveitem() {

        final String name = et1.getText().toString().trim();
        final String desc = et2.getText().toString().trim();
        final String price = et3.getText().toString().trim();

        /*name=name.trim();
        desc=desc.trim();
        price=price.trim();*/

        // If user doesn't enter a title or content, do nothing
        // If user enters title, but no content, save
        // If user enters content with no title, give warning
        // If user enters both title and content, save

        if (!name.isEmpty()) {
            if (olditem == null) {
                // Check if post is being created or edited


                // create new post
                //res = getResources();
                final ParseObject post = new ParseObject("Items");

                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                //byte[] data = stream.toByteArray();

                //ParseFile file = new ParseFile("resume.txt", data);
                //file.saveInBackground();


                //post.put("mediatype", data);
                final Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
                post.put("name", name);
                post.put("description", desc);
                post.put("price", price);
                post.put("image", imagefile);
                post.put("postedby", ParseUser.getCurrentUser().getUsername());
                setProgressBarIndeterminateVisibility(true);
                post.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        setProgressBarIndeterminateVisibility(false);
                        if (e == null) {
                            // Saved successfully.
                            olditem=new ItemInfo(post.getObjectId(),name,ParseUser.getCurrentUser().getUsername(),desc,new BitmapDrawable(getResources(), CommonResources.bmp),price,cat);
                            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // The save failed.
                            Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                            Log.d(getClass().getSimpleName(), "User update error: " + e);
                        }
                    }
                });


            }else{ParseQuery<ParseObject> query = ParseQuery.getQuery("Items");
                          Log.d("test","Modifing Old item"+olditem.getId());
                // Retrieve the object by id
                query.getInBackground(olditem.getId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject post, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data.
                            post.put("name", name);
                            post.put("description", desc);
                            post.put("price", price);
                            post.put("image", imagefile);
                            post.put("postedby", ParseUser.getCurrentUser().getUsername());
                            if(neg.isChecked()==true)post.put("negotiable","yes");
                            else post.put("negotiable","no");
                            setProgressBarIndeterminateVisibility(true);
                            post.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    setProgressBarIndeterminateVisibility(false);
                                    if (e == null) {
                                        //ItemInfo olditem = new ItemInfo(name, null, desc, null, price);
                                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                        Log.d("test","Saved");
                                    } else {
                                        // The save failed.
                                        Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                                        Log.d(getClass().getSimpleName(), "User update error: " + e);
                                    }
                                }
                            });
                        }else{Log.d("test","no item found");}
                    }
                });
            }

            }
            else if (name.isEmpty() && !desc.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
                builder.setMessage("Missing name or description!")
                        .setTitle(R.string.edit_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

    }


package com.cs360.jamiehorner.portfolio;

import androidx.appcompat.app.AppCompatActivity;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cma.CMACallback;
import com.contentful.java.cma.CMAClient;
import com.contentful.java.cma.model.CMAAsset;
import com.contentful.java.cma.model.CMAAssetFile;
import com.contentful.java.cma.model.CMALink;
import com.contentful.java.cma.model.CMAUpload;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import static com.contentful.java.cda.image.ImageOption.https;

public class PhotoGalleryActivity extends AppCompatActivity {

    Context context = this;
    FileInputStream foundFile;
    CMAAsset newAsset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        Context context = this;
        OkHttpClient httpClient = new OkHttpClient();
        LinearLayout baseLayout;
        baseLayout = findViewById(R.id.Content);


        // Create the Contentful client.

        final CDAClient client = CDAClient.builder()
            .setSpace("pwv4o9e408cc")
            .setToken("oY-T5MxeABq-rthvjj9xOvm8G_EG9Vc_WnRc5Xa1gGA")
            .build();

        client.observe(CDAAsset.class)
            .all()
            .map(new Function<CDAArray, byte[][]>() {
                 @Override
                 public byte[][] apply(CDAArray array) {
                     byte[][] data;
                     data = new byte[array.items().size()][];
                     for(int i = 0; i < array.items().size(); i++) {
                         final CDAAsset asset = (CDAAsset) array.items().get(i);
                         final String url = asset.urlForImageWith(https());

                         final Call call = httpClient
                             .newCall(
                                 new Request
                                     .Builder()
                                     .url(url)
                                     .build()
                             );
                         Response execute;
                         try {
                             execute = call.execute();
                         } catch (IOException e) {
                             // Networking layer failed.
                             throw new IllegalStateException("Downloading the url threw an error.", e);
                         }

                         // Fetch the body.
                         try {
                              data[i] = execute.body().bytes();
                         } catch (IOException e) {
                             // The body could not be fetched.
                             throw new IllegalStateException("Could not read body.", e);
                         }
                     }

                     return data;
                 }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe(new DisposableSubscriber<byte[][]>() {
                byte[][] result;

                @Override public void onComplete() {

                    for (int i = 0; i < result.length; i++) {
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(result[i], 0, result[i].length);
                        ImageView image = new ImageView(context);
                        image.setImageBitmap(bitmap);
                        image.setVisibility(View.VISIBLE);
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        image.setLayoutParams(params);
                        image.getLayoutParams().height = 400;
                        image.getLayoutParams().width = 400;
                        baseLayout.addView(image);
                    }
                }

                @Override public void onError(Throwable error) {
                    Log.e("Contentful" , "could not request entry", error);
                }

                @Override public void onNext(byte[][] data) {
                    result = data;
                }
            });
    }

    public void UploadPhoto (View view) {
        Intent filePicker = new Intent(Intent.ACTION_GET_CONTENT);
        filePicker.setType("image/jpeg");
        startActivityForResult(
            Intent.createChooser(filePicker, "Choose a file"), 200
        );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            // Create the Contentful client.
            final CMAClient client =
                new CMAClient
                    .Builder()
                    .setAccessToken("CFPAT-Bhbkk84gHgd5SitEfsRs6kd3VjEnAEzlAZnZ6XlBNMs")
                    .setSpaceId("pwv4o9e408cc")
                    .setEnvironmentId("master")
                    .build();

            String filePath = "";
            try {
                String query = queryName (getContentResolver(), data.getData());
                File tempFile = createTempFile(query);
                File savedFile = saveContentToFile(data.getData(), tempFile, getContentResolver());
                foundFile = new FileInputStream(savedFile.getAbsolutePath());
            } catch (FileNotFoundException err) {
                Log.i("err", err.toString() + filePath);
                return;
            }
            Log.i("success", foundFile.toString());


            client
                .uploads()
                .async()
                .create(
                    "pwv4o9e408cc",
                    // this stream should point to your file to be uploaded.
                    // could also be
                    // context.getResources().openRawResource(R.raw.somefile);
                    foundFile,
                    new CMACallback<CMAUpload>() {
                        @Override protected void onSuccess(CMAUpload result) {
                            final CMALink link = new CMALink(result);
                            final CMAAssetFile file = new CMAAssetFile();
                            file.setUploadFrom(link);
                            file.setFileName(UUID.randomUUID().toString());
                            file.setContentType("image/jpeg");
                            final CMAAsset asset = new CMAAsset();
                            asset
                                .getFields()
                                .setTitle("en-US", data.getData().getPath())
                                .setFile("en-US", file);
                            asset.setId(UUID.randomUUID().toString());
                            asset.setSpaceId("pwv4o9e408cc");
                            asset.setVersion(1);

                            newAsset = asset;
                            client
                                .assets()
                                .async()
                                .create(
                                    asset,
                                    new CMACallback<CMAAsset>() {
                                        @Override protected void onSuccess(CMAAsset result) {
                                            // The fetched asset will be the parameter of this method.
                                            processAsset(client, result.getId());
                                        }

                                        @Override protected void onFailure(RuntimeException exception) {
                                            // An error occurred! Inform the user.
                                            new AlertDialog.Builder(context)
                                                .setTitle("Contentful Error")
                                                .setMessage("Could not create a new asset." +
                                                    "\n\nReason: " + exception.toString())
                                                .show();

                                            super.onFailure(exception);
                                        }
                                    }
                                );
                        }

                        @Override protected void onFailure(RuntimeException exception) {
                            // An error occurred! Inform the user.
                            new AlertDialog.Builder(context)
                                .setTitle("Contentful Error")
                                .setMessage("Could not upload a new file." +
                                    "\n\nReason: " + exception.toString())
                                .show();

                            super.onFailure(exception);
                        }
                    });

        }
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            return Drawable.createFromPath(url);
        } catch (Exception e) {
            return null;
        }
    }
    private String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor = resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    private File createTempFile(String name) {
        File file = null;
        try {
            file = File.createTempFile(name, null, context.getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private File saveContentToFile(Uri uri, File file, ContentResolver resolver) {
        try {
            InputStream stream = resolver.openInputStream(uri);
            BufferedSource source = Okio.buffer(Okio.source(stream));
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(source);
            sink.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    private void processAsset(CMAClient client, String assetId) {
        client
            .assets()
            .async()
            .process(
                newAsset,
                "en-US",
                new CMACallback<Integer>() {
                    @Override protected void onSuccess(Integer result) {
                        // Done, your asset will be processed
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                publishAsset(client);
                            }
                        }, 5000);
                    }

                    @Override protected void onFailure(RuntimeException exception) {
                        // An error occurred! Inform the user.
                        new AlertDialog.Builder(context)
                            .setTitle("Contentful Error")
                            .setMessage("Could not start processing the asset." +
                                "\n\nReason: " + exception.toString())
                            .show();

                        super.onFailure(exception);
                    }
                }
            );
    }
    private void publishAsset(CMAClient client) {
        newAsset.setVersion(2);
        client
            .assets()
            .async()
            .publish(
                newAsset,
                new CMACallback<CMAAsset>() {
                    @Override protected void onSuccess(CMAAsset result) {
                        // Done, your published asset will be the parameter of this callback.
                        new AlertDialog.Builder(context)
                            .setTitle("Contentful")
                            .setMessage("Asset published.")
                            .show();

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }

                    @Override protected void onFailure(RuntimeException exception) {
                        // An error occurred! Inform the user.
                        new AlertDialog.Builder(context)
                            .setTitle("Contentful Error")
                            .setMessage("Asset could not be published." +
                                "\n\nReason: " + exception.toString())
                            .show();

                        super.onFailure(exception);
                    }
                }
            );
    }
}
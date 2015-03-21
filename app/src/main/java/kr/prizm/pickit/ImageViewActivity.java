package kr.prizm.pickit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by koma on 15. 3. 21..
 */

public class ImageViewActivity extends Activity {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_imageview);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bundle extras = getIntent().getExtras();
        byte[] food = extras.getByteArray("picture");
        Bitmap fo = BitmapFactory.decodeByteArray(food, 0, food.length);
        imageView.setImageBitmap(fo);

        /*

        Bitmap bmp = (Bitmap) extras.getParcelable("image");

        imageView.setUri
        */
    }
}

package com.pikon.codesgenerator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class QrGeneratorFrag extends Fragment {

    private View fragment;
    private EditText input;
    private ImageView ivBarcode;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.generator_frag, container, false );
        fragment = v;
        input = (EditText)v.findViewById( R.id.etBarInput);
        ivBarcode = (ImageView)v.findViewById( R.id.ivGeneratedBarcode );
        String text = input.getText().toString();

        ((Button)v.findViewById( R.id.btnBarGenerate )).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBarCode( ivBarcode, input.getText().toString() );
            }
        });
        newBarCode( ivBarcode, text );
        return v;
    }

    private void newBarCode( ImageView iv, String text ){
        iv.setImageBitmap( this.generate( text ) );
    }

    private Bitmap generate( String text ){
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bm = writer.encode( text, BarcodeFormat.QR_CODE, 400, 400 );
            int w = bm.getWidth();
            int h = bm.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    pixels[y * w + x] = bm.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return Bitmap.createBitmap( 100, 150, Bitmap.Config.ARGB_8888 );
    }
}

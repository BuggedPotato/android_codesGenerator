package com.pikon.codesgenerator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Arrays;


public class BarGeneratorFrag extends Fragment {

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
        iv.setImageBitmap( generate( text ) );
    }

    private Bitmap generate( String text ){
        MultiFormatWriter writer = new MultiFormatWriter();
        String data = Uri.encode( text, "utf-8" );
        int w = 800;
        try {
            BitMatrix bm = writer.encode( data, BarcodeFormat.CODE_128, w, 1 );
            int bmWidth = bm.getWidth();
            Bitmap imageBitmap = Bitmap.createBitmap( bmWidth, w, Bitmap.Config.ARGB_8888 );

            for( int i = 0; i < bmWidth; i++ ){
                int[] cols = new int[w];
                Arrays.fill( cols, bm.get(i, 0) ? Color.BLACK : Color.WHITE );
                imageBitmap.setPixels( cols, 0, 1, i, 0, 1, w / 2 );
            }
            return imageBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return Bitmap.createBitmap( 100, 150, Bitmap.Config.ARGB_8888 );
    }
}

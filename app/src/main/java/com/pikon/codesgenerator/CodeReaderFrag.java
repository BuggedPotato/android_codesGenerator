package com.pikon.codesgenerator;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class CodeReaderFrag extends Fragment {

    TextView tvCodeScan;
    ScanOptions options;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.reader_frag, container, false );
        tvCodeScan = (TextView) v.findViewById( R.id.tvCodeScan );
        options = new ScanOptions();

        ( (Button) v.findViewById( R.id.btnScan ) ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodeLauncher.launch( options );
            }
        });
        ( (Button) v.findViewById( R.id.btnCopy ) ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipboard( tvCodeScan.getText().toString() );
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            requestPermissions( new String[] {Manifest.permission.CAMERA}, 420);
        } else {
            barcodeLauncher.launch( options );
        }
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 420) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                barcodeLauncher.launch( options );
            } else {
                Log.e( "DEBUG", "camera denied" );
            }
        }
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(getActivity(), "Scan cancelled", Toast.LENGTH_LONG).show();
                } else {
                    tvCodeScan.setText( result.getContents() );
                }
            });

    private void copyToClipboard( String content ){
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText( "readCode", content );
        clipboard.setPrimaryClip( clip );
        Toast.makeText(getActivity(), "Copied!", Toast.LENGTH_SHORT).show();
    }
}

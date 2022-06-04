package de.androidcrypto.nfcndefvcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button readNfcNdefVcard, writeNfcNdefVcard;
    Intent readNfcNdefVcardIntent, writeNfcNdefVcardIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readNfcNdefVcard = findViewById(R.id.btnReadNfcNdefVcard);
        writeNfcNdefVcard = findViewById(R.id.btnWriteNfcNdefVcard);
        readNfcNdefVcardIntent = new Intent(MainActivity.this, ReadNfcNdefVcard.class);
        writeNfcNdefVcardIntent = new Intent(MainActivity.this, WriteNfcNdefVcard.class);

        readNfcNdefVcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(readNfcNdefVcardIntent);
            }
        });

        writeNfcNdefVcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(writeNfcNdefVcardIntent);
            }
        });
    }
}
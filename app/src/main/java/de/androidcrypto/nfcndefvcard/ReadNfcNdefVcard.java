package de.androidcrypto.nfcndefvcard;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.FormattedName;
import ezvcard.property.StructuredName;

public class ReadNfcNdefVcard extends AppCompatActivity implements NfcAdapter.ReaderCallback {

    TextView ndefMessage, vcardData;
    String ndefMessageString;

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_nfc_ndef_vcard);
        ndefMessage = findViewById(R.id.tvNdefMessage);
        vcardData = findViewById(R.id.tvVcardMessage);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mNfcAdapter != null) {
            Bundle options = new Bundle();
            // Work around for some broken Nfc firmware implementations that poll the card too fast
            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250);

            // Enable ReaderMode for all types of card and disable platform sounds
            // the option NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK is NOT set
            // to get the data of the tag afer reading
            mNfcAdapter.enableReaderMode(this,
                    this,
                    NfcAdapter.FLAG_READER_NFC_A |
                            NfcAdapter.FLAG_READER_NFC_B |
                            NfcAdapter.FLAG_READER_NFC_F |
                            NfcAdapter.FLAG_READER_NFC_V |
                            NfcAdapter.FLAG_READER_NFC_BARCODE |
                            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
                    options);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableReaderMode(this);
    }

    // This method is run in another thread when a card is discovered
    // !!!! This method cannot cannot direct interact with the UI Thread
    // Use `runOnUiThread` method to change the UI from this method
    @Override
    public void onTagDiscovered(Tag tag) {
        // Read and or write to Tag here to the appropriate Tag Technology type class
        // in this example the card should be an Ndef Technology Type

        Ndef mNdef = Ndef.get(tag);

        if (mNdef == null) {
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(),
                        "mNdef is null",
                        Toast.LENGTH_SHORT).show();
            });
        }

        // Check that it is an Ndef capable card
        if (mNdef != null) {

            // If we want to read
            // As we did not turn on the NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
            // We can get the cached Ndef message the system read for us.

            NdefMessage mNdefMessage = mNdef.getCachedNdefMessage();
            ndefMessageString = mNdefMessage.toString();

            // Make a Sound
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, 10));
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
            }

            NdefRecord[] record = mNdefMessage.getRecords();
            String ndefContent = "";
            int ndefRecordsCount = record.length;
            ndefContent = "nr of records: " + ndefRecordsCount + "\n";
            // Success if got to here
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(),
                        "Read from NFC success, number of records: " + ndefRecordsCount,
                        Toast.LENGTH_SHORT).show();
            });

            if (ndefRecordsCount > 0) {
                for (int i = 0; i < ndefRecordsCount; i++) {
                    short ndefInf = record[i].getTnf();
                    byte[] ndefType = record[i].getType();
                    byte[] ndefPayload = record[i].getPayload();

                    ndefContent = ndefContent + "rec " + i + " inf: " + ndefInf +
                            " type: " + bytesToHex(ndefType) +
                            " payload: " + bytesToHex(ndefPayload) +
                            " \n" + new String(ndefPayload) + " \n";
                    String finalNdefContent = ndefContent;
                    runOnUiThread(() -> {
                        ndefMessage.setText(finalNdefContent);
                    });

                    // now we are parsing the vcard
                    String ndefPayloadString = new String(ndefPayload);
                    System.out.println("*** ndefPayloadString: " + ndefPayloadString);
                    VCard vcard = Ezvcard.parse(ndefPayloadString).first();
                    StructuredName structuredName = vcard.getStructuredName();
                    FormattedName formattedName = vcard.getFormattedName();
                    String firstName = structuredName.getGiven();
                    String lastName = structuredName.getFamily();
                    String prefixName = structuredName.getPrefixes().get(0); // just get the first one
                    String suffixName = structuredName.getSuffixes().get(0); // just get the first one
                    String finalVCardString =   prefixName + " " + firstName + " " + lastName + " " + suffixName;
                    runOnUiThread(() -> {
                        vcardData.setText(finalVCardString);
                    });
                }
            }

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
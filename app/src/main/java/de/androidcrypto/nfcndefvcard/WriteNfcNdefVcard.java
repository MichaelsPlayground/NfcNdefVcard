package de.androidcrypto.nfcndefvcard;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import ezvcard.Ezvcard;
import ezvcard.VCardVersion;

public class WriteNfcNdefVcard extends AppCompatActivity implements NfcAdapter.ReaderCallback {

    EditText etName, etOrganisation, etTelefoneBusiness;
    TextView ndefMessage;

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_nfc_ndef_vcard);
        etName = findViewById(R.id.etWriteName);
        etOrganisation = findViewById(R.id.etWriteOrganisation);
        etTelefoneBusiness = findViewById(R.id.etWriteTelBusiness);
        ndefMessage = findViewById(R.id.tvNdefMessage);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }
    @Override
    protected void onResume() {
        super.onResume();

        if(mNfcAdapter!= null) {
            Bundle options = new Bundle();
            // Work around for some broken Nfc firmware implementations that poll the card too fast
            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250);

            // Enable ReaderMode for all types of card and disable platform sounds
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
        if(mNfcAdapter!= null)
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

        // Check that it is an Ndef capable card
        if (mNdef != null) {

            // If we want to read
            // As we did not turn on the NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
            // We can get the cached Ndef message the system read for us.

            //NdefMessage mNdefMessage = mNdef.getCachedNdefMessage();
            //ndefMessageString = mNdefMessage.toString();

            // Or if we want to write a Ndef message
            // Create a Ndef text record
            String timeNow = getTimestampFormatted();
            runOnUiThread(() -> {
                etOrganisation.setText(timeNow);
            });
            byte[] vcardData = new byte[0];
            String givenName = "Maria";
            //String givenName = "Vorname";
            String familyName = "Schraa";
            //String familyName = "Nach + " + getTimestampFormatted();
            String prefixName = "";
            String suffixName = "";
            String organization = "Insanto Seniorenresidenz Wesel GmbH";
            String workTitle = "Einrichtungsleitung";
            String workAddressStreet = "Dinslakener Landstr. 15";
            String workAddressPostcode = "46483";
            String workAddressCity = "Wesel";
            String workAddressCountry = "Deutschland";
            String workTelephoneNumber = "0281475740";
            String workFaxNumber = "028147574499";
            String workTelephoneCellNumber = "01621079892";
            String workEmail = "EL-WEL@insanto.de";
            String workUrl = "https://insanto.de";
            //String workGeoCoordinate = "51.653257241208195, 6.625781480567649";

            /*
            String orga = "Testorga";
            String addr = "Dinslakener Landstr. 15, 46483 Wesel";
            String telBusi = "0281475740";
            String emailBusi = "EL-WEL@insanto.de";
            String url = "https://insanto.de";
            */


            /*
            String givenName,
            String familyName,
            String prefixName, // like dr
            String suffixName,
            String organization,
            String workTitle,
            String workAddressStreet,
            String workAddressPostcode,
            String workAddressCity,
            String workAddressCountry,
            String workTelephoneNumber,
            String workFaxNumber,
            String workTelephoneCellNumber,
            String workEmail,
            String workUrl,
            String workGeoCoordinate // from google maps 51.653257241208195, 6.625781480567649
             */

            //String vcardString = generateVcard(name, orga, "", telBusi, emailBusi, url);
            String vcardString = "";

            //vcardString = Ezvcard.write(CreateVCard.createSampleVCard()).version(VCardVersion.V2_1).go();
            vcardString = Ezvcard.write(CreateVCard.createWorkVCard(givenName, familyName, prefixName, suffixName, organization, workTitle, workAddressStreet, workAddressPostcode, workAddressCity, workAddressCountry, workTelephoneNumber, workFaxNumber, workTelephoneCellNumber, workEmail, workUrl)).version(VCardVersion.V2_1).go();
            System.out.println("VCard sample\n" + vcardString);

            String finalVcardString = vcardString;
            runOnUiThread(() -> {
                ndefMessage.setText(finalVcardString);
            });

            // data is encoded in US-ASCII:
            vcardData = vcardString.getBytes(StandardCharsets.US_ASCII);

            NdefRecord ndefRecordVcard = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, ("text/vcard").getBytes(StandardCharsets.US_ASCII), new byte[] {}, vcardData);

            NdefRecord ndefRecord1Text = NdefRecord.createTextRecord("en", String.valueOf(timeNow) +
                    timeNow);
            // Create a Ndef Android application record
            String packageName = "de.androidcrypto.ntagapp";
            NdefRecord ndefRecord2Aar = NdefRecord.createApplicationRecord(packageName);
            // Create a Ndef URI record
            String uriString = "http://androidcrypto.bplaced.net";
            NdefRecord ndefRecord3Uri = NdefRecord.createUri(uriString);
            // Add to a NdefMessage
            NdefMessage mMsg = new NdefMessage(ndefRecordVcard); // this gives exact 1 message with 1 record
            //NdefMessage mMsg = new NdefMessage(ndefRecord1Text, ndefRecord3Uri, ndefRecord2Aar); // gives 1 message with 3 records
            // Catch errors

            try {
                mNdef.connect();
                mNdef.writeNdefMessage(mMsg);
                // Success if got to here
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(),
                            "Write to NFC Success",
                            Toast.LENGTH_SHORT).show();
                });
                // Make a Sound
                if (Build.VERSION.SDK_INT >= 26) {
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150, 10));
                } else {
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(150);
                }

                // if the NDEF Message to write is malformed
            } catch (TagLostException e) {
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(),
                            "TagLostException: " + e,
                            Toast.LENGTH_SHORT).show();
                });
                // Tag went out of range before operations were complete
            } catch (IOException e) {
                // if there is an I/O failure, or the operation is cancelled
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(),
                            "IOException: " + e,
                            Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(),
                            "Exception: " + e,
                            Toast.LENGTH_SHORT).show();
                });
            } finally {
                // Be nice and try and close the tag to
                // Disable I/O operations to the tag from this TagTechnology object, and release resources.
                try {
                    mNdef.close();
                } catch (IOException e) {
                    // if there is an I/O failure, or the operation is cancelled
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(),
                                "IOException: " + e,
                                Toast.LENGTH_SHORT).show();
                    });
                }
            }

            // Make a Sound
            // Make a Sound

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private String generateVcardWork21(String givenName,
                                       String familyName,
                                       String prefixName,
                                       String suffixName,
                                       String organization,

                                 String address, String telBusiness,
                                 String emailBusiness,
                                 String url) {
        String vString = "BEGIN:VCARD" + "\n" + "VERSION:2.1" + "\n";

        // as a family name is neccessary I use a dummy name if no name is given
        if (familyName.equals("")) familyName = "No name provided";
        vString = vString + "N:" + familyName + ";" + givenName + ";" +
                ";" + prefixName + ";" + suffixName;
        vString = vString + "FN:" + prefixName.trim() + " " + givenName.trim() +
                " " +  "\n";
        if (!organization.equals("")) {
            vString = vString + "ORG:" + organization + ";\n";
        }
        // address is street, \, plz place ;;;;;
        if (!address.equals("")) {
            vString = vString + "ADR:;;" + address + "\n";
        }
        if (!telBusiness.equals("")) {
            vString = vString + "TEL:" + telBusiness + "\n";
        }
        if (!emailBusiness.equals("")) {
            vString = vString + "EMAIL:" + emailBusiness + "\n";
        }
        if (!url.equals("")) {
            vString = vString + "URL:" + url + "\n";
        }
        vString = vString + "END:VCARD";
        return vString;
    }

    private static String getTimestampFormatted() {
        // gives atimestamp in this format: yyyy-MM-dd HH:mm:ss
        // java.time is available from SDK 26+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dtf = null;
            dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            return dtf.format(now);
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            return dateFormat.format(cal.getTime());
        }
    }

}
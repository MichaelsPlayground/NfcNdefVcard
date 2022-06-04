package de.androidcrypto.nfcndefvcard;

import ezvcard.VCard;
import ezvcard.parameter.AddressType;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.Gender;
import ezvcard.property.Kind;
import ezvcard.property.Revision;
import ezvcard.property.StructuredName;
import ezvcard.property.Timezone;
import ezvcard.property.Uid;

public class CreateVCard {
    // this class will create a VCard using library ez-vcard
    // https://github.com/mangstadt/ez-vcard

    public static VCard createVCard() {
        VCard vcard = new VCard();

        vcard.setKind(Kind.individual());

        vcard.setGender(Gender.male());

        vcard.addLanguage("en-US");

        StructuredName n = new StructuredName();
        n.setFamily("Doe");
        n.setGiven("Jonathan");
        n.getPrefixes().add("Mr");
        vcard.setStructuredName(n);

        vcard.setFormattedName("Jonathan Doe");

        vcard.setNickname("John", "Jonny");

        vcard.addTitle("Widget Engineer");

        vcard.setOrganization("Acme Co. Ltd.", "Widget Department");

        Address adr = new Address();
        adr.setStreetAddress("123 Wall St.");
        adr.setLocality("New York");
        adr.setRegion("NY");
        adr.setPostalCode("12345");
        adr.setCountry("USA");
        adr.setLabel("123 Wall St.\nNew York, NY 12345\nUSA");
        adr.getTypes().add(AddressType.WORK);
        vcard.addAddress(adr);

        adr = new Address();
        adr.setStreetAddress("123 Main St.");
        adr.setLocality("Albany");
        adr.setRegion("NY");
        adr.setPostalCode("54321");
        adr.setCountry("USA");
        adr.setLabel("123 Main St.\nAlbany, NY 54321\nUSA");
        adr.getTypes().add(AddressType.HOME);
        vcard.addAddress(adr);

        vcard.addTelephoneNumber("1-555-555-1234", TelephoneType.WORK);
        vcard.addTelephoneNumber("1-555-555-5678", TelephoneType.WORK, TelephoneType.CELL);

        vcard.addEmail("johndoe@hotmail.com", EmailType.HOME);
        vcard.addEmail("doe.john@acme.com", EmailType.WORK);

        vcard.addUrl("http://www.acme-co.com");

        vcard.setCategories("widgetphile", "biker", "vCard expert");

        vcard.setGeo(37.6, -95.67);

        java.util.TimeZone tz = java.util.TimeZone.getTimeZone("America/New_York");
        vcard.setTimezone(new Timezone(tz));

        /*
        File file = new File("portrait.jpg");
        Photo photo = null;
        try {
            photo = new Photo(file, ImageType.JPEG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        vcard.addPhoto(photo);

        file = new File("pronunciation.ogg");
        Sound sound = null;
        try {
            sound = new Sound(file, SoundType.OGG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        vcard.addSound(sound);
        */

        vcard.setUid(Uid.random());

        vcard.setRevision(Revision.now());

        return vcard;
    }
}

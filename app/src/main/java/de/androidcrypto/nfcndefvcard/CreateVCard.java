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

    public static VCard createWorkVCard(
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
            String workUrl
            //String workGeoCoordinate // from google maps 51.653257241208195, 6.625781480567649
    ) {
        VCard vcard = new VCard();
        //vcard.setKind(Kind.individual());
        //vcard.setGender(Gender.male());
        //vcard.addLanguage("en-US");
        vcard.addLanguage("de-DE");
        StructuredName n = new StructuredName();
        n.setFamily(familyName);
        n.setGiven(givenName);
        n.getPrefixes().add(prefixName);
        vcard.setStructuredName(n);
        vcard.setFormattedName(buildFormattedName(givenName, familyName, prefixName, suffixName));
        //vcard.setNickname("John", "Jonny");
        vcard.addTitle(workTitle);
        vcard.setOrganization(organization);
        Address adr = new Address();
        adr.setStreetAddress(workAddressStreet);
        adr.setLocality(workAddressCity);
        //adr.setRegion("NY");
        adr.setPostalCode(workAddressPostcode);
        adr.setCountry(workAddressCountry);
        //adr.setLabel("123 Wall St.\nNew York, NY 12345\nUSA");
        adr.getTypes().add(AddressType.WORK);
        vcard.addAddress(adr);
        /*
        adr = new Address();
        adr.setStreetAddress("123 Main St.");
        adr.setLocality("Albany");
        adr.setRegion("NY");
        adr.setPostalCode("54321");
        adr.setCountry("USA");
        adr.setLabel("123 Main St.\nAlbany, NY 54321\nUSA");
        adr.getTypes().add(AddressType.HOME);
        vcard.addAddress(adr);
        */
        vcard.addTelephoneNumber(workTelephoneNumber, TelephoneType.WORK);
        vcard.addTelephoneNumber(workFaxNumber, TelephoneType.WORK, TelephoneType.FAX);
        vcard.addTelephoneNumber(workTelephoneCellNumber, TelephoneType.WORK, TelephoneType.CELL);
        //vcard.addEmail("johndoe@hotmail.com", EmailType.HOME);
        vcard.addEmail(workEmail, EmailType.WORK);
        vcard.addUrl(workUrl);

        /* data is not shown in Samsung contact data
        // sample 51.653257241208195, 6.625781480567649
        double workGeoLatitude = 0, workGeoLongitude = 0;
        // try to get the coordinates from the string
        // check for a comma
        String[] geoParts = workGeoCoordinate.split(",");
        if (geoParts.length == 2) {
            workGeoLatitude = Double.parseDouble(geoParts[0].trim());
            workGeoLongitude = Double.parseDouble(geoParts[1].trim());
            vcard.setGeo(workGeoLatitude, workGeoLongitude);
        }*/
        return vcard;
    }

    private static String buildFormattedName(String givenName,
                                             String familyName,
                                             String prefixName, // like dr
                                             String suffixName) {
        String returnString = "";
        if (!prefixName.equals("")) returnString = returnString + prefixName + ". ";
        if (!givenName.equals("")) returnString = returnString + givenName + " ";
        if (!familyName.equals("")) {
            returnString = returnString + familyName + " ";
        } else {
            returnString = returnString + "No Name" + " ";
        }
        if (!suffixName.equals("")) returnString = returnString + suffixName;
        return returnString;
    }

    public static VCard createSampleVCard() {
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

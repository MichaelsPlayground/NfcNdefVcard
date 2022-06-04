# NFC NDEF VCard reader and writer

NFC basics: https://developer.android.com/guide/topics/connectivity/nfc/nfc

A VCard is defined in: https://developer.android.com/guide/topics/connectivity/nfc/nfc#mime

Generell information about NDEF class: https://developer.android.com/reference/android/nfc/tech/Ndef

VCard (or VCF = Virtual Contact File) in Wikipedia: https://en.wikipedia.org/wiki/VCard

For working with VCards - better use a library like: https://github.com/mangstadt/ez-vcard

Wiki: https://github.com/mangstadt/ez-vcard/wiki

https://mvnrepository.com/artifact/com.googlecode.ez-vcard/ez-vcard

// https://mvnrepository.com/artifact/com.googlecode.ez-vcard/ez-vcard
implementation group: 'com.googlecode.ez-vcard', name: 'ez-vcard', version: '0.11.3'



```plaintext
String str =
"BEGIN:VCARD\r\n" +
"VERSION:4.0\r\n" +
"N:Doe;Jonathan;;Mr;\r\n" +
"FN:John Doe\r\n" +
"END:VCARD\r\n";
VCard vcard = Ezvcard.parse(str).first();
String fullName = vcard.getFormattedName().getValue();
String lastName = vcard.getStructuredName().getFamily();

VCard vcard = new VCard();
StructuredName n = new StructuredName();
n.setFamily("Doe");
n.setGiven("Jonathan");
n.getPrefixes().add("Mr");
vcard.setStructuredName(n);
vcard.setFormattedName("John Doe");
String str = Ezvcard.write(vcard).version(VCardVersion.V4_0).go();
```


Example VCard from real Android device:

````plaintext
BEGIN:VCARD
VERSION:2.1
N:Marion;Schrage;;;
FN:Schrage Marion
TEL;CELL:015757061987
TEL;WORK:+49281471230
TEL;WORK;FAX:+4928147123499
TEL;X-CUSTOM(CHARSET=UTF-8,ENCODING=QUOTED-PRINTABLE,=41=72=62=65=69=74=20=4D=6F=62=69=6C):+491621079892
TEL;HOME;PREF:028144459543
EMAIL;HOME:marion.schrage@gmy.net
EMAIL;WORK:MANAGER@inhouse.de
EMAIL;X-CUSTOM(CHARSET=UTF-8,ENCODING=QUOTED-PRINTABLE,=41=72=62=65=69=74=20=48=61=6E=64=79):inhouse@gmail.net
ADR;HOME:;;Marktstr. 5;Haminkeln;;45678;
ADR;WORK:;;Dinslakener Landstrasse 33;Wesel;NRW;46483;Deutschland
ORG:Inhouse Wesel GmbH
TITLE:Senior Manager
END:VCARD
````
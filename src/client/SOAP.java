package client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.*;
import prg.A;
import prg.P;
import prg.V;

public class SOAP {

    public static final String SHA256_ALGORITHM = "SHA-256";
    public static String url = "http://integration-rarus-master.ammopay.ru/ws/";
    public static String serverURI = "http://ammopay.com/api/contract/soap";
    private static String timestamp;
    private static String login;
    private static String pass;
    private static String ProxyUser;
    private static String ProxyPass;
    private static String ProxyIP;
    private static int ProxyPort;
    private static boolean ProxyFlag;

    public SOAP() {
        P.SQLEXECT("select nvl(max(sys_val),'null_value') sys_val from ri_system where sys_var = 'A_CREDIT_CONNECT'", "A_CRED");
        if (A.GETVALS("A_CRED.sys_val").equals("null_value")) {
            P.MESSERR("Не настроено подключение к серверу Ammopay!");
            return;
        }
        String[] arr = A.GETVALS("A_CRED.sys_val").split(",");
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
        this.ProxyFlag = arr[0].equals("T");
        this.ProxyUser = arr[1];
        this.ProxyPass = arr[2];
        this.ProxyIP = arr[3];
        this.ProxyPort = Integer.parseInt(arr[4]);
        this.login = arr[5];
        this.pass = arr[6];
        this.url = arr[7];
        this.serverURI = arr[8];
    }

    ;

    public String soapCall(String method, String... xml) throws Exception {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyIP, ProxyPort));
        URL endpoint = new URL(null, url, new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL url) throws IOException {
                URL clone = new URL(url.toString());

                URLConnection connection = null;

                connection = clone.openConnection(proxy);

                return connection;
            }
        });
// Create SOAP Connection
//        doTrustToCertificates();
        // Create SSL context and trust all certificates
        if (V.httpsConnection == null) {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] trustAll
                    = new TrustManager[]{new TrustAllCertificates()};
            sslContext.init(null, trustAll, new java.security.SecureRandom());
            // Set trust all certificates context to HttpsURLConnection
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            // Open HTTPS connection
            URL url2 = new URL(url);
//        V.httpsConnection = null;

            if (ProxyFlag) {
                V.httpsConnection = (HttpsURLConnection) endpoint.openConnection();
            } else {
                V.httpsConnection = (HttpsURLConnection) url2.openConnection();
            }

            // Trust all hosts
            V.httpsConnection.setHostnameVerifier(new TrustAllHosts());
            // Connect
            V.httpsConnection.connect();
        }

        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();

        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Send SOAP Message to SOAP Server
        SOAPMessage soapResponse;
        if (ProxyFlag) {
            System.out.println("Proxy");
            soapResponse = soapConnection.call(createSOAPRequest(method, xml), endpoint);
        } else {
            System.out.println("No proxy");
            soapResponse = soapConnection.call(createSOAPRequest(method, xml), url);
        }
//        V.httpsConnection.disconnect();
        System.out.println("success");

        // print SOAP Response
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapResponse.writeTo(out);

        String response = new String(out.toByteArray());

        String response2 = "1";
        try {
            response2 = new String(response.getBytes("Cp1251"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
        char[] chr = response2.toCharArray();
        for (int i = 0; i < chr.length; i++) {
            if (chr[i] >= 'А' && chr[i] <= 'я') {
                response = response2;
                break;
            }
        }

        P.SQLUPDATE("insert into history ( BIT_STATUS,DATE_S1,PC_S,SQL_T,TIME_S,USER_L,USER_S) values ('T',sysdate,' ',q'[" + (response.length() > 3000 ? response.substring(0, 3000) : response) + "]',1,' ',' ')");
//        soapResponse.writeTo(System.out);
        System.out.println(response.replaceAll(">", ">\n"));

        soapConnection.close();
        return response;
    }

    private static SOAPMessage createSOAPRequest(String method, String... xml) throws Exception {
        System.out.println("a1");
        /*
         <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
         <SOAP-ENV:Body>
        
         <createTransactionRequest xmlns="http://integration-rarus-master.ammopay.ru/ws/">
         <authentication>
         <merchant_id>1</merchant_id>
         <code>jCcKB1NIy01EWsbFJzhbdWaM5AC0jhA+1Jbk9afma+A=</code>
         <timestamp>2016-10-04T10:34:08.075+03:00</timestamp>
         </authentication>
         <code>2832942</code>
         <serial>4545</serial>
         <number>000002</number>
         <amount>4000</amount>
         </createTransactionRequest>
         </SOAP-ENV:Body>
         </SOAP-ENV:Envelope>
         */
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement(method, "", serverURI);
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("authentication");
        SOAPElement soapBodyElem2 = soapBodyElem1.addChildElement("merchant_id");
        soapBodyElem2.addTextNode(login);
        SOAPElement soapBodyElem3 = soapBodyElem1.addChildElement("code");
        soapBodyElem3.addTextNode(generate(login, pass));
        SOAPElement soapBodyElem4 = soapBodyElem1.addChildElement("timestamp");
        soapBodyElem4.addTextNode(timestamp);
//        if (method.equals("returnTransactionRequest")|| method.equals("createTransactionSignatureRequest") || method.equals("createTransactionOfferRequest")) {
        if (!method.equals("creditSettingsRequest")) {
            SOAPElement soapBodyElem5 = soapBodyElem.addChildElement("properties");
            SOAPElement soapBodyElem6 = soapBodyElem5.addChildElement("property");
            soapBodyElem6.addAttribute(envelope.createName("key"), "check.width");
            soapBodyElem6.addTextNode("34");

            SOAPElement soapBodyElem7 = soapBodyElem5.addChildElement("property");
            soapBodyElem7.addAttribute(envelope.createName("key"), "printer");
            soapBodyElem7.addTextNode("true");
            System.out.println("a2");

        }
        //xml
        for (int i = 0; i < xml.length; i += 2) {
            SOAPElement soapBodyElemX = soapBodyElem.addChildElement(xml[i]);
            soapBodyElemX.addTextNode(xml[i + 1]);
        }

        System.out.println("a3");
//        SOAPElement soapBodyElem5 = soapBodyElem.addChildElement("code");
//        soapBodyElem5.addTextNode("2832942");
//        SOAPElement soapBodyElem6 = soapBodyElem.addChildElement("serial");
//        soapBodyElem6.addTextNode("4545");
//        SOAPElement soapBodyElem7 = soapBodyElem.addChildElement("number");
//        soapBodyElem7.addTextNode("000002");
//        SOAPElement soapBodyElem8 = soapBodyElem.addChildElement("amount");
//        soapBodyElem8.addTextNode("4000");
        MimeHeaders headers = soapMessage.getMimeHeaders();
//        headers.addHeader("SOAPAction", serverURI + "create-transaction");
//        headers.addHeader("Host", "http://integration-rarus-master.ammopay.ru/ws/");

        soapMessage.saveChanges();
        System.out.println("a4");

        /* Print the request message */
//        soapMessage.writeTo(System.out);
//        System.out.println();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapMessage.writeTo(out);

        String request = new String(out.toByteArray());
//        soapResponse.writeTo(System.out);
        System.out.println(request.replaceAll(">", ">\n"));
        System.out.println();
        System.out.println("a5");
        return soapMessage;
    }

    public static String generate(String login, String password) throws Exception {
        GregorianCalendar c = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
//        timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(date);
        c.setTime(date);
        XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        timestamp = "" + calendar;
        return getSHA256(password, login + calendar);
    }

    public static String getSHA256(String key, String text) throws Exception {
        MessageDigest md = MessageDigest.getInstance(SHA256_ALGORITHM);
        String hashString = text + key;
        md.update(hashString.getBytes("UTF-8"));
//        return new String(Base64.encodeBase64(md.digest()));
        return DatatypeConverter.printBase64Binary(md.digest());
    }

    static public void doTrustToCertificates() throws Exception {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    return;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    return;
                }
            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    private static class TrustAllCertificates implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private static class TrustAllHosts implements HostnameVerifier {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}

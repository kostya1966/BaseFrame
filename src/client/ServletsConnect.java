/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import baseclass.Configs;
import prg.P;
import prg.V;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @author DaN
 */
public class ServletsConnect {

    static {
        disableSslVerification();
    }

    private static void disableSslVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private String ip, port, service, proxyIp, proxyPort, proxyName, proxyPass;
    Boolean proxyFlag;
    private Map<String, String> headers;

    public ServletsConnect() {
        // saveConnectProperties();

        loadConnectProperties();
    }

    public URLConnection getServletConnection(String servletName) throws MalformedURLException, IOException {
        URLConnection con;
        // System.out.println("client.ServletsConnect:\n" + "URL: http://" + ip + ":" + port + "//" + service + "//" + servletName);
        String str_con = "http://" + ip + ":" + port + "//" + service + "//" + servletName;
        URL urlServlet = new URL(str_con); // 192.168.2.121:8084/Service_BW/SendToSap
        //URL urlServlet = new URL(  ip + ":" + port + "/" + service + "/" + servletName); // 192.168.2.121:8084/Service_BW/SendToSap
        System.out.println(str_con);
        Authenticator authenticator = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication(proxyName,
                        proxyPass.toCharArray()));
            }
        };
        Authenticator.setDefault(authenticator);
        if (proxyFlag == true) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, Integer.parseInt(proxyPort)));
            con = urlServlet.openConnection(proxy);
        } else {
            con = urlServlet.openConnection();
            con.setConnectTimeout(4000);

        }
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
//        con.setRequestProperty("Content-Type", "application/x-java-serialized-object");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        return con;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    private void saveConnectProperties() {
        P.FIRSTSAVECONNPROP();
    }

    // загрузка свойств подключения
    private void loadConnectProperties() {
        Configs proper = new Configs(V.fileNameConServ);
        try {
            ip = proper.getProperty("$" + "IP", "");
            port = proper.getProperty("$" + "PORT", "");
            service = proper.getProperty("$" + "SERVICE", "");
            proxyFlag = Boolean.valueOf(proper.getProperty("$" + "PROXYFLAG"));
            proxyIp = proper.getProperty("$" + "PROXYIP");
            proxyPort = proper.getProperty("$" + "PROXYPORT");
            proxyName = proper.getProperty("$" + "PROXYNAME");
            proxyPass = proper.getProperty("$" + "PROXYPASS");

        } catch (Exception e) {
            System.out.println("Не найдены свойства подключения!");
            saveConnectProperties();
            JOptionPane.showMessageDialog(null, "Не найдены свойства подключения!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
        }
    }

    //KAA простое выполнение сервлета по целому URL через GET-запрос. например - http://192.168.2.121:8084/Hybris-ImpEx/HybrisImpExServlet?product=5465
    //возвращает ответ в виде строки
    public String callServlet(String ip, String port, String url) throws MalformedURLException, IOException {
        HttpURLConnection con;
        URL urlServlet = new URL("http://" + ip + ":" + port + "/" + url);
        Authenticator authenticator = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication(proxyName,
                        proxyPass.toCharArray()));
            }
        };
        Authenticator.setDefault(authenticator);
        if (proxyFlag == true) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, Integer.parseInt(proxyPort)));
            con = (HttpURLConnection) urlServlet.openConnection(proxy);
        } else {
            con = (HttpURLConnection) urlServlet.openConnection();
//            con.setConnectTimeout(4000);
        }

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Accept-Charset", "UTF-8");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        return response.toString();
    }

    public HttpResponse sendRequest(String method, boolean isHttps, String ip, int port, String url, Map<String, String> params) throws IOException {
        return sendRequest(method, isHttps, ip, port, url, params, headers);
    }

    public HttpResponse sendRequest(String method, boolean isHttps, String ip, int port, String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        String httpUrl = (isHttps ? "https" : "http") + "://" + ip + (port == 80 && !isHttps || port == 443 && isHttps ? "" : (":" + port)) + "/" + url;
        return sendRequest(method, httpUrl, params, headers);
    }

    public HttpResponse sendRequest(String method, String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return sendRequest(method, new URL(url), params, headers);
    }


    public HttpResponse sendRequest(String method, URL url, Map<String, String> params, Map<String, String> headers) throws IOException {
        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            data.append(param.getKey()).append("=").append(P.urlDecoder(param.getValue(), StandardCharsets.UTF_8.name())).append("&");
        }

        if ("GET".equalsIgnoreCase(method)) {
            url = new URL(url.toString() + "?" + data.toString());
        }

        HttpURLConnection connection;
        Authenticator authenticator = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication(proxyName,
                        proxyPass.toCharArray()));
            }
        };
        Authenticator.setDefault(authenticator);


        if (proxyFlag) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, Integer.parseInt(proxyPort)));
            connection = (HttpURLConnection) url.openConnection(proxy);
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }

        connection.setRequestMethod(method);
        connection.setRequestProperty("User-Agent", "POS/" + V.VER);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        connection.setRequestProperty("Connection", "keep-alive");
        if (headers != null) {
            for (Map.Entry<String, String> headerProp : headers.entrySet()) {
                connection.setRequestProperty(headerProp.getKey(), headerProp.getValue());
            }
        }
        connection.setUseCaches(false);
        if (!"GET".equalsIgnoreCase(method)) {
            connection.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(data.toString().getBytes());
            }
        }

        System.out.println("\nSending '" + method + "' request to URL : " + url);

        int responseCode = connection.getResponseCode();

        StringBuilder responseBody = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine = in.readLine();
            while (inputLine != null) {
                responseBody.append(inputLine);
                inputLine = in.readLine();
            }
        } catch (Exception ignored) {
        }
        StringBuilder responseError = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
            String inputLine = in.readLine();
            while (inputLine != null) {
                responseError.append(inputLine);
                inputLine = in.readLine();
            }
        } catch (Exception ignored) {
        }


        System.out.printf("Response %d with body: %n%s%nError:%n%s%n", responseCode, responseBody.toString(), responseError.toString());

        return new HttpResponse(responseCode, connection.getHeaderFields(), responseBody.toString(), responseError.toString());
    }
}

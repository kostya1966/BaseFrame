package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Класс для работы с JSON
 *
 * @author KAA
 */
public class JSON {

    JSONObject json = new JSONObject();
    JSONObject jsonTemp = new JSONObject();
    JSONArray jsonArrTemp = new JSONArray();
    ArrayList<Object[]> jsonArr = new ArrayList<>();
    ArrayList<ArrayList<Object[]>> arrTemp = new ArrayList<>();
    public String result = "";

    /**
     * Создание JSON и отправка его по указанному адресу
     *
     * @param url - ссылка, куда отправить json
     * @param arr - массив объектов, по которому будет построен json пример -
     * arr.add(new Object[]{4, "param", "params", "name", "phone", 1, 0}); 4 -
     * уровень в иерархии узлов json "param" - тип объекта (param, obj, array)
     * "params" - имя родительского объекта "name" - имя объекта "phone" -
     * значение объекта "1" - определяет порядковый номер объекта в массиве "0"
     * - определяет порядковый номер массива в родительском массиве
     *
     * пример использования - FIRM.forms.D_INET_BOOK1_F_DPD.dpdOrderCreate()
     *
     * чтобы содать пустой массив params: arr.add(new Object[]{1, "array", null,
     * "params", null, null, null}); arr.add(new Object[]{2, "param", "params",
     * "", "", 0, 0});
     */
    public JSON(String url, ArrayList<Object[]> arr) {
        int max = 0;
        for (int i = 0; i < arr.size(); i++) {
            if ((int) arr.get(i)[0] > max) {
                max = (int) arr.get(i)[0];
            }
        }
        for (int i = max; i > 0; i--) {
            for (int j = 0; j < arr.size(); j++) {
                if (((String) arr.get(j)[1]).equals("obj")) {
                    for (int k = 0; k < arr.size(); k++) {
                        if (i == (int) arr.get(k)[0] && ((String) arr.get(j)[3]).equals(((String) arr.get(k)[2]))) {
                            if (((String) arr.get(k)[1]).equals("param")) {
                                jsonTemp.put((String) arr.get(k)[3], putStringOrNull(arr.get(k)[4] == null ? JSONObject.NULL : arr.get(k)[4]));
                            }
                            if (!((String) arr.get(k)[1]).equals("param")) {
                                for (int x = 0; x < jsonArr.size(); x++) {
                                    if ((int) jsonArr.get(x)[0] == i + 1 && ((String) jsonArr.get(x)[2]).equals((String) arr.get(k)[3])) {
                                        jsonTemp.put((String) arr.get(k)[3], putStringOrNull(jsonArr.get(x)[3]));
                                    }
                                }
                            }
                        }
                    }
                    if (jsonTemp.length() > 0) {
                        jsonArr.add(new Object[]{i, "obj", (String) arr.get(j)[3], jsonTemp, null, arr.get(j)[6]});
                        jsonTemp = new JSONObject();
                    }
                }
                if (((String) arr.get(j)[1]).equals("array")) {
                    for (int k = 0; k < arr.size(); k++) {
                        arrTemp.add(new ArrayList<Object[]>());
                    }
                    for (int k = 0; k < arr.size(); k++) {
                        if (i == (int) arr.get(k)[0] && ((String) arr.get(j)[3]).equals(((String) arr.get(k)[2]))
                                && (arr.get(j)[5] == arr.get(k)[6] || arr.get(j)[5] == null || arr.get(k)[6] == null)) {
                            if (((String) arr.get(k)[1]).equals("param")) {
                                arrTemp.get((int) arr.get(k)[5]).add(new Object[]{((String) arr.get(k)[3]), arr.get(k)[4], arr.get(k)[6]});
                            }
                            if (!((String) arr.get(k)[1]).equals("param")) {
                                for (int x = 0; x < jsonArr.size(); x++) {
                                    if ((int) jsonArr.get(x)[0] == i + 1 && ((String) jsonArr.get(x)[2]).equals((String) arr.get(k)[3])
                                            && (arr.get(k)[6] == jsonArr.get(x)[5] || arr.get(k)[6] == null || jsonArr.get(x)[5] == null)) {
                                        arrTemp.get((int) arr.get(k)[5]).add(new Object[]{((String) arr.get(k)[3]), jsonArr.get(x)[3], arr.get(k)[6]});
                                        jsonArr.remove(x);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    int tempInd = 0;
                    for (int x = 0; x < arrTemp.size(); x++) {
                        if (arrTemp.get(x).size() > 0) {
                            for (int y = 0; y < arrTemp.get(x).size(); y++) {
                                jsonTemp.put((String) arrTemp.get(x).get(y)[0], putStringOrNull(arrTemp.get(x).get(y)[1]));
                                tempInd = (int) arrTemp.get(x).get(y)[2];
                            }
                            jsonArrTemp.put(jsonTemp);
                            jsonTemp = new JSONObject();
                        }
                    }
                    arrTemp = new ArrayList<>();
                    if (jsonArrTemp.length() > 0) {
                        if (jsonArrTemp.get(0).getClass().toString().equals("class org.json.JSONObject")
                                && jsonArrTemp.getJSONObject(0).toString().equals("{\"\":\"\"}")) {
                            jsonArr.add(new Object[]{i, "array", (String) arr.get(j)[3], new JSONArray(), null, arr.get(j)[6]});
                        } else {
                            jsonArr.add(new Object[]{i, "array", (String) arr.get(j)[3], jsonArrTemp, null, arr.get(j)[6]});
                        }

                        jsonArrTemp = new JSONArray();
                    }
                }
            }
        }
        for (int i = 0; i < arr.size(); i++) {
            if (1 == (int) arr.get(i)[0] && ((String) arr.get(i)[1]).equals("param")) {
                json.put((String) arr.get(i)[3], putStringOrNull(arr.get(i)[4] == null ? JSONObject.NULL : arr.get(i)[4]));
            }
        }
        for (int i = 0; i < jsonArr.size(); i++) {
            if (2 == (int) jsonArr.get(i)[0]) {
                json.put((String) jsonArr.get(i)[2], putStringOrNull(jsonArr.get(i)[3]));
            }
        }
        System.out.println(json);
        result = send(url, json);
        System.out.println(result);
    }

    private Object putStringOrNull(Object str) {
        if (str!=null && str.equals("null")) {
            return JSONObject.NULL;
        } else {
            return str;
        }
    }
    
    /**
     * Отправка json на указанный адрес
     *
     * @param url - ссылка, на которую нужно отправить json
     * @param jsonObj - json для отправки
     * @return - возвращенная в ответе строка
     */
    public static String send(String url, JSONObject jsonObj) {
        String res = "";
        if (url.contains("https:")) {
            CloseableHttpClient httpClient = null;
            try {
                httpClient = createAcceptSelfSignedCertificateClient();
            } catch (KeyManagementException ex) {
                Logger.getLogger(ServletsConnect.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ServletsConnect.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyStoreException ex) {
                Logger.getLogger(ServletsConnect.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {

                HttpPost request = new HttpPost(url);

                StringEntity params = new StringEntity(jsonObj.toString(), "UTF-8");

                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                //System.out.println("json  " + jsonObj);
                HttpResponse response = httpClient.execute(request);

                //String location = response.getLastHeader("Location").getValue();
                //System.out.println("location " + location);
                //System.out.println("ответ " + EntityUtils.toString(response.getEntity()));
                InputStream is = null;
                try {
                    is = response.getEntity().getContent();
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);
                    String inputLine;

                    while ((inputLine = br.readLine()) != null) {
                        //System.out.println(inputLine);
                        res += inputLine + "\n";
                    }

                    br.close();
                } catch (IOException | UnsupportedOperationException ex) {
                    return "Exception" + ex.getMessage();
                } finally {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        return "IOException" + ex.getMessage();
                    }
                }
            } catch (Exception ex) {
                return "Exception" + ex.getMessage();
            } finally {
                try {
                    httpClient.close();
                } catch (IOException ex) {
                    return "IOException" + ex.getMessage();
                }
            }
        } else {
            HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpResponse response;
                HttpPost request = new HttpPost(url);
                StringEntity params = new StringEntity(jsonObj.toString(), "UTF-8");
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                response = httpClient.execute(request);

                InputStream is = null;

                is = response.getEntity().getContent();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String inputLine;

                while ((inputLine = br.readLine()) != null) {
                    res += inputLine + "\n";
                }

                br.close();
                is.close();
            } catch (Exception ex) {
                return "Exception" + ex.getMessage();
            } finally {
                httpClient = null;
            }
        }
        return res;
    }

    private static CloseableHttpClient createAcceptSelfSignedCertificateClient()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();

        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();

        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);

        CloseableHttpClient httpclient = HttpClients
                .custom()
                .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build())
                .setSSLSocketFactory(connectionFactory)
                .build();

        return httpclient;
    }

    public ArrayList<String> dpdResponseParsing(String res) {
        ArrayList<String> arr = new ArrayList<>();
        JSONObject jo = new JSONObject(res);
        if (jo.has("orderStatuses")) {
            JSONArray arrStatuses = (JSONArray) jo.get("orderStatuses");
            if (arrStatuses != null) {
                jo = arrStatuses.getJSONObject(0);
                arr.add(jo.get("orderNumber").toString());
                arr.add(jo.get("logisticCompanyOrderNumber").toString());
                arr.add(jo.get("status").toString());
                arr.add(jo.get("errorMessage").toString());
                return arr;
            }
        }
        return null;
    }
    
    public ArrayList<String> dpdResponseParsingAddress(String res) {
        ArrayList<String> arr = new ArrayList<>();
        JSONObject jo = new JSONObject(res);
        if (jo.has("code") && jo.has("status") && jo.has("errorMessage")) {
                arr.add(jo.get("code").toString());
                arr.add(jo.get("status").toString());
                arr.add(jo.get("errorMessage").toString());
                return arr;
        }
        return null;
    }
    
    public String getJsonString() {
        return json.toString();
    }
}

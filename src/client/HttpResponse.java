package client;

import java.util.List;
import java.util.Map;

public class HttpResponse {

    private final int statusCode;
    private final Map<String, List<String>> headers;
    private final String body;
    private final String error;

    public HttpResponse(int statusCode, Map<String, List<String>> headers, String body, String error) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusCode=" + statusCode +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}

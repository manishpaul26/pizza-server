package com.cool.server.coolest;

import com.cool.server.coolest.pojo.ContentDisposition;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.StringTokenizer;

public class HTTPHeader {

    private String method;
    private String requestPath;
    private String protocol;
    private String serverName;
    private String connection;
    private Long contentLength;
    private String contentType;
    private String cookie;
    private ContentDisposition contentDisposition;


    /**
     * Request: GET / HTTP/1.1
     * Request: Host: localhost:4444
     * Connection: keep-alive
     * Content-Length: 191277
     * Cache-Control: max-age=0
     * Origin: http://localhost:4444
     * Upgrade-Insecure-Requests: 1
     * Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryH4fdg2GeLoPTzT23
     * User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36
     * Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng
     * Referer: http://localhost:4444/upload.html
     * Accept-Encoding: gzip, deflate, br
     * Accept-Language: en-US,en;q=0.9
     * Cookie: gig_hasGmid=ver2;
     * Content-Disposition: form-data; name="fileToUpload"; filename="testAI2.jpg"
     * Content-Type: image/jpeg
     * **/
    public HTTPHeader(List<String> headerList) {

        for (String header : headerList) {
            if (StringUtils.containsIgnoreCase(header, "HTTP/1.1")) {
                this.setRequestData(header);
            } else if (StringUtils.containsIgnoreCase(header, "Connection")) {
                this.setConnection(header);
            }  else if (StringUtils.containsIgnoreCase(header, "Host")) {
                this.setServerName(header);
            } else if (StringUtils.containsIgnoreCase(header, "Content-Type")) {
                this.setContentType(header);
            } else if (StringUtils.containsIgnoreCase(header, "Cookie")) {
                this.setCookie(header);
            } else if (StringUtils.containsIgnoreCase(header, "Content-Disposition")) {
                this.setContentDisposition(header);
            } else if (StringUtils.containsIgnoreCase(header, "Content-Length")) {
                this.setContentLength(header);
            }
        }

    }

    private void setConnection(String header) {
        StringTokenizer tokenizer = new StringTokenizer(header);
        tokenizer.nextToken();
        this.connection = tokenizer.nextToken();
    }

    private void setContentLength(String header) {
        StringTokenizer tokenizer = new StringTokenizer(header);
        tokenizer.nextToken();
        this.contentLength = Long.valueOf(tokenizer.nextToken());
    }


    private void setRequestData(String header) {
        StringTokenizer tokenizer = new StringTokenizer(header);
        this.method = tokenizer.nextToken();
        this.requestPath = tokenizer.nextToken();
        this.protocol = tokenizer.nextToken();

    }

    private void setServerName(String header) {
        StringTokenizer tokenizer = new StringTokenizer(header);
        tokenizer.nextToken();
        this.serverName = tokenizer.nextToken();
    }


    private void setContentType(String header) {
        StringTokenizer tokenizer = new StringTokenizer(header);
        tokenizer.nextToken();
        this.contentType = tokenizer.nextToken();
    }


    public void setCookie(String header) {
        StringTokenizer tokenizer = new StringTokenizer(header);
        tokenizer.nextToken();
        this.cookie = tokenizer.nextToken();
    }


    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = new ContentDisposition(contentDisposition);
    }

    public HTTPHeader() {
        this.method = "GET";
        this.requestPath = "/";
        this.protocol = "";
        this.serverName = "";
        this.connection = "";
        this.contentLength = 0L;
    }


    public String getMethod() {
        return method;
    }

    public String getRequestPath() {
        return requestPath.isEmpty() || requestPath.equalsIgnoreCase("/") ?  "index.html" : requestPath;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getServerName() {
        return serverName;
    }

    public String getConnection() {
        return connection;
    }

    public Long getContentLength() {
        return contentLength;
    }


    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }
}

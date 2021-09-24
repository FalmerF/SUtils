package net.smb.moderutils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.eq2online.console.Log;

public class HttpStateData
{
    private HttpURLConnection huc;
    private AtomicInteger responseCode;
    private String responseContent;
    private List<String> responseList;
    private EnumHttpMethod httpMethod;
    private String url;
    private String postData;
    private static final String HTTP_CONTENT_TYPE = "Content-Type";
    public static final String HTTP_PREFIX = "HTTP_";
    
    public HttpStateData(final EnumHttpMethod httpMethod, final String url, final String postData) {
        this.responseList = new LinkedList<String>();
        this.responseCode = new AtomicInteger(0);
        this.responseContent = "";
        this.httpMethod = httpMethod;
        this.url = url;
        this.postData = postData;
    }
    
    public int getResponseCode() {
        return this.responseCode.get();
    }
    
    public String getContent() {
        return this.responseContent;
    }
    
    public List<String> getResponseList() {
        return this.responseList;
    }
    
    public HttpURLConnection getConnection() {
        return this.huc;
    }
    
    public void doConnectThread(final Map<String, Object> requestHeaders) {
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpStateData.this.doConnectWithTimeout(requestHeaders);
            }
        });
        t.start();
    }
    
    private void doConnectWithTimeout(final Map<String, Object> requestHeaders) {
        int tempResponseCode = -1;
        try {
            String charset = "utf-8";
            final URLConnection uc = new URL(this.url).openConnection();
            this.huc = (HttpURLConnection)uc;
            new Thread(new InterruptThread(Thread.currentThread(), uc, this.responseCode)).start();
            this.huc.setRequestProperty("Accept-Charset", charset);
            for (final Map.Entry<String, Object> field : requestHeaders.entrySet()) {
                String key = field.getKey();
                if (key.startsWith("HTTP_")) {
                    key = key.replaceFirst("HTTP_", "");
                    if (key.toLowerCase().equals("Content-Type".toLowerCase()) || !(field.getValue() instanceof String)) {
                        continue;
                    }
                    this.huc.setRequestProperty(key, (String) field.getValue());
                }
            }
            this.huc.setInstanceFollowRedirects(true);
            this.huc.setRequestMethod(this.httpMethod.toString());
            if (this.httpMethod == EnumHttpMethod.POST || this.httpMethod == EnumHttpMethod.PUT || this.httpMethod == EnumHttpMethod.PATCH) {
                uc.setDoOutput(true);
                if (requestHeaders.containsKey("HTTP_Content-Type")) {
                    this.huc.setRequestProperty("Content-Type", (String) requestHeaders.get("HTTP_Content-Type"));
                }
                else {
                    this.huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
                }
                OutputStream output = null;
                try {
                    output = this.huc.getOutputStream();
                    output.write(this.postData.getBytes(charset));
                }
                finally {
                    if (output != null) {
                        try {
                            output.close();
                        }
                        catch (IOException ex5) {}
                    }
                }
            }
            this.huc.setConnectTimeout(5000);
            this.huc.setReadTimeout(5000);
            this.huc.connect();
            tempResponseCode = this.huc.getResponseCode();
            final InputStream response = this.huc.getInputStream();
            final String contentType = this.huc.getHeaderField("Content-Type");
            charset = "UTF-8";
            for (final String param : contentType.replace(" ", "").split(";")) {
                if (param.startsWith("charset=")) {
                    charset = param.split("=", 2)[1];
                    break;
                }
            }
            if (charset != null) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(response, charset));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        this.responseList.add(line);
                    }
                }
                finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        }
                        catch (IOException ex6) {}
                    }
                }
            }
        }
        catch (MalformedURLException ex) {
            tempResponseCode = -2;
        }
        catch (ProtocolException ex2) {
            tempResponseCode = -3;
        }
        catch (UnsupportedEncodingException ex3) {
            tempResponseCode = -4;
        }
        catch (IOException ex4) {
        	System.out.print(ex4);
        	Log.info(ex4);
            tempResponseCode = -1;
        }
        finally {
            this.responseCode.lazySet(tempResponseCode);
            this.huc.disconnect();
        }
    }
}

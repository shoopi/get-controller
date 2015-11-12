package main.java.nl.tue.ieis.get.json.rpc;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;



public class JsonRpcFunctions {
	
	private URL url;
    private Map<String, String> headers;
    private HttpURLConnection connection;
    private List<String> cookies;
    
    public JsonRpcFunctions(String urlStr) {
    	
    	//cache(urlStr);
		
    	try {
	    	this.url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
    }
    
	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }
	
	public String call(URL url, String requestData, boolean setCookie) throws Exception {
	    String responseData = post(url, headers, requestData, setCookie);
	    return responseData;
	}
		
	private String post(URL url, Map<String, String> headers, String data, boolean setCookie) throws IOException {

		connection = (HttpURLConnection) url.openConnection();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        
        if(!setCookie) {
        	for (String cookie : cookies) {
        	    connection.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
        	}
        }
        
        connection.addRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();
        OutputStream out = null;

        try {
            out = connection.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new JsonRpcClientException("unexpected status code returned : " + statusCode);
            } else {
            	/* SEE HEADER VALUES */
            	/*
            	for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            	    System.out.println(header.getKey() + "=" + header.getValue());
            	}
            	*/
            	if(setCookie) {
                	cookies = connection.getHeaderFields().get("Set-Cookie");
                	/*
                	System.out.println("cookie set to: ");
                	for(String s : cookies) System.out.println(s);
                	*/
                }
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }

        String responseEncoding = connection.getHeaderField("Content-Encoding");
        responseEncoding = (responseEncoding == null ? "" : responseEncoding.trim());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        InputStream in = connection.getInputStream();
        try {
            in = connection.getInputStream();
            if ("gzip".equalsIgnoreCase(responseEncoding)) {
                in = new GZIPInputStream(in);
            }
            in = new BufferedInputStream(in);
            
            byte[] buff = new byte[1024];
            int n;
            while ((n = in.read(buff)) > 0) {
                bos.write(buff, 0, n);
            }
            bos.flush();
            bos.close();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return bos.toString();
	}
	/*
	private void cache(String url) {
		CacheConfig cacheConfig = CacheConfig.custom()
		        .setMaxCacheEntries(1000)
		        .setMaxObjectSize(8192)
		        .build();
		
		RequestConfig requestConfig = RequestConfig.custom()
		        .setConnectTimeout(30000)
		        .setSocketTimeout(30000)
		        .build();

		CloseableHttpClient cachingClient =  (caching) HttpClients.custom()
				.setCacheConfig(cacheConfig)
		        .setDefaultRequestConfig(requestConfig)
		        .build();

		HttpCacheContext context = HttpCacheContext.create();
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = cachingClient.execute(httpget, context);
		try {
		    CacheResponseStatus responseStatus = context.getCacheResponseStatus();
		    switch (responseStatus) {
		        case CACHE_HIT:
		            System.out.println("A response was generated from the cache with " +
		                    "no requests sent upstream");
		            break;
		        case CACHE_MODULE_RESPONSE:
		            System.out.println("The response was generated directly by the " +
		                    "caching module");
		            break;
		        case CACHE_MISS:
		            System.out.println("The response came from an upstream server");
		            break;
		        case VALIDATED:
		            System.out.println("The response was generated from the cache " +
		                    "after validating the entry with the origin server");
		            break;
		    }
		} finally {
		    response.close();
		}
	}
	*/
}

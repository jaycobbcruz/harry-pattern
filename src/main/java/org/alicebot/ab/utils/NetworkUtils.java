package org.alicebot.ab.utils;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;


public class NetworkUtils {

	private static final Logger log = LoggerFactory.getLogger(org.alicebot.ab.utils.NetworkUtils.class);

    static {
        try {
            trustEveryone();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static final HttpTransport httpTransport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();
    private static HttpTransport getHttpTransport() {
        return httpTransport;
    }
    private static JsonFactory getJsonFactory() {
        return jsonFactory;
    }

    private static HttpRequestFactory getHttpRequestFactory() {
        return getHttpTransport().createRequestFactory(request -> request.setParser(new JsonObjectParser(getJsonFactory())));
    }

    public static String localIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipAddress = inetAddress.getHostAddress();
                        int p = ipAddress.indexOf("%");
                        if (p > 0) ipAddress = ipAddress.substring(0, p);
                        log.info("--> localIPAddress = {}", ipAddress);
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return "127.0.0.1";
    }

	public static String responseContent(String url) throws Exception {
        final HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        return request.execute().parseAsString();
	}


	public static String spec(String host, String botid, String custid, String input) throws UnsupportedEncodingException {
		log.trace("--> custid = {}", custid);
		String spec;
		if (custid.equals("0"))      // get custid on first transaction with Pandorabots
			spec =    String.format("%s?botid=%s&input=%s",
									"http://" + host + "/pandora/talk-xml",
									botid,
									URLEncoder.encode(input, "UTF-8"));
		else spec =                 // re-use custid on each subsequent interaction
				 String.format("%s?botid=%s&custid=%s&input=%s",
							   "http://" + host + "/pandora/talk-xml",
							   botid,
							   custid,
							   URLEncoder.encode(input, "UTF-8"));
		return spec;
	}

    private static void trustEveryone() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLContext.setDefault(ctx);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> true);
    }

}

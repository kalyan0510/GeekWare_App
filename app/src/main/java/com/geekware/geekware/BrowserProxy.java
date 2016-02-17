package com.geekware.geekware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


public class BrowserProxy {
    HttpClient httpclient = new DefaultHttpClient();
    CookieStore cookieStore = new BasicCookieStore();
    HttpContext localContext = new BasicHttpContext();
    HttpGet httpget;
    HttpResponse response;
    HttpEntity entity;
    List<Cookie> cookies;
    Cookie cookie = null;
    private String action1;
    final String sendSMSPage = "http://160by2.com/SendSMS?";
    final String sendSMSActionPage =
            "http://160by2.com/SendSMSAction?hid_exists=yes";
    final String loginPage = "http://160by2.com/re-login?";

    public BrowserProxy() {
    }

    public boolean getLoggedIn(String uname, String pwd) throws IOException,
            ClientProtocolException {
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        URI uri = null;
        uri = URI.create(loginPage + "username=" + uname + "&password=" + pwd);
        HttpGet httpget = new HttpGet(uri);
        response = httpclient.execute(httpget, localContext);
        entity = response.getEntity();
        cookies = cookieStore.getCookies();
        Cookie cookie = cookies.get(0);
        String cookieSet = cookie.getName() + "=" + cookie.getValue();
        entity.consumeContent();
        fetchAction(cookieSet);

        if (isLoggedIn())
            return true;
        else
            return false;
    }

    private void fetchAction(String cookieSet) throws IOException {

        URLConnection conn = new URL(sendSMSPage).openConnection();
        conn.setRequestProperty("Cookie", cookieSet);
        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String content;
        StringBuilder sBuilder = new StringBuilder();
        while (true) {
            content = br.readLine();
            if (content == null)
                break;
            else if (content.contains("id=\"action1\"")) {
                sBuilder.append(content);
                break;
            }
        }
        String action[] = content.split("value=\"");
        String action1[] = action[1].split("\"");
        setAction1(action1[0]);
    }

    public void sendSms(String to, String msg) throws URISyntaxException,
            UnsupportedEncodingException,
            IOException,
            ClientProtocolException {

        URI uri = null;
        msg = URLEncoder.encode(msg, "utf-8");
        String params =
                "&action1=" + getAction1() + "&mobile1=" + to + "&msg1=" + msg;
        uri = new URI(sendSMSActionPage + params);
        httpget = new HttpGet(uri);
        httpclient.execute(httpget, localContext);
    }

    public boolean isLoggedIn() {
        try {
            if (cookies.size() == 2)
                return true;
            else
                return false;

        } catch (NullPointerException e) {
            return false;
        }
    }

    public void setAction1(String action1) {
        this.action1 = action1;
    }

    public String getAction1() {
        return action1;
    }



}
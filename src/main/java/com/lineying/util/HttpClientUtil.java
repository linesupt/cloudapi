package com.lineying.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StreamUtils;

import java.io.*;

/**
 * @author ganjing
 */
public class HttpClientUtil {
    /**
     * get请求，参数拼接在地址上
     *
     * @param url 请求地址加参数
     * @return 响应
     */
    public static String get(String url) {
        //System.out.println("url:"+url);
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
                response.close();
                httpClient.close();
                return result;
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param url
     * @param filePath
     * @return
     * @throws Throwable
     */
    public static boolean downloadImage(String url, String filePath) throws Throwable {
        try {
            //创建httpClient对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            httpget.setConfig(RequestConfig.custom() //
                    .setConnectionRequestTimeout(3000) //
                    .setConnectTimeout(3000) //
                    .setSocketTimeout(3000) //
                    .build());
            //下载对象放入文件夹
            CloseableHttpResponse response = httpClient.execute(httpget);
            org.apache.http.HttpEntity entity = response.getEntity();

            String[] urlArray = url.split("/");
            int maxValue = urlArray.length - 1;
            File desc = new File("F://" + File.separator + filePath + File.separator + urlArray[maxValue]);

            File folder = desc.getParentFile();
            if (!folder.exists()) {//如果文件夹不存在
                folder.mkdirs();//创建文件夹
            }
            //folder.mkdirs();
            InputStream is = entity.getContent(); //
            OutputStream os = new FileOutputStream(desc);
            StreamUtils.copy(is, os);
            //httpClient.close();
            is.close();
            os.close();
            System.out.println("F://" + File.separator + filePath + File.separator + urlArray[maxValue]);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}

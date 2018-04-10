package com.whalegoods.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

/**
 * http请求工具类
 * @author chencong
 *
 */
public class HttpUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	
	/**
	 * 功能描述:  get方式
	 * Author: HenrySun
	 * Date:   2017年3月24日 下午4:23:58 
	 * param   @param url
	 * return  String
	 */
	public static String get(String url,JSONObject params) throws ClientProtocolException, IOException {
		return get(url,params,null);
	}
	
	
	/**
	 * 功能描述:  delete
	 * Author: HenrySun
	 * Date:   2017年3月24日 下午4:47:57 
	 * param   @param url
	 * return  String
	 */
	public static String delete(String url) throws ClientProtocolException, IOException {
		return delete(url, null);
	}
	
	
	
	/**
	 * 功能描述:  post方式
	 * Author: HenrySun
	 * Date:   2017年3月24日 下午4:22:04 
	 * param   @param url
	 * param   @param sendData 请求的JSON字符串
	 * param   @param headers  请求头
	 * return  String
	 */
	public static String post(String url, String sendData,Map<String,String> headers)
			throws ClientProtocolException, IOException {
		return post(url, sendData, headers,null);
	}
	
	
	/**
	 * 功能描述:  put
	 * Author: HenrySun
	 * Date:   2017年3月24日 下午4:50:05 
	 * param   @param url
	 * param   @param sendData
	 * param   @param headers
	 * return  String
	 */
	public static String put(String url, String sendData,Map<String,String> headers)
			throws ClientProtocolException, IOException {
		return put(url, sendData, headers, null);
	}
	
	/**
	 * 功能描述:  form表单提交
	 * Author: HenrySun
	 * Date:   2017年3月29日 下午6:15:32 
	 * param   @param url
	 * param   @param sendData
	 * return  String
	 */
	public static String form(String url,JSONObject params)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);
			config(httpPost);
			List<NameValuePair> formParams =null;
			if(params!=null){
				formParams = new ArrayList<NameValuePair>(params.size());
				for(String key:params.keySet()){
					String value =params.getString(key);
					formParams.add(new BasicNameValuePair(key, value));
				}
			}
			UrlEncodedFormEntity uefEntity= new UrlEncodedFormEntity(formParams, Consts.UTF_8.name());
			httpPost.setEntity(uefEntity);
			HttpResponse response = httpClient.execute(httpPost);
			return HttpResToString(response, Consts.UTF_8.name());
		} finally {
			close(httpClient);
		}
	}
	
	/**
	 * POST XML
	 * @author chencong
	 * 2018年4月10日 上午10:51:04
	 */
	public static String sendPost(String url, String requestXml, Map<String, String> heads) {
		logger.debug("sendPost Url:" + url);
		logger.debug("sendPost xml:" + requestXml);
		String result = "";
		try {
			URL u0 = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u0.openConnection();
			conn.setRequestMethod("POST");
			byte contentbyte[] = requestXml.toString().getBytes();
			if(heads!=null)
			{
				for (Map.Entry<String, String> header : heads.entrySet()) {
					conn.setRequestProperty(header.getKey(), header.getValue());
				}
			}
			conn.setRequestProperty("Content-Type", String.format("application/x-www-form-urlencoded;charset=%s", "UTF-8"));
			conn.setRequestProperty("Content-Length", (new StringBuilder()).append(contentbyte.length).toString());
			conn.setRequestProperty("Content-Language", "en-US");
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(
					conn.getOutputStream()));
			bWriter.write(requestXml.toString());
			bWriter.flush();
			bWriter.close();
			InputStream in = conn.getInputStream();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i != -1;) {
				i = in.read();
				if (i != -1)
					buffer.append((char) i);
			}

			in.close();
			result = new String(buffer.toString().getBytes("iso-8859-1"),"UTF-8");
		} catch (Exception e) {
			logger.error("请求异常: " + e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 功能描述:  get方式重载
	 * Author: HenrySun
	 * Date:   2017年3月24日 下午2:32:07 
	 * url:	   
	 * decodeCharset: 返回内容字符编码,默认utf8
	 * return  String
	 */
	private static String get(String url,JSONObject params,String decodeCharset) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			if(params!=null){
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for(String key:params.keySet()){
					String value =params.getString(key);
					pairs.add(new BasicNameValuePair(key, value));
				}
				url+="?"+ EntityUtils.toString(new UrlEncodedFormEntity(pairs,decodeCharset));
			}
			HttpGet httpGet = new HttpGet(url);
			config(httpGet);
			HttpResponse response = httpClient.execute(httpGet);
			return HttpResToString(response, decodeCharset);
		} finally {
			close(httpClient);
		}
	}
	/**
	 * 功能描述:  delete方式重载
	 * Author: HenrySun
	 * Date:   2017年3月24日 下午2:35:10 
	 * url:
	 * decodeCharset: 返回内容字符编码,默认utf8
	 * return  String
	 */
	private static String delete(String url, String decodeCharset) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpDelete httpdelete = new HttpDelete(url);
			config(httpdelete);
			HttpResponse response = httpClient.execute(httpdelete);
			return HttpResToString(response, decodeCharset);
		} finally {
			close(httpClient);
		}
	}
	/**
	 * 功能描述: post方式重载
	 * Author: HenrySun
	 * Date:   2017年3月24日 下午2:35:52 
	 * url:
	 * sendData: 发送内容
	 * header:
	 * decodeCharset: 返回内容字符编码,默认utf8
	 * return  String
	 */
	private static String post(String url, String sendData, Map<String,String> headers,String decodeCharset)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(url);
			config(httpPost);
			Set<Map.Entry<String,String>> headerSet=headers.entrySet();
			for(Entry<String,String> entry:headerSet){
				httpPost.setHeader(entry.getKey(),entry.getValue());
			}
			httpPost.setEntity(new StringEntity(sendData));
			HttpResponse response = httpClient.execute(httpPost);
			return HttpResToString(response, decodeCharset);
		} finally {
			close(httpClient);
		}
	}
	
	
	/**
	 * 功能描述:  put方式重载
	 * Author: HenrySun
	 * Date:   2017年3月24日 下午2:37:11 
	 * url
	 * sendData: 发送内容
	 * contentType: header
	 * decodeCharset: 返回内容字符编码,默认utf8
	 * return  String
	 */
	private static String put(String url, String sendData,Map<String,String> headers, String decodeCharset)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPut HttpPut = new HttpPut(url);
			config(HttpPut);
			Set<Map.Entry<String,String>> headerSet=headers.entrySet();
			for(Entry<String,String> entry:headerSet){
				HttpPut.setHeader(entry.getKey(),entry.getValue());
			}
			HttpPut.setEntity(new StringEntity(sendData));
			HttpResponse response = httpClient.execute(HttpPut);
			return HttpResToString(response, decodeCharset);
		} finally {
			close(httpClient);
		}
	}
	
	
	
	/**
	 * 功能描述:  返回内容转成string
	 * Author: HenrySun
	 * Date:   2017年3月24日 下午2:38:04 
	 * response
	 * decodeCharset
	 * return  String
	 */
	private static String HttpResToString(HttpResponse response, String decodeCharset)
			throws ParseException, IOException {
		HttpEntity entity = response.getEntity();
		if (null != entity) {
			return EntityUtils.toString(entity, decodeCharset == null ? Consts.UTF_8.name() : decodeCharset);
		}
		return null;
	}
	/**
	 * 功能描述:  配置超时时间
	 * Author: HenrySun
	 * Date:   2017年3月24日 下午2:38:30 
	 * request   
	 * return  void
	 */
	private static void config(HttpRequestBase request) {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(1000)// 连接超时ms
				.setSocketTimeout(2000).build();// 请求获取数据的超时时间
		request.setConfig(config);
	}
	
	/**
	 * 功能描述:  关闭资源
	 * Author: HenrySun
	 * Date:   2017年3月24日 下午2:38:46 
	 * httpClient   
	 * return  void
	 */
	private static void close(CloseableHttpClient httpClient) {
		try {
			httpClient.close();
		} catch (IOException e) {
			httpClient = null;
			logger.error("httpclient资源关闭异常信息:{}", e.getMessage());
		}
	}
	

}
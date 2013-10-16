package com.sysu.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import weibo4j.Oauth;
import weibo4j.Timeline;
import weibo4j.http.AccessToken;
import weibo4j.util.WeiboConfig;

import com.sysu.config.Config;
import com.sysu.mainloop.Application;

public class AuthUtils {
	static Logger logger=Logger.getLogger(Application.class);
	public static AccessToken authentication()
	{
		try
		{
			    logger.info("Authentication Start");
				HttpClient client=new HttpClient();
				PostMethod postmethod=new PostMethod(WeiboConfig.getValue("authorizeURL").trim());
				NameValuePair username = new NameValuePair( "userId" , Config.USERNAME );
				NameValuePair password = new NameValuePair("passwd",Config.PASSWORD);
				NameValuePair clientId=  new NameValuePair("client_id",WeiboConfig.getValue("client_ID").trim()); 
				NameValuePair responseType=  new NameValuePair("response_type","code"); 
				NameValuePair redirtUrl= new NameValuePair("redirect_uri",WeiboConfig.getValue("redirect_URI").trim());
				NameValuePair display=  new NameValuePair("display","wap2.0"); 
				NameValuePair withcookie=  new NameValuePair("with_cookie",""); 
				NameValuePair state=  new NameValuePair("state",""); 
				NameValuePair from=  new NameValuePair("from","");
				NameValuePair action=  new NameValuePair("action","submit");
				NameValuePair paramX=  new NameValuePair("x","0");
				NameValuePair paramY=  new NameValuePair("y","0");
				//NameValuePair regCallback=new NameValuePair("regCallback","https%3A%2F%2Fapi.weibo.com%2F2%2Foauth2%2Fauthorize%3Fclient_id%3D1377849012%26response_type%3Dcode%26display%3Dwap2.0%26redirect_uri%3Dhttps%3A%2F%2Fapi.weibo.com%2Foauth2%2Fdefault.html%26from%3D%26with_cookie%3D");
				postmethod.addParameters(new NameValuePair[]{display,action,responseType,redirtUrl,clientId,state,from,withcookie,username,password,paramX,paramY});
				List<Header> headers = new ArrayList<Header>(); 
                headers.add(new Header("Referer", "https://api.weibo.com/oauth2/authorize?client_id=your_client_id&redirect_uri=your_redirect_url&from=sina&response_type=code"));//伪造referer 
                headers.add(new Header("Host", "api.weibo.com")); 
                headers.add(new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:11.0) Gecko/189100101 Android Google Chrome/9.0")); 
                client.getHostConfiguration().getParams().setParameter("http.default-headers", headers); 
				int status=client.executeMethod(postmethod);
				if(status==302)
				{
					 Header location = postmethod.getResponseHeader("Location"); 
                     if(location != null){ 
                             String retUrl = location.getValue(); 
                             int begin = retUrl.indexOf("code="); 
                             if(begin != -1){ 
                                     int end = retUrl.indexOf("&", begin); 
                                     if(end == -1) 
                                             end = retUrl.length(); 
                                     String code = retUrl.substring(begin+5, end); 
                                     if(code != null){
                                    	 			Oauth oauth = new Oauth(); 
                                                    AccessToken token = oauth.getAccessTokenByCode(code);
                                                     
                                                     return token; 
                                     }
                                     else
                                     {
                                    	 logger.warn("No code found!");
                                    	 return null;
                                     }
                             }
                             else
                             {
                            	 logger.warn("No code found -2!");
                            	 return null;
                             }
                             
                     }
                     else
                     {
                    	 logger.warn("No location found");
                    	 return null;
                     }
				}
				else
				{
					logger.warn("Get Token Failed");
					return null;
				}
				//return false;
		}
		catch(Exception e)
		{
			logger.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
}

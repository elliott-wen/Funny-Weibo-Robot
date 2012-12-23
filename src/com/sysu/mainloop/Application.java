package com.sysu.mainloop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.sysu.config.Config;
import com.sysu.plugins.FakeAstroLuckyPlugin;
import com.sysu.plugins.FakeAstroRankingPlugin;
import com.sysu.plugins.JokePlugin;
import com.sysu.plugins.Plugin;
import com.sysu.plugins.SamplePlugin;
import com.sysu.util.ImageUtils;

import weibo4j.Oauth;
import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.http.AccessToken;
import weibo4j.http.ImageItem;
import weibo4j.model.Status;
import weibo4j.util.WeiboConfig;

public class Application extends Thread{
	
	private List<Plugin> pluginsList=new ArrayList<Plugin>();		
	boolean threadFlag=false;
	int refreshTime=Config.MAXREFRSHTIME;
	Timeline tm = null;
	
	Random random = new Random();
	/**
	 * @param args
	 */
	static Logger logger=Logger.getLogger(Application.class);
	Oauth oauth = new Oauth(); 
	AccessToken token=null;
	public static void main(String[] args) {
		Application application=new Application();
			
			if(application.loadPlugin())
			{
				
				application.startThread();
			}
			else
			{
				logger.warn("Load Plugin Failed! No Plugins can work!");
			}
		
	
	}
	public boolean loadPlugin()
	{
		logger.info("Loading Plugin");
		//this.pluginsList.add(new SamplePlugin());
		this.pluginsList.add(new FakeAstroLuckyPlugin());
		this.pluginsList.add(new JokePlugin());
		this.pluginsList.add(new FakeAstroRankingPlugin());
		logger.info("Initing Plugin");
		for(int i=0;i<pluginsList.size();i++)
		{
			Plugin p=this.pluginsList.get(i);
			if(p.init())
			{
				logger.info(p.getPluginName()+" Init Okay!");
				logger.info(p.aboutPlugin());
			}
			else
			{
				logger.info(p.getPluginName()+" Init Failed! Plugin May not work Properly!");
				pluginsList.remove(i);
			}
		}
		logger.info("loaded "+this.pluginsList.size()+" plugins");
		return this.pluginsList.size()>0;
		
	}
	public void startThread()
	{
		logger.info("Deamon Will Start Soon");
		this.threadFlag=true;
		this.start();
	}
	public void stopThread()
	{
		logger.info("Deamon Will Stop Soon");
		this.threadFlag=false;
	}
	@Override
	public void run()
	{
		
		while(this.threadFlag)
		{
			try
			{
				
				logger.info("Deamon will sleep for 2 minute");
				sleep(180000);
				this.refreshTime++;
				if(this.refreshTime>Config.MAXREFRSHTIME)
				{
					logger.info("Need to refresh key!");
					this.refreshTime=0;
					if(this.authentication())
					{
						logger.info("Re-Get Key Okay");
					}
					else
					{
						this.stopThread();
						logger.warn("Re-get Key failed");
						break;
					}
				}
				int chosenIndex=random.nextInt(Config.SEEDNUM)%pluginsList.size();
				Plugin p=pluginsList.get(chosenIndex);
				logger.info("Using "+p.getPluginName());
				if(!p.isReadytoRun())
				{
					logger.info("Sorry!this plugin is not ready now!");
					continue;
				}
				Map<String,String> status=p.generateMessage();
				String text=status.get("Text");
				String url=status.get("Url");
				if(url!=null&&!url.isEmpty())
				{
					logger.info("Sending Picture Weibo");
					byte[] picture=ImageUtils.getImagefromInternet(url);
					if(picture!=null)
					{
						tm.UploadStatus(text, new ImageItem(picture));
					}
					else
					{
						logger.info("Try to Send pure Weibo but just send pure!");
						tm.UpdateStatus(text);
					}
				}
				else
				{
					logger.info("Sending pure Weibo");
					tm.UpdateStatus(text);
				}
				
				
			}
			catch(Exception e)
			{
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
			
		}
	}
	public boolean authentication()
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
                                                     token = oauth.getAccessTokenByCode(code);
                                                     tm=new Timeline();
                                             		 tm.setToken(token.getAccessToken());
                                                     return true; 
                                     }
                                     else
                                     {
                                    	 logger.warn("No code found!");
                                    	 return false;
                                     }
                             }
                             else
                             {
                            	 logger.warn("No code found -2!");
                            	 return false;
                             }
                             
                     }
                     else
                     {
                    	 logger.warn("No location found");
                    	 return false;
                     }
				}
				else
				{
					logger.warn("Get Token Failed");
					return false;
				}
				//return false;
		}
		catch(Exception e)
		{
			logger.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
		
	}
	

}

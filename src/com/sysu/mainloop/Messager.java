package com.sysu.mainloop;

import java.util.ArrayList;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import weibo4j.Timeline;
import weibo4j.http.AccessToken;
import weibo4j.http.ImageItem;
import weibo4j.util.URLEncodeUtils;

import com.sysu.config.Config;

import com.sysu.messager.plugins.DuowanPlugin;
import com.sysu.messager.plugins.HongkongWeatherPlugin;
import com.sysu.messager.plugins.MegaCenterWeatherPlugin;
import com.sysu.messager.plugins.Plugin;
import com.sysu.messager.plugins.SamplePlugin;
import com.sysu.messager.plugins.XinhuiWeatherPlugin;
import com.sysu.util.AuthUtils;
import com.sysu.util.ImageUtils;

public class Messager extends Thread{
	private List<Plugin> pluginsList=new ArrayList<Plugin>();		
	boolean threadFlag=false;
	
	long lastUpdateTokenTime=0;
	Timeline tm = null;
	Random random = new Random();
	static Logger logger=Logger.getLogger(Application.class);
	AccessToken token=null;
	public boolean loadPlugin()
	{
		logger.info("Loading Plugin");
		//this.pluginsList.add(new SamplePlugin());
		this.pluginsList.add(new DuowanPlugin());
		this.pluginsList.add(new MegaCenterWeatherPlugin());
		this.pluginsList.add(new XinhuiWeatherPlugin());
		this.pluginsList.add(new HongkongWeatherPlugin());
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
		logger.info("Deamon will start soon!");
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
				
				sleep(random.nextInt(Config.RESTTIME));
				
				if(new Date().getTime()-lastUpdateTokenTime>Config.MAXREFRSHTIME)
				{
					logger.info("Need to refresh key!");
					if(((token=AuthUtils.authentication())!=null))
					{
						tm=new Timeline();
                		tm.setToken(token.getAccessToken());
						logger.info("Re-Get Key Okay");
						lastUpdateTokenTime=new Date().getTime();
					}
					else
					{
						this.stopThread();
						logger.warn("Re-get Key failed");
						break;
					}
				}
				
				for(int i=0;i<pluginsList.size();i++)
				{
					Plugin p=pluginsList.get(i);
					if(!p.isReadytoRun())
					{
						//logger.info("Sorry!this plugin is not ready now!");
						continue;
					}
					logger.info("Plugin prepares to work! Using "+p.getPluginName());
					sleep(random.nextInt(Config.RESTTIME));
					
					Map<String,String> status=p.generateMessage();
					String text=status.get("text");
					String url=status.get("url");
					if(url!=null&&!url.isEmpty())
					{
						logger.info("Sending Picture Weibo");
						byte[] picture=ImageUtils.getImagefromInternet(url);
						if(picture!=null)
						{
							tm.UploadStatus(URLEncodeUtils.encodeURL(text), new ImageItem(picture));
						}
						else
						{
							logger.info("Try to Send picture Weibo but just send pure!");
							tm.UpdateStatus(text);
						}
					}
					else
					{
						logger.info("Sending pure Weibo");
						tm.UpdateStatus(text);
					}
					logger.info(p.getPluginName()+" Working Fine!");
					
				}
				
				
			}
			catch(Exception e)
			{
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
			
		}
	}
	
}

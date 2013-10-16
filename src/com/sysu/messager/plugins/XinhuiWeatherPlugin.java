package com.sysu.messager.plugins;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.sysu.config.Config;

public class XinhuiWeatherPlugin implements Plugin{

	private static long PERIOD=86400000;
	private long lastRunTime=new Date().getTime();
	
	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, String> generateMessage() {
		// TODO Auto-generated method stub
		Map<String,String> messageMap=new HashMap<String,String>();
		try
		{
			HttpClient client=new HttpClient();
			GetMethod getMethod=new GetMethod("http://weather.raychou.com/?/detail/59476/count_1/rss");
			int status=client.executeMethod(getMethod);
			if(status==200)
			{
				
				SAXBuilder builder=new SAXBuilder();
				Document document=builder.build(getMethod.getResponseBodyAsStream());
				Element root=document.getRootElement();
				Element channelRoot=root.getChild("channel");
				String address=channelRoot.getChildText("title");
				Element itemRoot=channelRoot.getChild("item");
				String time=itemRoot.getChildText("title");
				String description=itemRoot.getChildText("description");
				messageMap.put("text", address+": "+time+". "+description+Config.ATER);
				if(description.contains("雷"))
				{
					messageMap.put("url", Config.RESOURCEPATH+"weather/thunder.jpg");
				}
				else if(description.contains("雨"))
				{
					messageMap.put("url", Config.RESOURCEPATH+"weather/rain.jpg");
				}
				else
				{
					messageMap.put("url", Config.RESOURCEPATH+"weather/fine.jpg");
				}
			}
			else
			{
				messageMap.put("text", "暂时没有大学城天气的信息1.");
			}
		}
		catch(Exception e)
		{
			messageMap.put("text", "暂时没有大学城天气的信息2.");
			e.printStackTrace();
		}
		return messageMap;
	}

	@Override
	public String aboutPlugin() {
		// TODO Auto-generated method stub
		return "Weather Plugin For Xinhui!";
	}

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return "Xinhui Weather Plugin!";
	}

	@Override
	public boolean isReadytoRun() {
		// TODO Auto-generated method stub
		if(new Date().getTime()-lastRunTime>PERIOD)
		{
			lastRunTime=new Date().getTime();
			return true;
		}
		return false;
	}
	public static void main(String args[])
	{
		Plugin p=new XinhuiWeatherPlugin();
		System.out.println(p.generateMessage().get("text"));
	}
	
}

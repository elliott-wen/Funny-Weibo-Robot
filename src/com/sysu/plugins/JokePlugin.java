package com.sysu.plugins;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import weibo4j.util.URLEncodeUtils;

import com.sysu.util.ImageUtils;
import com.sysu.util.WordUtils;

public class JokePlugin implements Plugin{

	private String aboutPluginString="It's a joke plugin!";
	private String pluginName="Jokes Plugin!";
	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, String> generateMessage() {
		Map<String,String> message=new HashMap<String,String>();
		int retryTime=0;
		String joke=null;
		while(retryTime<3)
		{
			retryTime++;
			joke=getJoke();
			if(joke!=null&&joke.length()<120)
				break;
		}
		if(joke==null)
		{
		joke="我以后生个儿子名字要叫“好帅”,那别人看到我就会说’好帅的爸爸“。";
		}
		message.put("Text", URLEncodeUtils.encodeURL(joke));
		message.put("Url",ImageUtils.genRandomPicture());
		return message;
	}
	
	private String getJoke()
	{
		BufferedInputStream inputStream=null;
		try
		{
			URL url = new URL("http://www.woween.cn/apis/?type=joke&msg=");
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder builder=new StringBuilder();;
			 String keyin = null; 
	            while((keyin = bufReader.readLine()) != null) { 
	            builder.append(keyin);
	        }
	        
	        String result=builder.toString();
	        if(result!=null)
	        {
	        	int sindex=result.indexOf(".")+1;
	        	result=result.substring(sindex);
	        }
	        return result;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				if(inputStream!=null)
				{
					inputStream.close();
				}
				
			}
			catch(Exception e)
			{
				
			}
		}
	}

	@Override
	public String aboutPlugin() {
		// TODO Auto-generated method stub
		return this.aboutPluginString;
	}

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return this.pluginName;
	}

	@Override
	public boolean isReadytoRun() {
		// TODO Auto-generated method stub
		return true;
	}

}

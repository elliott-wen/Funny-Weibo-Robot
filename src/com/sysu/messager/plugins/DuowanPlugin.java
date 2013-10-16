package com.sysu.messager.plugins;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sysu.config.Config;

public class DuowanPlugin implements Plugin{
	private static long PERIOD=2000000;
	private long lastRunTime=0;
	private String address="http://tu.duowan.com/index.php?r=show/getTagPics/&callback=t&tid=";
	private int gallery[]={4897,0,15203,10607};
	Random random=new Random();
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
			GetMethod getMethod=new GetMethod(address+gallery[random.nextInt(3)]);//Take Care oF 3
			int status=client.executeMethod(getMethod);
			if(status==200)
			{
				String result=getMethod.getResponseBodyAsString(102400);
				int start=result.indexOf("picInfo\":[");
				int end=result.indexOf("]", start);
				String subResult=result.substring(start+9,end+1);
				JSONArray resultArray=JSON.parseArray(subResult);
				int arraySize=resultArray.size();
				JSONObject object=resultArray.getJSONObject(random.nextInt(arraySize-1));
				messageMap.put("text", object.getString("intro")+Config.ATER);
				messageMap.put("url", object.getString("source"));
			}
			else
			{
				messageMap.put("text", "啊哦，小棋子暂时不能为你提供笑话");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			messageMap.put("text", "啊哦，小棋子暂时不能为你提供笑话");
		}
		return messageMap;
	}

	@Override
	public String aboutPlugin() {
		// TODO Auto-generated method stub
		return "Duowan Tuku Funny Picture";
	}

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return "Duowan Plugin!";
	}

	@Override
	public boolean isReadytoRun() {
		if(new Date().getTime()-lastRunTime>PERIOD)
		{
			lastRunTime=new Date().getTime();
			return true;
		}
		return false;
	}

	public static void main(String args[])
	{
		Plugin p=new DuowanPlugin();
		System.out.println(p.generateMessage().get("url"));
	}

}

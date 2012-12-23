package com.sysu.plugins;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.sysu.config.Config;
import com.sysu.util.ImageUtils;

import weibo4j.http.AccessToken;
import weibo4j.model.Status;
import weibo4j.util.URLEncodeUtils;

public class FakeAstroRankingPlugin implements Plugin{
	private String aboutPluginString="It's a fake astro ranking plugin for joking!";
	private String pluginName="Astro Ranking Plugin!";
	private List<String> topicArray=new ArrayList<String>();
	private String[] allAstro={"白羊座","金牛座","双子座","巨蟹座","狮子座","处女座","天秤座","天蝎座","射手座","摩羯座","水瓶座","双鱼座"};
	Random random=new Random();
	@Override
	
	public boolean init() {
		// TODO Auto-generated method stub
		BufferedReader bufReader=null;
		try
		{
			if(topicArray.isEmpty())
			{
				//picStore.clear();
				bufReader = new BufferedReader(new FileReader("dat/astrotopic.dat"));
				String keyin = null; 
	            while((keyin = bufReader.readLine()) != null) {
	            	topicArray.add(keyin);
	            }
	         
			}
				return topicArray.size()!=0;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			try
			{
				if(bufReader!=null) bufReader.close();
			}
			catch(Exception e)
			{
				
			}
		}
	}

	

	@Override
	public Map<String,String> generateMessage() {
		int index=random.nextInt(Config.SEEDNUM)%topicArray.size();
		StringBuilder builder=new StringBuilder();
		
		builder.append(topicArray.get(index));
		builder.append(": ");
		builder.append(getRandomAstroSort());
		
		Map<String,String> message=new HashMap<String,String>();
		message.put("Text", URLEncodeUtils.encodeURL(builder.toString()));
		message.put("Url",ImageUtils.genRandomPicture());
		return message;
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

	

	private  String getRandomAstroSort()
	{
		int data[]={0,1,2,3,4,5,6,7,8,9,10,11};
		permuteBySort(data);
		StringBuilder builder=new StringBuilder();
		for(int i=1;i<=12;i++)
		{
			builder.append("第"+i+"名:");
			builder.append(allAstro[data[i-1]]);
			builder.append("; ");
		}
		return builder.toString();
	}
	private static void permuteBySort(int[] data)
	{
		int len=data.length;
		int len3=len*len*len;
		int P[]=getRandom(1,len3,len);
		
		//冒泡排序
		for(int i=len-1; i>0; i--)
		{
			for(int j=0; j<i ; j++)
			{
				if(P[j]>P[j+1])
				{
					int temp=data[j];
					data[j]=data[j+1];
					data[j+1]=temp;
					
					temp=P[j];
					P[j]=P[j+1];
					P[j+1]=temp;					
				}
			}
		}
	}
	/*
	 * 元素A[i]是从 元素A[i]到A[n]中随机选取的
	 */
	private static void randomizeInPlace(int[] data)
	{
		Date dt=new Date();
		Random random=new Random(dt.getSeconds());
		int len=data.length;
		for(int i=0; i<len; i++)
		{
			int pos=(int)(random.nextDouble()*(len-i+1)+i)-1;
			int temp=data[i];
			data[i]=data[pos];
			data[pos]=temp;
		}
	}
	
	/*
	 * 获得在a到b之间的n个随机数
	 */
	private static int[] getRandom(int a,int b,int n)
	{
		if(a>b)
		{
			int temp=a;
			a=b;
			b=temp;
		}
		
		Date dt=new Date();
		Random random=new Random(dt.getSeconds());
		int res[]=new int[n];
		for(int i=0; i<n; i++)
		{
				res[i]=(int)(random.nextDouble()*(Math.abs(b-a)+1))+a;
		}
		return res;
	}



	@Override
	public boolean isReadytoRun() {
		// TODO Auto-generated method stub
		return true;
	}

}

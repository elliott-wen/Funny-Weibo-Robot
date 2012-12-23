package com.sysu.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.sysu.config.Config;
import com.sysu.mainloop.Application;

public class ImageUtils {
	static Logger logger=Logger.getLogger(ImageUtils.class);
	public static final String DEFAULTIMAGE="http://www.sysu.edu.cn/2012/images/logo.jpg";
	public static List<String> picStore=new ArrayList<String>();
	public static Random random=new Random();
	public static byte[] getImagefromInternet(String Url)
	{
		BufferedInputStream inputStream=null;
		try
		{
			URL url = new URL(Url);
			inputStream =new BufferedInputStream(url.openStream());
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			int read = 0;
	        while((read = inputStream.read()) != -1) { 
	            bout.write(read); 
	        }
	        //inputStream.close();
	        bout.flush();
	        return bout.toByteArray();
		}
		catch(Exception e)
		{
			logger.warn(e.getMessage());
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
	public static String genRandomPicture()
	{
		BufferedReader bufReader=null;
		try
		{
			if(picStore.isEmpty())
			{
				//picStore.clear();
				bufReader = new BufferedReader(new FileReader("dat/picstore.dat"));
				String keyin = null; 
	            while((keyin = bufReader.readLine()) != null) {
	            	if(keyin.startsWith("http://")&&(keyin.endsWith(".jpg")||keyin.endsWith(".gif")||keyin.endsWith(".png")||keyin.endsWith(".bmp")))
	            	picStore.add(keyin);
	            }
	            
	            if(picStore.isEmpty())
	            {
	            	logger.warn("Not found any pic in database");
	            	return DEFAULTIMAGE;
	            }
	         
			}
				
				int index=random.nextInt(Config.SEEDNUM)%picStore.size();
				//System.out.println(picStore.size());
				//System.out.println(index);
				return picStore.get(index);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.warn("Gen random picture failed");
			return DEFAULTIMAGE;
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
}

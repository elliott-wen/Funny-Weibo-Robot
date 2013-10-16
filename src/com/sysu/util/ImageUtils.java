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
			logger.warn("URL Invalid");
			//e.printStackTrace();
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
	
}

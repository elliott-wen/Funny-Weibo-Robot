package com.sysu.plugins;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.sysu.util.ImageUtils;
import com.sysu.util.WordUtils;

import weibo4j.http.AccessToken;
import weibo4j.model.Status;
import weibo4j.util.URLEncodeUtils;

public class SamplePlugin implements Plugin {
	String aboutPlugin="It's a sample plugin for developer!";
	String pluginName="Sample Plugin";
	

	boolean initFlag=false;
	@Override
	public boolean init() {
		this.initFlag=true;
		return true;
	}

	@Override
	public Map<String,String> generateMessage() {
		Map<String,String> message=new HashMap<String,String>();
		message.put("Text", URLEncodeUtils.encodeURL(WordUtils.randomString(100)));
		message.put("Url",ImageUtils.genRandomPicture());
		return message;
	}



	@Override
	public String aboutPlugin() {
		// TODO Auto-generated method stub
		return this.aboutPlugin;
	}

	@Override
	public String getPluginName() {
		
		return this.pluginName;
	}

	@Override
	public boolean isReadytoRun() {
		// TODO Auto-generated method stub
		return true;
	}


}

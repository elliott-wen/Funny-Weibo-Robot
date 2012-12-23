package com.sysu.plugins;

import java.util.Map;

import weibo4j.http.AccessToken;
import weibo4j.model.Status;

public interface Plugin {
	boolean init();
	Map<String,String> generateMessage();
	String aboutPlugin();
	String getPluginName();
	boolean isReadytoRun();

}

A Funny Robot that amuses your girlfriend.

The robot running in sina weibo. A most popual chinese weibo in China. The robot will automatic grab some information
and @GF (or others).

The robot currently support following plugins

1.Broadcast Plugin that sends weather status everyday.
2.Funny picture plugin that sends funny pictures.

Of course, you could write your own plugins according the Sample Plugin (SamplePlugin.java) in source path.
You only need to implment the method in the interface Plugin.

	boolean init();//Init plugin. Do some network connect etc.
	Map<String,String> generateMessage();//Generate a message. Returning a message map. Map[text] for content and Map[url] for image.
	String aboutPlugin();//Tell the console about the plugin
	String getPluginName();//Tell the console about the name
	boolean isReadytoRun(); //Tell the application whether the plugin can be used.
	
When try to use the Robot. You must hava a developer permission from sina.
Please fill the information from SINA in config.properties. 
client_ID =#
client_SERCRET =#

And Fill the username and password in Src/Config/Config.java.
In Src/Config/Config.Java You could also change the people you want to amuse.
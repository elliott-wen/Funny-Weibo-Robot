package com.sysu.mainloop;



import org.apache.log4j.Logger;






public class Application extends Thread{
	
	
	/**
	 * @param args
	 */
	static Logger logger=Logger.getLogger(Application.class);
	
	
	
	

		public static void main(String[] args) {
		
		Messager speaker=new Messager();
		if(speaker.loadPlugin())
		{
			speaker.startThread();
		}
		else
		{
			logger.warn("Load Plugin Failed! No Plugins can work! Speaker will not work too");
		}
	}
	
	

}

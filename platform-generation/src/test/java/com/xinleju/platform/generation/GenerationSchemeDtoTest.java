package com.xinleju.platform.generation;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GenerationSchemeDtoTest {

	@Test
	public void load(){
		 
	        try {
	        	 ApplicationContext ctx = new  ClassPathXmlApplicationContext(new String[]{"applicationContext.xml","dubbo-producer.xml"});
	   		  //为保证服务一直开着，利用输入流的阻塞来模拟
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 


	}
}

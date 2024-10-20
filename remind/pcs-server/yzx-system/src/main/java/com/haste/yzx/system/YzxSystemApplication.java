package com.haste.yzx.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication(scanBasePackages = {"com.haste.yzx.common","com.haste.yzx.system"})
public class YzxSystemApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(YzxSystemApplication.class, args);
        Environment env = application.getEnvironment();
        String applicationName = env.getProperty("spring.application.name","");
        String address = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port","");
        String contextPath = env.getProperty("server.servlet.context-path","");
        if(contextPath.length()<1){
            contextPath = env.getProperty("spring.mvc.servlet.path","");
        }
        System.out.println( "\n----------------------------------------------------------\n\t" +
                "Application '"+applicationName+ " ' is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost: "+port+"\n\t" +
                "External: \thttp://"+address+":"+port+"\n\t"+
                "Doc: \thttp://"+address+":"+port+contextPath+"/doc.html\n"+
                "----------------------------------------------------------"
        );
    }

}

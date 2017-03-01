package ru.mike.felix2.config;

import org.apache.felix.main.AutoProcessor;
import org.apache.felix.main.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mkopylov on 27.02.17.
 */
@Configuration
@ComponentScan(basePackages = {
        "ru.mike.felix2"})
public class AppConfig extends WebMvcConfigurerAdapter {
    private static Framework felix;
    private static final Logger log = LogManager.getLogger(AppConfig.class);

    public AppConfig() {
        super();
    }

    @Bean
    public Framework felix() {
        try {
            Main.loadSystemProperties();
            Map<String, String> configProps = Main.loadConfigProperties();
            if (configProps == null) {
                configProps = new HashMap<>();
            }
            // (4) Copy framework properties from the system properties.
            Main.copySystemProperties(configProps);
            // (5) Use the specified auto-deploy directory over default.
            configProps.put(AutoProcessor.AUTO_DEPLOY_DIR_PROPERTY, "/home/mkopylov/IdeaProjects/felix-framework-5.6.1/bundle");
            configProps.put("felix.log.level", "4");
/*
            configProps.put(Constants.FRAMEWORK_SYSTEMPACKAGES,
                    "org.osgi.dto;version=1.0," +
                            "org.osgi.framework," +
                            "org.osgi.framework.dto;version=1.8," +
                            "org.osgi.framework.hooks.bundle;version=1.1," +
                            "org.osgi.framework.hooks.resolver;version=1.0," +
                            "org.osgi.framework.hooks.service;version=1.1," +
                            "org.osgi.framework.hooks.weaving;version=1.1," +
                            "org.osgi.framework.launch;version=1.2," +
                            "org.osgi.framework.namespace;version=1.1," +
                            "org.osgi.framework.startlevel;version=1.0," +
                            "org.osgi.framework.startlevel.dto;version=1.0," +
                            "org.osgi.framework.wiring;version=1.2," +
                            "org.osgi.resource,org.osgi.framework.wiring.dto;version=1.2," +
                            "org.osgi.resource.dto," +
                            "org.osgi.resource;version=1.0," +
                            "org.osgi.resource.dto;version=1.0," +
                            "org.osgi.service.packageadmin;version=1.2," +
                            "org.osgi.service.resolver;version=1.0," +
                            "org.osgi.service.startlevel;version=1.1," +
                            "org.osgi.service.url;version=1.0," +
                            "org.osgi.util.tracker;version=1.5.1");
*/
            configProps.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, "ru.mike.felix2.common;version=1.0.0");
            //configProps.put(Constants.FRAMEWORK_STORAGE_CLEAN, "true");

            FrameworkFactory ff = getFrameworkFactory();
            felix = ff.newFramework(configProps);
            felix.init();
            AutoProcessor.process(configProps, felix.getBundleContext());
            felix.start();
            //felix.waitForStop(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread("Felix Shutdown Hook") {
            public void run() {
                try {
                    if (felix != null) {
                        felix.stop();
                        felix.waitForStop(0);
                    }
                } catch (Exception ex) {
                    System.err.println("Error stopping framework: " + ex);
                }
            }
        });
        return felix;
    }

    private static FrameworkFactory getFrameworkFactory() throws Exception {
        java.net.URL url = AppConfig.class.getClassLoader().getResource(
                "META-INF/services/org.osgi.framework.launch.FrameworkFactory");
        if (url != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            try {
                for (String s = br.readLine(); s != null; s = br.readLine()) {
                    s = s.trim();
                    // Try to load first non-empty, non-commented line.
                    if ((s.length() > 0) && (s.charAt(0) != '#')) {
                        return (FrameworkFactory) Class.forName(s).newInstance();
                    }
                }
            } finally {
                if (br != null)
                    br.close();
            }
        }
        throw new Exception("Could not find framework factory.");
    }
}

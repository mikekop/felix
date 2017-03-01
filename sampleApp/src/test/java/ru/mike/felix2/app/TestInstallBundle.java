package ru.mike.felix2.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.mike.felix2.common.SampleService;
import ru.mike.felix2.config.AppConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by mkopylov on 14.02.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class TestInstallBundle {

    @Autowired
    private Framework felix;

    @Test
    public void testInstallBundle() {
        assertNotNull("Felix is not initialized", felix);
        try {

/*
            File bundleFile = new File("/home/mkopylov/tmp/common.jar");
            InputStream is = new FileInputStream(bundleFile);
            felix.getBundleContext().installBundle("bundle1", is);
            for (Bundle bundle : felix.getBundleContext().getBundles()) {
                System.out.println("after 1 = " + bundle.getSymbolicName());
            }
            Bundle common = felix.getBundleContext().getBundle("bundle1");
            common.start();
*/


            File bundleFile = new File("/home/mkopylov/tmp/sampleBundle.jar");
            InputStream is = new FileInputStream(bundleFile);
            for (Bundle bundle : felix.getBundleContext().getBundles()) {
                System.out.println("before 1 = " + bundle.getSymbolicName());
            }
            felix.getBundleContext().installBundle("bundle2", is);
            for (Bundle bundle : felix.getBundleContext().getBundles()) {
                System.out.println("after 2 = " + bundle.getSymbolicName());
            }
            assertEquals("Bundle install failed", 2, felix.getBundleContext().getBundles().length);

            Bundle server = felix.getBundleContext().getBundle("bundle2");
            server.start();
            ServiceReference<SampleService> serref = felix.getBundleContext().getServiceReference(SampleService.class);
            Object service = felix.getBundleContext().getService(serref);
            System.out.println("1 = " + service.getClass().getClassLoader());
            System.out.println("2= " + SampleService.class.getClassLoader());
            SampleService servI = (SampleService) service;
            servI.connect(null);
            servI.check();
            servI.pay();
            servI.cleanup();
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            server.stop();

        } catch (FileNotFoundException | BundleException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }
}

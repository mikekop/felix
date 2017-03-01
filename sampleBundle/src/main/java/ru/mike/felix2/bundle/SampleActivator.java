package ru.mike.felix2.bundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import ru.mike.felix2.common.SampleService;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by mkopylov on 28.02.17.
 */
public class SampleActivator implements BundleActivator {
    public void start(BundleContext context) throws Exception {
        System.out.println("Bundle started");
        context.registerService(SampleService.class.getName(), new SampleServiceImpl(new HashMap<String, String>()), new Hashtable<String, String>());
        for (ServiceReference sr : context.getAllServiceReferences(SampleService.class.getName(), null)) {
            System.out.println("refer " + sr.toString());
        }
    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("Bundle stoped");
    }
}

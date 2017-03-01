package ru.mike.felix2.bundle;

import ru.mike.felix2.common.SampleService;

import java.util.HashMap;

/**
 * Created by mkopylov on 28.02.17.
 */
public class SampleServiceImpl implements SampleService {
    SampleServiceImpl(HashMap<String, String> hm) {
        System.out.println("impl constructed ");
        init(hm);
    }

    public void init(HashMap<String, String> initParams) {
        System.out.println("service init called");
    }

    public void connect(HashMap<String, Object> connectParams) {
        System.out.println("service connect called");
    }

    public void check() {
        System.out.println("service check called");
    }

    public void pay() {
        System.out.println("service pay called");
    }

    public void cleanup() {
        System.out.println("service cleanup called");
    }
}

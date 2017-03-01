package ru.mike.felix2.common;

import java.util.HashMap;

/**
 * Created by mkopylov on 28.02.17.
 */
public interface SampleService {
    void init(HashMap<String, String> initParams);
    void connect(HashMap<String, Object> connectParams);
    void check();
    void pay();
    void cleanup();
}

package org.trypticon.talker.model;

/**
 * Interface to implement if your component can be started and stopped.
 */
public interface Startable {

    void start();

    void stop();
}

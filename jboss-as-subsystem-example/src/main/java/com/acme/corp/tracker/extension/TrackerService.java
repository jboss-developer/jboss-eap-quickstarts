package com.acme.corp.tracker.extension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class TrackerService implements Service<TrackerService>{

    private AtomicLong tick = new AtomicLong(10000);

    private Set<String> deployments = Collections.synchronizedSet(new HashSet<String>());

    private Set<String> coolDeployments = Collections.synchronizedSet(new HashSet<String>());

    private final String suffix;

    private Thread OUTPUT = new Thread() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(tick.get());
                    System.out.println("Current deployments deployed while " + suffix + " tracking active:\n" + deployments + "\nCool: " + coolDeployments.size());
                } catch (InterruptedException e) {
                    interrupted();
                    break;
                }
            }
        }
    };

    public TrackerService(String suffix, long tick) {
        this.suffix = suffix;
        this.tick.set(tick);
    }

    @Override
    public TrackerService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    @Override
    public void start(StartContext context) throws StartException {
        OUTPUT.start();
    }

    @Override
    public void stop(StopContext context) {
        OUTPUT.interrupt();
    }

    public static ServiceName createServiceName(String suffix) {
        return ServiceName.JBOSS.append("tracker", suffix);
    }

    public void addDeployment(String name) {
        deployments.add(name);
    }

    public void addCoolDeployment(String name) {
        coolDeployments.add(name);
    }

    public void removeDeployment(String name) {
        deployments.remove(name);
        coolDeployments.remove(name);
    }

    void setTick(long tick) {
        this.tick.set(tick);
    }

    public long getTick() {
        return this.tick.get();
    }
}

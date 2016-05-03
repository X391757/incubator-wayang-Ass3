package org.qcri.rheem.spark.test;

import org.qcri.rheem.core.api.Configuration;
import org.qcri.rheem.core.plan.executionplan.Channel;
import org.qcri.rheem.core.platform.ChannelDescriptor;
import org.qcri.rheem.core.util.RheemCollections;
import org.qcri.rheem.java.channels.CollectionChannel;
import org.qcri.rheem.spark.channels.RddChannel;
import org.qcri.rheem.spark.platform.SparkExecutor;

import java.util.Collection;

/**
 * Utility to create {@link Channel}s in tests.
 */
public class ChannelFactory {

    public static RddChannel.Instance createRddChannelInstance(ChannelDescriptor rddChannelDescriptor, Configuration configuration) {
        return (RddChannel.Instance) rddChannelDescriptor.createChannel(null, configuration).createInstance();
    }

    public static RddChannel.Instance createRddChannelInstance(Configuration configuration) {
        return createRddChannelInstance(RddChannel.UNCACHED_DESCRIPTOR, configuration);
    }

    public static RddChannel.Instance createRddChannelInstance(Collection<?> data,
                                                               SparkExecutor sparkExecutor,
                                                               Configuration configuration) {
        RddChannel.Instance instance = createRddChannelInstance(configuration);
        instance.accept(sparkExecutor.sc.parallelize(RheemCollections.asList(data)), sparkExecutor);
        return instance;
    }

    public static CollectionChannel.Instance createCollectionChannelInstance(Configuration configuration) {
        return (CollectionChannel.Instance) CollectionChannel.DESCRIPTOR.createChannel(null, configuration).createInstance();
    }

    public static CollectionChannel.Instance createCollectionChannelInstance(Collection<?> colleciton, Configuration configuration) {
        CollectionChannel.Instance instance = createCollectionChannelInstance(configuration);
        instance.accept(colleciton);
        return instance;
    }

}
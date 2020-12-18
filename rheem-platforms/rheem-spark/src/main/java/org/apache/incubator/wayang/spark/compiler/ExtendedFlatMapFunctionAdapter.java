package io.rheem.rheem.spark.compiler;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import io.rheem.rheem.core.function.ExecutionContext;
import io.rheem.rheem.core.function.FunctionDescriptor;
import io.rheem.rheem.spark.execution.SparkExecutionContext;

import java.util.Iterator;

/**
 * Implements a {@link FlatMapFunction} that calls {@link io.rheem.rheem.core.function.ExtendedFunction#open(ExecutionContext)}
 * of its implementation before delegating the very first {@link Function#call(Object)}.
 */
public class ExtendedFlatMapFunctionAdapter<InputType, OutputType> implements FlatMapFunction<InputType, OutputType> {

    private final FunctionDescriptor.ExtendedSerializableFunction<InputType, Iterable<OutputType>> impl;

    private final SparkExecutionContext executionContext;

    private boolean isFirstRun = true;

    public ExtendedFlatMapFunctionAdapter(FunctionDescriptor.ExtendedSerializableFunction extendedFunction,
                                          SparkExecutionContext sparkExecutionContext) {
        this.impl = extendedFunction;
        this.executionContext = sparkExecutionContext;
    }

    @Override
    public Iterator<OutputType> call(InputType v1) throws Exception {
        if (this.isFirstRun) {
            this.impl.open(this.executionContext);
            this.isFirstRun = false;
        }

        return this.impl.apply(v1).iterator();
    }

}
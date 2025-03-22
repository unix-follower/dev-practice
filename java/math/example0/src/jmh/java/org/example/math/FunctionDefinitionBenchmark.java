package org.example.math;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 2, time = 1)
@Threads(2)
@Fork(1)
public class FunctionDefinitionBenchmark {
    @State(Scope.Benchmark)
    public static class FDefinitionState {
        @Param({"0", "2", "-1"})
        double x;
    }

    @Benchmark
    public void fDefinition(Blackhole blackhole, FDefinitionState fDefinitionState) {
        var result = FunctionDefinition.fDefinition(fDefinitionState.x);
        blackhole.consume(result);
    }
}

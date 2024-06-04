package ar.edu.itba.pod.client.queries;

import com.hazelcast.core.HazelcastInstance;

public interface Query {
    void execute(HazelcastInstance hazelcastInstance, String outputPath);
}
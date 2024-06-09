package ar.edu.itba.pod.client.queries;

import ar.edu.itba.pod.api.HazelcastCollections;
import ar.edu.itba.pod.api.collators.InfractionPairCollator;
import ar.edu.itba.pod.api.combiners.InfractionPairCombinerFactory;
import ar.edu.itba.pod.api.interfaces.TriConsumer;
import ar.edu.itba.pod.api.mappers.InfractionPairMapper;
import ar.edu.itba.pod.api.models.InfractionPair;
import ar.edu.itba.pod.api.models.dtos.InfractionDto;
import ar.edu.itba.pod.api.models.dtos.InfractionFineDto;
import ar.edu.itba.pod.api.reducers.InfractionPairReducerFactory;
import ar.edu.itba.pod.client.utils.Constants;
import ar.edu.itba.pod.client.utils.CsvMappingConfig;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class InfractionPairsQuery extends Query{
    private static final String HEADER = "Group;Infraction A;Infraction B";

    @Override
    protected TriConsumer<String[], CsvMappingConfig, Integer> ticketsConsumer() {
        IMap<Integer, InfractionFineDto> infractionFinesMap = hazelcastInstance.getMap(HazelcastCollections.INFRACTION_FINE_MAP.getName());
        IMap<String, InfractionDto> infractions = hazelcastInstance.getMap(HazelcastCollections.INFRACTIONS_MAP.getName());

        return (fields, config, id) -> {
            if (fields.length >= Constants.FIELD_COUNT) {
                try {
                    Double fineAmount = Double.parseDouble(fields[config.getColumnIndex("fineAmount")]);
                    String infractionCode = fields[config.getColumnIndex("infractionCode")];
                    InfractionDto infractionDto = infractions.get(infractionCode);

                    //TODO: remove this block and get the infraction definition from the dto (because exists)
                    //TODO: its just to test the complete dataset of CHI
                    String infractionDefinition;
                    if (infractionDto != null) {
                        infractionDefinition = infractionDto.getDefinition();
                    } else {
                        infractionDefinition = fields[config.getColumnIndex("infractionDefinition")];
                    }
                    infractionFinesMap.putIfAbsent(id, new InfractionFineDto(infractionDefinition, fineAmount));
                } catch (Exception e) {
                    logger.error("Error processing ticket data", e);
                }
            } else {
                logger.error(String.format("Invalid line format, expected %d fields, found %d", Constants.FIELD_COUNT, fields.length));
            }
        };
    }

    @Override
    protected void executeJob() throws ExecutionException, InterruptedException {
        IMap<Integer, InfractionFineDto> infractionFinesMap = hazelcastInstance.getMap(HazelcastCollections.INFRACTION_FINE_MAP.getName());

        System.out.println(infractionFinesMap.size()); //TODO: remove, only for debugging parallel reading

        JobTracker jobTracker = hazelcastInstance.getJobTracker(Constants.QUERY_5_JOB_TRACKER_NAME);
        KeyValueSource<Integer, InfractionFineDto> source = KeyValueSource.fromMap(infractionFinesMap);
        Job<Integer, InfractionFineDto> job = jobTracker.newJob(source);

        final ICompletableFuture<TreeSet<InfractionPair>> future = job
                .mapper(new InfractionPairMapper())
                .combiner(new InfractionPairCombinerFactory())
                .reducer(new InfractionPairReducerFactory())
                .submit(new InfractionPairCollator());

        TreeSet<InfractionPair> result = future.get();
        writeData(HEADER, result);
        infractionFinesMap.clear();
    }
}

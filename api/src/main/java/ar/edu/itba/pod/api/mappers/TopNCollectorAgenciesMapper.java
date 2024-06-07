package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.AgencyFineDto;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.math.BigDecimal;

@SuppressWarnings("deprecation")
public class TopNCollectorAgenciesMapper implements Mapper<Integer, AgencyFineDto, String, BigDecimal> {
    @Override
    public void map(Integer integer, AgencyFineDto agencyFineDto, Context<String, BigDecimal> context) {
        context.emit(agencyFineDto.getIssuingAgency(), BigDecimal.valueOf(agencyFineDto.getFineAmount()));
    }
}

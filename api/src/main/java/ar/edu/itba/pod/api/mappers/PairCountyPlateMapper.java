package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.dtos.PlateCountyDateDto;
import ar.edu.itba.pod.api.models.dtos.PlateCountyDto;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Date;

@SuppressWarnings("deprecation")
public class PairCountyPlateMapper implements Mapper<Integer, PlateCountyDateDto, PlateCountyDto, Integer> {
    private final Date from;
    private final Date to;

    public PairCountyPlateMapper(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    private boolean isDateWithinRange(Date targetDate) {
        return (this.from.compareTo(targetDate) <= 0 && this.to.compareTo(targetDate) >= 0);
    }

    @Override
    public void map(Integer integer, PlateCountyDateDto plateCountyDateDto, Context<PlateCountyDto, Integer> context) {
        if (isDateWithinRange(plateCountyDateDto.getDate())) {
            context.emit(new PlateCountyDto(plateCountyDateDto.getPlate(), plateCountyDateDto.getCounty()),1);
        }
    }
}

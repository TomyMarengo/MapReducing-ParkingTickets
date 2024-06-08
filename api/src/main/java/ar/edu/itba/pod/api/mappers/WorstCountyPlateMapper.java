package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.dtos.PlateCountyDto;
import ar.edu.itba.pod.api.models.dtos.PlateTicketsDto;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class WorstCountyPlateMapper implements Mapper<PlateCountyDto, Integer, String, PlateTicketsDto> {

    @Override
    public void map(PlateCountyDto plateCountyDto, Integer integer, Context<String, PlateTicketsDto> context) {
        context.emit(plateCountyDto.getCounty(), new PlateTicketsDto(plateCountyDto.getPlate(), integer));
    }
}
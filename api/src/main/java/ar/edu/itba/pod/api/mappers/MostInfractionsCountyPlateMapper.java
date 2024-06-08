package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.dtos.InfractionPlateDateDto;
import ar.edu.itba.pod.api.models.dtos.InfractionPlateDto;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Date;

@SuppressWarnings("deprecation")
public class MostInfractionsCountyPlateMapper implements Mapper<Integer, InfractionPlateDateDto, String, String> {


    private final  Date from;
    private final  Date to;

    public MostInfractionsCountyPlateMapper(final Date from, final Date to)
    {
        this.from = from;
        this.to = to;
    }

    @Override
    public void map(Integer integer, InfractionPlateDateDto infractionPlateDateDto, Context<String, String> context) {
        if (isDateWithinRange(infractionPlateDateDto.getDate()))
        {
            context.emit(infractionPlateDateDto.getCounty(),infractionPlateDateDto.getPlate());
        }
    }


    private boolean isDateWithinRange(Date targetDate) {
        return (from.compareTo(targetDate) <= 0 && to.compareTo(targetDate) >= 0);
    }
}

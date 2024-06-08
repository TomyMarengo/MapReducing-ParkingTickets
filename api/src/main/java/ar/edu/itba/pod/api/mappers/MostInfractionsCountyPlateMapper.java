package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.models.dtos.InfractionPlateDateDto;
import ar.edu.itba.pod.api.models.dtos.InfractionPlateDto;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.Date;

@SuppressWarnings("deprecation")
public class MostInfractionsCountyPlateMapper implements Mapper<Integer, InfractionPlateDateDto, InfractionPlateDto, Integer> {


    private final transient Date from;
    private final transient Date to;

    public MostInfractionsCountyPlateMapper(final Date from, final Date to)
    {
        this.from = from;
        this.to = to;
    }

    @Override
    public void map(Integer integer, InfractionPlateDateDto infractionPlateDateDto, Context<InfractionPlateDto, Integer> context) {
        if (isDateWithinRange(infractionPlateDateDto.getDate()))
        {
            context.emit(new InfractionPlateDto(infractionPlateDateDto.getPlate(),infractionPlateDateDto.getCounty()),1);
        }
    }


    private boolean isDateWithinRange(Date targetDate) {
        return (from.compareTo(targetDate) <= 0 && to.compareTo(targetDate) >= 0);
    }
}

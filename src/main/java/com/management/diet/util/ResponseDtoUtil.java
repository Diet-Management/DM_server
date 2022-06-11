package com.management.diet.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseDtoUtil {
    private static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static <D,T> D mapping(final T entity, Class<D> dto){
        return mapper.map(entity, dto);
    }

    public static <D, T> List<D> mapAll(final List<T> entityList, Class<D> dto) {
        return entityList.stream()
                .map(entity -> mapper.map(entity, dto))
                .collect(Collectors.toList());
    }
}

package com.mitocode.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UtilMapper{

    @Autowired
    @Qualifier("defaultMapper")
    protected ModelMapper mapper;

    public <D, T> D toDto(final T entity, Class<D> dtoClass) {
        return mapper.map(entity, dtoClass);
    }

    public <D, T> T toEntity(final D dto, Class<T> entityClass) {
        return mapper.map(dto, entityClass);
    }


}

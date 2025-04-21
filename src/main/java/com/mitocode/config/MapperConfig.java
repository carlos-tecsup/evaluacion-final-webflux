package com.mitocode.config;

import com.mitocode.dto.CourseResponse;
import com.mitocode.dto.TuitionDTO;
import com.mitocode.dto.TuitionResponse;
import com.mitocode.model.Course;
import com.mitocode.model.Tuition;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class MapperConfig {

    @Bean(name = "defaultMapper")
    public ModelMapper defaultMapper(){
     return new ModelMapper();
    }

    @Bean(name = "tuitionMapper")
    public ModelMapper tuitionMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


        //Lectura
        mapper.createTypeMap(Tuition.class, TuitionResponse.class)
                .addMapping(Tuition::getId, (dest, v) -> dest.setId((String) v))
                .addMapping(tuition->tuition.getStudent().getName(), (dest, v) -> dest.setNameStudent((String) v))
                .addMapping(tuition->tuition.getStudent().getLastName(), (dest, v) -> dest.setLastNameStudent((String) v ))
                .addMapping(Tuition::getRegistrationDate, (dest, v) -> dest.setRegistrationDate((LocalDateTime) v ))
                .addMapping(tuition -> tuition.getCourses() == null ?
                        new ArrayList<String>() :
                        tuition.getCourses().stream()
                                .map(Course::getName)
                                .collect(Collectors.toList()), TuitionResponse::setCoursesName);

        return mapper;
    }
}

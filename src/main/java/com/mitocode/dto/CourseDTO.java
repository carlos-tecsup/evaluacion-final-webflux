package com.mitocode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDTO {
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String acronym;
}

package com.mitocode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TuitionResponse {
    private String id;
    private String nameStudent;
    private String lastNameStudent;
    private List<CourseResponse> coursesName;
    private LocalDateTime registrationDate;

}

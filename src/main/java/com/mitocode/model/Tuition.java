package com.mitocode.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "tuitions")
public class Tuition {
    @Id
    private String id;
    @Field
    private LocalDateTime registrationDate;
    @Field
    private Student student;
    @Field
    private List<Course> courses;
    @Field
    private boolean status;
}

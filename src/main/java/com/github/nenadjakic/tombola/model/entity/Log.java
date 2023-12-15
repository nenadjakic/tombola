package com.github.nenadjakic.tombola.model.entity;

import com.github.nenadjakic.tombola.util.ListOfIntegersToStringConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "LOGS")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime dateTime;

    @Column
    private String combination;

    @Convert(converter = ListOfIntegersToStringConverter.class)
    @Column
    private List<Integer> pickedNumbers = new LinkedList<>();
}

package com.example.awbdmicroservices.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="event")
public class Event extends RepresentationModel<Event> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Date date;

    private String name;

    private Integer duration;

    private Integer price;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Reservation> reservations;
}

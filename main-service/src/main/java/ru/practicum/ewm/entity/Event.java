package ru.practicum.ewm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.status.EventStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

    private Boolean paid;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator")
    private User initiator;

    private String description;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private EventStatus state;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @Column(name = "published_on")
    private LocalDateTime published;

    private Integer views;
}

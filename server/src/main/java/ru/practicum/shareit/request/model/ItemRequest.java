package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long itemRequestId;

    @Column(name = "description")
    private String requestDescription;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "request_date")
    private LocalDateTime requestDate;
}

package edu.cit.studentclearancesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private ClearanceTask task;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    private String message;

    private LocalDateTime createdAt;
}

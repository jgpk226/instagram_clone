package com.instagram.instagram.modules.Account;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Follow {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Account follower;

    @ManyToOne
    private Account following;

    private LocalDateTime followingDate;
}

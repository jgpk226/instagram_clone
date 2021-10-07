package com.instagram.instagram.modules.Account;

import com.instagram.instagram.modules.Post.Post;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String realName;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private String description;

    @OneToMany(mappedBy = "writer")
    @Builder.Default
    @OrderBy(value = "savedDate DESC")
    private Set<Post> postList = new HashSet<>();

    private boolean useProfileImage;

    private boolean isEmailChecked;

    private LocalDateTime enrollmentDateTime;

    private LocalDateTime recentVisitedDateTime;
}

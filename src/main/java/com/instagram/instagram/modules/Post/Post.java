package com.instagram.instagram.modules.Post;

import com.instagram.instagram.modules.Account.Account;
import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Post {

    @Id @GeneratedValue
    private Long id;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    private String content;

    @ManyToOne
    private Account writer;

    @OneToMany
    @Builder.Default
    private Set<Account> likedBy = new HashSet<>();

    private LocalDateTime savedDate;


    public String getFormattedSavedDate() {
        String strDate = savedDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

        if(strDate.equals(nowDate)) {
            Duration duration = Duration.between(savedDate, LocalDateTime.now());
            if((int) duration.getSeconds()/3600 > 0) {
                return (int) duration.getSeconds()/3600 + "시간 전";
            } else {
                return (int) duration.getSeconds()/60 + "분 전";
            }
        } else {
            return strDate;
        }
    }
}

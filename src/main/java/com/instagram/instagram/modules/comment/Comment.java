package com.instagram.instagram.modules.comment;

import com.instagram.instagram.modules.Account.Account;
import com.instagram.instagram.modules.Post.Post;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Comment {

    @Id @GeneratedValue
    private Long id;

    private String comment;

    @ManyToOne
    private Account writer;

    @ManyToOne
    private Post forPost;

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

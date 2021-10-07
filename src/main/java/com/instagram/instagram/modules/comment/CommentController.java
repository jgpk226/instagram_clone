package com.instagram.instagram.modules.comment;

import com.instagram.instagram.modules.Account.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
@Log4j2
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/new")
    public String newComment(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") Account account,
                             Long postId, String comment, HttpServletRequest request) {
        log.info(postId);
        log.info(comment);
        commentService.saveComment(postId, comment, account);

        // 이전 페이지로 url 생성 (http://localhost:8080/ 뒤부터 적용)
        String referer = (String)request.getHeader("REFERER");
        String[] name = referer.split("/");
        String url = "";
        for(int i=3; i<name.length; i++) {
            url += name[i];
        }

        return "redirect:/" + url;
    }
}

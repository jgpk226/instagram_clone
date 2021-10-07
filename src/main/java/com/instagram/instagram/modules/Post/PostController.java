package com.instagram.instagram.modules.Post;

import com.instagram.instagram.modules.Account.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Log4j2
public class PostController {

    private final PostRepository postRepository;

    private final PostService postService;


    @GetMapping("/new-post")
    public String newPostForm(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")Account account, Model model) {
        model.addAttribute("account", account);
        model.addAttribute("newPostForm", new NewPostForm());

        return "post/new_post";
    }

    @PostMapping("/new-post")
    public String newPost(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")Account account,
                          @Valid @ModelAttribute NewPostForm newPostForm, Errors errors, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute("account", account);
            return "post/new_post";
        } else {
            postService.newPost(account, newPostForm);
            return "redirect:/" + account.getUsername();
        }
    }

    @PostMapping("/like")
    public String like(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")Account account,
                       @RequestParam("postId") Long postId, Model model, HttpServletRequest request) {
        boolean isSaved = postService.saveOrDeleteLike(postId, account.getId());

        // 리다이렉트니까 이것들 다 없어도 될껄???
        model.addAttribute("account", account);
        model.addAttribute("pageAccount", postRepository.findById(postId).get().getWriter());
        model.addAttribute("post", postRepository.findById(postId).get());
        model.addAttribute("isLiked", postRepository.findById(postId).get().getLikedBy().contains(account));

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

package com.instagram.instagram.modules.main;

import com.instagram.instagram.modules.Account.Account;
import com.instagram.instagram.modules.Account.AccountRepository;
import com.instagram.instagram.modules.Account.AccountService;
import com.instagram.instagram.modules.Account.FollowService;
import com.instagram.instagram.modules.Post.Post;
import com.instagram.instagram.modules.Post.PostRepository;
import com.instagram.instagram.modules.Post.PostService;
import com.instagram.instagram.modules.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@Log4j2
public class MainController {

    private final AccountRepository accountRepository;

    private final PostRepository postRepository;

    private final AccountService accountService;

    private final PostService postService;

    private final FollowService followService;

    private final CommentRepository commentRepository;


    @GetMapping("/")
    public String loginForm(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") Account account, Model model,
                            @PageableDefault(page = 0, size = 4, sort = "savedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        if(account == null) {
            return "login";
        } else {
            model.addAttribute("account", account);
            model.addAttribute("pageablePost", postService.findByAccount(account, pageable));
            return "index";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // if 는 account 메인 = 프로필 페이지 조회
    // else 는 '?post='을 보낸 경우 = 게시물 조회
    @GetMapping("/{userName}")
    public String profileForm(@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") Account account, Model model,
                              @PathVariable("userName") String userName , @RequestParam(required = false) Long post) {
        if(post == null) {
            Account pageAccount = accountRepository.findByUsername(userName);

            // 의미 없는 코드
            if(pageAccount == null) {
                model.addAttribute("account", account);
                return "redirect:/";
            }

            model.addAttribute("account", account);
            model.addAttribute("pageAccount", pageAccount);
            model.addAttribute("followers", accountService.countFollower(pageAccount));
            model.addAttribute("followings", accountService.countFollowing(pageAccount));
            model.addAttribute("postList", pageAccount.getPostList());
            model.addAttribute("isFollowing", followService.isFollowing(account, pageAccount));

            return "account/profile";
        } else {
            Account pageAccount = accountRepository.findByUsername(userName);
            Post getPost = postRepository.findById(post).get();

            model.addAttribute("account", account);
            model.addAttribute("pageAccount", pageAccount);
            model.addAttribute("post", getPost);
            model.addAttribute("isLiked", getPost.getLikedBy().contains(account));
            model.addAttribute("commentList", commentRepository.findAllByForPost(getPost));

            return "/post/get-post";
        }
    }

}

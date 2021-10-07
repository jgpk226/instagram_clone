package com.instagram.instagram.modules.Post;

import com.instagram.instagram.modules.Account.Account;
import com.instagram.instagram.modules.Account.QAccount;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Set;


public class PostRepositoryExtensionImpl extends QuerydslRepositorySupport implements PostRepositoryExtension {

    public PostRepositoryExtensionImpl() {
        super(Post.class);
    }

    @Override
    public Page<Post> findByAccount(Account account, Set<Account> accountSet, Pageable pageable) {
        JPQLQuery<Post> jpqlQuery = from(QPost.post);

        BooleanBuilder builder = new BooleanBuilder();
        builder.or(QPost.post.writer.eq(account));
        for(Account a : accountSet) {
            builder.or(QPost.post.writer.eq(a));
        }
        jpqlQuery.where(builder);
        jpqlQuery.leftJoin(QPost.post.writer, QAccount.account).fetchJoin();
        jpqlQuery.distinct();
        JPQLQuery<Post> pageableQuery = getQuerydsl().applyPagination(pageable, jpqlQuery);
        QueryResults<Post> fetchResults = pageableQuery.fetchResults();


        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }
}

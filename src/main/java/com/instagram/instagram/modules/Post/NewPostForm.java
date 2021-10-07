package com.instagram.instagram.modules.Post;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

@Data
public class NewPostForm {

    @NotBlank
    private String image;

    @Length(max = 140)
    private String content;
}

package com.nexus.alumcon.dto.post;

import com.nexus.alumcon.dto.user.UserBasicDTO;
import com.nexus.alumcon.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    @NotBlank
    private String title;
    @NotBlank
    @Size(min = 10, max = 5000)
    private String content;
    private String username;
    private String timeStamp;

    public PostDTO(Post post) {
        setTitle(post.getTitle());
        setContent(post.getContent());
        setUsername(post.getUsername());
        setTimeStamp(post.getTimeStamp());
    }
}

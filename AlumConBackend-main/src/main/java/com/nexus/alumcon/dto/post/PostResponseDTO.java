package com.nexus.alumcon.dto.post;

import com.nexus.alumcon.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.repository.query.Param;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private String id;
    private String username;

    @NotBlank
    private String title;
    @NotBlank
    @Size(min = 10, max = 5000)
    private String content;
    private String timeStamp;
    private int voteCount;
    private int ownVote;

    public PostResponseDTO(Post post, String username) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUsername();
        this.timeStamp = post.getTimeStamp();
        this.voteCount = post.getVoteCount();
        this.ownVote = post.getUserVote(username);
    }
}

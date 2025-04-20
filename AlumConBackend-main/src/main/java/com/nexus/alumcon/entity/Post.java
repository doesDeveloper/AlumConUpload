package com.nexus.alumcon.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    @NotBlank
    private String title;
    @NotBlank
    @Size(min = 10, max = 5000)
    private String content;
    private String username;
    private String timeStamp;
    @Builder.Default
    private int voteCount = 0;
    @Builder.Default
    private Map<String, Integer> votes = new HashMap<>();

    //votes count??? upvote and downvote count?

    public int getUserVote(String username) {
        return votes.getOrDefault(username, 0);
    }
}

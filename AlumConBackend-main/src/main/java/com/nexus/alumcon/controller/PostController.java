package com.nexus.alumcon.controller;

import com.nexus.alumcon.dto.post.PostResponseDTO;
import com.nexus.alumcon.entity.Post;
import com.nexus.alumcon.service.PostService;
import com.nexus.alumcon.service.UserProfileDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts/")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserProfileDetailsService userProfileDetailsService;

    @PostMapping("/create")
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody @Valid PostResponseDTO post, Principal principal) {
        if (principal.getName() == null)
            return ResponseEntity.notFound().build();
        Post newPost = new Post();
        newPost.setTitle(post.getTitle());
        newPost.setContent(post.getContent());
        newPost.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        newPost.setUsername(principal.getName());
        Post savedPost = postService.save(newPost);

        // Patch values for returning
        post.setId(savedPost.getId());
        post.setUsername(savedPost.getUsername());
        post.setTimeStamp(savedPost.getTimeStamp());
        post.setVoteCount(savedPost.getVoteCount());
        return ResponseEntity.ok(post);
    }

    @GetMapping("/indiv/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable String id, Principal principal) {
        if (principal.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Post post = postService.findById(id);
        if (post == null)
            return ResponseEntity.notFound().build();
        PostResponseDTO postResponseDTO = new PostResponseDTO(post, principal.getName());
        return ResponseEntity.ok(postResponseDTO);
    }

    @GetMapping("/feed")
    public ResponseEntity<Page<PostResponseDTO>> getFeed(@RequestParam(value = "page", defaultValue = "0") int page, Principal principal) {
        if (principal.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Page<Post> posts = postService.getAllPosts(PageRequest.of(page, 10, Sort.by("timeStamp").descending()));
//        Page<PostResponseDTO> postResponseDTOs = posts.map(PostResponseDTO::new);
        Page<PostResponseDTO> postResponseDTOs = posts.map(post -> new PostResponseDTO(post, principal.getName()));
        return ResponseEntity.ok(postResponseDTOs);

    }

    @GetMapping("/feed/{username}")
    public ResponseEntity<Page<PostResponseDTO>> getFeedByUsername(@PathVariable(value = "username") String username, @RequestParam(value = "page", defaultValue = "0") int page, Principal principal) {
        if (principal.getName().isEmpty())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Page<Post> posts = postService.getAllPostsByUsername(username, PageRequest.of(page, 10, Sort.by("timeStamp").descending()));
//        Page<PostResponseDTO> postResponseDTOs = posts.map(PostResponseDTO::new);
        Page<PostResponseDTO> postResponseDTOs = posts.map(post -> new PostResponseDTO(post, principal.getName()));

        return ResponseEntity.ok(postResponseDTOs);
    }


    // Votes Handling
    @PostMapping("/indiv/{postId}/vote")
    public ResponseEntity<String> votePost(@PathVariable(value = "postId") String postId,
                                           @RequestParam(value = "voteValue", defaultValue = "1") int voteValue,
                                           Principal principal) {
        if (voteValue != 1 && voteValue != -1 && voteValue != 0) {
            return ResponseEntity.badRequest().body("Vote must be 1 (upvote) or -1 (downvote) or 0 (no vote)");
        }

        Post post = postService.findById(postId);
        if (post == null)
            return ResponseEntity.notFound().build();


        String username = principal.getName();
        if (!userProfileDetailsService.existsByUsername(username)) {
            return ResponseEntity.notFound().build();
        }

        Integer previousVote = post.getVotes().get(username);

        if (previousVote != null) {
            post.setVoteCount(post.getVoteCount() - previousVote);
        }

        post.getVotes().put(username, voteValue);
        post.setVoteCount(post.getVoteCount() + voteValue);

        postService.save(post);
        return ResponseEntity.ok("" + post.getVoteCount());
    }
}

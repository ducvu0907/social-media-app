package com.dev.SocialMedia.comment;

import com.dev.SocialMedia.common.ApiResponse;
import com.dev.SocialMedia.common.Mapping;
import com.dev.SocialMedia.entity.Comment;
import com.dev.SocialMedia.entity.Post;
import com.dev.SocialMedia.entity.User;
import com.dev.SocialMedia.exception.CustomException;
import com.dev.SocialMedia.post.PostRepository;
import com.dev.SocialMedia.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final Mapping mapping;

    public ApiResponse createCommentOnPost(Long postId, CreateCommentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException("user id not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("post id not found"));
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPost(post);
        comment.setUser(user);
        comment.setParent(null);
        comment.setChildren(new ArrayList<>());
        commentRepository.save(comment);
        return new ApiResponse("success", "comment on post successfully", mapping.mapCommentToCommentDetailsDto(comment));
    }

    public ApiResponse getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException("comment id not found"));
        return new ApiResponse("success", "get comment successfully", mapping.mapCommentToCommentDetailsDto(comment));
    }

    public ApiResponse updateComment(Long commentId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException("comment id not found"));
        comment.setContent(request.getContent());
        commentRepository.save(comment);
        return new ApiResponse("success", "update comment successfully", mapping.mapCommentToCommentDetailsDto(comment));
    }

    public ApiResponse deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException("comment id not found"));
        commentRepository.delete(comment);
        return new ApiResponse("success", "delete comment successfully", null);
    }

    public ApiResponse replyToComment(Long postId, Long commentId, CreateCommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("post id not found"));
        User user = userRepository.findById(postId)
                .orElseThrow(() -> new CustomException("user id not found"));
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException("comment id not found"));
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setPost(post);
        comment.setUser(user);
        comment.setParent(parentComment);
        comment.setChildren(new ArrayList<>());
        return new ApiResponse("success", "reply to comment successfully", mapping.mapCommentToCommentDetailsDto(comment));
    }
}

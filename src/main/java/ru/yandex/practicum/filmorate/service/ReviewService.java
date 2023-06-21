package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {

    ReviewDTO addReview(ReviewDTO review);

    ReviewDTO getReviewById(long reviewId);

    List<ReviewDTO> getAllReviews(Long filmId, Long count);

    ReviewDTO updateReview(ReviewDTO reviewDTO);

    void deleteFilmById(Long filmId);

    void addReviewLike(Long reviewId, Long userId);

    void addReviewDISLike(Long reviewId, Long userId);
}

package com.example.demo.repository;

import com.example.demo.entity.Feedback;
import com.example.demo.model.response.FeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback>findFeedbackByShopId(long id);

    @Query("SELECT new com.example.demo.model.response.FeedbackResponse(f.id,a.email,f.content,f.rating) " +
            "FROM Feedback  f join Account  a on f.shop.id = a.id WHERE f.shop.id = :shop_id")
    List<FeedbackResponse> feedbackList(@Param("shop_id") Long shop_id);

}

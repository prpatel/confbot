package dev.prpatel.confbot.repository;

import dev.prpatel.confbot.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, String> {

    @Query("SELECT s FROM Session s JOIN s.speakers sp WHERE sp.id = :speakerId")
    List<Session> findBySpeakerId(@Param("speakerId") String speakerId);

    @Query("SELECT s FROM Session s WHERE lower(s.room) LIKE lower(concat('%', :roomName, '%'))")
    List<Session> findByRoom(@Param("roomName") String roomName);

    @Query("SELECT s FROM Session s WHERE s.startsAt BETWEEN :start AND :end")
    List<Session> findByTimeRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT s FROM Session s WHERE lower(s.title) LIKE lower(concat('%', :title, '%'))")
    List<Session> findByTitle(@Param("title") String title);

    @Query("SELECT s FROM Session s WHERE lower(s.description) LIKE lower(concat('%', :keyword, '%'))")
    List<Session> findByDescriptionKeyword(@Param("keyword") String keyword);

    @Query("SELECT count(s) FROM Session s")
    long countTotalSessions();
}

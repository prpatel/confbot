package dev.prpatel.confbot.repository;

import dev.prpatel.confbot.model.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpeakerRepository extends JpaRepository<Speaker, String> {
    
    @Query("SELECT s FROM Speaker s WHERE lower(s.fullName) LIKE lower(concat('%', :name, '%'))")
    List<Speaker> findByName(@Param("name") String name);

    @Query("SELECT s FROM Speaker s WHERE lower(s.company) LIKE lower(concat('%', :company, '%'))")
    List<Speaker> findByCompany(@Param("company") String company);

    @Query("SELECT count(s) FROM Speaker s")
    long countTotalSpeakers();
}

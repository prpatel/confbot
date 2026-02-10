package dev.prpatel.confbot.repository;

import dev.prpatel.confbot.model.Session;
import dev.prpatel.confbot.model.Speaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SessionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SessionRepository sessionRepository;

    private Speaker speaker;
    private Session session1;
    private Session session2;

    @BeforeEach
    void setUp() {
        speaker = new Speaker();
        speaker.setId("speaker1");
        speaker.setFullName("John Doe");
        entityManager.persist(speaker);

        session1 = new Session();
        session1.setId("session1");
        session1.setTitle("Spring Boot Basics");
        session1.setDescription("Introduction to Spring Boot");
        session1.setRoom("Room A");
        session1.setStartsAt(LocalDateTime.of(2024, 1, 1, 10, 0));
        session1.setEndsAt(LocalDateTime.of(2024, 1, 1, 11, 0));
        session1.setSpeakers(List.of(speaker));
        entityManager.persist(session1);

        session2 = new Session();
        session2.setId("session2");
        session2.setTitle("Advanced Java");
        session2.setDescription("Deep dive into Java features");
        session2.setRoom("Room B");
        session2.setStartsAt(LocalDateTime.of(2024, 1, 1, 14, 0));
        session2.setEndsAt(LocalDateTime.of(2024, 1, 1, 15, 0));
        entityManager.persist(session2);

        entityManager.flush();
    }

    @Test
    void findBySpeakerId() {
        List<Session> sessions = sessionRepository.findBySpeakerId("speaker1");
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getTitle()).isEqualTo("Spring Boot Basics");
    }

    @Test
    void findByRoom() {
        List<Session> sessions = sessionRepository.findByRoom("Room A");
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getRoom()).isEqualTo("Room A");
    }

    @Test
    void findByTimeRange() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 12, 0);
        List<Session> sessions = sessionRepository.findByTimeRange(start, end);
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getTitle()).isEqualTo("Spring Boot Basics");
    }

    @Test
    void findByTitle() {
        List<Session> sessions = sessionRepository.findByTitle("Spring");
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getTitle()).isEqualTo("Spring Boot Basics");
    }

    @Test
    void findByDescriptionKeyword() {
        List<Session> sessions = sessionRepository.findByDescriptionKeyword("deep dive");
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getDescription()).isEqualTo("Deep dive into Java features");
    }

    @Test
    void countTotalSessions() {
        long count = sessionRepository.countTotalSessions();
        assertThat(count).isEqualTo(2);
    }
}

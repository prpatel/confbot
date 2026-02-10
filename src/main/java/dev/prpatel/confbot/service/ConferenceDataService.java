package dev.prpatel.confbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.prpatel.confbot.model.Session;
import dev.prpatel.confbot.model.Speaker;
import dev.prpatel.confbot.repository.SessionRepository;
import dev.prpatel.confbot.repository.SpeakerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ConferenceDataService {

    private static final Logger logger = LoggerFactory.getLogger(ConferenceDataService.class);

    @Value("classpath:data/speakers.json")
    private Resource speakersResource;

    @Value("classpath:data/sessions.json")
    private Resource sessionsResource;

    @Value("classpath:data/schedule.json")
    private Resource scheduleResource;

    private final SpeakerRepository speakerRepository;
    private final SessionRepository sessionRepository;
    private final VectorStore vectorStore;
    private final ObjectMapper objectMapper;

    public ConferenceDataService(SpeakerRepository speakerRepository, SessionRepository sessionRepository, VectorStore vectorStore, ObjectMapper objectMapper) {
        this.speakerRepository = speakerRepository;
        this.sessionRepository = sessionRepository;
        this.vectorStore = vectorStore;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void loadData() throws IOException {
        loadSpeakers();
        loadSessions();
        loadSchedule(); // To update room info if needed, though sessions.json has room info too.
        populateVectorStore();
    }

    private void loadSpeakers() throws IOException {
        JsonNode root = objectMapper.readTree(speakersResource.getInputStream());
        if (root.isArray()) {
            for (JsonNode node : root) {
                Speaker speaker = new Speaker();
                speaker.setId(node.get("id").asText());
                speaker.setFirstName(node.get("firstName").asText());
                speaker.setLastName(node.get("lastName").asText());
                speaker.setFullName(node.get("fullName").asText());
                speaker.setBio(node.get("bio").asText());
                speaker.setTagLine(node.get("tagLine").asText());

                JsonNode questions = node.get("questionAnswers");
                if (questions != null && questions.isArray()) {
                    for (JsonNode q : questions) {
                        if ("Company".equals(q.get("question").asText())) {
                            speaker.setCompany(q.get("answer").asText());
                        } else if ("Twitter handle with @ ex: @prpatel".equals(q.get("question").asText())) {
                            speaker.setTwitter(q.get("answer").asText());
                        } else if ("LinkedIn URL".equals(q.get("question").asText())) {
                            speaker.setLinkedIn(q.get("answer").asText());
                        }
                    }
                }
                speakerRepository.save(speaker);
            }
        }
        logger.info("Speakers loaded: {}", speakerRepository.count());
    }

    private void loadSessions() throws IOException {
        JsonNode root = objectMapper.readTree(sessionsResource.getInputStream());
        if (root.isArray()) {
            for (JsonNode groupNode : root) {
                JsonNode sessionsNode = groupNode.get("sessions");
                if (sessionsNode != null && sessionsNode.isArray()) {
                    for (JsonNode node : sessionsNode) {
                        Session session = new Session();
                        session.setId(node.get("id").asText());
                        session.setTitle(node.get("title").asText());
                        session.setDescription(node.get("description").asText());
                        session.setStartsAt(LocalDateTime.parse(node.get("startsAt").asText(), DateTimeFormatter.ISO_DATE_TIME));
                        session.setEndsAt(LocalDateTime.parse(node.get("endsAt").asText(), DateTimeFormatter.ISO_DATE_TIME));
                        session.setRoom(node.get("room").asText());

                        JsonNode speakersNode = node.get("speakers");
                        if (speakersNode != null && speakersNode.isArray()) {
                            List<Speaker> speakers = new ArrayList<>();
                            for (JsonNode sNode : speakersNode) {
                                String speakerId = sNode.get("id").asText();
                                Optional<Speaker> speakerOpt = speakerRepository.findById(speakerId);
                                speakerOpt.ifPresent(speakers::add);
                            }
                            session.setSpeakers(speakers);
                        }
                        sessionRepository.save(session);
                    }
                }
            }
        }
        logger.info("Sessions loaded: {}", sessionRepository.count());
    }

    private void loadSchedule() throws IOException {
        // Schedule data seems redundant with sessions.json for the core fields we need, 
        // but could be used to double check rooms or times. 
        // For now, sessions.json seems to have what we need.
    }

    private void populateVectorStore() {
        List<Session> sessions = sessionRepository.findAll();
        List<Document> documents = new ArrayList<>();

        for (Session session : sessions) {
            String content = "Title: " + session.getTitle() + "\n" +
                    "Description: " + session.getDescription() + "\n";
            
            Map<String, Object> metadata = Map.of(
                    "sessionId", session.getId(),
                    "title", session.getTitle(),
                    "startsAt", session.getStartsAt().toString(),
                    "endsAt", session.getEndsAt().toString()
            );
            
            documents.add(new Document(content, metadata));
        }
        
        List<Speaker> speakers = speakerRepository.findAll();
        for (Speaker speaker : speakers) {
            String content = "Speaker Name: " + speaker.getFullName() + "\n" +
                    "Bio: " + speaker.getBio() + "\n" +
                    "Tagline: " + speaker.getTagLine() + "\n";
            
            Map<String, Object> metadata = Map.of(
                    "speakerId", speaker.getId(),
                    "fullName", speaker.getFullName()
            );
            
            documents.add(new Document(content, metadata));
        }

        vectorStore.add(documents);
        logger.info("Vector store populated with {} documents", documents.size());
    }
}

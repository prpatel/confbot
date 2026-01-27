package dev.prpatel.confbot.service;

import dev.prpatel.confbot.model.Session;
import dev.prpatel.confbot.model.Speaker;
import dev.prpatel.confbot.repository.SessionRepository;
import dev.prpatel.confbot.repository.SpeakerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConferenceTools {

    private final SpeakerRepository speakerRepository;
    private final SessionRepository sessionRepository;

    private static final Logger logger = LoggerFactory.getLogger(ConferenceTools.class);

    public ConferenceTools(SpeakerRepository speakerRepository, SessionRepository sessionRepository) {
        this.speakerRepository = speakerRepository;
        this.sessionRepository = sessionRepository;
    }

    @Tool(description = "Finds when a specific speaker is presenting. Returns a list of session titles and times.")
    public String whenIsSpeakerPresenting(String speakerName) {
        logger.info("\n\ninvoking whenIsSpeakerPresenting() speakerName:"+speakerName);
        List<Speaker> speakers = speakerRepository.findByName(speakerName);
        if (speakers.isEmpty()) {
            return "No speaker found with name: " + speakerName;
        }

        StringBuilder result = new StringBuilder();
        for (Speaker speaker : speakers) {
            List<Session> sessions = sessionRepository.findBySpeakerId(speaker.getId());
            if (sessions.isEmpty()) {
                result.append(speaker.getFullName()).append(" has no presentations.\n");
            } else {
                result.append(speaker.getFullName()).append(" is presenting:\n");
                for (Session session : sessions) {
                    result.append("- ").append(session.getTitle())
                            .append(" at ").append(session.getStartsAt())
                            .append(" in room ").append(session.getRoom()).append("\n");
                }
            }
        }
        return result.toString();
    }

    @Tool(description = "Counts how many presentations a speaker has.")
    public String howManyPresentations(String speakerName) {
        logger.info("\n\ninvoking howManyPresentations()");
        List<Speaker> speakers = speakerRepository.findByName(speakerName);
        if (speakers.isEmpty()) {
            return "No speaker found with name: " + speakerName;
        }

        StringBuilder result = new StringBuilder();
        for (Speaker speaker : speakers) {
            List<Session> sessions = sessionRepository.findBySpeakerId(speaker.getId());
            result.append(speaker.getFullName()).append(" has ").append(sessions.size()).append(" presentations.\n");
        }
        return result.toString();
    }

//    @Tool(description = "Lists all sessions for a specific speaker.")
//    public String listSessionsBySpeaker(String speakerName) {
//        logger.info("\n\ninvoking listSessionsBySpeaker()");
//        List<Speaker> speakers = speakerRepository.findByName(speakerName);
//        if (speakers.isEmpty()) {
//            return "No speaker found with name: " + speakerName;
//        }
//
//        StringBuilder result = new StringBuilder();
//        for (Speaker speaker : speakers) {
//            List<Session> sessions = sessionRepository.findBySpeakerId(speaker.getId());
//            if (sessions.isEmpty()) {
//                result.append(speaker.getFullName()).append(" has no sessions.\n");
//            } else {
//                result.append("Sessions for ").append(speaker.getFullName()).append(":\n");
//                for (Session session : sessions) {
//                    result.append("- ").append(session.getTitle()).append("\n");
//                }
//            }
//        }
//        return result.toString();
//    }
//
//    @Tool(description = "Finds sessions happening in a specific room.")
//    public String sessionsInRoom(String roomName) {
//        logger.info("\n\ninvoking sessionsInRoom()");
//        List<Session> sessions = sessionRepository.findByRoom(roomName);
//        if (sessions.isEmpty()) {
//            return "No sessions found in room: " + roomName;
//        }
//        StringBuilder result = new StringBuilder("Sessions in " + roomName + ":\n");
//        for (Session session : sessions) {
//            result.append("- ").append(session.getTitle())
//                    .append(" at ").append(session.getStartsAt()).append("\n");
//        }
//        return result.toString();
//    }
//
//    @Tool(description = "Finds sessions happening at a specific time or time range. Format: yyyy-MM-ddTHH:mm:ss")
//    public String sessionsAtTime(String startTime, String endTime) {
//        logger.info("\n\ninvoking sessionsAtTime()");
//        try {
//            LocalDateTime start = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_DATE_TIME);
//            LocalDateTime end = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_DATE_TIME);
//            List<Session> sessions = sessionRepository.findByTimeRange(start, end);
//
//            if (sessions.isEmpty()) {
//                return "No sessions found between " + startTime + " and " + endTime;
//            }
//
//            StringBuilder result = new StringBuilder("Sessions between " + startTime + " and " + endTime + ":\n");
//            for (Session session : sessions) {
//                result.append("- ").append(session.getTitle())
//                        .append(" (").append(session.getRoom()).append(")\n");
//            }
//            return result.toString();
//        } catch (Exception e) {
//            return "Invalid date format. Please use ISO format: yyyy-MM-ddTHH:mm:ss";
//        }
//    }
//
//    @Tool(description = "Finds information about a specific session by title.")
//    public String sessionInfo(String title) {
//        logger.info("\n\ninvoking sessionInfo()");
//        List<Session> sessions = sessionRepository.findByTitle(title);
//        if (sessions.isEmpty()) {
//            return "No session found with title containing: " + title;
//        }
//
//        StringBuilder result = new StringBuilder();
//        for (Session session : sessions) {
//            result.append("Title: ").append(session.getTitle()).append("\n")
//                  .append("Description: ").append(session.getDescription()).append("\n")
//                  .append("Room: ").append(session.getRoom()).append("\n")
//                  .append("Time: ").append(session.getStartsAt()).append(" - ").append(session.getEndsAt()).append("\n")
//                  .append("Speakers: ").append(session.getSpeakers().stream().map(Speaker::getFullName).collect(Collectors.joining(", "))).append("\n\n");
//        }
//        return result.toString();
//    }
//
//    @Tool(description = "Finds the bio of a specific speaker.")
//    public String speakerBio(String speakerName) {
//        logger.info("\n\ninvoking speakerBio() speakerName:"+ speakerName);
//        List<Speaker> speakers = speakerRepository.findByName(speakerName);
//        if (speakers.isEmpty()) {
//            return "No speaker found with name: " + speakerName;
//        }
//        logger.info("\n\ninvoking speakerBio() speakers:"+ speakers);
//        StringBuilder result = new StringBuilder();
//        for (Speaker speaker : speakers) {
//            result.append(speaker.getFullName()).append(": ").append(speaker.getBio()).append("\n");
//        }
//        return result.toString();
//    }
//
//    @Tool(description = "Finds speakers from a specific company.")
//    public String speakersByCompany(String company) {
//        logger.info("\n\ninvoking speakersByCompany()");
//        List<Speaker> speakers = speakerRepository.findByCompany(company);
//        if (speakers.isEmpty()) {
//            return "No speakers found from company: " + company;
//        }
//        return "Speakers from " + company + ": " + speakers.stream().map(Speaker::getFullName).collect(Collectors.joining(", "));
//    }
//
//    @Tool(description = "Counts the total number of sessions.")
//    public String countTotalSessions() {
//        logger.info("\n\ninvoking countTotalSessions()");
//        return "Total number of sessions: " + sessionRepository.countTotalSessions();
//    }
//
//    @Tool(description = "Counts the total number of speakers.")
//    public String countTotalSpeakers() {
//        logger.info("\n\ninvoking countTotalSpeakers()");
//        return "Total number of speakers: " + speakerRepository.countTotalSpeakers();
//    }
//
//    @Tool(description = "Finds sessions related to a specific keyword in the description.")
//    public String findSessionsByKeyword(String keyword) {
//        logger.info("\n\ninvoking findSessionsByKeyword()");
//        List<Session> sessions = sessionRepository.findByDescriptionKeyword(keyword);
//        if (sessions.isEmpty()) {
//            return "No sessions found with keyword: " + keyword;
//        }
//        StringBuilder result = new StringBuilder("Sessions related to '" + keyword + "':\n");
//        for (Session session : sessions) {
//            result.append("- ").append(session.getTitle()).append("\n");
//        }
//        return result.toString();
//    }
//
//    @Tool(description = "Finds the Twitter handle of a specific speaker.")
//    public String speakerTwitter(String speakerName) {
//        logger.info("\n\ninvoking speakerTwitter()");
//        List<Speaker> speakers = speakerRepository.findByName(speakerName);
//        if (speakers.isEmpty()) {
//            return "No speaker found with name: " + speakerName;
//        }
//        StringBuilder result = new StringBuilder();
//        for (Speaker speaker : speakers) {
//            String twitter = speaker.getTwitter();
//            if (twitter != null && !twitter.isEmpty()) {
//                result.append(speaker.getFullName()).append("'s Twitter: ").append(twitter).append("\n");
//            } else {
//                result.append(speaker.getFullName()).append(" does not have a Twitter handle listed.\n");
//            }
//        }
//        return result.toString();
//    }
//
//    @Tool(description = "Finds the LinkedIn profile of a specific speaker.")
//    public String speakerLinkedIn(String speakerName) {
//        logger.info("\n\ninvoking speakerLinkedIn()");
//        List<Speaker> speakers = speakerRepository.findByName(speakerName);
//        if (speakers.isEmpty()) {
//            return "No speaker found with name: " + speakerName;
//        }
//        StringBuilder result = new StringBuilder();
//        for (Speaker speaker : speakers) {
//            String linkedIn = speaker.getLinkedIn();
//            if (linkedIn != null && !linkedIn.isEmpty()) {
//                result.append(speaker.getFullName()).append("'s LinkedIn: ").append(linkedIn).append("\n");
//            } else {
//                result.append(speaker.getFullName()).append(" does not have a LinkedIn profile listed.\n");
//            }
//        }
//        return result.toString();
//    }
//
//    @Tool(description = "Finds the company of a specific speaker.")
//    public String speakerCompany(String speakerName) {
//        logger.info("\n\ninvoking speakerCompany()");
//        List<Speaker> speakers = speakerRepository.findByName(speakerName);
//        if (speakers.isEmpty()) {
//            return "No speaker found with name: " + speakerName;
//        }
//        StringBuilder result = new StringBuilder();
//        for (Speaker speaker : speakers) {
//            String company = speaker.getCompany();
//            if (company != null && !company.isEmpty()) {
//                result.append(speaker.getFullName()).append(" works at ").append(company).append("\n");
//            } else {
//                result.append(speaker.getFullName()).append(" does not have a company listed.\n");
//            }
//        }
//        return result.toString();
//    }
//
//    @Tool(description = "Finds the tagline of a specific speaker.")
//    public String speakerTagline(String speakerName) {
//        logger.info("\n\ninvoking speakerTagline()");
//        List<Speaker> speakers = speakerRepository.findByName(speakerName);
//        if (speakers.isEmpty()) {
//            return "No speaker found with name: " + speakerName;
//        }
//        StringBuilder result = new StringBuilder();
//        for (Speaker speaker : speakers) {
//            String tagline = speaker.getTagLine();
//            if (tagline != null && !tagline.isEmpty()) {
//                result.append(speaker.getFullName()).append("'s tagline: ").append(tagline).append("\n");
//            } else {
//                result.append(speaker.getFullName()).append(" does not have a tagline listed.\n");
//            }
//        }
//        return result.toString();
//    }
}

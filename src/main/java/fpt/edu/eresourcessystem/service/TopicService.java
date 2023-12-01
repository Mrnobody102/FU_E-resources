package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.Response.DocumentResponseDto;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Topic;
import org.bson.types.ObjectId;

import java.util.List;

public interface TopicService {
    List<Topic> findAll();

    Topic addTopic(Topic topic);

    Topic findById(String topicId);

    Topic updateTopic(Topic topic);

    Topic removeDocuments(Document document);

    void removeDocumentFromTopic(String topicId, ObjectId documentId);

    boolean softDelete(Topic topic);

    boolean delete(String topicId);

    void addDocumentToTopic(String topicId, ObjectId documentId);

    List<DocumentResponseDto> findByTopic(String topicId);
}

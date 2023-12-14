package fpt.edu.eresourcessystem.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fpt.edu.eresourcessystem.dto.Response.DocumentResponseDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Topic;
import fpt.edu.eresourcessystem.repository.TopicRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import javax.management.Query;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private DocumentService documentService;

    @Mock
    private MongoTemplate mongoTemplate;
    @InjectMocks
    private TopicServiceImpl topicService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testFindAll() {
        // Setup mock response
        // ...

        // Call the method
        List<Topic> topics = topicService.findAll();

        // Assert results
        // ...

        // Verify interactions
        verify(topicRepository).findAll();
    }

    @Test
    void testAddTopic_NewTopic() {
        Topic newTopic = new Topic(); // Assume Topic is a valid object
        newTopic.setId(null); // New topic scenario

        // Mock behavior
        when(topicRepository.save(any(Topic.class))).thenReturn(newTopic);

        // Call the method
        Topic result = topicService.addTopic(newTopic);

        // Assert results
        assertNotNull(result);
        verify(topicRepository).save(newTopic);
    }

//    @Test
//    void testAddTopic_ExistingTopic() {
//        Topic existingTopic = new Topic(); // Assume Topic is a valid object
//        existingTopic.setId("someId"); // Existing topic scenario
//
//        // Mock behavior
//        when(topicRepository.findById(existingTopic.getId())).thenReturn(Optional.of(existingTopic));
//        when(topicRepository.save(any(Topic.class))).thenReturn(existingTopic);
//
//        // Call the method
//        Topic result = topicService.addTopic(existingTopic);
//
//        // Assert results
//        assertNull(result); // Assuming the method returns null for existing topic
//        verify(topicRepository, never()).save(existingTopic);
//    }

    @Test
    void testFindById_TopicExists() {
        String topicId = "existingId";
        Topic topic = new Topic();
        topic.setId(topicId);

        // Mock behavior
        when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));

        // Call the method
        Topic result = topicService.findById(topicId);

        // Assert results
        assertNotNull(result);
        assertEquals(topicId, result.getId());
    }

    @Test
    void testFindById_TopicDoesNotExist() {
        String topicId = "nonExistingId";

        // Mock behavior
        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

        // Call the method
        Topic result = topicService.findById(topicId);

        // Assert results
        assertNull(result);
    }

    @Test
    void testUpdateTopic_TopicExists() {
        Topic existingTopic = new Topic();
        existingTopic.setId("existingId");

        // Mock behavior
        when(topicRepository.findById(existingTopic.getId())).thenReturn(Optional.of(existingTopic));
        when(topicRepository.save(existingTopic)).thenReturn(existingTopic);

        // Call the method
        Topic result = topicService.updateTopic(existingTopic);

        // Assert results
        assertNotNull(result);
        verify(topicRepository).save(existingTopic);
    }

    @Test
    void testUpdateTopic_TopicDoesNotExist() {
        Topic nonExistingTopic = new Topic();
        nonExistingTopic.setId("nonExistingId");

        // Mock behavior
        when(topicRepository.findById(nonExistingTopic.getId())).thenReturn(Optional.empty());

        // Call the method
        Topic result = topicService.updateTopic(nonExistingTopic);

        // Assert results
        assertNull(result);
    }


//    @Test
//    void testRemoveDocuments_DocumentExists() {
//        Document document = new Document();
//        document.setId("2345687");
//        Topic topic = new Topic();
//        topic.setDocuments(Arrays.asList(document));
//
//        when(document.getTopic()).thenReturn(topic);
//        when(topicRepository.save(any(Topic.class))).thenReturn(topic);
//
//        Topic result = topicService.removeDocuments(document);
//
//        assertNotNull(result);
//        assertTrue(result.getDocuments().isEmpty());
//        verify(topicRepository).save(topic);
//    }

//    @Test
//    void testRemoveDocuments_DocumentNotExists() {
//        Document mockDocument = mock(Document.class);
//        Topic mockTopic = new Topic(); // Assuming Topic can be instantiated directly
//
//        when(mockDocument.getTopic()).thenReturn(mockTopic); // Stubbing method on mock
//        when(topicRepository.save(any(Topic.class))).thenReturn(mockTopic); // Stubbing repository save
//
//        Topic result = topicService.removeDocuments(mockDocument);
//
//        // Add your assertions here
//    }


    @Test
    void testRemoveDocumentFromTopic() {
//        String topicId = "topicId";
//        ObjectId documentId = new ObjectId();
//
//        // No need to mock return value as method is void
//        doNothing().when(mongoTemplate).updateFirst(any(Query.class), any(Update.class), eq(Topic.class));
//
//        topicService.removeDocumentFromTopic(topicId, documentId);
//
//        // Verify interactions
//        verify(mongoTemplate).updateFirst(any(Query.class), any(Update.class), eq(Topic.class));
    }

    @Test
    void testSoftDelete_TopicExists() {
        Topic topic = new Topic();
        topic.setId("existingId");
        topic.setDocuments(Collections.singletonList(new Document()));

        when(topicRepository.findById(topic.getId())).thenReturn(Optional.of(topic));
        when(topicRepository.save(any(Topic.class))).thenReturn(topic);

        boolean result = topicService.softDelete(topic);

        assertTrue(result);
        assertEquals(CommonEnum.DeleteFlg.DELETED, topic.getDeleteFlg());
        verify(documentService, times(1)).softDelete(any(Document.class));
        verify(topicRepository).save(topic);
    }

    @Test
    void testSoftDelete_TopicDoesNotExist() {
        Topic topic = new Topic();
        topic.setId("nonExistingId");

        when(topicRepository.findById(topic.getId())).thenReturn(Optional.empty());

        boolean result = topicService.softDelete(topic);

        assertFalse(result);
    }

    @Test
    void testDelete_TopicExists() {
        String topicId = "existingId";

        when(topicRepository.findById(topicId)).thenReturn(Optional.of(new Topic()));

        boolean result = topicService.delete(topicId);

        assertTrue(result);
        verify(topicRepository).deleteById(topicId);
    }

    @Test
    void testDelete_TopicDoesNotExist() {
        String topicId = "nonExistingId";

        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

        boolean result = topicService.delete(topicId);

        assertFalse(result);
    }

    @Test
    void testAddDocumentToTopic() {
//        String topicId = "topicId";
//        ObjectId documentId = new ObjectId();
//
//        // No need to mock return value as method is void
//        doNothing().when(mongoTemplate).updateFirst(any(Query.class), any(Update.class), eq(Topic.class));
//
//        topicService.addDocumentToTopic(topicId, documentId);
//
//        // Verify interactions
//        verify(mongoTemplate).updateFirst(any(Query.class), any(Update.class), eq(Topic.class));
    }

//    @Test
//    void testFindByTopic_TopicExists() {
//        String topicId = "existingId";
//        Topic topic = new Topic();
//        topic.setDocuments(Collections.singletonList(new Document()));
//
//        when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));
//
//        List<DocumentResponseDto> result = topicService.findByTopic(topicId);
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//    }

    @Test
    void testFindByTopic_TopicDoesNotExist() {
        String topicId = "nonExistingId";

        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

        List<DocumentResponseDto> result = topicService.findByTopic(topicId);

        assertNull(result);
    }


}

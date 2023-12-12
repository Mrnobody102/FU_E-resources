package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.repository.FeedbackRepository;
import fpt.edu.eresourcessystem.repository.StudentRepository;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import fpt.edu.eresourcessystem.repository.UserLogRepository;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {
    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;
    private Feedback sampleFeedback;
    //    String
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp()  {
        sampleFeedback = new Feedback();
        sampleFeedback.setId("1");
        sampleFeedback.setCreatedDate("10/12/2023");
        sampleFeedback.setStatus("Pending");

    }

    @AfterEach
    void tearDown() throws Exception {
    }


    @Test
    void testSaveFeedback_ReturnFeedback() {
        // Mocking data
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(sampleFeedback);

        // Testing
        Feedback result = feedbackService.saveFeedback(sampleFeedback);
        assertEquals(sampleFeedback, result);
    }

    @Test
    void testSaveFeedback_ReturnNull() {
        assertThrows(IllegalArgumentException.class, () -> feedbackService.saveFeedback(null));
        // Verify if save method is not called
        verify(feedbackRepository, times(0)).save(any());
    }



    @Test
    void testGetFeedbackById_ValidId_ReturnsFeedback() {
        // Arrange
        String validId = "1";
        when(feedbackRepository.findById(validId)).thenReturn(Optional.of(sampleFeedback));

        // Act
        Optional<Feedback> result = feedbackService.getFeedbackById(validId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1", result.get().getId());
        assertEquals("This is a sample feedback", result.get().getFeedbackContent());

        verify(feedbackRepository, times(1)).findById(validId);
    }

    @Test
    void testGetFeedbackById_NullId_ThrowsIllegalArgumentException() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> feedbackService.getFeedbackById(null));

        // Verify if findById method is not called
        verify(feedbackRepository, times(0)).findById(any());
    }

    @Test
    void testGetFeedbackById_EmptyId_ThrowsIllegalArgumentException() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> feedbackService.getFeedbackById(""));

        // Verify if findById method is not called
        verify(feedbackRepository, times(0)).findById(any());
    }

    @Test
    void testDeleteFeedback() {
        // Testing
        feedbackService.deleteFeedback("1");

        // Verify if deleteById method is called with the correct argument
        verify(feedbackRepository, times(1)).deleteById("1");
    }

    @Test
    void testFindAllWithPageable() {
        // Mocking data
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Feedback> page = mock(Page.class);
        when(feedbackRepository.findAll(pageRequest)).thenReturn(page);

        // Testing
        Page<Feedback> result = feedbackService.findAll(pageRequest);
        assertEquals(page, result);
    }

    @Test
    void testFindAllByDateRange() {
        // Mocking data
        Date minDate = new Date();
        Date maxDate = new Date();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Feedback> page = mock(Page.class);
        when(feedbackRepository.findAllByCreatedDateBetween(minDate, maxDate, pageable)).thenReturn(page);

        // Testing
        Page<Feedback> result = feedbackService.findAllByDateRange(minDate, maxDate, pageable);
        assertEquals(page, result);
    }

    @Test
    void testSoftDelete() {
        // Mocking data
        when(feedbackRepository.findById("1")).thenReturn(Optional.of(sampleFeedback));

        // Testing
        boolean result = feedbackService.softDelete("1");
        assertTrue(result);

        // Verify if save method is called with the correct argument
        verify(feedbackRepository, times(1)).save(sampleFeedback);
        assertEquals(CommonEnum.DeleteFlg.DELETED, sampleFeedback.getDeleteFlg());
    }

    @Test
    void testSoftDeleteFeedbackNotFound() {
        // Mocking data
        when(feedbackRepository.findById("1")).thenReturn(Optional.empty());

        // Testing
        boolean result = feedbackService.softDelete("1");
        assertFalse(result);

        // Verify if save method is not called
        verify(feedbackRepository, times(0)).save(any());
    }

    @Test
    void testUpdateFeedbackStatus() {
        // Mocking data
        when(feedbackRepository.findById("1")).thenReturn(Optional.of(sampleFeedback));

        // Testing
//        feedbackService.updateFeedback("1", "Done");

        // Verify if save method is called with the correct argument
        verify(feedbackRepository, times(1)).save(sampleFeedback);
        assertEquals("Done", sampleFeedback.getStatus());
    }

//    @Test
//    void testUpdateFeedbackStatusFeedbackNotFound() {
//        // Mocking data
//        when(feedbackRepository.findById("1")).thenReturn(Optional.empty());
//
//        // Testing
//        assertThrows(EntityNotFoundException.class, () -> feedbackService.updateFeedbackStatus("1", "CLOSED"));
//
//        // Verify if save method is not called
//        verify(feedbackRepository, times(0)).save(any());
//    }
}
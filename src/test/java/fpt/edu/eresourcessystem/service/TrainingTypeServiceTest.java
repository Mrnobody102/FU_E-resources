package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.TrainingType;
import fpt.edu.eresourcessystem.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveTrainingType_ValidTrainingType_ReturnsSavedTrainingType() {
        // Arrange
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Test Training Type");
        trainingType.setTrainingTypeDescription("Description");

        when(trainingTypeRepository.save(any())).thenReturn(trainingType);

        // Act
        TrainingType savedTrainingType = trainingTypeService.save(trainingType);

        // Assert
        assertNotNull(savedTrainingType);
        assertEquals("Test Training Type", savedTrainingType.getTrainingTypeName());
        assertEquals("Description", savedTrainingType.getTrainingTypeDescription());

        verify(trainingTypeRepository, times(1)).save(any());
    }


    @Test
    void saveTrainingType_NullTrainingType_ThrowsIllegalArgumentException() {
        // Arrange
        TrainingType nullTrainingType = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.save(nullTrainingType));

        // Verify that save method is not called
        verify(trainingTypeRepository, never()).save(any());
    }

    @Test
    void saveTrainingType_RepositoryReturnsNull_ReturnsNull() {
        // Arrange
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Test Training Type");
        trainingType.setTrainingTypeDescription("Description");

        when(trainingTypeRepository.save(any())).thenReturn(null);

        // Act
        TrainingType savedTrainingType = trainingTypeService.save(trainingType);

        // Assert
        assertNull(savedTrainingType);

        verify(trainingTypeRepository, times(1)).save(any());
    }


    @Test
    void findById_WithValidId_ReturnsTrainingType() {
        // Arrange
        String trainingTypeId = "1012345";
        TrainingType expectedTrainingType = new TrainingType();
        expectedTrainingType.setId(trainingTypeId);

        when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.of(expectedTrainingType));

        // Act
        Optional<TrainingType> result = trainingTypeService.findById(trainingTypeId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(trainingTypeId, result.get().getId());
        verify(trainingTypeRepository, times(1)).findById(trainingTypeId);
    }
    @Test
    void findById_WithEmptyId_ThrowsIllegalArgumentException() {
        // Arrange
        String emptyId = "";

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> trainingTypeService.findById(emptyId));

        assertEquals("ID cannot be null or empty", exception.getMessage());
        verify(trainingTypeRepository, never()).findById(any());
    }

    @Test
    void findById_WithNullId_ThrowsIllegalArgumentException() {
        // Arrange
        String nullId = null;

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> trainingTypeService.findById(nullId));

        assertEquals("ID cannot be null or empty", exception.getMessage());
        verify(trainingTypeRepository, never()).findById(any());
    }
    @Test
    void findById_WithInvalidId_ReturnsEmptyOptional() {
        // Arrange
        String invalidId = "-123";

        when(trainingTypeRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act
        Optional<TrainingType> result = trainingTypeService.findById(invalidId);

        // Assert
        assertFalse(result.isPresent());
        verify(trainingTypeRepository, times(1)).findById(invalidId);
    }

    @Test
    void findById_WithValidIdNoTrainingTypeFound_ReturnsEmptyOptional() {
        // Arrange
        String trainingTypeId = "2346";

        when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.empty());

        // Act
        Optional<TrainingType> result = trainingTypeService.findById(trainingTypeId);

        // Assert
        assertFalse(result.isPresent());
        verify(trainingTypeRepository, times(1)).findById(trainingTypeId);
    }



    @Test
    void deleteById_NullId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.deleteById(null));
    }


    @Test
    void deleteById_EmptyId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> trainingTypeService.deleteById(""));
    }


    @Test
    void deleteById_NonExistingId_ThrowsRuntimeException() {
        String nonExistingId = "-123";
        when(trainingTypeRepository.existsById(nonExistingId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> trainingTypeService.deleteById(nonExistingId));
    }

    @Test
    void deleteById_ValidId_DeletesTrainingType() {
        String existingId = "16547892146";
        when(trainingTypeRepository.existsById(existingId)).thenReturn(true);

        trainingTypeService.deleteById(existingId);

        verify(trainingTypeRepository).deleteById(existingId);
    }

    @Test
    void updateTrainingType_NotFound_ThrowsRuntimeException() {
        TrainingType trainingType = new TrainingType();
        trainingType.setId("-123");
        when(trainingTypeRepository.findById(trainingType.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> trainingTypeService.updateTrainingType(trainingType));
    }
    @Test
    void updateTrainingType_WithNullObject_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> trainingTypeService.updateTrainingType(null));
    }



    @Test
    void updateTrainingType_WithUnchangedData_ReturnsSameTrainingType() {
        TrainingType unchangedTrainingType = new TrainingType();
        unchangedTrainingType.setId("123321");
        unchangedTrainingType.setTrainingTypeName("Existing Name");
        unchangedTrainingType.setTrainingTypeDescription("Existing Description");

        when(trainingTypeRepository.findById(unchangedTrainingType.getId())).thenReturn(Optional.of(unchangedTrainingType));
        when(trainingTypeRepository.save(any(TrainingType.class))).thenReturn(unchangedTrainingType);

        TrainingType result = trainingTypeService.updateTrainingType(unchangedTrainingType);

        assertNotNull(result);
        assertEquals(unchangedTrainingType.getTrainingTypeName(), result.getTrainingTypeName());
        assertEquals(unchangedTrainingType.getTrainingTypeDescription(), result.getTrainingTypeDescription());
    }


    @Test
    void updateTrainingType_Successful_UpdateTrainingType() {
        TrainingType existingTrainingType = new TrainingType();
        existingTrainingType.setId("16547892146");
        existingTrainingType.setTrainingTypeName("Old Name");
        existingTrainingType.setTrainingTypeDescription("Old Description");

        TrainingType updatedInfo = new TrainingType();
        updatedInfo.setId(existingTrainingType.getId());
        updatedInfo.setTrainingTypeName("New Name");
        updatedInfo.setTrainingTypeDescription("New Description");

        when(trainingTypeRepository.findById(existingTrainingType.getId())).thenReturn(Optional.of(existingTrainingType));
        when(trainingTypeRepository.save(any(TrainingType.class))).thenReturn(updatedInfo);

        TrainingType updatedTrainingType = trainingTypeService.updateTrainingType(updatedInfo);

        assertNotNull(updatedTrainingType);
        assertEquals(updatedInfo.getTrainingTypeName(), updatedTrainingType.getTrainingTypeName());
        assertEquals(updatedInfo.getTrainingTypeDescription(), updatedTrainingType.getTrainingTypeDescription());
    }


    @Test
    void addCourseToTrainingType_TrainingTypeExists_CourseAdded() {
        String trainingTypeId = "16547892146";
        Course course = new Course();
        TrainingType trainingType = new TrainingType();
        trainingType.setCourses(new ArrayList<>());

        when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.of(trainingType));
        when(trainingTypeRepository.save(any(TrainingType.class))).thenReturn(trainingType);

        TrainingType updatedTrainingType = trainingTypeService.addCourseToTrainingType(trainingTypeId, course);

        assertNotNull(updatedTrainingType);
        assertTrue(updatedTrainingType.getCourses().contains(course));
        verify(trainingTypeRepository).save(trainingType);
    }

    @Test
    void addCourseToTrainingType_WhenTrainingTypeNotFound_ReturnsNull() {
        String trainingTypeId = "-123";
        Course course = new Course();

        when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.empty());

        TrainingType result = trainingTypeService.addCourseToTrainingType(trainingTypeId, course);

        assertNull(result);
        verify(trainingTypeRepository, never()).save(any(TrainingType.class));
    }

    @Test
    void addCourseToTrainingType_NullTrainingTypeId_ReturnsNull() {
        Course course = new Course();

        TrainingType result = trainingTypeService.addCourseToTrainingType(null, course);

        assertNull(result);
        verify(trainingTypeRepository, never()).save(any(TrainingType.class));
    }

    @Test
    void addCourseToTrainingType_WhenCourseIsNull_ThrowsException() {
        String trainingTypeId = "123654232";
        when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.of(new TrainingType()));

        assertThrows(NullPointerException.class, () -> trainingTypeService.addCourseToTrainingType(trainingTypeId, null));
    }



    @Test
    void softDelete_WhenTrainingTypeExists_MarksAsDeletedAndReturnsTrue() {
        String trainingTypeId = "123456412";
        TrainingType trainingType = new TrainingType();
        trainingType.setId(trainingTypeId);

        when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.of(trainingType));

        boolean result = trainingTypeService.softDelete(trainingType);

        assertTrue(result);
        assertEquals(CommonEnum.DeleteFlg.DELETED, trainingType.getDeleteFlg());
        verify(trainingTypeRepository).save(trainingType);
    }

    @Test
    void softDelete_WhenTrainingTypeNotFound_ReturnsFalse() {
        String trainingTypeId = "-136542";
        TrainingType trainingType = new TrainingType();
        trainingType.setId(trainingTypeId);

        when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(Optional.empty());

        boolean result = trainingTypeService.softDelete(trainingType);

        assertFalse(result);
        verify(trainingTypeRepository, never()).save(any(TrainingType.class));
    }

    @Test
    void softDelete_WhenTrainingTypeIsNull_ThrowsException() {
        assertThrows(NullPointerException.class, () -> trainingTypeService.softDelete(null));
    }







}


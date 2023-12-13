package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.TrainingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeService {


     TrainingType save(TrainingType trainingType) ;

     List<TrainingType> findAll() ;

     Optional<TrainingType> findById(String id) ;

     void deleteById(String id) ;

     TrainingType updateTrainingType(TrainingType trainingType);

     boolean softDelete(TrainingType trainingType);

     TrainingType addCourseToTrainingType(String trainingTypeId, Course course);
}

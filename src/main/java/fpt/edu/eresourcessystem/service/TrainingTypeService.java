package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeService {

     TrainingType save(TrainingType trainingType) ;

     List<TrainingType> findAll() ;

     Optional<TrainingType> findById(String id) ;

     void deleteById(String id) ;

}

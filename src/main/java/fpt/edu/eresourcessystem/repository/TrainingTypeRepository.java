package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.TrainingType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrainingTypeRepository extends MongoRepository<TrainingType, String> {

}

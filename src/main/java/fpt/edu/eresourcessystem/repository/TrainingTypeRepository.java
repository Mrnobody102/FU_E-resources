package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.TrainingType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TrainingTypeRepository extends MongoRepository<TrainingType, String> {

    @NotNull
    @Query("{ 'deleteFlg' : 'PRESERVED' }")
    List<TrainingType> findAll();

    Page<TrainingType> findByTrainingTypeNameContainingIgnoreCase(String search, Pageable pageable);

}

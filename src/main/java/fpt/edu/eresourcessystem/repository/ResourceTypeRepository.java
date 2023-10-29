package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.ResourceType;
import fpt.edu.eresourcessystem.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("resourceTypeRepository")
public interface ResourceTypeRepository extends MongoRepository<ResourceType,String> {

    @Override
    List<ResourceType> findAll();

    Optional<ResourceType> findById(String id);

}

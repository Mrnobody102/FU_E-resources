package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.ResourceType;
import fpt.edu.eresourcessystem.repository.ResourceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("ResourceTypeService")
public class ResourceTypeServiceImpl implements ResourceTypeService{
    private final ResourceTypeRepository ResourceTypeRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public ResourceTypeServiceImpl(ResourceTypeRepository ResourceTypeRepository) {
        this.ResourceTypeRepository = ResourceTypeRepository;
    }

    @Override
    public List<ResourceType> findAll() {
        List<ResourceType> ResourceTypes = ResourceTypeRepository.findAll();
        return ResourceTypes;
    }

    @Override
    public List<ResourceType> findByCourseId(String courseId) {
        Query query = new Query(Criteria.where("courseId").is(courseId));
        List<ResourceType> ResourceTypes =  mongoTemplate.find(query, ResourceType.class);
        return  ResourceTypes;
    }

    @Override
    public ResourceType addResourceType(ResourceType ResourceType) {
        if(null==ResourceType.getId()) {
            ResourceType result = ResourceTypeRepository.save(ResourceType);
            return result;
        }else{
            Optional<ResourceType> checkExist = ResourceTypeRepository.findById(ResourceType.getId());
            if(!checkExist.isPresent()){
                ResourceType result = ResourceTypeRepository.save(ResourceType);
                return result;
            }return null;
        }
    }

    @Override
    public ResourceType findById(String ResourceTypeId) {
        Optional<ResourceType> ResourceType = ResourceTypeRepository.findById(ResourceTypeId);
        return ResourceType.orElse(null);
    }

    @Override
    public ResourceType updateResourceType(ResourceType ResourceType) {
        Optional<ResourceType> checkExist = ResourceTypeRepository.findById(ResourceType.getId());
       if(checkExist.isPresent()){
           ResourceType result = ResourceTypeRepository.save(ResourceType);
           return result;
       }
       return null;
    }

    @Override
    public boolean delete(String ResourceTypeId) {
        Optional<ResourceType> check = ResourceTypeRepository.findById(ResourceTypeId);
        if(check.isPresent()){
            ResourceTypeRepository.deleteById(ResourceTypeId);
            return true;
        }
        return false;
    }

    @Override
    public ResourceType addDocument(ResourceType ResourceType) {
        return null;
    }
}

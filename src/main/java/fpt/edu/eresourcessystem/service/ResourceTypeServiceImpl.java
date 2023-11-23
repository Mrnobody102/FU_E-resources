package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.ResourceType;
import fpt.edu.eresourcessystem.repository.ResourceTypeRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("ResourceTypeService")
public class ResourceTypeServiceImpl implements ResourceTypeService {
    private final ResourceTypeRepository resourceTypeRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public ResourceTypeServiceImpl(ResourceTypeRepository ResourceTypeRepository) {
        this.resourceTypeRepository = ResourceTypeRepository;
    }

    @Override
    public List<ResourceType> findAll() {
        List<ResourceType> ResourceTypes = resourceTypeRepository.findAll();
        return ResourceTypes;
    }

    @Override
    public List<ResourceType> findByCourseId(String courseId) {
        Query query = new Query(Criteria.where("courseId").is(courseId));
        List<ResourceType> ResourceTypes = mongoTemplate.find(query, ResourceType.class);
        return ResourceTypes;
    }

    @Override
    public ResourceType addResourceType(ResourceType ResourceType) {
        ResourceType resourceType = new ResourceType(ResourceType);
        if (null == resourceType.getId()) {
            ResourceType result = resourceTypeRepository.save(resourceType);
            return result;
        } else {
            Optional<ResourceType> checkExist = resourceTypeRepository.findById(resourceType.getId());
            if (!checkExist.isPresent()) {
                ResourceType result = resourceTypeRepository.save(resourceType);
                return result;
            }
            return null;
        }
    }

    @Override
    public ResourceType findById(String ResourceTypeId) {
        Optional<ResourceType> ResourceType = resourceTypeRepository.findById(ResourceTypeId);
        return ResourceType.orElse(null);
    }

    @Override
    public ResourceType updateResourceType(ResourceType ResourceType) {
        Optional<ResourceType> checkExist = resourceTypeRepository.findById(ResourceType.getId());
        if (checkExist.isPresent()) {
            ResourceType result = resourceTypeRepository.save(ResourceType);
            return result;
        }
        return null;
    }

    @Override
    public boolean delete(String ResourceTypeId) {
        Optional<ResourceType> check = resourceTypeRepository.findById(ResourceTypeId);
        if (check.isPresent()) {
            resourceTypeRepository.deleteById(ResourceTypeId);
            return true;
        }
        return false;
    }

    @Override
    public void addDocumentToResourceType(String resourceTypeId, ObjectId documentId) {
        Query query = new Query(Criteria.where("id").is(resourceTypeId));
        Update update = new Update().push("documents", documentId);
        mongoTemplate.updateFirst(query, update, ResourceType.class);
    }
}

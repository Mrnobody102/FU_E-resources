package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.ResourceType;
import org.bson.types.ObjectId;

import java.util.List;

public interface ResourceTypeService {
    List<ResourceType> findAll();

    List<ResourceType> findByCourseId(String courseId);

    ResourceType addResourceType(ResourceType ResourceType);

    ResourceType findById(String ResourceTypeId);

    ResourceType updateResourceType(ResourceType ResourceType);

    boolean delete(String ResourceTypeId);

    void addDocumentToResourceType(String resourceTypeId, ObjectId documentId);
}

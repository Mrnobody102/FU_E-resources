package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.ResourceType;

import java.util.List;

public interface ResourceTypeService {
    List<ResourceType> findAll();

    List<ResourceType> findByCourseId(String courseId);

    ResourceType addResourceType(String ResourceType);

    ResourceType findById(String ResourceTypeId);

    ResourceType updateResourceType(ResourceType ResourceType);

    boolean delete(String ResourceTypeId);

    ResourceType addDocument(ResourceType ResourceType);
}

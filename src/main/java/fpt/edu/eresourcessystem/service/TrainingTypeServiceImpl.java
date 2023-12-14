package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.TrainingType;
import fpt.edu.eresourcessystem.repository.TrainingTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("trainingtypeService")
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

//    @Autowired
    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }
    public TrainingType save(TrainingType trainingType) {
        if (trainingType == null) {
            throw new IllegalArgumentException("TrainingType cannot be null");
        }
        return trainingTypeRepository.save(trainingType);
    }

    public List<TrainingType> findAll() {
        return trainingTypeRepository.findAll();
    }

    public Optional<TrainingType> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        return trainingTypeRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (!trainingTypeRepository.existsById(id)) {
            throw new RuntimeException("TrainingType with ID " + id + " does not exist");
        }
        trainingTypeRepository.deleteById(id);
    }

    @Override
    public TrainingType updateTrainingType(TrainingType trainingType) {
        if (trainingType == null) {
            throw new IllegalArgumentException("TrainingType cannot be null");
        }

        TrainingType existingTrainingType = trainingTypeRepository.findById(trainingType.getId())
                .orElseThrow(() -> new RuntimeException("Training type not found"));

        existingTrainingType.setTrainingTypeName(trainingType.getTrainingTypeName());
        existingTrainingType.setTrainingTypeDescription(trainingType.getTrainingTypeDescription());

        return trainingTypeRepository.save(existingTrainingType);
    }


    @Override
    public TrainingType addCourseToTrainingType(String trainingTypeId, Course course) {
        Optional<TrainingType> trainingTypeOpt = trainingTypeRepository.findById(trainingTypeId);

        if (trainingTypeOpt.isPresent()) {
            TrainingType trainingType = trainingTypeOpt.get();
//            course.setTrainingType(trainingType);
            trainingType.getCourses().add(course);
            return trainingTypeRepository.save(trainingType);
        }
        return null;
    }

    @Override
    public boolean softDelete(TrainingType trainingType) {
        Optional<TrainingType> existingTrainingType = trainingTypeRepository.findById(trainingType.getId());
        if (existingTrainingType.isPresent()) {
            TrainingType toDelete = existingTrainingType.get();
            toDelete.setDeleteFlg(CommonEnum.DeleteFlg.DELETED); // Mark as deleted
            trainingTypeRepository.save(toDelete);
            return true;
        }
        return false;
    }



}

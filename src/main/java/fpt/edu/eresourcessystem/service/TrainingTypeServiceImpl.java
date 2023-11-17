package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.TrainingType;
import fpt.edu.eresourcessystem.repository.TrainingTypeRepository;
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
        return trainingTypeRepository.save(trainingType);
    }

    public List<TrainingType> findAll() {
        return trainingTypeRepository.findAll();
    }

    public Optional<TrainingType> findById(String id) {
        return trainingTypeRepository.findById(id);
    }

    @Override
    public void deleteById(String id) {
        trainingTypeRepository.deleteById(id);
    }

}

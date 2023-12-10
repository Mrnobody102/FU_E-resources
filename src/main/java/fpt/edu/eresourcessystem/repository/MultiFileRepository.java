package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.MultiFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("multiFileRepository")
public interface MultiFileRepository extends MongoRepository<MultiFile, String> {
    Optional<MultiFile> findByIdAndDeleteFlg(String id, CommonEnum.DeleteFlg flg);
    MultiFile findByFileName(String name);
}

package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.UserLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userLogRepository")
public interface UserLogRepository extends MongoRepository<UserLog, String> {

    @Override
    List<UserLog> findAll();

    List<UserLog> findByAccount(String email);
    List<UserLog> findByUrl(String url);
}

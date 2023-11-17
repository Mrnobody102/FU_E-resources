package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.UserLog;
import fpt.edu.eresourcessystem.repository.UserLogRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("userLogService")
public class UserLogServiceImpl implements UserLogService{
    private final UserLogRepository userLogRepository;
    private final MongoTemplate mongoTemplate;


    public UserLogServiceImpl(UserLogRepository userLogRepository, MongoTemplate mongoTemplate) {
        this.userLogRepository = userLogRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<UserLog> findAll() {
        List<UserLog> userLogs = userLogRepository.findAll();
        return userLogs;
    }

    @Override
    public List<UserLog> findByAccount(String email) {
        List<UserLog> userLogs = userLogRepository.findByAccount(email);
        return userLogs;
    }

    @Override
    public List<UserLog> searchLog(String email, String role, LocalDateTime startDate, LocalDateTime endDate) {
        Criteria criteria = Criteria.where("time").gte(startDate).lte(endDate)
                .and("email").regex(email, "i")
                .and("role").regex(email, "i");

        Query query = new Query(criteria);
        return mongoTemplate.find(query, UserLog.class);
    }

    @Override
    public List<UserLog> findByUrl(String url) {
        List<UserLog> userLogs = userLogRepository.findByUrl(url);
        return userLogs;
    }

    @Override
    public UserLog addUserLog(UserLog userLog) {
        if(null!=userLog){
            UserLog result = userLogRepository.save(userLog);
            return result;
        }
        return null;
    }
}

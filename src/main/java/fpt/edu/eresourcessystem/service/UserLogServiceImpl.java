package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.CourseLog;
import fpt.edu.eresourcessystem.model.UserLog;
import fpt.edu.eresourcessystem.repository.UserLogRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

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
    public List<UserLog> findByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        List<UserLog> userLogs = userLogRepository.findByEmail(email);
        return userLogs;
    }

    @Override
    public List<UserLog> searchLog(String email, String role, LocalDateTime startDate, LocalDateTime endDate) {
        Criteria criteria = Criteria.where("time").gte(startDate).lte(endDate)
                .and("email").regex(Pattern.quote(email), "i")
                .and("role").regex(Pattern.quote(email), "i");

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

    @Override
    public List<Course> findStudentRecentView(String email) {
        String urlPrefix = "/student/courses/";
        Criteria criteria = Criteria.where("email").is(email)
                .and("url").regex(Pattern.quote("^" + urlPrefix))
                .and("createdDate").exists(true);

        // Sort by the "time" in descending order to get the most recent documents
        Query query = new Query(criteria).with(Sort.by(Sort.Order.desc("createdDate")));

        // Use a Pageable to limit the result set to 5 documents
        PageRequest pageable = PageRequest.of(0, 5);
        query.with(pageable);
        List<String> listCourseIds = mongoTemplate.findDistinct(query,"url" , UserLog.class, String.class)
                .stream().map(o -> o.substring(o.indexOf(urlPrefix) + urlPrefix.length()))
                .toList();
        List<Course> result = mongoTemplate.find(
                Query.query(Criteria.where("id").in(listCourseIds)),
                Course.class
        );

        return result;

    }
}

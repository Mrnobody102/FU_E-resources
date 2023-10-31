package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.CourseLog;
import fpt.edu.eresourcessystem.repository.CourseLogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("courseLogService")
public class CourseLogServiceImpl implements CourseLogService{
    private final CourseLogRepository courseLogRepository;
    private final MongoTemplate mongoTemplate;

    public CourseLogServiceImpl(CourseLogRepository courseLogRepository, MongoTemplate mongoTemplate) {
        this.courseLogRepository = courseLogRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CourseLog addCourseLog(CourseLog courseLog) {
        if(null == courseLog || null == courseLog.getCourseLogId()){
            return null;
        }
        if(null == courseLog.getCourseLogId().getTime()){
            courseLog.getCourseLogId().setTime(LocalDateTime.now());
        }
        CourseLog result =  courseLogRepository.save(courseLog);
        return result;
    }

    @Override
    public List<String> findStudentRecentView(String accountId) {
//        System.out.println(accountId);
        Criteria criteria = new Criteria();

        // Sort by the "time" in descending order to get the most recent documents
        criteria.and("courseLogId.time").exists(true); // Ensure "time" field exists
        criteria.and("courseLogId.action").is(CommonEnum.Action.VIEW);
        criteria.and("courseLogId.accountId").is(accountId);
        Query query = new Query(criteria).with(Sort.by(Sort.Order.desc("time")));

        // Use a Pageable to limit the result set to 5 documents
        PageRequest pageable = PageRequest.of(0, 5);
        query.with(pageable);
        return mongoTemplate.findDistinct(query,"courseLogId.courseId" ,CourseLog.class, String.class);
    }

    @Override
    public List<CourseLog> findLecturerRecentView(String accountId) {
        Criteria criteria = new Criteria();

        // Sort by the "time" in descending order to get the most recent documents
        criteria.and("courseLogId.time").exists(true); // Ensure "time" field exists
        criteria.and("courseLogId.action").is(CommonEnum.Action.VIEW);
        criteria.and("courseLogId.accountId").is(accountId);
        Query query = new Query(criteria).with(Sort.by(Sort.Order.desc("time")));

        // Use a Pageable to limit the result set to 5 documents
        PageRequest pageable = PageRequest.of(0, 5);
        query.with(pageable);
        return mongoTemplate.find(query, CourseLog.class);
    }
}

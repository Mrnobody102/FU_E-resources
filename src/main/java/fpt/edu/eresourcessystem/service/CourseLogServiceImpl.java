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
        if(null == courseLog){
            return null;
        }
        if(null == courseLog.getCreatedDate()){
            courseLog.setCreatedBy(LocalDateTime.now().toString());
        }
        CourseLog result =  courseLogRepository.save(courseLog);
        return result;
    }

    @Override
    public List<String> findStudentRecentView(String accountId) {
//        System.out.println(accountId);
        Criteria criteria = new Criteria();

        // Sort by the "time" in descending order to get the most recent documents
        criteria.and("createdDate").exists(true); // Ensure "time" field exists
        criteria.and("action").is(CommonEnum.Action.VIEW);
        criteria.and("createdBy").is(accountId);
        Query query = new Query(criteria).with(Sort.by(Sort.Order.desc("createdDate")));

        // Use a Pageable to limit the result set to 5 documents
        PageRequest pageable = PageRequest.of(0, 5);
        query.with(pageable);
        return mongoTemplate.findDistinct(query,"courseId" ,CourseLog.class, String.class);
    }

    @Override
    public List<String> findLecturerRecentView(String accountId) {
        Criteria criteria = new Criteria();

        // Sort by the "time" in descending order to get the most recent documents
        criteria.and("createdDate").exists(true); // Ensure "time" field exists
        criteria.and("action").is(CommonEnum.Action.VIEW);
        criteria.and("createdBy").is(accountId);
        Query query = new Query(criteria).with(Sort.by(Sort.Order.desc("createdDate")));

        // Use a Pageable to limit the result set to 5 documents
        PageRequest pageable = PageRequest.of(0, 5);
        query.with(pageable);
        return mongoTemplate.findDistinct(query,"courseId", CourseLog.class, String.class);
    }

    @Override
    public List<CourseLog> findByCourseCodeOrCodeName(String search) {
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("course.code").regex(search, "i"),
                Criteria.where("course.name").regex(search, "i")
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, CourseLog.class);
    }

}

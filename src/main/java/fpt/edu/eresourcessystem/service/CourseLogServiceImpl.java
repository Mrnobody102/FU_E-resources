package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.Response.CourseLogResponseDto;
import fpt.edu.eresourcessystem.model.CourseLog;
import fpt.edu.eresourcessystem.repository.CourseLogRepository;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service("courseLogService")
public class CourseLogServiceImpl implements CourseLogService {
    private final CourseLogRepository courseLogRepository;
    private final MongoTemplate mongoTemplate;

    public CourseLogServiceImpl(CourseLogRepository courseLogRepository, MongoTemplate mongoTemplate) {
        this.courseLogRepository = courseLogRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CourseLog addCourseLog(CourseLog courseLog) {
        if (null == courseLog) {
            return null;
        }
        if (null == courseLog.getCreatedDate()) {
            courseLog.setCreatedBy(LocalDateTime.now().toString());
        }
        CourseLog result = courseLogRepository.save(courseLog);
        return result;
    }

    @Override
    public List<CourseLogResponseDto> findAllSortedByCreatedDate() {
        Sort sortByCreatedDate = Sort.by(Sort.Direction.DESC, "createdDate");
        List<CourseLogResponseDto> courseLogResponseDtos = courseLogRepository.findAll(sortByCreatedDate).stream().map(o -> new CourseLogResponseDto(o)).toList();
        return courseLogResponseDtos;
    }

    @Override
    public void deleteCourseLog(CourseLog courseLog) {
        courseLogRepository.delete(courseLog);
    }

    @Override
    public List<String> findByLecturer(String email) {
        Criteria criteria = new Criteria();
        // Sort by the "time" in descending order to get the most recent documents
        criteria.and("createdDate").exists(true); // Ensure "time" field exists
//        criteria.and("action").is(CommonEnum.Action.VIEW);
        criteria.and("email").is(email);
        Query query = new Query(criteria).with(Sort.by(Sort.Order.desc("createdDate")));
        // Use a Pageable to limit the result set to 5 documents
        PageRequest pageable = PageRequest.of(0, 5);
        query.with(pageable);
        return mongoTemplate.findDistinct(query, "courseId", CourseLog.class, String.class);
    }

    @Override
    public List<CourseLog> findByCourseCodeOrCodeName(String search) {
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("course.code").regex(Pattern.quote(search), "i"),
                Criteria.where("course.name").regex(Pattern.quote(search), "i")
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, CourseLog.class);
    }

    @Override
    public Page<CourseLog> getLogsBySearchAndDate(String search, LocalDate startDate, LocalDate endDate, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Criteria criteria = new Criteria();

        // Match on email, coursename, courseCode, or objectName containing the search string
        criteria.orOperator(
                Criteria.where("email").regex(Pattern.quote(search), "i"),
                Criteria.where("courseName").regex(Pattern.quote(search), "i"),
                Criteria.where("courseCode").regex(Pattern.quote(search), "i"),
                Criteria.where("objectName").regex(Pattern.quote(search), "i")
        );

        // Match on createdDate greater than or equal to start date and less than or equal to end date
        if (startDate != null && endDate != null) {
            // Convert java.util.Date to LocalDateTime
            criteria.and("createdDate").gte(startDate).lte(endDate);
        } else if (startDate != null) {
            criteria.and("createdDate").gte(startDate);
        } else if (endDate != null) {
            criteria.and("createdDate").lte(endDate);
        }


        Query query = new Query(criteria).with(pageable);
        List<CourseLog> result = mongoTemplate.find(query, CourseLog.class, "course_log");

        // Fetch total count without pagination
        long totalCount = mongoTemplate.count(new Query(criteria), CourseLog.class, "course_log");

        return new PageImpl<>(result, pageable, totalCount);
        //        return courseLogRepository
//                .findByEmailLikeAndCourseNameLikeAndCourseCodeLikeAndCreatedDateBetween
//                        (search, search,search, startDate,endDate, pageable);
    }

    @Override
    public List<CourseLog> getLogsBySearchAndDateListAll(String search, LocalDate startDate, LocalDate endDate) {
        Criteria criteria = new Criteria();

        // Match on email, coursename, courseCode, or objectName containing the search string
        criteria.orOperator(
                Criteria.where("email").regex(Pattern.quote(search), "i"),
                Criteria.where("courseName").regex(Pattern.quote(search), "i"),
                Criteria.where("courseCode").regex(Pattern.quote(search), "i"),
                Criteria.where("objectName").regex(Pattern.quote(search), "i")
        );
        Query query = new Query(criteria);
        List<CourseLog> result = mongoTemplate.find(query, CourseLog.class, "course_log");
        return result;
    }

}



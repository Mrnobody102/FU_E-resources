package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.CourseEnum;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.LecturerCourse;
import fpt.edu.eresourcessystem.repository.LecturerCourseRepository;
import fpt.edu.eresourcessystem.repository.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service(value = "lecturerService")
public class LecturerServiceImpl implements LecturerService {
    private final LecturerRepository lecturerRepository;
    private final LecturerCourseRepository lecturerCourseRepository;
    private final MongoTemplate mongoTemplate;

    public LecturerServiceImpl(LecturerRepository lecturerRepository, LecturerCourseRepository lecturerCourseRepository, MongoTemplate mongoTemplate) {
        this.lecturerRepository = lecturerRepository;
        this.lecturerCourseRepository = lecturerCourseRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Lecturer findByCourseId(String courseId) {
        return lecturerRepository.findByCourseId(courseId);
    }

    @Override
    public Lecturer addLecturer(Lecturer lecturer) {
        lecturer.setDeleteFlg(CommonEnum.DeleteFlg.PRESERVED);
        return lecturerRepository.save(lecturer);
    }

    @Override
    public Lecturer updateLecturer(Lecturer lecturer) {
        Optional<Lecturer> foundLecturer = lecturerRepository.findById(lecturer.getId());
        if (foundLecturer.isPresent()) {
            Lecturer result = lecturerRepository.save(lecturer);
            return result;
        }
        return null;
    }

    @Override
    public List<Lecturer> findAll() {
        return lecturerRepository.findAll();
    }

    @Override
    public Lecturer findByAccountId(String accountId) {
        return lecturerRepository.findByAccountId(accountId);
    }

    @Override
    public List<Lecturer> findByListLecturerIds(List<String> ids) {
        List<Lecturer> lecturers = lecturerRepository.findByIds(ids);
        return lecturers;
    }

    @Override
    public Lecturer findCurrentCourseLecturer(String courseId) {
        LecturerCourse lecturerCourse = lecturerCourseRepository.findCurrentCourseLecturer(courseId);
        if (null != lecturerCourse) {
            if (null != lecturerCourse.getId().getLecturerId()) {
                Optional<Lecturer> lecturer = lecturerRepository.findById(
                        lecturerCourse.getId().getLecturerId());
                return lecturer.orElse(null);
            }
        }
        return null;
    }

    @Override
    public List<Lecturer> findAllLecture() {
        return lecturerRepository.findAll();
    }

    @Override
    public Page<Lecturer> findLecturerByLecturerIdLike(String lectureId, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Lecturer> page = lecturerRepository.findLecturerByIdLike(lectureId, pageable);
        return page;
    }


//    @Override
//    public Lecturer addLectureWithCourse(Lecturer lecturer) {
//        Optional<Lecturer> foundLecturer = lecturerRepository.findById(lecturer.getId());
//        if(foundLecturer.isPresent()){
//
//            Lecturer result =  lecturerRepository.save(lecturer);
//            return result;
//        }
//        return null;
//    }


    @Override
    public boolean removeCourse(Lecturer lecturer, Course course) {
        if (lecturer == null || course == null) {
            return false;
        }

        if (lecturer.getCourses().contains(course)) {
            lecturer.getCourses().remove(course);
            lecturerRepository.save(lecturer);

            return true; // Indicate that the course was successfully removed
        }

        return false;
    }

    @Override
    public Page<Course> findListManagingCourse(Lecturer lecturer, String status, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Criteria criteria = new Criteria();
        // Sort by the "time" in descending order to get the most recent documents
        criteria.andOperator(
                criteria.where("lecturer.id").is(lecturer.getId()),
                status.equalsIgnoreCase("ALL") ? new Criteria() : criteria.where("status").is(status.toUpperCase()),
                criteria.where("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED)
        );
        Query query = new Query(criteria).with(Sort.by(Sort.Order.desc("lecturerCourseIds.createdDate")));

        // Use a Pageable to limit the result set to 5 documents

        List<Course> results = mongoTemplate.find(query, Course.class);
        return PageableExecutionUtils.getPage(results, pageable,
                () -> mongoTemplate.count(query, Course.class));
    }


    public Lecturer findLecturerByAccount(Account account) {
        return lecturerRepository.findLecturerByAccount(account);
    }
}

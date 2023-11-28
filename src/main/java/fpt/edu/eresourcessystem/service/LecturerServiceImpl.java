package fpt.edu.eresourcessystem.service;

import com.mongodb.client.result.UpdateResult;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.repository.LecturerCourseRepository;
import fpt.edu.eresourcessystem.repository.LecturerRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@AllArgsConstructor
@Service(value = "lecturerService")
public class LecturerServiceImpl implements LecturerService {
    private final LecturerRepository lecturerRepository;
    private final LecturerCourseRepository lecturerCourseRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
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
    public Lecturer findByEmail(String email) {
        return lecturerRepository.findByAccount_Email(email);
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


    @Override
    public void addCourseToLecturer(String lecturerId, ObjectId courseId) {
        Query query = new Query(Criteria.where("id").is(lecturerId));
        Update update = new Update().push("courses", courseId);
        mongoTemplate.updateFirst(query, update, Lecturer.class);
    }


    @Override
    public boolean removeCourse(String lecturerId, Course courseId) {
        if (lecturerId != null || courseId != null) {
            Query query = new Query(Criteria.where("id").is(lecturerId));
            Update update = new Update().pull("courses", courseId);
            UpdateResult result = mongoTemplate.updateFirst(query, update, Lecturer.class);

            return result.getModifiedCount() > 0; // Indicate that the course was successfully removed
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

    @Override
    public Page<Document> findListDocuments(Lecturer lecturer, String status, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Criteria criteria = new Criteria();
        // Sort by the "time" in descending order to get the most recent documents
        criteria.andOperator(
                criteria.where("createdBy").is(lecturer.getAccount().getEmail()),
//                criteria.where("status").is(status.toUpperCase()),
                criteria.where("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED)
        );
        Query query = new Query(criteria).with(pageable);
        List<Document> results = mongoTemplate.find(query, Document.class);
        return PageableExecutionUtils.getPage(results, pageable,
                () -> mongoTemplate.count(query, Document.class));
    }

    public Lecturer findLecturerByAccount(Account account) { // status
        return lecturerRepository.findLecturerByAccount(account);
    }

    @Override
    public Lecturer findLecturerById(String lectureId) {
        Lecturer lecturer = lecturerRepository.findLecturerById(lectureId);
        return lecturer;
    }

    @Override
    public boolean update(Lecturer lecturer) {
        Optional<Lecturer> existingLecturer = lecturerRepository.findById(lecturer.getId());
        if (existingLecturer.isEmpty()) {
            return false; // Lecturer not found, update failed
        }

        Lecturer lecturerToUpdate = existingLecturer.get();
        lecturerRepository.save(lecturerToUpdate);

        return true; // Update successful
    }

    @Override
    public boolean softDelete(Lecturer lecturer) {
        Optional<Lecturer> existingLecturer = lecturerRepository.findById(lecturer.getId());
        if (existingLecturer.isPresent()) {
            Lecturer toDelete = existingLecturer.get();
            toDelete.setDeleteFlg(CommonEnum.DeleteFlg.DELETED); // Mark as deleted
            lecturerRepository.save(toDelete);
            return true;
        }
        return false;
    }


}

package fpt.edu.eresourcessystem.service;

import com.mongodb.client.result.UpdateResult;
import fpt.edu.eresourcessystem.dto.Response.LecturerDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.*;
import fpt.edu.eresourcessystem.repository.LecturerCourseRepository;
import fpt.edu.eresourcessystem.repository.LecturerRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public  Lecturer updateCourseForLecturer(Lecturer lecturer, Course result) {
        Query query = new Query(Criteria.where("id").is(lecturer.getId()));
        Update update = new Update().push("courses", new ObjectId(result.getId()));
        mongoTemplate.updateFirst(query, update, Lecturer.class);
        return lecturer;
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
    public boolean removeCourse(String lecturerId, ObjectId courseId) {
        if (lecturerId != null || courseId != null) {
            Query query = new Query(Criteria.where("id").is(lecturerId));
            Update update = new Update().pull("courses", courseId);
            UpdateResult result = mongoTemplate.updateFirst(query, update, Lecturer.class);

            return result.getModifiedCount() > 0; // Indicate that the course was successfully removed
        }

        return false;
    }

    @Override
    public Page<Course> findListManagingCourse(Lecturer lecturer, String search, String status, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize,Sort.by(Sort.Direction.DESC, "lecturerCourseIds.createdDate"));
        Criteria criteria = new Criteria();
        // Sort by the "time" in descending order to get the most recent documents
        criteria.andOperator(
                criteria.where("lecturer.id").is(lecturer.getId()),
                status.equalsIgnoreCase("ALL") ? new Criteria() : criteria.where("status").is(status.toUpperCase()),
                criteria.where("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED),
                // Add search conditions for courseCode or courseName using regex
                new Criteria().orOperator(
                        Criteria.where("courseCode").regex(Pattern.quote(search), "i"),
                        Criteria.where("courseName").regex(Pattern.quote(search), "i")
                )
        );
        Query query = new Query(criteria);
        long total = mongoTemplate.count(query, Course.class);
        List<Course> courses = mongoTemplate.find(query.with(pageable), Course.class);
        return new PageImpl<>(courses, pageable, total);
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

    @Override
    public long getTotalLecturers() {
        return lecturerRepository.count();
    }

    @Override
    public Page<Lecturer> findLecturers(int start, int length, String searchValue) {
        int page = start / length;
        Pageable pageable = PageRequest.of(page, length);

        Query query = new Query().with(pageable);

        if (searchValue != null && !searchValue.isEmpty()) {
            // Tìm các tài khoản phù hợp
            Criteria criteria = new Criteria().orOperator(
                    Criteria.where("name").regex(Pattern.quote(searchValue), "i"),
                    Criteria.where("email").regex(Pattern.quote(searchValue), "i")
            );
            Query accountQuery = new Query(criteria);
            List<Account> matchingAccounts = mongoTemplate.find(accountQuery, Account.class);

            // Chuyển đổi thành danh sách ID tài khoản
            List<ObjectId> accountIds = matchingAccounts.stream()
                    .map(Account::getId)
                    .map(ObjectId::new)
                    .collect(Collectors.toList());

            // Lọc giảng viên theo ID tài khoản
            if (!accountIds.isEmpty()) {
                query.addCriteria(Criteria.where("account.id").in(accountIds));
            }
        }

        // Truy vấn và trả về danh sách giảng viên với phân trang

        List<Lecturer> lecturerList = mongoTemplate.find(query, Lecturer.class);
        long total = mongoTemplate.count(query, Lecturer.class);
        return new PageImpl<>(lecturerList, pageable, total);
    }

//    @Override
//    public List<Lecturer> findLecturers(int start, int length, String searchValue) {
//        int page = start / length;
//        Pageable pageable = PageRequest.of(page, length);
//        Query query = new Query().with(pageable);
//
//        if (searchValue != null && !searchValue.isEmpty()) {
//            // Tìm các tài khoản phù hợp
//            Query accountQuery = new Query().addCriteria(new Criteria().orOperator(
//                    Criteria.where("name").regex(searchValue, "i"),
//                    Criteria.where("email").regex(searchValue, "i")
//            ));
//            List<Account> matchingAccounts = mongoTemplate.find(accountQuery, Account.class);
//
//            // Chuyển đổi thành danh sách ID tài khoản
//            List<ObjectId> accountIds = matchingAccounts.stream()
//                    .map(Account -> new ObjectId(Account.getId()))
//                    .collect(Collectors.toList());
//            // Lọc giảng viên theo ID tài khoản
//            if (!accountIds.isEmpty()) {
//                query.addCriteria(Criteria.where("account.id").in(accountIds));
//            }
//        }
//        // Truy vấn và trả về danh sách giảng viên
//        List<Lecturer> lecturerList = mongoTemplate.find(query, Lecturer.class);
//        return lecturerList;
//    }






    @Override
    public int getFilteredCount(String searchValue) {
        if (searchValue == null || searchValue.isEmpty()) {
            // Nếu không có giá trị tìm kiếm, trả về tổng số lượng giảng viên
            return (int) mongoTemplate.count(new Query(), Lecturer.class);
        }

        // Tìm các tài khoản phù hợp
        List<Account> matchingAccounts = mongoTemplate.find(
                Query.query(new Criteria().orOperator(
                        Criteria.where("name").regex(Pattern.quote(searchValue), "i"),
                        Criteria.where("email").regex(Pattern.quote(searchValue), "i")
                )), Account.class);

        // Lấy danh sách ID tài khoản
        List<String> accountIds = matchingAccounts.stream()
                .map(Account::getId)
                .collect(Collectors.toList());

        // Đếm số lượng giảng viên có liên kết với các ID tài khoản
        Query countQuery = Query.query(Criteria.where("account").in(accountIds));
        return (int) mongoTemplate.count(countQuery, Lecturer.class);
    }

    public Page<LecturerDto> findAllLecturersWithSearch(String searchValue, Pageable pageable) {
        // Step 1: Query the associated "account" documents based on the email field
        Query accountQuery = new Query(Criteria.where("email").regex(Pattern.quote(searchValue), "i"));
        List<Account> matchingAccounts = mongoTemplate.find(accountQuery, Account.class);
        List<String> accountIds = matchingAccounts.stream()
                .map(Account::getId)
                .collect(Collectors.toList());

        // Step 2: Query the "lecturers" based on the IDs obtained from Step 1
        Criteria lecturerCriteria = Criteria.where("account.$id").in(accountIds);
        Query lecturerQuery = new Query(lecturerCriteria).with(pageable);

        List<Lecturer> matchingLecturers = mongoTemplate.find(lecturerQuery, Lecturer.class);
        List<LecturerDto> lecturerDtos = matchingLecturers.stream()
                .map(LecturerDto::new)
                .collect(Collectors.toList());

        long total = mongoTemplate.count(lecturerQuery, Lecturer.class);

        return new PageImpl<>(lecturerDtos, pageable, total);
    }





}

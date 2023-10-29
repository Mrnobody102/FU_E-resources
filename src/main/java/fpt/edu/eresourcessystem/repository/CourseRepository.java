package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("courseRepository")
public interface CourseRepository extends
        MongoRepository<Course, String> {

    @Query("{ '_id' : ?0, 'deleteFlg' : 'PRESERVED' }")
    Optional<Course> findById(String id);

    @Query("{ 'courseCode' : ?0, 'deleteFlg' : 'PRESERVED' }")
    Course findByCourseCode(String courseCode);

    @Query("SELECT c FROM Courses c WHERE c.id in ?1")
    List<Course> findByListId(List<String> id);

    @Query(("{$and:[{ 'deleteFlg' : 'PRESERVED' },"
            + "{$or: ["
            + "    {courseCode: {$regex: ?0}},"
            + "    {courseName: {$regex: ?1}}"
            + "    ]}"
            + "]}"))
    Page<Course> findByCourseCodeLikeOrCourseNameLikeOrDescriptionLike(String code,String name,String description,Pageable pageable);

    @Query("{$and:[{ 'deleteFlg' : 'PRESERVED' },"
            + "{$or: ["
            + "    {courseCode: {$regex: ?0}},"
            + "    {courseName: {$regex: ?1}}"
            + "    ]}"
            + "]}")
    List<Course> findByCodeOrName(String code, String name);

    @Query("{$and: [{ 'deleteFlg' : 'PRESERVED' },"
            + "{$or: ["
            + "    {courseCode: {$regex: ?0}},"
            + "    {courseName: {$regex: ?1}}"
            + "    ]},"
            + "{id: ?2}"
            + "]}")
    List<Course> findByCodeOrNameAndId(String code, String name, String id);

    @Query("{ 'deleteFlg' : 'PRESERVED' }")
    Page<Course> findByCourseNameLikeOrCourseCodeLike(String courseName, String courseCode, Pageable pageable);

    @Query("{ 'deleteFlg' : 'PRESERVED' }")
    Page<Course> findByCourseNameLike(String courseName, Pageable pageable);

    @Query("{ 'deleteFlg' : 'PRESERVED' }")
    Page<Course> findByCourseCodeLike(String courseCode, Pageable pageable);

}

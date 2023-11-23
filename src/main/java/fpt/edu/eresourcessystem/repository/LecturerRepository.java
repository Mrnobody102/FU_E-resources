package fpt.edu.eresourcessystem.repository;

import com.mongodb.lang.NonNull;
import fpt.edu.eresourcessystem.model.Account;
import fpt.edu.eresourcessystem.model.Lecturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("lecturerRepository")
public interface LecturerRepository extends MongoRepository<Lecturer, String> {

    @NonNull
    Optional<Lecturer> findById(@NonNull String id);

    @NonNull
    @Query("{ 'deleteFlg' : 'PRESERVED' }")
    List<Lecturer> findAll();

    Lecturer findByAccountId(String accountId);

    @Query("SELECT l FROM lecturers l  where l.id in ?1")
    List<Lecturer> findByIds(List<String> ids);

    @Query("SELECT l FROM lecturers l  where l.id in ?1")
    Lecturer findByCourseId(String courseId);

    Page<Lecturer> findLecturerByIdLike(String Id, Pageable pageable);

//    Page<Lecturer> findLecturer( Pageable pageable );

    Page<Lecturer> findLecturerByCreatedByLike(String createBy, Pageable pageable);

    Lecturer findLecturerByAccount(Account account);

    Lecturer findLecturerById(String lectureId);

}


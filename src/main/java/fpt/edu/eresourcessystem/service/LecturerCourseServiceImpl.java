package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.dto.Response.LecturerCourseResponseDto;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.LecturerCourse;
import fpt.edu.eresourcessystem.model.LecturerCourseId;
import fpt.edu.eresourcessystem.repository.LecturerCourseRepository;
import fpt.edu.eresourcessystem.repository.LecturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("lecturerCourseService")
@RequiredArgsConstructor
public class LecturerCourseServiceImpl implements LecturerCourseService{
    private final LecturerCourseRepository lecturerCourseRepository;
    private final MongoTemplate mongoTemplate;
    private final LecturerRepository lecturerRepository;

    @Override
    public LecturerCourse findById(LecturerCourseId lecturerCourseId) {
        Optional<LecturerCourse> lecturer = lecturerCourseRepository.findById(lecturerCourseId);
        return lecturer.orElse(null);
    }

    @Override
    public LecturerCourse add(LecturerCourse lecturerCourse) {
        if(null!=lecturerCourse && null!=lecturerCourse.getId()){
            if(null!=findById(lecturerCourse.getId())){
                return null;
            }else {
                LecturerCourse result = lecturerCourseRepository.save(lecturerCourse);
                return result;
            }
        }
        return null;
    }

    @Override
    public void delete(LecturerCourse lecturerCourse) {
        lecturerCourseRepository.delete(lecturerCourse);
    }

    @Override
    public List<LecturerCourse> findLecturerCoursesById(Lecturer lecturer) {

        List<LecturerCourse> lecturerCourseList = lecturerCourseRepository.findLecturerCoursesById(lecturer);
        return lecturerCourseList;
    }

    @Override
    public List<LecturerCourseResponseDto> findCourseManageHistory(String courseId) {
        Query query = new Query();

        Criteria criteria = Criteria.where("id.courseId").is(courseId)
                .and("id.createdDate").exists(true)
                .and("id.lastModifiedDate").exists(true);
        query.addCriteria(criteria);
        List<LecturerCourse> lecturerCourses = mongoTemplate.find(query, LecturerCourse.class);
        List<String> lecturerIds = lecturerCourses.stream()
                .map(lecturerCourse -> lecturerCourse.getId().getLecturerId())
                .collect(Collectors.toList());
        List<LecturerCourseResponseDto> result = new ArrayList<>();
        List<Lecturer>  lecturers = lecturerRepository.findByIds(lecturerIds);
        for (LecturerCourse lecturerCourse : lecturerCourses) {
            Lecturer lecturer = lecturers.stream()
                    .filter(l -> l.getId().equals(lecturerCourse.getId().getLecturerId()))
                    .findFirst()
                    .orElse(null);

            if (lecturer != null) {
                result.add(new LecturerCourseResponseDto(lecturer, lecturerCourse));
                System.out.println(lecturerCourse.getId().getCreatedDate() + "courseId = " + lecturerCourse.getId().getCourseId() + "lecturer" +lecturer.getId());
            }
        }
        return result;
    }

    @Override
    public LecturerCourse findCurrentLecturer(String courseId) {
        System.out.println(courseId);
        Query query = new Query();
        Criteria criteria = Criteria.where("id.courseId").is(courseId)
                .and("id.createdDate").exists(true)
                .and("id.lastModifiedDate").exists(false);
        query.addCriteria(criteria);
        LecturerCourse lecturerCourses = mongoTemplate.findOne(query, LecturerCourse.class);
        return lecturerCourses;
    }


}

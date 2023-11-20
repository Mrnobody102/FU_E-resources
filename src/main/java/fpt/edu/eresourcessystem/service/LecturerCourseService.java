package fpt.edu.eresourcessystem.service;


import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.LecturerCourse;
import fpt.edu.eresourcessystem.model.LecturerCourseId;

import java.util.List;
import java.util.Optional;

public interface LecturerCourseService {
    LecturerCourse findById(LecturerCourseId lecturerCourseId);

    LecturerCourse add(LecturerCourse lecturerCourse);

    List<LecturerCourse> findLecturerCoursesById(Lecturer lecturer);
}

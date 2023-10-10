package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Lecturer;

import java.util.List;

public interface LecturerService {
    List<Lecturer> findByCourseId(String courseId);

}

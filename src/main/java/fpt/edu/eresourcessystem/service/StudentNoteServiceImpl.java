package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.CourseLog;
import fpt.edu.eresourcessystem.model.StudentNote;
import fpt.edu.eresourcessystem.repository.StudentNoteRepository;
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

@Service("studentNoteService")
public class StudentNoteServiceImpl implements StudentNoteService{

    private final StudentNoteRepository studentNoteRepository;
    private final MongoTemplate mongoTemplate;

    public StudentNoteServiceImpl(StudentNoteRepository studentNoteRepository, MongoTemplate mongoTemplate) {
        this.studentNoteRepository = studentNoteRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<StudentNote> getNoteByStudent(String studentId, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Criteria criteria = new Criteria();
        criteria.and("createdDate").exists(true);
        criteria.and("createdBy").is(studentId);
        Query query = new Query(criteria).with(Sort.by(Sort.Order.desc("createdDate"))).with(pageable);
        List<StudentNote> results = mongoTemplate.find(query, StudentNote.class);
        Page<StudentNote> page =  PageableExecutionUtils.getPage(results, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), StudentNote.class));
        return page;
    }
}

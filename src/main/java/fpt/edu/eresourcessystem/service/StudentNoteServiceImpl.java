package fpt.edu.eresourcessystem.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import fpt.edu.eresourcessystem.dto.StudentNoteDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.DocumentNote;
import fpt.edu.eresourcessystem.model.StudentNote;
import fpt.edu.eresourcessystem.repository.StudentNoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service("studentNoteService")
public class StudentNoteServiceImpl implements StudentNoteService{

    private final StudentNoteRepository studentNoteRepository;
    private final MongoTemplate mongoTemplate;
    private GridFsTemplate template;
    private GridFsOperations operations;

    public StudentNoteServiceImpl(StudentNoteRepository studentNoteRepository, MongoTemplate mongoTemplate, GridFsTemplate template, GridFsOperations operations) {
        this.studentNoteRepository = studentNoteRepository;
        this.mongoTemplate = mongoTemplate;
        this.template = template;
        this.operations = operations;
    }

    @Override
    public StudentNote findById(String studentNoteId) {
        StudentNote studentNote = studentNoteRepository
                .findByIdAndDeleteFlg(studentNoteId, CommonEnum.DeleteFlg.PRESERVED);
        return studentNote;
    }

    @Override
    public Page<StudentNote> getNoteByStudent(String studentId, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Criteria criteria = new Criteria();
        criteria.and("createdDate").exists(true);
//        criteria.and("createdBy").is(studentId);
        criteria.and("studentId").is(studentId);
        criteria.and("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED);
        Query query = new Query(criteria).with(Sort.by(Sort.Order.desc("createdDate"))).with(pageable);
        List<StudentNote> results = mongoTemplate.find(query, StudentNote.class);
        Page<StudentNote> page =  PageableExecutionUtils.getPage(results, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), StudentNote.class));
        return page;
    }

    @Override
    public String addFile(MultipartFile upload) throws IOException {
        //define additional metadata
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());

        //store in database which returns the objectID
        Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);

        //return as a string
        return fileID.toString();
    }

    @Override
    public StudentNote addStudentNote(StudentNoteDto studentNoteDTO){
        //search file
        if (null == studentNoteDTO.getId()) {
                StudentNote result = studentNoteRepository.save(new StudentNote(studentNoteDTO));
                return result;
        } else {
            Optional<StudentNote> checkExist = studentNoteRepository.findById(studentNoteDTO.getId());
            if (!checkExist.isPresent()) {
                StudentNote result = studentNoteRepository.save(new StudentNote(studentNoteDTO));
                return result;
            }
            return null;
        }
    }

    @Override
    public StudentNote updateStudentNote(StudentNote studentNote) {
        if (null == studentNote || null==studentNote.getId()){
            return null;
        }
        StudentNote savedStudentNote = studentNoteRepository
                .findByIdAndDeleteFlg(studentNote.getId(), CommonEnum.DeleteFlg.PRESERVED);
        if(null != savedStudentNote){
            StudentNote result =  studentNoteRepository.save(studentNote);
            return result;
        }
        return null;
    }

    @Override
    public boolean softDeleteStudentNote(StudentNote studentNote) {
        StudentNote check = studentNoteRepository.findByIdAndDeleteFlg(studentNote.getId(), CommonEnum.DeleteFlg.PRESERVED);
        if(null != check){
            // SOFT DELETE
            studentNote.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            studentNoteRepository.save(studentNote);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteStudentNote(StudentNote studentNote) {
        StudentNote check = studentNoteRepository.findByIdAndDeleteFlg(studentNote.getId(), CommonEnum.DeleteFlg.PRESERVED);
        if(null != check){
            studentNoteRepository.delete(studentNote);
            return true;
        }
        return false;
    }
}

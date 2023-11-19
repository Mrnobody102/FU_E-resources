package fpt.edu.eresourcessystem.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import fpt.edu.eresourcessystem.dto.StudentNoteDTO;
import fpt.edu.eresourcessystem.model.StudentNote;
import fpt.edu.eresourcessystem.repository.StudentNoteRepository;
import org.apache.commons.io.IOUtils;
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
import org.springframework.util.StringUtils;
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
    public Page<StudentNote> getNoteByStudent(String studentId, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Criteria criteria = new Criteria();
        criteria.and("createdDate").exists(true);
//        criteria.and("createdBy").is(studentId);
        criteria.and("studentId").is(studentId);
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
    public StudentNote addStudentNote(StudentNoteDTO studentNoteDTO, String id) throws IOException {
        //search file
        if (null == studentNoteDTO.getId()) {
            if (!id.equalsIgnoreCase("fileNotFound")){
                GridFSFile file = template.findOne(new Query(Criteria.where("_id").is(id)));
                studentNoteDTO.setContent(IOUtils.toByteArray(operations.getResource(file).getInputStream()));
                String filename = StringUtils.cleanPath(file.getFilename());
                String fileExtension = StringUtils.getFilenameExtension(filename);
                studentNoteDTO.setSuffix(fileExtension);
                StudentNote result = studentNoteRepository.save(new StudentNote(studentNoteDTO));
                return result;
            } else {
                studentNoteDTO.setSuffix("unknown");
                StudentNote result = studentNoteRepository.save(new StudentNote(studentNoteDTO));
                return result;
            }
        } else {
            Optional<StudentNote> checkExist = studentNoteRepository.findById(studentNoteDTO.getId());
            if (!checkExist.isPresent()) {
                StudentNote result = studentNoteRepository.save(new StudentNote(studentNoteDTO));
                return result;
            }
            return null;
        }
    }
}

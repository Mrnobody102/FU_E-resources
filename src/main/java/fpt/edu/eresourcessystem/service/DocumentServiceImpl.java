package fpt.edu.eresourcessystem.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.repository.DocumentRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service("documentService")
public class DocumentServiceImpl implements DocumentService {
    private DocumentRepository documentRepository;
    private final MongoTemplate mongoTemplate;

    private GridFsTemplate template;

    private GridFsOperations operations;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, MongoTemplate mongoTemplate, GridFsTemplate template, GridFsOperations operations) {
        this.documentRepository = documentRepository;
        this.mongoTemplate = mongoTemplate;
        this.template = template;
        this.operations = operations;
    }

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
    public Document findById(String documentId) {
        Optional<Document> document = documentRepository.findById(documentId);
        return document.orElse(null);
    }

    @Override
    public List<Document> findByListId(List<String> documentIds) {
            Query query = new Query(Criteria.where("id").in(documentIds));
            List<Document> documents = mongoTemplate.find(query, Document.class);
            return documents;
    }

    @Override
    public List<Document> findAll() {
        List<Document> documents = documentRepository.findAll();
        return documents;
    }

    @Override
    public Page<Document> filterAndSearchDocument(String course, String topic, String title, String description, int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<Document> page = documentRepository.filterAndSearchDocument(course, topic, title, description,
                pageable);
        return page;
    }


    @Override
    public Document addDocument(Document document, String id) throws IOException {
        //search file
        GridFSFile file = template.findOne( new Query(Criteria.where("_id").is(id)) );
        if (null == document.getId()) {
            document.setContent( IOUtils.toByteArray(operations.getResource(file).getInputStream()) );

            System.out.println(operations.getResource(file).getFilename());
            Document result = documentRepository.save(document);
            return result;
        } else {
            Optional<Document> checkExist = documentRepository.findById(document.getId());
            if (!checkExist.isPresent()) {
                document.setContent( IOUtils.toByteArray(operations.getResource(file).getInputStream()) );
                Document result = documentRepository.save(document);
                return result;
            }
            return null;
        }
    }

    @Override
    public Document updateDocument(Document document) throws IOException {
        Optional<Document> checkExist = documentRepository.findById(document.getId());
        if (checkExist.isPresent()) {
            Document result = documentRepository.save(document);
            return result;
        }
        return null;
    }

    @Override
    public boolean delete(String documentId) {
        Optional<Document> check = documentRepository.findById(documentId);
        if (check.isPresent()) {
            documentRepository.deleteById(documentId);
            return true;
        }
        return false;
    }
}

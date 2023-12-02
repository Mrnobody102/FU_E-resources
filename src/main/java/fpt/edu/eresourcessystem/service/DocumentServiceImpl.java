package fpt.edu.eresourcessystem.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import fpt.edu.eresourcessystem.dto.DocumentDto;
import fpt.edu.eresourcessystem.dto.Response.DocumentResponseDto;
import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.enums.DocumentEnum;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import fpt.edu.eresourcessystem.repository.DocumentRepository;
import fpt.edu.eresourcessystem.repository.elasticsearch.EsDocumentRepository;
import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fpt.edu.eresourcessystem.utils.CommonUtils.extractTextFromFile;

@Service("documentService")
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final EsDocumentRepository esDocumentRepository;
    private final MongoTemplate mongoTemplate;

    private final GridFsTemplate template;

    private final GridFsOperations operations;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, EsDocumentRepository esDocumentRepository, MongoTemplate mongoTemplate, GridFsTemplate template, GridFsOperations operations) {
        this.documentRepository = documentRepository;
        this.esDocumentRepository = esDocumentRepository;
        this.mongoTemplate = mongoTemplate;
        this.template = template;
        this.operations = operations;
    }

    @Override
    public List<DocumentResponseDto> findRelevantDocument(String topicId, String docId) {
        Query query = new Query(Criteria.where("deleteFlg").is(CommonEnum.DeleteFlg.PRESERVED)
                .and("id").ne(docId)
                .and("topic.id").is(topicId))
                .skip(0)
                .limit(9)
                .with(Sort.by(Sort.Order.desc("createdDate")));
        List<Document> documents = mongoTemplate.find(query, Document.class);
        if (null != documents) {
            List<DocumentResponseDto> responseList = documents.stream()
                    .filter(entity -> CommonEnum.DeleteFlg.PRESERVED.equals(entity.getDeleteFlg()))
                    .map(entity -> new DocumentResponseDto(entity))
                    .collect(Collectors.toList());
            return responseList;
        } else return null;
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
    public List<Document> findByLecturer(Lecturer lecturer) {
        List<Document> documents = documentRepository.findByCreatedBy(lecturer.getAccount().getEmail());
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
    public Iterable<EsDocument> searchDocument(String search) {
        return esDocumentRepository.findByTitleContainingOrDescriptionContainingOrDocTypeLikeIgnoreCase(search);
    }

    public String addFile(MultipartFile upload) throws IOException {

        //define additional metadata
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());

        //store in database which returns the objectID
        Object fileID = template.store(upload.getInputStream(),
                upload.getOriginalFilename(),
                upload.getContentType(),
                metadata);

        //return as a string
        return fileID.toString();
    }


    @Override
    public GridFSFile getGridFSFile(ObjectId id) {
        return template.findOne(new Query(Criteria.where("_id").is(id)));
    }

    @Override
    public byte[] getGridFSFileContent(ObjectId id) throws IOException {
        GridFSFile file = template.findOne(new Query(Criteria.where("_id").is(id)));
        return IOUtils.toByteArray(operations.getResource(file).getInputStream());
    }


    @Override
    public Document addDocument(DocumentDto documentDTO, String id) throws IOException, TikaException, SAXException {
        //search file
        if (null == documentDTO.getId()) {
            if (id.equalsIgnoreCase("fileNotFound")) {
                documentDTO.setSuffix("unknown");
                Document result = documentRepository.save(new Document(documentDTO));
                esDocumentRepository.save(new EsDocument(result));
                return result;
            } else if (id.equalsIgnoreCase("uploadToCloud")) {
                Document result = documentRepository.save(new Document(documentDTO));
                esDocumentRepository.save(new EsDocument(result));
                return result;
            } else {
                GridFSFile file = getGridFSFile(new ObjectId(id));
                documentDTO.setContentId(file.getObjectId());
                String filename = StringUtils.cleanPath(file.getFilename());
                String fileExtension = StringUtils.getFilenameExtension(filename);
                documentDTO.setSuffix(fileExtension);
                Document result = documentRepository.save(new Document(documentDTO));
                esDocumentRepository.save(new EsDocument(result));
                return result;
            }
        } else {
            Optional<Document> checkExist = documentRepository.findById(documentDTO.getId());
            if (!checkExist.isPresent()) {
                Document result = documentRepository.save(new Document(documentDTO));
                esDocumentRepository.save(new EsDocument(result));
                return result;
            }
            return null;
        }
    }

    @Override
    public Document updateDocument(Document document, String currentFileId, String id) throws IOException {
        //search file
        if(null != currentFileId) {
            template.delete(new Query(Criteria.where("_id").is(currentFileId)));
        }
        if (null != document.getId()) {
            if (id.equalsIgnoreCase("fileNotFound")) {
                document.setSuffix("unknown");
                Document result = documentRepository.save(document);
                esDocumentRepository.save(new EsDocument(result));
                return result;
            } else if (id.equalsIgnoreCase("uploadToCloud")) {
                Document result = documentRepository.save(document);
                esDocumentRepository.save(new EsDocument(result));
                return result;
            } else {
                GridFSFile file = getGridFSFile(new ObjectId(id));
                document.setContentId(file.getObjectId());
                String filename = StringUtils.cleanPath(file.getFilename());
                String fileExtension = StringUtils.getFilenameExtension(filename);
                document.setFileName(filename);
                document.setSuffix(fileExtension);
                document.setDocType(DocumentEnum.DocumentFormat.getDocType(fileExtension));
                Document result = documentRepository.save(document);
                esDocumentRepository.save(new EsDocument(result));
                return result;
            }
        } else {
            Optional<Document> checkExist = documentRepository.findById(document.getId());
            if (!checkExist.isPresent()) {
                Document result = documentRepository.save(document);
                esDocumentRepository.save(new EsDocument(result));
                return result;
            }
            return null;
        }
    }

    @Override
    public Document updateDoc(Document document){
        Document updateDoc = documentRepository.save(document);
        return updateDoc;
    }

    @Override
    public boolean softDelete(Document document) {
        Optional<Document> check = documentRepository.findById(document.getId());
        if (check.isPresent()) {
            // Here: Soft delete note, question & answer

            // Soft delete
            document.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            documentRepository.save(document);
            return true;
        }
        return false;
    }

    @Override
    public boolean setToDefaultResourceType(Course course, Document document) {
        Optional<Document> check = documentRepository.findById(document.getId());
        if (check.isPresent()) {
            // Here: Soft delete note, question & answer

            // Soft delete
            document.setResourceType(course.getResourceTypes().get(4));
            documentRepository.save(document);
            return true;
        }
        return false;
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

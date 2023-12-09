package fpt.edu.eresourcessystem.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import fpt.edu.eresourcessystem.dto.DocumentDto;
import fpt.edu.eresourcessystem.dto.Response.DocumentResponseDto;
import fpt.edu.eresourcessystem.dto.Response.TopicResponseDto;
import fpt.edu.eresourcessystem.model.Course;
import fpt.edu.eresourcessystem.model.Document;
import fpt.edu.eresourcessystem.model.Lecturer;
import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
import org.apache.tika.exception.TikaException;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface DocumentService {
    List<Document> findAll();

    List<Document> findByLecturer(Lecturer lecturer);

    Page<Document> filterAndSearchDocument(String course, String topic, String title, String description, int pageIndex, int pageSize);

    Iterable<EsDocument> searchDocument(String search);

    GridFSFile getGridFSFile(ObjectId id) throws IOException;

    byte[] getGridFSFileContent(ObjectId id) throws IOException;

    Document addDocument(DocumentDto documentDTO, String id) throws IOException, TikaException, SAXException;

    Document findById(String documentId);
    List<Document> findByListId(List<String> documentIds);

    Document updateDocument(Document document, String currentFileId, String id) throws IOException;

    Document updateDoc(Document document);

    boolean softDelete(Document document);

    boolean setToDefaultResourceType(Course course, Document document);

    boolean delete(String documentId);
    String addFile(MultipartFile file) throws IOException;

    List<DocumentResponseDto> findRelevantDocument(String topicId, String docId);

    HashMap<TopicResponseDto, List<DocumentResponseDto>> findAllDocumentsByCourseAndResourceType(String courseId, String resourceTypeId);
}

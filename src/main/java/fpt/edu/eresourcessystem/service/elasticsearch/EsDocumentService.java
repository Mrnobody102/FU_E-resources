//package fpt.edu.eresourcessystem.service.elasticsearch;
//
//import java.util.List;
//import java.util.Optional;
//
//import fpt.edu.eresourcessystem.model.elasticsearch.EsDocument;
//import fpt.edu.eresourcessystem.repository.elasticsearch.EsDocumentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EsDocumentService {
//
//    private final EsDocumentRepository bookRepository;
//
//    @Autowired
//    public EsDocumentService(EsDocumentRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }
//
//    public EsDocument save(EsDocument book) {
//        return bookRepository.save(book);
//    }
//
//    public void delete(EsDocument book) {
//        bookRepository.delete(book);
//    }
//
//    public Optional<EsDocument> findOne(String id) {
//        return bookRepository.findById(id);
//    }
//
//    public Iterable<EsDocument> findAll() {
//        return bookRepository.findAll();
//    }
//}

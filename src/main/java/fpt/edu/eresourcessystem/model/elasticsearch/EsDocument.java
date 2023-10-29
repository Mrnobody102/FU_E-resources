//package fpt.edu.eresourcessystem.model.elasticsearch;
//
//import fpt.edu.eresourcessystem.enums.DocumentEnum;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//
//import java.io.Serializable;
//
//@Document(indexName = "documents")
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@ToString
//public class EsDocument implements Serializable {
//    @Id
//    private String documentId;
//    @Field
//    private String topic;
//    @Field
//    private String course;
//    @Field
//    private String title;
//    @Field
//    private String description;
//    @Field
//    private DocumentEnum.DocumentFormat docType;
//    @Field
//    private String contentLink; //link video, audio - cloud
//}
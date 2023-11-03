package fpt.edu.eresourcessystem.model;


import fpt.edu.eresourcessystem.enums.CommonEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.management.Query;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("librarians")
public class Librarian{
    @Id
    private String id;
    @NotNull
    @DocumentReference
    private Account account;
    @DocumentReference(lazy = true)
    private List<Course> createdCourses;

    // Delete flag
    private CommonEnum.DeleteFlg deleteFlg;
    //Audit Log
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdDate;
    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    private String lastModifiedDate;

//    // Constructor DTO
//    public List<Course> loadCreatedCourses() {
//        if (createdCourses == null) {
//            // Trigger the loading of createdCourses by querying the database
//            createdCourses = loadCreatedCoursesFromDatabase();
//        }
//        return createdCourses;
//    }
//
//    // Private method to load createdCourses from the database
//    private List<Course> loadCreatedCoursesFromDatabase() {
//        // You can implement the logic here to query the database for createdCourses
//        // For example, using Spring Data MongoDB or MongoClient
//        // Return the loaded createdCourses
//        Query query = new Query(Criteria.where("librarian").is(this.id));
//        List<Course> createdCourses = mongoOperations.find(query, Course.class,
//                List<Course> createdCourses =
//                        "your_course_collection_name");
//        return createdCourses;
//    }
}

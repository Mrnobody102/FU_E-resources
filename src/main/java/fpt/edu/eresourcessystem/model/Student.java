package fpt.edu.eresourcessystem.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends Account{
    @Id
    private String accountId;  // Reference to Account
    //Reference by String ID, increase performance
    private List<String> enrolledCourses;
    private List<String> savedDocuments;
}

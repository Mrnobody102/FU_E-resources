package fpt.edu.eresourcessystem.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("major")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Major {
    @Id
    private String majorId;
    private String majorName;
    private String description;  // or perhaps a URL to the content if stored externally

    private List<String> courses;
}
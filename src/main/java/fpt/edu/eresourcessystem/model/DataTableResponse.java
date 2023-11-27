package fpt.edu.eresourcessystem.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DataTableResponse {
    private int draw;
    private long recordsTotal;
    private long recordsFiltered;
    private List<TrainingType> data;

    // Getters and setters
}


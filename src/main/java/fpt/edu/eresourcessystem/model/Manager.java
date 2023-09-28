package fpt.edu.eresourcessystem.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Manager extends Account{

}

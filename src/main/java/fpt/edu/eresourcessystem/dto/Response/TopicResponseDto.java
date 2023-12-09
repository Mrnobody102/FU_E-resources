package fpt.edu.eresourcessystem.dto.Response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class TopicResponseDto {

    private String id;
    private String topicTitle;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TopicResponseDto that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString(){
        return "TopicResponseDto : { " +
                " id:'" +id +"\'" +
                ", title: '" + topicTitle +"\'}";
    }

}

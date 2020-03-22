package io.xxnjdg.learning.mongodb1.QueryingDocuments.jsonschema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ssn {
    @Id
    private String id;
    private String ssn;
    private String dssn;

    public Ssn(String ssn,String dssn){
        this.ssn = ssn;
        this.dssn = dssn;
    }
}

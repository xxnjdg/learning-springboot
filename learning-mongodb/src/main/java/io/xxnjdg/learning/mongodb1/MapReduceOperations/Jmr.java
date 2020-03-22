package io.xxnjdg.learning.mongodb1.MapReduceOperations;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Jmr {
    @Id
    private String id;
    private List<String> x = new ArrayList<>();

    public Jmr(String ...x){
        this.x.addAll(Arrays.asList(x));
    }
}

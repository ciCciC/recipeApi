package com.example.recipeapi.document;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@NoArgsConstructor
@Data
@Document(indexName = "user")
public class User {
    @Id
    private String id;
    private String fname;
    private String lname;

    @Builder
    public User(String id, String fname, String lname){
        this.id = id;
        this.fname = fname;
        this.lname = lname;
    }
}

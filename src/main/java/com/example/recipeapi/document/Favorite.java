package com.example.recipeapi.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@NoArgsConstructor
@Data
@Document(indexName = "favorite")
public class Favorite {

    @Id
    private String id;
    private String userId;
    private String recipeId;
}

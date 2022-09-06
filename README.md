# Recipe Application
Provides (simple n layered architecture) CRUD operations for managing recipes along with the ability to search for recipes by text/filter within the distribution of documents.

# Architecture
- The presentation layer consist of CRUD operations for user, favorite, recipe and search
- The business layer consist of acquiring data by applying functions using repositories along with populating (see DataLoader) the document oriented database with 200 records from a csv file in order to simulate real application
- The config pkg consist of the configuration of Elastic Search communication
- Elastic Search and Kibana are executed within a docker environment which can be accessed by their representative ports
  - elastic: 9200
  - kibana: 5601

# Techniques
- Spring Boot
- Elastic Search
- Map Struct
- Lombok

# Prerequisite
- install Docker (should support docker-compose)
- install JDK 17
- install a SDK e.g. IntelliJ or VScode (optional)
- git clone the repo

# Reproducibility
- go to the root of the project within the terminal
- then run the following: docker-compose -f docker-compose.yml up
- lastly run within the terminal (unless a SDK is being used):
  - for Application -> ```./gradlew bootRun ```
  - for Test -> ```./gradlew test ```
    - unit/ integration test
    - testing crud and search/filter operations

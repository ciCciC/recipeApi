# Recipe Application
Provides (simple n layered architecture) CRUD operations for managing recipes along with the ability to search for recipes by text/filter within the distribution of documents.

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

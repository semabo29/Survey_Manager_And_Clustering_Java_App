Behavioral Prototype Extractor - Programming Project

This repository contains the implementation of a behavioral profile clustering application developed as part of an academic programming course. 
The project is divided into three main deliveries, reflecting the incremental development and refinement of the application.

The goal of the application is to analyze survey responses, identify different behavioral profiles, and compute representative individuals for each group. 
People with similar answers are clustered together using k-means and k-medoids algorithms. The project follows software architecture principles 
and applies design patterns to ensure modularity, maintainability, and extensibility.


📂 Repository Structure

The repository is organized by deliveries:

Entrega1
 📝Expected:
  - Implements the initial clustering algorithms:
    - k-medoids with greedy initialization
    - k-means with k-means++ and random initialization
  - Includes drivers that simulate the application via the terminal.
  - Uses JUnit and Mockito for unit testing, with mocks and stubs where appropriate.
  - Demonstrates the core architecture of the project, including modular classes, domain objects, and data structures.

 ⚙️Done:
  Features
    - Profiles: create, load, view, and import multiple profiles from a file.
    - Surveys: create, modify, delete, and load surveys; add questions of type Free Text, Numeric, or With Options; 
      manage options for multiple-choice questions.
    - Responses: answer full surveys or individual questions; load, save, and delete responses; validates required questions and numeric ranges.
    - Test Dataset: load surveys and responses from structured files for testing.
    - Analysis: configure clustering algorithms (KMeans/KMedoids), initializers, number of clusters (K), and quality evaluators 
      (Silhouette, Calinski-Harabasz, Davies-Bouldin); run analysis and view clusters with participant emails.
    - Unit Testing: comprehensive unit tests covering domain objects, controllers, and persistence logic using JUnit and Mockito.
    - Persistence: profiles, surveys, and responses are persisted to disk; imported and saved automatically.
    - Drivers: 2 drivers developed, one for the generic use of app called DriverGeneral and the other focused on sruvey analysis called DriverEncuesta.
    
Entrega2

  - Focuses on diagrams and design documentation:
  - Domain and controller diagrams
  - Views and presentation controllers
  - Data managers
  - Includes brief descriptions of attributes, methods, data structures, and algorithms used for the main functionalities.

Entrega3

  - Final delivery with complete project implementation:
  - All classes of the project organized according to packages for direct compilation
  - Executable applications and full test datasets
  - Optional documentation generated with Doxygen or similar tools
  - User manuals and comprehensive test descriptions
  - Represents the final, polished version of the software, ready for evaluation.

🛠 Technology & Tools

  - Language: Java 21.*
  - Testing: JUnit 4.* and Mockito for unit and integration tests
  - Build system: Gradle (project template provided by the university)
  - Software design: Modular architecture, design patterns, separation of concerns
  - Version control: Git (GitLab for coursework; GitHub for portfolio and public showcase)

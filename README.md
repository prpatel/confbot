# Confbot - your handy conference QA bot

This is a Spring Boot and Spring AI application to answer attendee, speaker, and sponsor questions about the Devnexus conference (devnexus.com).

It is built using:
* Spring Boot
* Spring AI
* Ollama to serve a local AI model
* Postgres in a docker compose for the sesssion/speaker data, as well as pgvectorstore for RAG queries

## Quickstart
1. git pull https://github.com/prpatel/confbot
2. adjust src/main/application.properties
3. ensure you have the model loaded into ollama (ollama run qwen3-coder:latest)
4. mvn spring-boot:run
5. visit: http://localhost:8888/admin and click the button
6. visit: http://localhost:8888/ to try it out!

Send any feedback here via a GH issue or reach out to my [Linkedin](https://www.linkedin.com/in/prpatel/)[]!

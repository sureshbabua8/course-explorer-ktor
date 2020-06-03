# ktings
A simulation of the CS 125 gradebook in Kotlin.  Uses KTOR to simulate grade updates and class statistics, as well as Kotlin-Statistics to calculate those values.

## API Documentation

|   **type**   | **endpoint**                |**result**     |
|   ---------- | -------------               |:------------- |
|   `GET`      | `/netid/overview`           | if a student with the`netid` is registered for the course, retrieves grade with and without drops |
|   `GET`      | `/netid/lecture`            | if a student with the `netid` is registered for the course, retrieves lecture performance with and without drops    |
|   `GET`      | `/netid/lab`                | if a student with the `netid` is registered for the course, retrieves lab performance with and without drops    |
|   `GET`      | `/netid/quiz`               | if a student with the `netid` is registered for the course, retrieves quiz performance with and without drops     |
|   `GET`      | `/netid/exam`               | if a student with the `netid` is registered for the course, retrieves exam performance with and without drops     |
|   `GET`      | `/netid/mp`                 | if a student with the `netid` is registered for the course, retrieves mp performance with and without drops     |
|   `GET`      | `/netid/homework`           | if a student with the `netid` is registered for the course, retrieves homework performance with and without drops     |
|   `GET`      | `/netid/final project`      | if a student with the `netid` is registered for the course, retrieves final project grade with and without drops            |
|   `GET`      | `/course/overview`          | display course's average performance with and without drops with and without drops      |
|   `GET`      | `/course/lecture`           | retrieves course's overall lecture performance with and without drops    |
|   `GET`      | `/course/lab`               | retrieves course's overall lab performance with and without drops    |
|   `GET`      | `/course/quiz`              | retrieves course's overall quiz performance with and without drops     |
|   `GET`      | `/course/exam`              | retrieves course's overall exam performance with and without drops     |
|   `GET`      | `/course/mp`                | retrieves course's overall mp performance with and without drops     |
|   `GET`      | `/course/homework`          | retrieves course's overall homework performance with and without drops     |
|   `GET`      | `/course/final project`     | retrieves course's overall final project grade with and without drops            |
|  `POST`      | `/addStudent/netid`         | registers a student with the `netid` for the course |
|  `POST`      | `/uploadAssignment/netid`         | adds assignment to a student with the `netid` if the student is registered for the course |

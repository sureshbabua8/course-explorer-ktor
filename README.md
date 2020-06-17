# UIUC CIS API Wrapper
**NEEDS TO BE UPDATED**

Databinds XML responses by [UIUC's Course Explorer API](https://courses.illinois.edu/cisdocs/) into usable `JSON` bodies.

## API Documentation

The API wrapper currently provides a `JSON` response to `GET` requests for a given year, term (summer, fall, winter, spring), subject, course, and section.

### By Year
This request returns a response of the given terms (semesters) in a year and original CIS API links for those terms
ex. endpoint: `/2020`

The `JSON` body has the following hierarchy:

```json
{"calendarYear": {
    "year": 2020,
    "terms": [
        {
            "href": "https://courses.illinois.edu/cisapp/explorer/schedule/2020/winter.xml",
            "content": "Winter 2020"
        },
        {
            "href": "https://courses.illinois.edu/cisapp/explorer/schedule/2020/spring.xml",
            "content": "Spring 2020"
        },
        {
            "href": "https://courses.illinois.edu/cisapp/explorer/schedule/2020/summer.xml",
            "content": "Summer 2020"
        },
        {
            "href": "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall.xml",
            "content": "Fall 2020"
        }
    ]
}}
```

### By Term

ex. `/2020/fall`

This request returns a given response of the different subjects instructed in a specific term.

```json
{"term": {
    "subjects": [
        {
            "code": "AAS",
            "href": "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/AAS.xml",
            "subjectName": "Asian American Studies"
        },
        {
            "code": "ABE",
            "href": "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/ABE.xml",
            "subjectName": "Agricultural and Biological Engineering"
        },
        {
            "code": "ACCY",
            "href": "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/ACCY.xml",
            "subjectName": "Accountancy"
        },
        {
            "code": "ACE",
            "href": "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/ACE.xml",
            "subjectName": "Agricultural and Consumer Economics"
        },
        {
            "code": "ZULU",
            "href": "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/ZULU.xml",
            "subjectName": "Zulu"
        }
    ],
    "label": "Fall 2020",
}}
```

### By Subject

ex. `/2020/fall/ZULU`

This returns a list of all courses in the given subject code and some basic attributes of the subject/department.

```json
{"subject": {
    "courses": [{
        "courseTitle": "Elementary Zulu II",
        "href": "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/ZULU/202.xml",
        "courseId": 202
    }],
    "unitName": "Linguistics",
    "webSiteURL": "www.linguistics.uiuc.edu",
    "subjectId": "ZULU",
    "term": "Fall 2020",
    "subjectComment": "Subjects associated with this department include: Arabic (ARAB), Bamana (BMNA), English as an International Language (EIL), English as a Second Language (ESL), Modern Greek (GRKM), Hindi (HNDI), Lingala (LGLA), Linguistics (LING), Persian (PERS), Sanskrit (SNSK), Swahili (SWAH), Turkish (TURK), Wolof (WLOF), and Zulu (ZULU)."
}}
```

### By Course

ex. `2020/fall/ZULU/202`

This request returns a description of the course, as well as all of its sections.

```json
{"course": {
    "creditHours": "5 hours.",
    "courseTitle": "Elementary Zulu II",
    "courseSectionInformation": "Same as AFST 252. Participation in the language laboratory is required. Prerequisite: ZULU 201.",
    "description": "Continuation of ZULU 201 with introduction of more advanced grammar; emphasis on more fluency in speaking, reading, and writing simple sentences in standard Zulu. Same as AFST 252. Participation in the language laboratory is required. Prerequisite: ZULU 201.",
    "term": "Fall 2020",
    "courseId": "ZULU 202",
    "sections": [{
        "name": "A",
        "href": "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/ZULU/202/71955.xml",
        "crn": 71955
    }]
}}
```

### By Section

ex. `2020/fall/ZULU/202/71955`

**Please note that the endpoint is the section's CRN.** This request returns a section's enrollment information, 
start/end times, location, etc.

```json
{"section": {
    "courseTitle": "Elementary Zulu II",
    "sectionStatusCode": "A",
    "endDate": "2020-12-09Z",
    "enrollmentStatus": "Open",
    "sectionNumber": "A",
    "course": "ZULU",
    "partOfTerm": 1,
    "department": "Zulu",
    "meeting": {
        "start": "ARRANGED",
        "type": {
            "code": "LCD",
            "name": "Lecture-Discussion"
        },
        "instructors": [
            "Gathogo, M",
            "Saadah, E"
        ]
    },
    "startDate": "2020-08-24Z",
    "crn": 71955
}}
```
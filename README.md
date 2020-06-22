# UIUC CIS API Wrapper
Databinds XML responses by [UIUC's Course Explorer API](https://courses.illinois.edu/cisdocs/) into usable `JSON` bodies.

## API Documentation

The API wrapper currently provides a `JSON` response to `GET` requests for a given year, term (summer, fall, winter, spring), subject, course, and section.

### By Year
This request returns a response of the given terms (semesters) in a year and original CIS API links for those terms
ex. endpoint: `/2020`

The `JSON` body has the following hierarchy:

```json
{
  "label" : "2020",
  "terms" : [ {
    "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020/winter.xml",
    "semester" : "Winter 2020"
  }, {
    "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020/spring.xml",
    "semester" : "Spring 2020"
  }, {
    "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020/summer.xml",
    "semester" : "Summer 2020"
  }, {
    "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall.xml",
    "semester" : "Fall 2020"
  } ]
}
```

### By Term

ex. `/2020/fall`

This request returns a given response of the different subjects instructed in a specific term.

```json
{
  "parents" : {
    "calendarYear" : {
      "id" : 2020,
      "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020.xml",
      "year" : 2020
    }
  },
  "label" : "Fall 2020",
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
    ]
}
```

### By Subject

ex. `/2020/fall/ZULU`

This returns a list of all courses in the given subject code and some basic attributes of the subject/department.

```json
{
  "parents" : {
    "calendarYear" : {
      "id" : 2020,
      "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020.xml",
      "year" : 2020
    },
    "term" : {
      "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall.xml",
      "semester" : "Fall 2020"
    }
  },
  "label" : "Zulu",
  "collegeCode" : "KV",
  "departmentCode" : 1864,
  "unitName" : "Linguistics",
  "contactName" : "James Yoon",
  "contactTitle" : "Department Head",
  "addressLine1" : "Department Office",
  "addressLine2" : "4080 Foreign Languages Building, 707 South Mathews, Urbana",
  "phoneNumber" : "217-333-3563",
  "webSiteURL" : "www.linguistics.uiuc.edu",
  "collegeDepartmentDescription" : "Department Head: James Yoon\nDepartment Office, 4080 Foreign Languages Building, 707 South Mathews, Urbana, 217-333-3563\nSubjects associated with this department include: Arabic (ARAB), Bamana (BMNA), English as an International Language (EIL), English as a Second Language (ESL), Modern Greek (GRKM), Hindi (HNDI), Lingala (LGLA), Linguistics (LING), Persian (PERS), Sanskrit (SNSK), Swahili (SWAH), Turkish (TURK), Wolof (WLOF), and Zulu (ZULU).\nwww.linguistics.uiuc.edu",
  "courses" : [ {
    "id" : "202",
    "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/ZULU/202.xml",
    "name" : "Elementary Zulu II"
  } ]
}
```

### By Course

ex. `2020/fall/ZULU/202`

This request returns a description of the course, as well as all of its sections.

```json
{
  "parents" : {
    "calendarYear" : {
      "id" : 2020,
      "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020.xml",
      "year" : 2020
    },
    "term" : {
      "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall.xml",
      "semester" : "Fall 2020"
    },
    "subject" : {
      "id" : "ZULU",
      "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/ZULU.xml",
      "subject" : "Zulu"
    }
  },
  "label" : "Elementary Zulu II",
  "description" : "Continuation of ZULU 201 with introduction of more advanced grammar; emphasis on more fluency in speaking, reading, and writing simple sentences in standard Zulu. Same as AFST 252. Participation in the language laboratory is required. Prerequisite: ZULU 201.",
  "creditHours" : "5 hours.",
  "sections" : [ {
    "id" : "71955",
    "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/ZULU/202/71955.xml",
    "name" : "A  "
  } ]
}
```

### By Section

ex. `2020/fall/ZULU/202/71955`

**Please note that the endpoint is the section's CRN.** This request returns a section's enrollment information, 
start/end times, location, etc.

```json
{
  "parents" : {
    "calendarYear" : {
      "id" : 2020,
      "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020.xml",
      "year" : 2020
    },
    "term" : {
      "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall.xml",
      "semester" : "Fall 2020"
    },
    "subject" : {
      "id" : "ZULU",
      "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/ZULU.xml",
      "subject" : "Zulu"
    },
    "course" : {
      "id" : "202",
      "href" : "https://courses.illinois.edu/cisapp/explorer/schedule/2020/fall/ZULU/202.xml",
      "course" : "Elementary Zulu II"
    }
  },
  "sectionNumber" : "A  ",
  "partOfTerm" : "1",
  "enrollmentStatus" : "Open",
  "startDate" : "2020-08-24Z",
  "endDate" : "2020-12-09Z",
  "meetings" : [ {
    "type" : "Lecture-Discussion",
    "start" : "ARRANGED",
    "end" : null,
    "daysOfTheWeek" : null,
    "roomNumber" : null,
    "buildingName" : null,
    "instructors" : [ {
      "name" : "Gathogo, M"
    }, {
      "name" : "Saadah, E"
    } ]
  } ]
}
```
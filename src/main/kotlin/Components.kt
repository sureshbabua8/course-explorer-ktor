//package hello
//
//import com.fasterxml.jackson.annotation.JsonIgnore
//import com.fasterxml.jackson.annotation.JsonValue
//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
//import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText
//
//@JacksonXmlRootElement(namespace = "ns2", localName = "calendarYear")
//data class Year(@JacksonXmlProperty(localName = "label") val year: String,
//                @JsonIgnore @JacksonXmlProperty(localName = "id") val id: String, val terms: List<Term>)
//
//@JacksonXmlRootElement(localName = "term")
//data class Semester(@JsonIgnore @JacksonXmlProperty(localName = "id", isAttribute = true) val id: String,
//                    @JacksonXmlProperty(localName = "href", isAttribute = true) val href: String,
//                    @JsonValue @JacksonXmlText val label: String)
//
//@JacksonXmlRootElement(namespace = "ns2", localName = "term")
//data class Term(@JacksonXmlProperty(localName = "href", isAttribute = true) val href: String, /*val subjects: List<SubjectInfo>?,*/
//                @JacksonXmlProperty(localName = "id", isAttribute = true) val id: String, @JacksonXmlText val termName: String
//    /*@JsonIgnore @JacksonXmlProperty(localName = "parents") val parents: ParentInfo*/)
//
////@JacksonXmlRootElement(namespace = "ns2", localName = "term")
////data class Term(@JacksonXmlProperty(localName = "label") val termName: String, val subjects: List<SubjectInfo>,
////                val id: String, @JsonIgnore @JacksonXmlProperty(localName = "parents") val parents: ParentInfo)
//
//@JacksonXmlRootElement(localName = "subject")
//data class SubjectInfo(val id: String, val href: String, @JacksonXmlProperty(localName = "label") val subjectName: String)
//
//@JacksonXmlRootElement(localName = "parents")
//data class ParentInfo(val calendarYear: YearInfo, val term: Semester?, val subject: SubjectInfo?)
//
//@JacksonXmlRootElement(localName = "calendarYear")
//data class YearInfo(val id: String, val href: String, val label: String)
//
//@JacksonXmlRootElement(namespace = "ns2", localName = "subject")
//data class Subject(val id: String, @JsonIgnore @JacksonXmlProperty(localName = "parents") val parents: ParentInfo,
//                   @JacksonXmlProperty(localName = "label") val title: String,
//                   @JsonIgnore @JacksonXmlProperty(localName = "collegeCode") val collegeCode: String,
//                   @JsonIgnore @JacksonXmlProperty(localName = "departmentCode") val departmentCode: String,
//                   @JsonIgnore @JacksonXmlProperty(localName = "unitName") val unitName: String,
//                   @JsonIgnore @JacksonXmlProperty(localName = "contactName") val contactName: String,
//                   @JsonIgnore @JacksonXmlProperty(localName = "contactTitle") val contactTitle: String,
//                   @JsonIgnore @JacksonXmlProperty(localName = "addressLine1") val addressLine1: String,
//                   @JsonIgnore @JacksonXmlProperty(localName = "addressLine2") val addressLine2: String,
//                   @JsonIgnore @JacksonXmlProperty(localName = "phoneNumber") val phoneNumber: String, val webSiteURL: String,
//                   @JsonIgnore @JacksonXmlProperty(localName = "collegeDepartmentDescription") val collegeDepartmentDescription: String,
//                   val courses: List<Course>)
//
//@JacksonXmlRootElement(localName = "course")
//data class Course(val id: String, val href: String, @JacksonXmlProperty(localName = "label") val name: String)
//
//@JacksonXmlRootElement(namespace = "ns2", localName = "subject")
//data class Class(val id: String, @JsonIgnore @JacksonXmlProperty(localName = "parents") val parents: ParentInfo,
//                 @JacksonXmlProperty(localName = "label") val title: String, val description: String,
//                 val creditHours: String, val courseSectionInformation: String, val sectionDegreeAttributes: String,
//                 val classScheduleInformation: String, val genEdCategories: List<Category>?, val sections: List<Section>)
//
//@JacksonXmlRootElement(localName = "category")
//data class Category(val id: String, val description: String, val genEdAttribute: GenEdAttribute)
//
//@JacksonXmlRootElement(namespace = "ns2", localName = "genEdAttributes")
//data class GenEdAttribute(val code: String, @JacksonXmlProperty(localName = "label") val type: String)
//
//data class Section(val id: String, val href: String, @JacksonXmlProperty(localName = "label") val sectionName: String)
//

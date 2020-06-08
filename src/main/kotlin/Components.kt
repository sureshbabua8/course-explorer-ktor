package hello

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(namespace = "ns2", localName = "calendarYear")
data class Year(@JacksonXmlProperty(localName = "label") val year: String,
                @JsonIgnore @JacksonXmlProperty(localName = "id") val id: String, val terms: List<Semester>)

@JacksonXmlRootElement(localName = "term")
data class Semester(@JsonIgnore @JacksonXmlProperty(localName = "id") val id: String, val href: String, val label: String)

@JacksonXmlRootElement(namespace = "ns2", localName = "term")
data class Term(@JacksonXmlProperty(localName = "label") val termName: String, val subjects: List<Subject>,
                val id: String, @JsonIgnore @JacksonXmlProperty(localName = "parents") val parents: ParentInfo)

@JacksonXmlRootElement(localName = "subject")
data class Subject(val id: String, val href: String, @JacksonXmlProperty(localName = "label") val subjectName: String)

@JacksonXmlRootElement(localName = "parents")
data class ParentInfo(val calendarYear: YearInfo)

@JacksonXmlRootElement(localName = "calendarYear")
data class YearInfo(val id: String, val href: String, val label: String)
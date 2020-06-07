package hello

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(namespace = "ns2", localName = "calendarYear")
data class calendarYear(@JacksonXmlProperty(localName = "label") val year: String, @JsonIgnore @JacksonXmlProperty(localName = "id") val id: String, val terms: List<term>)
data class term(@JsonIgnore @JacksonXmlProperty(localName = "id") val id: String, val href: String, val label: String)
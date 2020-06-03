import hello.Student
import org.junit.Before

class StudentTest {

    private var studentTest: Student = Student("as43")

    @org.junit.jupiter.api.Test
    @Before
    fun init() {
        studentTest = Student("as43")
    }

    @org.junit.jupiter.api.Test
    fun addGrade() {
        // test modifying lab grade
        studentTest.addGrade(100.0, "Lab")
        assert(studentTest.getLabs().size > 0)

        // test final project grade
        studentTest.addGrade(90.0, "Final Project")
        assert(studentTest.getFinalProject()[0] == 90.0)
    }

    @org.junit.jupiter.api.Test
    fun calculateGradeDrops() {
        // test basic MP drops
        var mpList = mutableListOf<Double>(100.0, 100.0, 100.0, 100.0, 50.0, 100.0)
        studentTest.setMps(mpList)

        assert(studentTest.calculateGradeDrops("MP") == 100.0)
    }

    @org.junit.jupiter.api.Test
    fun calculateGradeNoDrops() {
        // test basic MP w/out drops
        var mpList = mutableListOf<Double>(50.0, 100.0)
        studentTest.setMps(mpList)

        assert(studentTest.calculateGradeNoDrops("MP") == 75.0)

        // test empty lab grade
        assert(studentTest.calculateGradeNoDrops("Lab") == 100.0)
    }

    @org.junit.jupiter.api.Test
    fun getOverallGradeDrops() {
        // test empty grades
        assert(studentTest.getOverallGradeDrops() == 100.0)

        studentTest.setExams(mutableListOf(0.0, 0.0, 0.0))
        assert(studentTest.getOverallGradeDrops() == 94.0)
    }

    @org.junit.jupiter.api.Test
    fun getOverallGradeNoDrops() {
        studentTest.setMps(mutableListOf(0.0, 100.0))

        assert(studentTest.getOverallGradeNoDrops() == 85.0)
    }
}
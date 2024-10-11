import java.util.List;

public interface CourseDAO {
    void addCourse(Course course) throws SQLException;
    Course getCourseById(String courseId) throws SQLException;
    List<Course> getAllCourses() throws SQLException;
    void updateCourse(Course course) throws SQLException;
    void deleteCourse(String courseId) throws SQLException;
}
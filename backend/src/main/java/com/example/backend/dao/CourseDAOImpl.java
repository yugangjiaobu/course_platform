import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements CourseDAO {
    private Connection connection;

    public CourseDAO(Connection connection) {
        this.connection = connection;
    }

    public void addCourse(Course course) throws SQLException {
        String query = "INSERT INTO courses (course_id, course_name, course_description, teacher_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, course.getCourseId());
            stmt.setString(2, course.getCourseName());
            stmt.setString(3, course.getCourseDescription());
            stmt.setString(4, course.getTeacherId());
            stmt.executeUpdate();
        }
    }

    public Course getCourseById(String courseId) throws SQLException {
        String query = "SELECT * FROM courses WHERE course_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Course(
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getString("course_description"),
                        rs.getString("teacher_id")
                );
            }
        }
        return null;
    }

    public List<Course> getAllCourses() throws SQLException {
        String query = "SELECT * FROM courses";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                courses.add(new Course(
                        rs.getString("course_id"),
                        rs.getString("course_name"),
                        rs.getString("course_description"),
                        rs.getString("teacher_id")
                ));
            }
            return courses;
        }
    }

    public void updateCourse(Course course) throws SQLException {
        String query = "UPDATE courses SET course_name = ?, course_description = ?, teacher_id = ? WHERE course_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, course.getCourseName());
            stmt.setString(2, course.getCourseDescription());
            stmt.setString(3, course.getTeacherId());
            stmt.setString(4, course.getCourseId());
            stmt.executeUpdate();
        }
    }

    public void deleteCourse(String courseId) throws SQLException {
        String query = "DELETE FROM courses WHERE course_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, courseId);
            stmt.executeUpdate();
        }
    }
}
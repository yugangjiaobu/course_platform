import java.util.List;

public interface UserCourseDAO {
    void enrollUserInCourse(UserCourse userCourse) throws SQLException;
    UserCourse getUserCourse(String userId, String courseId) throws SQLException;
    List<UserCourse> getAllUserCourses(String userId) throws SQLException;
    void deleteUserCourse(String userId, String courseId) throws SQLException;
}
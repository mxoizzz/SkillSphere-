package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.User;
import utils.DBConnection;

public class UserDAO {
    public boolean registerUser(User user) {
        String query = "INSERT INTO users (name, email, password, role_id, profile_info, resume_path, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword());   
                ps.setInt(4, user.getRoleId());
                ps.setString(5, user.getProfileInfo());
                ps.setString(6, user.getResumePath());
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            finally {
                System.out.println("User registered successfully.");
            }
      
    }

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM USERS WHERE email = ?";
        User user = null;
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    user = extractUserFromResultSet(rs);
                }
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DBConnection.closeConnection();
        }
        return user;
    }

    public List<User> getAllUsers() {
        String query = "SELECT * FROM USERS";
        List<User> users = new ArrayList<>();
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    User user = extractUserFromResultSet(rs);
                    users.add(user);
                }
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            DBConnection.closeConnection();
        }
        return users;
    }

    public boolean updateUserProfile(User user) {
        String query = "UPDATE users SET name = ?, profile_info = ?, resume_path = ?, updated_at = NOW() WHERE user_id = ?";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, user.getName());
                ps.setString(2, user.getProfileInfo());
                ps.setString(3, user.getResumePath());
                ps.setInt(4, user.getUserId());
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            DBConnection.closeConnection();
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRoleId(rs.getInt("role_id"));
        user.setProfileInfo(rs.getString("profile_info"));
        user.setResumePath(rs.getString("resume_path"));
        user.setCreatedAt(rs.getString("created_at"));
        user.setUpdatedAt(rs.getString("updated_at"));
        return user;
    }
}
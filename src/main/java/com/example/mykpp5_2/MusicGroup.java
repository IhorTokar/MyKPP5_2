package com.example.mykpp5_2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicGroup {
    private static final List<MusicGroup> musicGroupsData = new ArrayList<>();
    private int musicGroupID;
    private String groupName;
    private String OwnerSurname;

    MusicGroup() {}

    MusicGroup(int musicGroupID, String groupName, String ownerSurname) {
        this.musicGroupID = musicGroupID;
        this.groupName = groupName;
        this.OwnerSurname = ownerSurname;
    }

    public void addMusicGroup(String groupName, String leaderSurname) {
        String sql = "INSERT INTO Band (band_name, leader_surname) VALUES (?, ?)";
        try (Connection connection = DBConnectionLab5_2.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, groupName);
            preparedStatement.setString(2, leaderSurname);

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    musicGroupsData.add(new MusicGroup(generatedId, groupName, leaderSurname));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<MusicGroup> getAllMusicGroups() {
        musicGroupsData.clear();
        String sql = "SELECT id, band_name, leader_surname FROM Band";
        try (Connection connection = DBConnectionLab5_2.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("band_name");
                String ownerName = resultSet.getString("leader_surname");

                musicGroupsData.add(new MusicGroup(id, name, ownerName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(musicGroupsData);
    }

    public static void deleteMusicGroupById(int groupId) {
        String sql = "DELETE FROM Band WHERE id = ?";
        try (Connection connection = DBConnectionLab5_2.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, groupId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                musicGroupsData.removeIf(group -> group.musicGroupID == groupId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMusicGroup(int musicGroupID, String groupName, String leaderSurname) {
        String sql = "UPDATE Band SET band_name = ?, leader_surname = ? WHERE id = ?";
        try (Connection connection = DBConnectionLab5_2.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, groupName);
            preparedStatement.setString(2, leaderSurname);
            preparedStatement.setInt(3, musicGroupID);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters and Setters
    public boolean exists(int groupId) {
        String query = "SELECT COUNT(*) FROM Band WHERE id = ?";
        try (Connection connection = DBConnectionLab5_2.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public int getMusicGroupID() {
        return musicGroupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getOwnerSurname() {
        return OwnerSurname;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setOwnerSurname(String ownerSurname) {
        this.OwnerSurname = ownerSurname;
    }
}

package com.example.mykpp5_2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Gastrole_tour extends MusicGroup {
    private static final List<Gastrole_tour> gastroleToursData = new ArrayList<>();
    private int tourID;
    private String city;
    private int year;
    private int concertsCount;
    public Gastrole_tour(){};
    public Gastrole_tour(String groupName, String leaderSurname, int musicGroupID, int tourID, String city, int year, int concertsCount) {
        super(musicGroupID, groupName, leaderSurname);
        this.tourID = tourID;
        this.city = city;
        this.year = year;
        this.concertsCount = concertsCount;
    }
    public void addGastroleTour(int musicGroupID, String city, int year, int concertsCount) {
        String sql = "INSERT INTO Tour (band_id, city, year, concert_count) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnectionLab5_2.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, musicGroupID);
            preparedStatement.setString(2, city);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, concertsCount);

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedTourID = generatedKeys.getInt(1);
                    gastroleToursData.add(new Gastrole_tour(getGroupName(), getOwnerSurname(), musicGroupID, generatedTourID, city, year, concertsCount));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Gastrole_tour> getAllGastroleTours() {
        gastroleToursData.clear();
        String sql = "SELECT t.id AS tourID, t.city, t.year, t.concert_count, b.id AS bandID, b.band_name, b.leader_surname " +
                "FROM Tour t JOIN Band b ON t.band_id = b.id";
        try (Connection connection = DBConnectionLab5_2.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int tourID = resultSet.getInt("tourID");
                int musicGroupID = resultSet.getInt("bandID");
                String city = resultSet.getString("city");
                int year = resultSet.getInt("year");
                int concertsCount = resultSet.getInt("concert_count");
                String groupName = resultSet.getString("band_name");
                String leaderSurname = resultSet.getString("leader_surname");

                gastroleToursData.add(new Gastrole_tour(groupName, leaderSurname, musicGroupID, tourID, city, year, concertsCount));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(gastroleToursData);
    }

    public static void deleteGastroleTourById(int tourID) {
        String sql = "DELETE FROM Tour WHERE id = ?";
        try (Connection connection = DBConnectionLab5_2.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, tourID);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                gastroleToursData.removeIf(tour -> tour.tourID == tourID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGastroleTour(int tourID, int musicGroupID, String city, int year, int concertsCount) {
        String sql = "UPDATE Tour SET band_id = ?, city = ?, year = ?, concert_count = ? WHERE id = ?";
        try (Connection connection = DBConnectionLab5_2.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, musicGroupID);
            preparedStatement.setString(2, city);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, concertsCount);
            preparedStatement.setInt(5, tourID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                for (Gastrole_tour tour : gastroleToursData) {
                    if (tour.tourID == tourID) {
                        tour.city = city;
                        tour.year = year;
                        tour.concertsCount = concertsCount;
                        break;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(int tourId) {
        // Example logic to check if the tour exists in the database
        String query = "SELECT COUNT(*) FROM Tour WHERE id = ?";
        try (Connection connection = DBConnectionLab5_2.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, tourId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Getters and Setters
    public int getTourID() {
        return tourID;
    }

    public String getCity() {
        return city;
    }

    public int getYear() {
        return year;
    }

    public int getConcertsCount() {
        return concertsCount;
    }
    public int getMusicGroupID() {
        return super.getMusicGroupID();
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setConcertsCount(int concertsCount) {
        this.concertsCount = concertsCount;
    }
}

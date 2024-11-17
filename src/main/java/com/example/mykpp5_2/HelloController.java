package com.example.mykpp5_2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class HelloController {
    @FXML
    private Button ShowUpperLetter;
    @FXML
    private Label last_letter_ofSurName;
    @FXML
    private AnchorPane main_page;
    @FXML
    private Button musicGroup_AddBtn;
    @FXML
    private Button musicGroup_DeleteBtn;
    @FXML
    private TextField musicGroup_ID_TextField;
    @FXML
    private TextField musicGroup_Owner_surName_TextField;
    @FXML
    private TableView<MusicGroup> musicGroup_TableView;
    @FXML
    private Button musicGroup_UpdateBtn;
    @FXML
    private TableColumn<MusicGroup, String> musicGroup_groupName_column;
    @FXML
    private TextField musicGroup_name_TextField;
    @FXML
    private TableColumn<MusicGroup, String> musicGroup_name_column;
    @FXML
    private TableColumn<MusicGroup, String> musicGroup_owner_surname_column;
    @FXML
    private Tab musicGroup_tab;
    @FXML
    private Button tour_AddBtn;
    @FXML
    private ChoiceBox<String> tour_ChoiseBox_musicGroup;
    @FXML
    private Button tour_DeleteBtn;
    @FXML
    private TableColumn<Gastrole_tour, Integer> tour_ID_Column;
    @FXML
    private TextField tour_ID_TextField;
    @FXML
    private Button tour_ShowConcertsInTown;
    @FXML
    private Button tour_ShowFullList;
    @FXML
    private Button tour_ShowMaxConcerTourBtn;
    @FXML
    private TableView<Gastrole_tour> tour_TableView;
    @FXML
    private Button tour_UpdateBtn;
    @FXML
    private TableColumn<Gastrole_tour, Integer> tour_concert_count_Column;
    @FXML
    private TextField tour_concert_count_TextField;
    @FXML
    private TableColumn<Gastrole_tour, String> tour_musicGroup_column;
    @FXML
    private Tab tour_tab;
    @FXML
    private ChoiceBox<String> tour_townForExample_ChoiseBox;
    @FXML
    private TextField tour_town_TextField;
    @FXML
    private TableColumn<Gastrole_tour, String> tour_town_column;
    @FXML
    private TextField tour_year_TextField;
    @FXML
    private TableColumn<Gastrole_tour, Integer> tour_year_column;
    //////////////////////////////////////////////////
    private ObservableList<MusicGroup> musicGroupList;
    private ObservableList<Gastrole_tour> tourList;

    public void initializeMusicGroupData() {
        musicGroupList = FXCollections.observableArrayList(MusicGroup.getAllMusicGroups());

        // Set the columns for the TableView
        musicGroup_groupName_column.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        musicGroup_owner_surname_column.setCellValueFactory(new PropertyValueFactory<>("ownerSurname"));
        musicGroup_name_column.setCellValueFactory(new PropertyValueFactory<>("musicGroupID")); // You may want to display the group name

        musicGroup_TableView.setItems(musicGroupList);
    }
    public void initializeTourData() {
        // Fetch data and populate the TableView
        tourList = FXCollections.observableArrayList(Gastrole_tour.getAllGastroleTours());

        // Set the columns for the TableView
        tour_ID_Column.setCellValueFactory(new PropertyValueFactory<>("tourID"));
        tour_musicGroup_column.setCellValueFactory(new PropertyValueFactory<>("groupName"));  // Use getGroupName() for musicGroup
        tour_town_column.setCellValueFactory(new PropertyValueFactory<>("city"));            // Use getCity() for town
        tour_year_column.setCellValueFactory(new PropertyValueFactory<>("year"));
        tour_concert_count_Column.setCellValueFactory(new PropertyValueFactory<>("concertsCount")); // Use getConcertsCount()

        tour_TableView.setItems(tourList);
    }
    public void musicGroupChoice() {
        MusicGroup selectedMusicGroup = musicGroup_TableView.getSelectionModel().getSelectedItem();
        Integer groupIndex = musicGroup_TableView.getSelectionModel().getSelectedIndex();

        musicGroup_ID_TextField.setText(String.valueOf(selectedMusicGroup.getMusicGroupID()));
        musicGroup_name_TextField.setText(selectedMusicGroup.getGroupName());
        musicGroup_Owner_surName_TextField.setText(selectedMusicGroup.getOwnerSurname());
    }
    public void tourChoice() {
        Gastrole_tour selectedTour = tour_TableView.getSelectionModel().getSelectedItem();
        Integer tourIndex = tour_TableView.getSelectionModel().getSelectedIndex();

        tour_ID_TextField.setText(String.valueOf(selectedTour.getTourID()));
        tour_ChoiseBox_musicGroup.setValue(selectedTour.getGroupName()); // Assuming the MusicGroup name is saved
        tour_town_TextField.setText(selectedTour.getCity());
        tour_year_TextField.setText(String.valueOf(selectedTour.getYear()));
        tour_concert_count_TextField.setText(String.valueOf(selectedTour.getConcertsCount()));
    }
    public void onAddMusicGroup() {
        String groupName = musicGroup_name_TextField.getText();
        String leaderSurname = musicGroup_Owner_surName_TextField.getText();

        if (groupName.isEmpty() || leaderSurname.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Усі поля мають бути заповнені!");
            return;
        }

        MusicGroup musicGroup = new MusicGroup();
        musicGroup.addMusicGroup(groupName, leaderSurname);

        initializeMusicGroupData();
        updateMusicGroupChoiceBox();

        showAlert(Alert.AlertType.INFORMATION, "Успіх", "Музичну групу додано!");
    }
    public void onAddTour() {
        String groupName = tour_ChoiseBox_musicGroup.getValue();
        String city = tour_town_TextField.getText();
        String yearText = tour_year_TextField.getText();
        String concertCountText = tour_concert_count_TextField.getText();

        if (groupName == null || city.isEmpty() || yearText.isEmpty() || concertCountText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Усі поля мають бути заповнені!");
            return;
        }

        try {
            int year = Integer.parseInt(yearText);
            int concertsCount = Integer.parseInt(concertCountText);

            MusicGroup musicGroup = new MusicGroup();
            List<MusicGroup> groups = musicGroup.getAllMusicGroups();
            MusicGroup selectedGroup = null;

            for (MusicGroup group : groups) {
                if (group.getGroupName().equals(groupName)) {
                    selectedGroup = group;
                    break;
                }
            }
            if (selectedGroup == null) {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Групу не знайдено!");
                return;
            }
            Gastrole_tour tour = new Gastrole_tour(selectedGroup.getGroupName(), "", selectedGroup.getMusicGroupID(), 0, city, year, concertsCount);
            tour.addGastroleTour(selectedGroup.getMusicGroupID(), city, year, concertsCount);
            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Гастрольний тур додано!");
            initializeTourData();
            initializeTourTownChoiceBox();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Рік та кількість концертів мають бути числами!");
        }
    }
    public void onDeleteMusicGroup() {
        String groupIdText = musicGroup_ID_TextField.getText();

        if (groupIdText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Введіть ID групи!");
            return;
        }
        try {
            int groupId = Integer.parseInt(groupIdText);

            MusicGroup musicGroup = new MusicGroup();
            if (!musicGroup.exists(groupId)) {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Музичну групу з таким ID не знайдено!");
                return;
            }

            if (isMusicGroupInTour(groupId)) {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Неможливо видалити групу, оскільки вона є частиною туру!");
                return;
            }

            MusicGroup.deleteMusicGroupById(groupId);
            updateMusicGroupChoiceBox();
            initializeMusicGroupData();
            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Музичну групу видалено!");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "ID має бути числом!");
        }
    }
    public void onDeleteTour() {
        String tourIdText = tour_ID_TextField.getText();
        if (tourIdText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Введіть ID туру!");
            return;
        }
        try {
            int tourId = Integer.parseInt(tourIdText);

            Gastrole_tour tour = new Gastrole_tour();
            if (!tour.exists(tourId)) {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Гастрольний тур з таким ID не знайдено!");
                return;
            }

            Gastrole_tour.deleteGastroleTourById(tourId);
            initializeTourData();
            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Гастрольний тур видалено!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "ID має бути числом!");
        }
    }
    public void onUpdateTour() {
        String tourIdText = tour_ID_TextField.getText();
        String city = tour_town_TextField.getText();
        String yearText = tour_year_TextField.getText();
        String concertCountText = tour_concert_count_TextField.getText();
        String groupName = tour_ChoiseBox_musicGroup.getValue();

        if (tourIdText.isEmpty() || groupName == null || city.isEmpty() || yearText.isEmpty() || concertCountText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Усі поля мають бути заповнені!");
            return;
        }

        try {
            int tourId = Integer.parseInt(tourIdText);

            Gastrole_tour tour = new Gastrole_tour();
            if (!tour.exists(tourId)) {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Гастрольний тур з таким ID не знайдено!");
                return;
            }

            int year = Integer.parseInt(yearText);
            int concertsCount = Integer.parseInt(concertCountText);

            MusicGroup musicGroup = new MusicGroup();
            List<MusicGroup> groups = musicGroup.getAllMusicGroups();
            MusicGroup selectedGroup = groups.stream()
                    .filter(group -> group.getGroupName().equals(groupName))
                    .findFirst()
                    .orElse(null);

            if (selectedGroup == null) {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Групу не знайдено!");
                return;
            }

            Gastrole_tour tourToUpdate = new Gastrole_tour();
            tourToUpdate.updateGastroleTour(tourId, selectedGroup.getMusicGroupID(), city, year, concertsCount);

            initializeTourData();
            initializeTourTownChoiceBox();
            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Гастрольний тур оновлено!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "ID, рік та кількість концертів мають бути числами!");
        }
    }
    public void onUpdateMusicGroup() {
        String groupIdText = musicGroup_ID_TextField.getText();
        String groupName = musicGroup_name_TextField.getText();
        String leaderSurname = musicGroup_Owner_surName_TextField.getText();

        if (groupIdText.isEmpty() || groupName.isEmpty() || leaderSurname.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Усі поля мають бути заповнені!");
            return;
        }

        try {
            int groupId = Integer.parseInt(groupIdText);
            MusicGroup musicGroup = new MusicGroup();
            if (!musicGroup.exists(groupId)) {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Музичну групу з таким ID не знайдено!");
                return;
            }

            musicGroup.updateMusicGroup(groupId, groupName, leaderSurname);
            initializeMusicGroupData();
            updateMusicGroupChoiceBox();
            showAlert(Alert.AlertType.INFORMATION, "Успіх", "Музичну групу оновлено!");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "ID має бути числом!");
        }
    }
    public void updateMusicGroupChoiceBox() {
        List<MusicGroup> groups = new MusicGroup().getAllMusicGroups();
        ObservableList<String> groupNames = FXCollections.observableArrayList();

        for (MusicGroup group : groups) {
            groupNames.add(group.getGroupName());
        }

        String selectedValue = tour_ChoiseBox_musicGroup.getValue();
        tour_ChoiseBox_musicGroup.setItems(groupNames);

        if (groupNames.contains(selectedValue)) {
            tour_ChoiseBox_musicGroup.setValue(selectedValue);
        } else {
            tour_ChoiseBox_musicGroup.setValue(null);
        }
    }
    public void initializeTourTownChoiceBox() {
        ObservableList<String> cities = FXCollections.observableArrayList();
        for (Gastrole_tour tour : Gastrole_tour.getAllGastroleTours()) {
            if (!cities.contains(tour.getCity())) {
                cities.add(tour.getCity());
            }
        }
        tour_townForExample_ChoiseBox.setItems(cities);
    }
    public void handleShowToursInTown() {
        String selectedCity = tour_townForExample_ChoiseBox.getValue();
        if (selectedCity == null || selectedCity.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Будь ласка, оберіть місто.");
            return;
        }

        ObservableList<Gastrole_tour> filteredTours = FXCollections.observableArrayList();
        for (Gastrole_tour tour : Gastrole_tour.getAllGastroleTours()) {
            if (selectedCity.equals(tour.getCity())) {
                filteredTours.add(tour);
            }
        }
        tour_TableView.setItems(filteredTours);
    }
    public void handleShowMaxConcertTour() {
        Gastrole_tour maxConcertTour = null;

        for (Gastrole_tour tour : Gastrole_tour.getAllGastroleTours()) {
            if (maxConcertTour == null || tour.getConcertsCount() > maxConcertTour.getConcertsCount()) {
                maxConcertTour = tour;
            }
        }

        if (maxConcertTour != null) {
            ObservableList<Gastrole_tour> maxTourList = FXCollections.observableArrayList(maxConcertTour);
            tour_TableView.setItems(maxTourList);
            showAlert(Alert.AlertType.INFORMATION, "Успіх",
                    "Гастрольний тур з найбільшою кількістю концертів відображено у таблиці!");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Інформація",
                    "Жодного гастрольного туру не знайдено.");
        }
    }
    public void handleShowLastLetterOfLeaderSurname() {
        String leaderSurname = musicGroup_Owner_surName_TextField.getText();
        if (leaderSurname == null || leaderSurname.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Помилка", "Будь ласка, введіть прізвище керівника!");
            return;
        }
        String lastLetter = leaderSurname.substring(leaderSurname.length() - 1);

        last_letter_ofSurName.setText(lastLetter);
    }
    public boolean isMusicGroupInTour(int groupId) {
        for (Gastrole_tour tour : tourList) {
            if (tour.getMusicGroupID() == groupId) {
                return true;
            }
        }
        return false;
    }
    public void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    public void initialize() {
        initializeMusicGroupData();
        initializeTourData();
        initializeTourTownChoiceBox();
        updateMusicGroupChoiceBox();
    }

}
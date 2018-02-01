package com.bonche;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.*;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

/** Created by Petar Bonchev ( 1607262 )**/

public class AddForm extends JFrame {

    public static final int MAXIMUM_NAME_CHARACTERS = 20;
    public static final int MINIMUM_NAME_LETTERS = 6;
    public static final int MAXIMUM_AGE_DIGITS = 3;
    public static final int ZERO_INDEX = 0;
    private final int MAXIMUM_DURATION_DIGITS = 6;
    private final int MAXIMUM_EPISODES_DIGITS = 4;
    private final int MUSIC_TYPE_INDEX = 1;
    private final int FILM_TYPE_INDEX = 2;
    private final int SERIES_TYPE_INDEX = 3;

    private JTextField titleTextField;
    private JTextField episodeTitleTextField;
    private JTextField episodeTextField;
    private JTextField durationTextField;
    private JTextField artistDirectorTextField;
    private JTextField ageTexField;

    private JButton addButton;
    private JButton resetButton;

    private JLabel ageLabel;
    private JLabel titleLabel;
    private JLabel artistDirectorLabel;
    private JLabel addFormMusicIconLabel;
    private JLabel durationLabel;
    private JLabel episodeLabel;
    private JLabel episodeTitleLabel;

    private JComboBox<String> typeOfFileComboBox;

    private MusicForm musicForm;

    private int typeNumber = 1;
    private final String MUSIC_ICON_DEST = "C:\\Users\\Bonchev\\IdeaProjects\\CourseworkY2S1\\src\\com\\bonche\\CourseWorkImages\\itunes.jpg";

    public AddForm(MusicForm musicForm) {
        this.musicForm = musicForm;
        setMainGUIStructure();
        setOtherComponents();
        this.setLocationRelativeTo(null);
    }

    private void setOtherComponents() {

        this.addButton.addActionListener(new AddButtonListener());
        this.resetButton.addActionListener(new ResetButtonListener());

        this.addFormMusicIconLabel.setIcon(
                this.musicForm.imageIconInitializer(
                        MUSIC_ICON_DEST, 50, 50));

        this.typeOfFileComboBox.setModel(
                new DefaultComboBoxModel<>(
                        new String[]{"Music", "Film", "Series"})
        );
        this.typeOfFileComboBox.addItemListener(new ComboBoxOptionSaver());
    }

    public String getSeriesInfo(PlayList playList, int index) {
        return (((Series) playList.getItems().get(index)).getEpisode()
                + " - " + ((Series) playList.getItems().get(index)).getEpisodeTitle()
                + " - " + playList.getItems().get(index).getDuration()
                + " - Age restriction: " + ((Series) playList.getItems().get(index)).getAgeRestriction());
    }

    public String getFilmInfo(PlayList playList, int index) {
        return playList.getItems().get(index).getDuration()
                + " - " + playList.getItems().get(index).getTitle()
                + " - Age restriction: " + ((Film) playList.getItems().get(index)).getAgeRestriction();
    }

    public String getMusicInfo(PlayList playList, int index) {
        return playList.getItems().get(index).getDuration()
                + " - " + playList.getItems().get(index).getTitle();
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean flag = false;
            if (!getTitleTextField().getText().trim().isEmpty()) {
                try {
                    String title = getTitleTextField().getText().trim();
                    String artistDirector = getArtistDirectorTextField().getText().trim();
                    String episodeTitle = getEpisodeTitleTextField().getText().trim();
                    if (title.length() <= MAXIMUM_NAME_CHARACTERS && artistDirector.length() <= MAXIMUM_NAME_CHARACTERS && episodeTitle.length() <= MAXIMUM_NAME_CHARACTERS) {
                        int duration = Integer.parseInt(getDurationTextField().getText().trim());
                        int age = (typeNumber == MUSIC_TYPE_INDEX) ? ZERO_INDEX : Integer.parseInt(getAgeTexField().getText().trim());
                        int episode = (typeNumber == MUSIC_TYPE_INDEX) ? ZERO_INDEX : Integer.parseInt(getEpisodeTextField().getText().trim());

                        if (typeNumber == MUSIC_TYPE_INDEX)
                            musicForm.getCurrentPlayList().addMedia(new Music(title, duration, artistDirector));
                        if (typeNumber == FILM_TYPE_INDEX)
                            musicForm.getCurrentPlayList().addMedia(new Film(title, duration, age, artistDirector));
                        if (typeNumber == SERIES_TYPE_INDEX)
                            musicForm.getCurrentPlayList().addMedia(new Series(title, duration, age, episode, episodeTitle));

                        flag = true;
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "The fields: Title, Artist/Director, and Episode Title " +
                                        "are supposed to be between " + MINIMUM_NAME_LETTERS + " and " + MAXIMUM_NAME_CHARACTERS,
                                "Alert", JOptionPane.WARNING_MESSAGE);

                    }

                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "The fields: Duration, Age, and Episode" +
                            " are supposed to be filled with numbers!", "Alert", JOptionPane.WARNING_MESSAGE);
                    setDurationTextField("");
                    setAgeTexField("");
                    setEpisodeTextField("");
                }
                musicForm.addToJList();
                if (flag) dispose();
            } else {
                JOptionPane.showMessageDialog(new Frame(), "Please, type a proper title name!"
                        , "Alert", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setTitleTextField("");
            setAgeTexField("");
            setArtistDirectorTextField("");
            setDurationTextField("");
            setEpisodeTextField("");
            setEpisodeTitleTextField("");
            typeNumber = MUSIC_TYPE_INDEX;
        }
    }

    private class ComboBoxOptionSaver implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getItem().equals("Music")) setTypeNumber(MUSIC_TYPE_INDEX);
            if (e.getItem().equals("Film")) setTypeNumber(FILM_TYPE_INDEX);
            if (e.getItem().equals("Series")) setTypeNumber(SERIES_TYPE_INDEX);
        }
    }

    public JTextField getAgeTexField() {
        return ageTexField;
    }

    public JTextField getArtistDirectorTextField() {
        return artistDirectorTextField;
    }

    public JTextField getDurationTextField() {
        return durationTextField;
    }

    public JTextField getEpisodeTextField() {
        return episodeTextField;
    }

    public JTextField getEpisodeTitleTextField() {
        return episodeTitleTextField;
    }

    public JTextField getTitleTextField() {
        return titleTextField;
    }

    public void setAgeTexField(String ageTexField) {
        this.ageTexField.setText(ageTexField);
    }

    public void setArtistDirectorTextField(String artistDirectorTextField) {
        this.artistDirectorTextField.setText(artistDirectorTextField);
    }

    public void setDurationTextField(String durationTextField) {
        this.durationTextField.setText(durationTextField);
    }

    public void setEpisodeTextField(String episodeTextField) {
        this.episodeTextField.setText(episodeTextField);
    }

    public void setEpisodeTitleTextField(String episodeTitleTextField) {
        this.episodeTitleTextField.setText(episodeTitleTextField);
    }

    public void setTitleTextField(String titleTextField) {
        this.titleTextField.setText(titleTextField);
    }

    public void setTypeNumber(int typeNumber) {
        this.typeNumber = typeNumber;
    }

    /**
     * GENERATED GUI -> initComponents()
     **/
    private void setMainGUIStructure() {

        this.artistDirectorTextField = new JTextField(this.MAXIMUM_NAME_CHARACTERS);
        this.durationTextField = new JTextField(this.MAXIMUM_DURATION_DIGITS);
        this.episodeTextField = new JTextField(this.MAXIMUM_EPISODES_DIGITS);
        this.episodeTitleTextField = new JTextField(this.MAXIMUM_NAME_CHARACTERS);
        this.ageTexField = new JTextField(this.MAXIMUM_AGE_DIGITS);
        this.titleTextField = new JTextField(this.MAXIMUM_NAME_CHARACTERS);

        this.addFormMusicIconLabel = new JLabel();
        this.titleLabel = new JLabel();
        this.artistDirectorLabel = new JLabel();
        this.durationLabel = new JLabel("Duration:");
        this.episodeLabel = new JLabel("Episode: ");
        this.episodeTitleLabel = new JLabel("Episode title:");
        this.ageLabel = new JLabel("Age:");

        this.addButton = new JButton("Add");
        this.resetButton = new JButton("Reset");

        this.typeOfFileComboBox = new JComboBox<>();

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(43, 43, 43)
                                                .addGroup(layout.createParallelGroup(LEADING)
                                                        .addComponent(artistDirectorTextField)
                                                        .addComponent(titleTextField)
                                                        .addComponent(addFormMusicIconLabel, TRAILING, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
                                                        .addComponent(artistDirectorLabel)
                                                        .addComponent(titleLabel, PREFERRED_SIZE, 35, PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(45, 45, 45)
                                                .addComponent(resetButton, PREFERRED_SIZE, 92, PREFERRED_SIZE)
                                                .addPreferredGap(RELATED)
                                                .addComponent(addButton, PREFERRED_SIZE, 145, PREFERRED_SIZE)
                                                .addGap(0, 0, MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(45, 45, 45)
                                                .addGroup(layout.createParallelGroup(LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(LEADING, false)
                                                                        .addComponent(episodeLabel, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
                                                                        .addComponent(durationLabel, DEFAULT_SIZE, 47, MAX_VALUE))
                                                                .addPreferredGap(RELATED)
                                                                .addGroup(layout.createParallelGroup(LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(episodeTextField, PREFERRED_SIZE, 45, PREFERRED_SIZE)
                                                                                .addGap(63, 63, 63))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(durationTextField, PREFERRED_SIZE, 45, PREFERRED_SIZE)
                                                                                .addPreferredGap(RELATED)))
                                                                .addGroup(layout.createParallelGroup(LEADING)
                                                                        .addComponent(typeOfFileComboBox, 0, DEFAULT_SIZE, MAX_VALUE)
                                                                        .addGroup(TRAILING, layout.createSequentialGroup()
                                                                                .addComponent(ageLabel, PREFERRED_SIZE, 27, PREFERRED_SIZE)
                                                                                .addPreferredGap(UNRELATED)
                                                                                .addComponent(ageTexField, PREFERRED_SIZE, 23, PREFERRED_SIZE)
                                                                                .addGap(32, 32, 32))))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(LEADING)
                                                                        .addComponent(episodeTitleTextField, PREFERRED_SIZE, 250, PREFERRED_SIZE)
                                                                        .addComponent(episodeTitleLabel))
                                                                .addGap(0, 1, MAX_VALUE)))))
                                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(addFormMusicIconLabel, PREFERRED_SIZE, 64, PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(titleLabel)
                                .addPreferredGap(RELATED)
                                .addComponent(titleTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addPreferredGap(RELATED, 15, MAX_VALUE)
                                .addComponent(artistDirectorLabel)
                                .addPreferredGap(RELATED)
                                .addComponent(artistDirectorTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(durationLabel)
                                        .addComponent(durationTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(ageTexField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(ageLabel, PREFERRED_SIZE, 19, PREFERRED_SIZE))
                                .addPreferredGap(UNRELATED)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(episodeTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(episodeLabel)
                                        .addComponent(typeOfFileComboBox, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addComponent(episodeTitleLabel)
                                .addPreferredGap(RELATED)
                                .addComponent(episodeTitleTextField, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(resetButton)
                                        .addComponent(addButton))
                                .addGap(24, 24, 24))
        );
        pack();
    }
}

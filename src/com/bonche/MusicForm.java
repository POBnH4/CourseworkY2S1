package com.bonche;

import com.sun.istack.internal.NotNull;

/** Created by Petar Bonchev ( 1607262 )**/

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.stream.IntStream;

import static com.bonche.AddForm.MAXIMUM_NAME_CHARACTERS;
import static com.bonche.AddForm.MINIMUM_NAME_LETTERS;
import static java.lang.Short.MAX_VALUE;
import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;
import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;
import static javax.swing.LayoutStyle.ComponentPlacement.RELATED;
import static javax.swing.LayoutStyle.ComponentPlacement.UNRELATED;

public class MusicForm extends JFrame {

    private final String DEFAULT_ACCOUNT_IMAGE_ICON = "C:\\Users\\Bonchev\\IdeaProjects\\CourseworkY2S1\\src\\com\\bonche\\CourseWorkImages\\user.png";
    private final String IMAGE_ICON_PLUS = "C:\\Users\\Bonchev\\IdeaProjects\\CourseworkY2S1\\src\\com\\bonche\\CourseWorkImages\\plus.jpg";
    private final String IMAGE_ICON_ITUNES = "C:\\Users\\Bonchev\\IdeaProjects\\CourseworkY2S1\\src\\com\\bonche\\CourseWorkImages\\itunes.jpg";
    private final String IMAGE_ICON_IMAGES = "C:\\Users\\Bonchev\\IdeaProjects\\CourseworkY2S1\\src\\com\\bonche\\CourseWorkImages\\images.jpg";
    private final String IMAGE_ICON_MUSIC_WALL_ONE = "C:\\Users\\Bonchev\\IdeaProjects\\CourseworkY2S1\\src\\com\\bonche\\CourseWorkImages\\music-wallpapers-2.jpg";
    private final String IMAGE_ICON_MUSIC_WALL_TWO = "C:\\Users\\Bonchev\\IdeaProjects\\CourseworkY2S1\\src\\com\\bonche\\CourseWorkImages\\1200X630bb.jpg";

    private final int SCENE_WINDOW_HEIGHT = 500;
    private final int SCENE_WINDOW_WIDTH = 700;
    private final int DELAY = 1000;
    private final int PAUSE = 1;
    private final int PLAY = 0;
    private final int EXIT_APPLICATION = 0;
    public static final int FIRST_INDEX = 0;

    public static final int BEGINNING_OF_PLAYLIST_INDEX = 0;
    public static final int LIST_EMPTY = 0;
    public static final int THROW_EXCEPTION_NUMBER = -1;
    private final int MAKE_INTO_PERCENTAGE = 100;
    public static final int CHILD_AGE_THRESHOLD = 12;
    public static final int DISCOUNT_PENCE_COST = 20;
    public static final int PENCE_COST = 50;
    public static final int DURATION_THRESHOLD = 20;
    public static final int DURATION_FEE = 20;
    public static final int DURATION_FEE_SET_FREE = 0;

    private final String[] MOST_POPULAR_VIDEO_FORMATS = new String[]{"MP4", "AVI", "WMV"};
    private final String[] MOST_POPULAR_AUDIO_FORMATS = new String[]{"MP3", "ACC", "WMA"};

    /**
     * JFXPanel (even if it is not used for anything) is required
     * since it initiates javaFxRuntime when the application gets started,
     * otherwise without it I will get: toolkit not initialized media player
     * EXCEPTION
     **/
    private final JFXPanel jfxPanel = new JFXPanel();

    private final int SONG_IMAGE_WIDTH = 200;
    private final int SONG_IMAGE_HEIGHT = 110;
    private final int ONE_MOUSE_CLICK = 1;
    private final int TWO_MOUSE_CLICKS = 2;
    public static final int SECONDS_IN_A_MINUTE = 60;
    private final int CENTS_IN_A_POUND = 100;
    private final int DIRECTORY_LEVEL = 1;
    private final int FILE_LEVEL = 2;

    public static final String POUND_SYMBOL = "\u00a3";
    private final String NO_PATH = "";

    private int totalPlayingTime, totalBalance;

    private JLabel balanceLabel, currentSongArtistName, currentSongName, songPhoto, totalPlayingTimeLabel;

    private JList<String> listWithSongsFilmsSeries;
    private DefaultListModel<String> elements = new DefaultListModel<>();

    private JMenuBar mainMenuBar;
    private JMenu file, management, sortMenu, menuHelp;
    private JMenuItem sortByTitle, sortByArtist, sortByDuration, sortByCost, shuffle;
    private JMenuItem logout, resetAccount, topUp, withdraw, exit, addItem, info;
    private JMenuItem loadMedia, saveMedia, loadMediaWithRealMusicAndVideo, saveMediaWithRealMusicAndVideo;

    private JPanel managementPanel, playListsPanel, elementsPanel;

    private JProgressBar progressBar;

    private JScrollPane jScrollPane2, jScrollPane3;

    private JButton nextSongButton, pauseSongButton, previousSongButton;

    private JTree playlistTree;
    private DefaultMutableTreeNode root;
    private DefaultMutableTreeNode defaultPlaylistNode;

    private ArrayList<PlayList> playLists;
    private PlayList currentPlayList;

    private MediaFile currentElement;
    private User currentUser;
    private int currentItemCounter = 0;
    private int pausePlayCounter = 0;
    private boolean pause = false;
    private Timer timer;
    private Object currentSelectedNode;
    private Database database;

    private TreePath store = null;

    private LoginForm loginForm;

    /**
     * To be done:
     * 1) Database synchronized and bugs free;
     * 2) JTree selections - bugs fixed;
     * 3) Progress bar - bugs fixed;
     * 4) The buttons have to work;
     * 5) Make GUI look nice;
     * 6) MAKE IT WORK WITH REAL MUSIC AND VIDEO;
     **/

    public MusicForm(@NotNull User user, @NotNull LoginForm loginForm) {
        this.loginForm = loginForm;
        this.currentUser = user;
        this.database = this.loginForm.getDatabase();
        this.playLists = user.getPlayLists();
        this.loginForm.getDatabase().setCurrentUser(this.currentUser);
        this.totalBalance = user.getWallet();
        this.currentPlayList = (playLists.size() > LIST_EMPTY) ? playLists.get(FIRST_INDEX) :
                new PlayList(this.currentUser, MusicForm.this, null);
        insertIntoDatabase(this.currentPlayList);
        setMainMusicFormGUIStructure();
        synchronizeData();
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowCloser());
    }

    public void insertIntoDatabase(PlayList playList) {
        database.execute(database.createPlaylist(playList));
        for (int i = 0; i < playList.getItems().size(); i++) {
            MediaFile data = playList.getItems().get(i);
            if (data instanceof Music) {
                database.execute(
                        database.insertInto(playList, new Music(data.getTitle(), data.getDuration(), data.getArtistOrDirector())));
            }
            if (data instanceof Film) {
                database.execute(
                        database.insertInto(playList, new Film(data.getTitle(), data.getDuration(), data.getAgeVerification(), data.getArtistOrDirector())));
            }
            if (data instanceof Series) {
                database.execute(
                        database.insertInto(playList, new Series(data.getTitle(), data.getDuration(), data.getAgeVerification(),
                                ((Series) data).getEpisode(), ((Series) data).getEpisodeTitle())));
            }
        }
    }

    /**
     * GENERATED GUI -> setMainMusicFormGUIStructure()
     **/
    private void setMainMusicFormGUIStructure() {

        this.managementPanel = new JPanel();
        this.elementsPanel = new JPanel();
        this.playListsPanel = new JPanel();

        this.balanceLabel = new JLabel(this.totalBalance + "$");
        this.totalPlayingTimeLabel = new JLabel(this.totalPlayingTime + "s");
        this.currentSongName = new JLabel("N/A");
        this.currentSongArtistName = new JLabel("N/A");
        this.songPhoto = new JLabel(imageIconInitializer(DEFAULT_ACCOUNT_IMAGE_ICON, SONG_IMAGE_WIDTH, SONG_IMAGE_HEIGHT));

        this.previousSongButton = new JButton("Prev");
        this.pauseSongButton = new JButton("Pause");
        this.nextSongButton = new JButton("Next");

        this.progressBar = new JProgressBar();
        this.progressBar.setStringPainted(true);
        this.progressBar.setString("Hello, " + currentUser.getUsername());

        this.jScrollPane2 = new JScrollPane();
        this.jScrollPane3 = new JScrollPane();

        this.listWithSongsFilmsSeries = new JList<>();
        this.listWithSongsFilmsSeries.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        this.mainMenuBar = new JMenuBar();

        this.file = new JMenu("File");
        this.management = new JMenu("Management");
        this.sortMenu = new JMenu("Sort");
        this.menuHelp = new JMenu("Help");

        this.addItem = new JMenuItem("Add");
        this.resetAccount = new JMenuItem("Reset");
        this.logout = new JMenuItem("Logout");
        this.exit = new JMenuItem("Exit");

        this.sortByTitle = new JMenuItem("By title");
        this.sortByArtist = new JMenuItem("By artist");
        this.sortByDuration = new JMenuItem("By duration");
        this.sortByCost = new JMenuItem("By cost");
        this.shuffle = new JMenuItem("Shuffle");

        this.topUp = new JMenuItem("Top-up");
        this.withdraw = new JMenuItem("Withdraw");

        /** Difference between load/save && load media/ save media
         * -> load media and save media will work with real music and video files**/

        this.loadMedia = new JMenuItem("Load");
        this.saveMedia = new JMenuItem("Save");
        this.loadMediaWithRealMusicAndVideo = new JMenuItem("Load media");
        this.saveMediaWithRealMusicAndVideo = new JMenuItem("Save media");

        this.info = new JMenuItem("Info");

        this.mainMenuBar.add(this.file);
        this.mainMenuBar.add(this.management);
        this.mainMenuBar.add(this.sortMenu);
        this.mainMenuBar.add(this.menuHelp);

        this.file.add(this.addItem);
        this.file.addSeparator();
        this.file.add(this.resetAccount);
        this.file.addSeparator();
        this.file.add(this.logout);
        this.file.addSeparator();
        this.file.add(this.exit);

        this.sortMenu.add(this.sortByCost);
        this.sortMenu.add(this.sortByDuration);
        this.sortMenu.add(this.sortByArtist);
        this.sortMenu.add(this.sortByTitle);
        this.sortMenu.addSeparator();
        this.sortMenu.add(this.shuffle);

        this.management.add(this.topUp);
        this.management.add(this.withdraw);
        this.management.addSeparator();
        this.management.add(this.loadMedia);
        this.management.add(this.saveMedia);
        this.management.addSeparator();
        this.management.add(this.loadMediaWithRealMusicAndVideo);
        this.management.add(this.saveMediaWithRealMusicAndVideo);

        this.menuHelp.add(this.info);

        setJMenuBar(this.mainMenuBar);
        this.root = new DefaultMutableTreeNode("Playlists");
        setOtherComponents();

        this.playlistTree = new JTree(this.root);
        this.playlistTree.addMouseListener(new TreeListListener());
        this.playlistTree.setCellRenderer(new TreeRender());
        this.playlistTree.addTreeSelectionListener(new MyTreeSelectionListener());
        add(new JScrollPane(this.playlistTree));

        this.managementPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        GroupLayout jPanel1Layout = new GroupLayout(managementPanel);
        managementPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(LEADING)
                        .addGroup(TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(songPhoto, PREFERRED_SIZE, 158, PREFERRED_SIZE)
                                .addPreferredGap(RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(currentSongName, PREFERRED_SIZE, 181, PREFERRED_SIZE)
                                                .addPreferredGap(RELATED, DEFAULT_SIZE, MAX_VALUE)
                                                .addComponent(balanceLabel, PREFERRED_SIZE, 85, PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(totalPlayingTimeLabel, PREFERRED_SIZE, 72, PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(LEADING)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(previousSongButton)
                                                                .addPreferredGap(UNRELATED)
                                                                .addComponent(pauseSongButton)
                                                                .addPreferredGap(RELATED)
                                                                .addComponent(nextSongButton))
                                                        .addComponent(currentSongArtistName, PREFERRED_SIZE, 196, PREFERRED_SIZE))
                                                .addGap(0, 0, MAX_VALUE))
                                        .addComponent(progressBar, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(LEADING)
                        .addComponent(songPhoto, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(LEADING)
                                        .addGroup(jPanel1Layout.createParallelGroup(TRAILING)
                                                .addComponent(totalPlayingTimeLabel)
                                                .addComponent(balanceLabel, PREFERRED_SIZE, 11, PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(0, 0, MAX_VALUE)
                                                .addComponent(currentSongName)
                                                .addPreferredGap(RELATED)
                                                .addComponent(currentSongArtistName)
                                                .addPreferredGap(RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(BASELINE)
                                                        .addComponent(previousSongButton)
                                                        .addComponent(pauseSongButton)
                                                        .addComponent(nextSongButton))))
                                .addPreferredGap(RELATED)
                                .addComponent(progressBar, PREFERRED_SIZE, 20, PREFERRED_SIZE)
                                .addGap(5, 5, 5))
        );
        this.playListsPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.jScrollPane2.setViewportView(this.playlistTree);

        GroupLayout jPanel2Layout = new GroupLayout(this.playListsPanel);
        this.playListsPanel.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(LEADING)
                        .addComponent(this.jScrollPane2, DEFAULT_SIZE, 158, MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(LEADING)
                        .addGroup(TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, MAX_VALUE)
                                .addComponent(this.jScrollPane2, PREFERRED_SIZE, 415, PREFERRED_SIZE))
        );

        this.elementsPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.jScrollPane3.setViewportView(this.listWithSongsFilmsSeries);

        GroupLayout jPanel4Layout = new GroupLayout(this.elementsPanel);
        this.elementsPanel.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(LEADING)
                        .addComponent(this.jScrollPane3, DEFAULT_SIZE, 452, MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(LEADING)
                        .addComponent(this.jScrollPane3, TRAILING)
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(this.playListsPanel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addPreferredGap(RELATED, DEFAULT_SIZE, MAX_VALUE)
                                .addComponent(this.elementsPanel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))
                        .addComponent(this.managementPanel, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(this.managementPanel, PREFERRED_SIZE, 100, PREFERRED_SIZE)
                                .addPreferredGap(UNRELATED)
                                .addGroup(layout.createParallelGroup(LEADING)
                                        .addComponent(this.elementsPanel, DEFAULT_SIZE, DEFAULT_SIZE, MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 0, MAX_VALUE)
                                                .addComponent(this.playListsPanel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE))))
        );
        pack();
    }

    private void setOtherComponents() {
        this.addItem.addActionListener(new AddElementsToList());
        this.listWithSongsFilmsSeries = new JList<>();
        this.listWithSongsFilmsSeries.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.elements = populateMediaList(this.currentPlayList);
        setBalanceAndTotalTimeLabels(currentPlayList);
        this.listWithSongsFilmsSeries.setModel(this.elements);

        this.resetAccount.addActionListener(new ResetAccountListener());
        this.logout.addActionListener(new LogoutListener());
        this.exit.addActionListener(new ExitListener());

        this.topUp.addActionListener(new TopUpListener());
        this.withdraw.addActionListener(new WithdrawListener());
        this.loadMedia.addActionListener(new LoadListener());
        this.saveMedia.addActionListener(new SaveListener());
        this.loadMediaWithRealMusicAndVideo.addActionListener(new LoadRealMediaListener());
        this.saveMediaWithRealMusicAndVideo.addActionListener(new SaveRealMediaListener());

        this.listWithSongsFilmsSeries.addMouseListener(new ChangeSongListener());
        this.listWithSongsFilmsSeries.addMouseListener(new ListRemoveListener());

        this.sortByArtist.addActionListener(new ByArtistSorter());
        this.sortByTitle.addActionListener(new ByTitleSorter());
        this.sortByDuration.addActionListener(new ByDurationSorter());
        this.sortByCost.addActionListener(new ByCostSorter());
        this.shuffle.addActionListener(new ShuffleItemsListener());

        this.previousSongButton.addActionListener(new PreviousElementButtonListener());
        this.pauseSongButton.addActionListener(new PauseElementButtonListener());
        this.nextSongButton.addActionListener(new NextElementButtonListener());

        DefaultMutableTreeNode initialDefaultList = new DefaultMutableTreeNode("Playlist 1");
        root.add(initialDefaultList);
        fillTreeWithData(currentPlayList, initialDefaultList);

    }

    public void setCurrentItem(int index) {
        if (currentPlayList.getItems().get(index) instanceof Music)
            currentElement = new Music(
                    currentPlayList.getItems().get(index).getTitle(),
                    currentPlayList.getItems().get(index).getDuration(),
                    currentPlayList.getItems().get(index).getArtistOrDirector());

        if (currentPlayList.getItems().get(index) instanceof Film)
            currentElement = new Film(
                    currentPlayList.getItems().get(index).getTitle(),
                    currentPlayList.getItems().get(index).getDuration(),
                    ((Film) currentPlayList.getItems().get(index)).getAgeRestriction(),
                    currentPlayList.getItems().get(index).getArtistOrDirector());

        if (currentPlayList.getItems().get(index) instanceof Series)
            currentElement = new Series(
                    currentPlayList.getItems().get(index).getTitle(),
                    currentPlayList.getItems().get(index).getDuration(),
                    ((Series) currentPlayList.getItems().get(index)).getAgeRestriction(),
                    ((Series) currentPlayList.getItems().get(index)).getEpisode(),
                    ((Series) currentPlayList.getItems().get(index)).getEpisodeTitle());
    }

    public void addToJList(MediaFile... newMedia) {
        if (newMedia.length == 0) {
            MediaFile newItem = this.currentPlayList.getItems().get(this.currentPlayList.getItems().size() - 1);

            if (this.totalBalance - newItem.costInPence() >= 0) refreshScreen(newItem);
            else JOptionPane.showMessageDialog(null, "You do not have enough money to purchase that item!");

        } else {
            int bound = newMedia.length;
            for (int i = 0; i < bound; i++) {
                if (this.totalBalance - newMedia[i].costInPence() >= 0) {
                    MediaFile mediaFile = newMedia[i];
                    refreshScreen(mediaFile);
                }
            }
        }
    }

    public void refreshScreen(MediaFile newMedia) {
        this.totalBalance -= newMedia.costInPence();
        //this.currentUser.purchase(newMedia);
        this.elements.addElement(setDuration(newMedia.getDuration()) + newMedia.getTitle());
        this.currentPlayList.addMedia(newMedia);
        this.root.insert(new DefaultMutableTreeNode(newMedia.getTitle()), this.root.getChildCount());
        this.database.createMedia(newMedia.getTitle(), this.currentPlayList);
        this.listWithSongsFilmsSeries.setModel(this.elements);
        ((DefaultTreeModel) this.playlistTree.getModel()).nodeStructureChanged(this.root);
        setNewTotalPlayingTime(newMedia.getDuration());
        setNewTotalBalance(newMedia.costInPence());
    }

    public void progressBarSetter(MediaFile currentItem) {
        ActionListener actionListener = (ActionEvent e) -> {
            currentItemCounter++;
            progressBar.setString(setDuration(currentItemCounter) + "/" + setDuration(currentItem.getDuration()));
            progressBar.setValue(getPercentage(currentItemCounter, currentItem.getDuration()));
            if (pause) {
                timer.stop();
            } else {
                timer.start();
                if (currentItemCounter == currentItem.getDuration()) {
                    int index = currentPlayList.getIndex(currentItem);
                    if (index > BEGINNING_OF_PLAYLIST_INDEX && !pause) {
                        currentSongName.setText(currentPlayList.getItems().get(++index).getTitle());
                        currentSongArtistName.setText(currentPlayList.getItems().get(++index).getArtistOrDirector());
                        setCurrentItem(++index);
                    }
                }
            }
        };

        timer = new Timer(DELAY, actionListener);
        timer.start();
    }

    public int getPercentage(int chunk, int totalTime) {
        return chunk / (totalTime / MAKE_INTO_PERCENTAGE);
    }

    public DefaultListModel<String> populateMediaList(PlayList aPlayList) {
        DefaultListModel<String> list = new DefaultListModel<>();
        aPlayList.getItems()
                .forEach(mediaElement -> {
                    if (mediaElement instanceof Music)
                        list.addElement(setMusicAndFilmText(mediaElement.getTitle(), mediaElement.getDuration()));
                    if (mediaElement instanceof Series)
                        list.addElement(setSeriesText(((Series) mediaElement).getEpisode(), ((Series) mediaElement).getEpisodeTitle(), mediaElement.getDuration()));
                    if (mediaElement instanceof Film)
                        list.addElement(setMusicAndFilmText(mediaElement.getTitle(), mediaElement.getDuration()));
                });
        return list;
    }

    public void fillTreeWithData(PlayList playList, DefaultMutableTreeNode treeNode) {
        playList.getItems()
                .stream()
                .map(media -> new DefaultMutableTreeNode(media.getTitle()))
                .forEach(treeNode::add);
    }

    public void nextItem(int index) {
        if (this.currentPlayList.getItems().size() - 1 > ++index) setCurrentItem(index);
        else setCurrentItem(BEGINNING_OF_PLAYLIST_INDEX);
        if (timer != null) {
            timer = null;
        }
        progressBarSetter(currentElement);
    }

    public void previousItem(int index) {
        // if the user asks for the previous item and he is
        // currently on the first one he will get the last
        // item in the list. Else return the item which is
        // before the current one;
        if (--index <= 0) setCurrentItem(this.currentPlayList.getItems().size() - 1);
        else setCurrentItem(index);
        if (timer != null) {
            timer = null;
        }
        progressBarSetter(currentElement);
    }

    public void setPauseBoolean(int counter) {
        if (counter == PAUSE) pause = true;
        else pause = false;
    }

    public void setBalanceAndTotalTimeLabels(PlayList playList) {
        IntStream.range(0, playList.getItems().size()).
                forEach(i -> {
                    setNewTotalBalance(playList.getItems().get(i).costInPence());
                    setNewTotalPlayingTime(playList.getItems().get(i).getDuration());
                });
    }

    public Image getResizedImage(Image src, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(src, 0, 0, width, height, null);
        graphics2D.dispose();

        return bufferedImage;
    }

    public ImageIcon imageIconInitializer(String source, int width, int height) {

        ImageIcon imageIcon = new ImageIcon();
        try {
            Image img = ImageIO.read(new File(source));
            imageIcon = new ImageIcon(getResizedImage(img, width, height));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ImageIcon ERROR!");
        }

        return imageIcon;
    }

    public void setNewTotalPlayingTime(int... duration) {
        if (duration.length > 0) {
            Arrays.stream(duration).
                    forEach(i -> this.totalPlayingTime += i);
        }
        int minutes = this.totalPlayingTime / this.SECONDS_IN_A_MINUTE;
        int seconds = this.totalPlayingTime % this.SECONDS_IN_A_MINUTE;
        this.totalPlayingTimeLabel.setText(minutes + ":" + seconds + "m");
    }

    public void setNewTotalBalance(int... amount) {
        if (amount.length > 0) {
            Arrays.stream(amount)
                    .forEach(i -> this.totalBalance += i);
        }
        int pounds = this.totalBalance / this.CENTS_IN_A_POUND;
        int cents = this.totalBalance % this.CENTS_IN_A_POUND;
        this.balanceLabel.setText(pounds + "." + cents + POUND_SYMBOL);
    }

    public String setDuration(int duration) {
        int minutes = duration / this.SECONDS_IN_A_MINUTE;
        int seconds = duration % this.SECONDS_IN_A_MINUTE;
        return minutes + ":" + seconds + " ";
    }

    public String setMusicAndFilmText(String title, int duration) {
        return setDuration(duration) + title;
    }

    public String setSeriesText(int episode, String episodeTitle, int duration) {
        return setDuration(duration) + "Episode: " + episode + ", " + episodeTitle;
    }

    private class AddElementsToList implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Add")) {
                AddForm addForm = new AddForm(MusicForm.this);
                EventQueue.invokeLater(() -> addForm.setVisible(true));
            }
        }
    }

    private class ResetAccountListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int answer = JOptionPane.showConfirmDialog(new Frame(), "Are you sure you want to reset all the information on this account?", "", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                playLists = new ArrayList<>();
                currentPlayList = new PlayList(currentUser, MusicForm.this, null);
                elements = populateMediaList(currentPlayList);
                setBalanceAndTotalTimeLabels(currentPlayList);
                listWithSongsFilmsSeries.setModel(elements);
                currentUser.setWallet(0);
            }
        }
    }

    private class LogoutListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int answer = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to logout?",
                    "WARNING", JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.YES_OPTION) {
                dispose();
                EventQueue.invokeLater(() -> new LoginForm(
                        loginForm.getDatabase()).setVisible(true));
            }
        }
    }

    private class ExitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int answer = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to exit?",
                    "WARNING", JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.YES_OPTION) {
                dispose();
            }
        }
    }

    private class ListRemoveListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem remove = new JMenuItem("Remove");
            JMenuItem moveUp = new JMenuItem("Move up");
            JMenuItem moveDown = new JMenuItem("Move down");
            JMenuItem addImage = new JMenuItem("Add image");
            remove.addActionListener(new PopupMenuRemoveItemListener());
            moveUp.addActionListener(new MoveUpListener());
            moveDown.addActionListener(new MoveDownListener());
            addImage.addActionListener(new AddImageListener());
            popupMenu.add(remove);
            popupMenu.add(moveUp);
            popupMenu.add(moveDown);
            popupMenu.add(addImage);
            if (SwingUtilities.isRightMouseButton(e)) {
                if (e.getClickCount() == ONE_MOUSE_CLICK) {
                    listWithSongsFilmsSeries.setSelectedIndex(listWithSongsFilmsSeries.locationToIndex(e.getPoint()));
                    popupMenu.show(listWithSongsFilmsSeries, e.getX(), e.getY());
                }
            }

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (e.getClickCount() == TWO_MOUSE_CLICKS) {
//                    if (!Objects.equals(currentElement.getImageIconPath(), NO_PATH)) {
//                        songPhoto.setIcon(imageIconInitializer(currentElement.getImageIconPath(), SONG_IMAGE_WIDTH, SONG_IMAGE_HEIGHT));
//                    }else {
                    songPhoto.setIcon(imageIconInitializer(DEFAULT_ACCOUNT_IMAGE_ICON, SONG_IMAGE_WIDTH, SONG_IMAGE_HEIGHT));
//                    }
                }
            }
        }
    }

    private class PopupMenuRemoveItemListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = listWithSongsFilmsSeries.getSelectedIndex();
            setNewTotalPlayingTime(-currentPlayList.getItems().get(index).getDuration());
            elements.remove(index);
            currentPlayList.removeMedia(index);
            listWithSongsFilmsSeries.setModel(elements);
            ((DefaultTreeModel) playlistTree.getModel()).nodeStructureChanged(root);
        }
    }

    private class MoveUpListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = listWithSongsFilmsSeries.getSelectedIndex();
            if (index != FIRST_INDEX) {
                MediaFile store = currentPlayList.getMedia(index - 1);
                currentPlayList.getItems().set(index - 1, currentPlayList.getItems().get(index));
                currentPlayList.getItems().set(index, store);
                elements.set(index - 1, elements.get(index));
                elements.set(index, store.getTitle());
                listWithSongsFilmsSeries.setModel(elements);
                ((DefaultTreeModel) playlistTree.getModel()).nodeStructureChanged(root);

            }
        }
    }

    private class MoveDownListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = listWithSongsFilmsSeries.getSelectedIndex();
            if (index != currentPlayList.getItems().size() - 1) {
                MediaFile store = currentPlayList.getMedia(index + 1);
                currentPlayList.getItems().set(index + 1, currentPlayList.getItems().get(index));
                currentPlayList.getItems().set(index, store);
                elements.set(index + 1, elements.get(index));
                elements.set(index, store.getTitle());
                listWithSongsFilmsSeries.setModel(elements);
                ((DefaultTreeModel) playlistTree.getModel()).nodeStructureChanged(root);

            }
        }
    }

    private class AddImageListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                //currentElement.setImageIconPath(fileChooser.getSelectedFile().getPath());
                songPhoto.setIcon(imageIconInitializer(fileChooser.getSelectedFile().getPath(), SONG_IMAGE_WIDTH, SONG_IMAGE_HEIGHT));
            }
        }
    }

    private class TopUpListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AmountChooser amountChooser = new AmountChooser(MusicForm.this);
            EventQueue.invokeLater(() -> amountChooser.setVisible(true));
        }
    }

    private class WithdrawListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            while (true) {
                String inputDialog = JOptionPane.showInputDialog(null,
                        "Write below the amount you would like to withdraw! (In pence)", JOptionPane.OK_CANCEL_OPTION);
                if (inputDialog != null) {
                    boolean flag = false;
                    int withdrawAmount = 0;
                    try {
                        withdrawAmount = Integer.parseInt(inputDialog);
                        if (withdrawAmount > currentUser.getWallet() || withdrawAmount <= 0) {
                            throw new InputMismatchException();
                        }
                    } catch (NumberFormatException | InputMismatchException exception) {
                        flag = true;
                    }
                    if (!flag) {
                        totalBalance -= withdrawAmount;
                        setNewTotalBalance();
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect input or insufficient balance in wallet");
                    }
                } else {
                    break;
                }
            }
        }
    }

    private class LoadListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                currentPlayList.readFromFile(new File(fileChooser.getSelectedFile().getPath()));
            }
        }
    }

    private class SaveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                currentPlayList.saveToFile(new File(path + "\\music.txt"));
            }
        }
    }

    /**
     * with real MUSIC && VIDEO, not finished
     **/
    private class LoadRealMediaListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = new File(fileChooser.getSelectedFile().getPath());
                    MediaPlayer mediaPlayer = new MediaPlayer(new Media(file.toURI().toString()));
                    MediaView mediaView = new MediaView(mediaPlayer);
                    mediaView.setSmooth(true);
                    mediaView.setFitWidth(400);
                    mediaView.setFitHeight(600);
                    mediaView.setPreserveRatio(true);

                    mediaPlayer.play();
                } catch (Exception l) {
                    l.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error with the file has occurred.", "Alert", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class SaveRealMediaListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class ByArtistSorter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            currentPlayList.sortItemsByArtistOrDirector();
            elements = populateMediaList(currentPlayList);
            listWithSongsFilmsSeries.setModel(elements);
        }
    }

    private class ByTitleSorter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            currentPlayList.sortItemsByTitle();
            elements = populateMediaList(currentPlayList);
            listWithSongsFilmsSeries.setModel(elements);
        }
    }

    private class ByDurationSorter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            currentPlayList.sortItemsByDuration();
            elements = populateMediaList(currentPlayList);
            listWithSongsFilmsSeries.setModel(elements);
        }
    }

    private class ByCostSorter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            currentPlayList.sortItemsByCost();
            elements = populateMediaList(currentPlayList);
            listWithSongsFilmsSeries.setModel(elements);
        }
    }

    private class ShuffleItemsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            currentPlayList.shuffleItems();
            elements = populateMediaList(currentPlayList);
            listWithSongsFilmsSeries.setModel(elements);
        }
    }

    private class ChangeSongListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == TWO_MOUSE_CLICKS) {
                pause = false;
                int index = listWithSongsFilmsSeries.getSelectedIndex();
                currentSongName.setText(currentPlayList.getItems().get(index).getTitle());
                currentSongArtistName.setText(currentPlayList.getItems().get(index).getArtistOrDirector());
                setCurrentItem(index);
                progressBarSetter(currentElement);
            }
        }
    }

    private class PreviousElementButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            previousItem(currentPlayList.getIndex(currentElement));
        }
    }

    private class PauseElementButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (pausePlayCounter == PLAY) pausePlayCounter++;
            else pausePlayCounter--;

            if (pausePlayCounter == PAUSE) setPauseBoolean(pausePlayCounter);
            else setPauseBoolean(pausePlayCounter);
        }
    }

    private class NextElementButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            nextItem(currentPlayList.getIndex(currentElement));
        }
    }

    private class TreeRender extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
                if (treeNode.getLevel() == DIRECTORY_LEVEL) {
                    setIcon(UIManager.getIcon("FileView.directoryIcon"));
                } else if (treeNode.getLevel() == FILE_LEVEL) {
                    setIcon(UIManager.getIcon("FileView.fileIcon"));
                }
            }
            return this;
        }
    }

    private class MyTreeSelectionListener implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            currentSelectedNode = playlistTree.getLastSelectedPathComponent();
        }
    }

    private class TreeListListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            JPopupMenu treePopup = new JPopupMenu();
            JMenuItem newPlaylist = new JMenuItem("Create new playlist");
            JMenuItem deletePlaylist = new JMenuItem("Delete playlist");

            newPlaylist.addActionListener(new TreeCreatePlaylistListener());
            deletePlaylist.addActionListener(new TreeDeletePlaylistListener());

            treePopup.add(newPlaylist);
            treePopup.add(deletePlaylist);

            int index = playlistTree.getClosestRowForLocation(e.getX(), e.getY());
            store = playlistTree.getPathForLocation(e.getX(), e.getY());

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (e.getClickCount() == ONE_MOUSE_CLICK) {
                    if (currentSelectedNode instanceof String) {

                    }
                    if (currentSelectedNode instanceof MediaFile) {

                    }
                }
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                if (e.getClickCount() == ONE_MOUSE_CLICK) {
                    playlistTree.setSelectionRow(index);
                    treePopup.show(e.getComponent(), e.getX(), e.getY());
                }

            }
        }

    }

    private class TreeCreatePlaylistListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            while (true) {
                String newPlaylistName = JOptionPane.showInputDialog(null, "Please, write the name of your new playlist!");
                if (newPlaylistName.length() >= MINIMUM_NAME_LETTERS && newPlaylistName.length() <= MAXIMUM_NAME_CHARACTERS) {
                    /** CREATE A new method that synchronises the playlists with playlistTree when the user enters **/
                    // create a new playlist, add it to the list of other playlists,
                    // set the current playlist to be the created playlist,
                    // update the screen with current songs/films/series so it can illustrate
                    // the new elements of the new playlist( which is empty),
                    // refresh the tree with playlists;
                    PlayList newPlaylist = new PlayList(currentUser, MusicForm.this, newPlaylistName);
                    playLists.add(newPlaylist);
                    currentPlayList = newPlaylist;
                    elements = new DefaultListModel<>();
                    listWithSongsFilmsSeries.setModel(elements);
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newPlaylistName);
                    root.insert(newNode, root.getChildCount());
                    ((DefaultTreeModel) playlistTree.getModel()).nodeStructureChanged(root);
                    root = newNode;
                    JOptionPane.showMessageDialog(null, "Your new playlist " + newPlaylistName + " has been created!");
                    break;
                } else {
                    int response = JOptionPane.showConfirmDialog(null, "Your playlist name must be between 6 and 20 characters!", "Alert", JOptionPane.OK_CANCEL_OPTION);
                    if (response == JOptionPane.CANCEL_OPTION) {
                        break;
                    }
                }
            }
        }
    }

    private class WindowCloser extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            int confirm = JOptionPane.showOptionDialog(
                    null, "Do you want to exit the application?",
                    "Leaving confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(EXIT_APPLICATION);
            }
        }
    }


    public void synchronizeData() {
        // this.loginForm.getDatabase().execute(synchronize());
    }

    private String synchronize() {
        return "SELECT title, artist_name, duration\n" +
                "FROM " + this.currentUser + ".PLAYLIST_NAME"
                ;
    }

    private class TreeDeletePlaylistListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //treeModel.removeNodeFromParent();
            playLists.remove(playlistTree.getModel().getIndexOfChild(root, store));
            listWithSongsFilmsSeries.setModel(elements);
            ((DefaultTreeModel) playlistTree.getModel()).nodeStructureChanged(root);
            //root = new DefaultMutableTreeNode(nextSongButton);
        }
    }

    public PlayList getCurrentPlayList() {
        return this.currentPlayList;
    }

    public JList<String> getListWithSongsFilmsSeries() {
        return this.listWithSongsFilmsSeries;
    }

    public void setListWithSongsFilmsSeries(JList<String> listWithSongsFilmsSeries) {
        this.listWithSongsFilmsSeries = listWithSongsFilmsSeries;
    }

    public DefaultListModel<String> getMediaDefaultListModel() {
        return this.elements;
    }

    public void setMediaDefaultListModel(DefaultListModel<String> mediaDefaultListModel) {
        this.elements = mediaDefaultListModel;
    }

    public void setPlayLists(PlayList playLists) {
        this.currentPlayList = playLists;
    }

    public int getTotalPlayingTime() {
        return totalPlayingTime;
    }

    public void setTotalPlayingTime(int totalPlayingTime) {
        this.totalPlayingTime = totalPlayingTime;
    }

    public int getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(int totalBalance) {
        this.totalBalance = totalBalance;
    }

    public JLabel getBalanceLabel() {
        return balanceLabel;
    }

    public void setBalanceLabel(JLabel balanceLabel) {
        this.balanceLabel = balanceLabel;
    }

    public JTree getPlaylistTree() {
        return playlistTree;
    }

    public DefaultMutableTreeNode getRoot() {
        return root;
    }

    public MusicForm getMusicForm() {
        return MusicForm.this;
    }

    public ArrayList<PlayList> getPlayLists() {
        return playLists;
    }

}
package View;

import game.Move;
import game.Position;
import game.STONECOLOR;

import java.awt.*;
import java.util.HashMap;

import javax.swing.*;

public class BoardImage extends Label {

    private HashMap<Position, Coordinates> coordinatesMap = new HashMap<>();
    private ImageIcon[][] imageIcons;
    private JLabel mainLabel;


    public static class Coordinates{

        private int x;
        private int y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }


    public BoardImage() {

        setupHashMap();

        final ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/Spielfeld.png"));
        mainLabel = new JLabel(backgroundImage) {
            @Override
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                Dimension lmPrefSize = getLayout().preferredLayoutSize(this);
                size.width = Math.max(size.width, lmPrefSize.width);
                size.height = Math.max(size.height, lmPrefSize.height);
                return size;
            }
        };
        GridBagLayout gridBagLayout = new GridBagLayout();
        mainLabel.setLayout(gridBagLayout);
        GridBagConstraints c = new GridBagConstraints();

        imageIcons = new ImageIcon[13][13];

        for (int i = 0; i < 13; i++){
            for (int j=0; j < 13; j++){
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx=i;
                c.gridy=j;
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/FullyTransparent.png"));
            imageIcons[i][j] = imageIcon;

            JLabel jLabel = new JLabel(imageIcon);
            jLabel.setPreferredSize(new Dimension(17,17));
            mainLabel.add(jLabel, c);
        }}


    }

    private void setupHashMap(){

        coordinatesMap.put(new Position(0,0), new Coordinates(0,0));
        coordinatesMap.put(new Position(0,1), new Coordinates(6,0));
        coordinatesMap.put(new Position(0,2), new Coordinates(12,0));
        coordinatesMap.put(new Position(0,3), new Coordinates(12,6));
        coordinatesMap.put(new Position(0,4), new Coordinates(12,12));
        coordinatesMap.put(new Position(0,5), new Coordinates(6,12));
        coordinatesMap.put(new Position(0,6), new Coordinates(0,12));
        coordinatesMap.put(new Position(0,7), new Coordinates(0,6));

        coordinatesMap.put(new Position(1,0), new Coordinates(2,2));
        coordinatesMap.put(new Position(1,1), new Coordinates(6,2));
        coordinatesMap.put(new Position(1,2), new Coordinates(10,2));
        coordinatesMap.put(new Position(1,3), new Coordinates(10,6));
        coordinatesMap.put(new Position(1,4), new Coordinates(10,10));
        coordinatesMap.put(new Position(1,5), new Coordinates(6,10));
        coordinatesMap.put(new Position(1,6), new Coordinates(2,10));
        coordinatesMap.put(new Position(1,7), new Coordinates(2,6));

        coordinatesMap.put(new Position(2,0), new Coordinates(4,4));
        coordinatesMap.put(new Position(2,1), new Coordinates(6,4));
        coordinatesMap.put(new Position(2,2), new Coordinates(8,4));
        coordinatesMap.put(new Position(2,3), new Coordinates(8,6));
        coordinatesMap.put(new Position(2,4), new Coordinates(8,8));
        coordinatesMap.put(new Position(2,5), new Coordinates(6,8));
        coordinatesMap.put(new Position(2,6), new Coordinates(4,8));
        coordinatesMap.put(new Position(2,7), new Coordinates(4,6));



    }

    public void put(Position position, STONECOLOR stonecolor){
        Coordinates coordinates = coordinatesMap.get(position);

        if (stonecolor == STONECOLOR.BLACK){
            imageIcons[coordinates.getX()][coordinates.getY()] = new ImageIcon(getClass().getResource("/StoneBlack.png"));}
        if (stonecolor == STONECOLOR.WHITE){
            imageIcons[coordinates.getX()][coordinates.getY()] = new ImageIcon(getClass().getResource("/StoneWhite.png"));}


        updateImage();
    }

    public void move(Move move){
        Coordinates coordinatesFrom = coordinatesMap.get(move.getFrom());
        Coordinates coordinatesTo = coordinatesMap.get(move.getTo());

        imageIcons[coordinatesTo.getX()][coordinatesTo.getY()] = imageIcons[coordinatesFrom.getX()][coordinatesFrom.getY()];
        imageIcons[coordinatesFrom.getX()][coordinatesFrom.getY()] = new ImageIcon(getClass().getResource("/FullyTransparent.png"));

        updateImage();
    }

    public void kill(Position position){
        Coordinates coordinates = coordinatesMap.get(position);
        imageIcons[coordinates.getX()][coordinates.getY()] = new ImageIcon(getClass().getResource("/FullyTransparent.png"));


        updateImage();
    }

    private void updateImage(){
        for (int i = 0; i < 13; i++){
            for (int j=0; j < 13; j++){

                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx=i;
                c.gridy=j;
                ImageIcon imageIcon = imageIcons[i][j];

                JLabel jLabel = new JLabel(imageIcon);
                jLabel.setPreferredSize(new Dimension(17,17));
                mainLabel.remove(0);
                mainLabel.add(jLabel, c);
            }}
    }

    public JLabel getMainLabel() {
        return mainLabel;
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                    BoardImage boardLabel = new BoardImage();
                    //boardLabel.initUI();
                    boardLabel.put(new Position(0,0), STONECOLOR.BLACK);


            }
        });
    }*/

}
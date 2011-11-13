package util;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import logic.FastLifeBoard;
import logic.LifeBoard;

public class LifeBoardSaveAndLoad {

    private LifeBoard board;

    public LifeBoardSaveAndLoad(LifeBoard board) {
        this.board = board;
    }

    public void saveToFile(String location) throws FileNotFoundException {
        PrintStream out = new PrintStream(new File(location));
        for (Point live : board) {
            out.println(live.x + ":" + live.y);
        }
        out.close();
    }

    public static LifeBoard load(String fileLocation) throws FileNotFoundException {
        Scanner fin = new Scanner(new File(fileLocation));
        List<Point> points = new ArrayList<Point>();
        while (fin.hasNextLine()) {
            String[] line = fin.nextLine().split(":");
            int x = Integer.parseInt(line[0].trim());
            int y = Integer.parseInt(line[1].trim());
            points.add(new Point(x, y));
        }
        return new FastLifeBoard(points);
    }
}

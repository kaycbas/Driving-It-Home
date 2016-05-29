package group60.perception;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kevin on 5/28/2016.
 */
public class Group {
    private ArrayList<MultiMapCell> group;

    public Group() {
        group = new ArrayList<MultiMapCell>();
    }

    public void addCell(MultiMapCell cell) {
        group.add(cell);
    }

    public boolean contains(MultiMapCell cell) {
        return group.contains(cell);
    }

    public ArrayList<MultiMapCell> getGroup() {
        return group;
    }
}

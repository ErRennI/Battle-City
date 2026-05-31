import java.util.Comparator;

public class ScoreComparator implements Comparator<String[]> {

    @Override
    public int compare(String[] sc1, String[] sc2){
        int s1 = Integer.parseInt(sc1[1].trim());
        int s2 = Integer.parseInt(sc2[1].trim());

        return Integer.compare(s2, s1);
    }
}

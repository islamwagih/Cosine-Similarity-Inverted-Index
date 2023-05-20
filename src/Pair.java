public class Pair implements Comparable<Pair>
{
    private double cosSim;
    private int docId;
    public Pair(double cosSim, int docId) {
        this.docId = docId;
        this.cosSim = cosSim;
    }

    public int getDocId() {
        return docId;
    }

    public double getCosSim() {
        return cosSim;
    }

    @Override
    public int compareTo(Pair pr)
    {
        if(cosSim > pr.cosSim) return -1;
        else if(cosSim < pr.cosSim) return 1;
        return 0;
    }
}

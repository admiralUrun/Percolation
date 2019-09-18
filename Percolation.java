/* *****************************************************************************
 *  Name: Andrii Yakovenko
 *  Date: 04.12.2018
 *  Description: Percolation
 **************************************************************************** */
public class Percolation {
    private int[] idWithVirtualSite;
    private int[] treeDepthWithVirtualSite;

    private int[] id;
    private int[] treeDepth;
    
    
    private boolean[] openSite;
    private int countOfOpenSites = 0;
    private final int size;

    private final int topRoot;
    private final int bottomRoot;

    private final int topRootOfCheckFullSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        int quickFindLength = n * n + 2;
        int quickFindLenghtWithOutVitualSites = n * n;

        id = new int[quickFindLenghtWithOutVitualSites + 1];
        treeDepth = new int[quickFindLenghtWithOutVitualSites + 1];
        topRootOfCheckFullSites = id.length - 1;

        idWithVirtualSite = new int[quickFindLength];
        treeDepthWithVirtualSite = new int[quickFindLength];

        openSite = new boolean[n * n];
        int i = 0;
        for (int index = 0; index < idWithVirtualSite.length; index++) {
            idWithVirtualSite[index] = i;
            treeDepthWithVirtualSite[index] = 1;
            i++;
        }

        i = 0;
        for (int index = 0; index < id.length; index++) {
            id[index] = i;
            treeDepth[index] = 1;
            i++;
        }
        size = n;
        topRoot = quickFindLength - 2;
        bottomRoot = quickFindLength - 1;

        for (int index = 0; index <  n; index++) {
            union(index, topRoot);
            unionForCheckIsFull(index, topRootOfCheckFullSites);
        }

        for (int index = getIndex(n, 1); index < quickFindLength - 2; index++) {
            union(index, bottomRoot);
        }
    }

    public void open(int row, int col) {
        validateRowCol(row, col);

        if (!openSite[getIndex(row, col)]) {
            openSite[getIndex(row, col)] = true;
            countOfOpenSites++;
            unionIfOpen(row, col);
        }
    }

    public boolean isOpen(int row, int col) {
        validateRowCol(row, col);
        return openSite[getIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        validateRowCol(row, col);
        if (isOpen(row, col)) {
            return connectedForCheckIsFull(rootForCheckIsFull(topRootOfCheckFullSites), rootForCheckIsFull(getIndex(row, col)));
        }
        return false;
    }

    public int numberOfOpenSites() {
        return countOfOpenSites;
    }

    public boolean percolates() {
        if (countOfOpenSites <= 0) {
            return false;
        }
        return connected(topRoot, bottomRoot);
    }

    private void unionIfOpen(int row, int col) {
        int[][] directions = { { -1, 0 }, { 0, -1 }, { 1, 0 }, { 0, 1 } };
        for (int direction = 0; direction < directions.length; direction++) {
            int dRow = row + directions[direction][0];
            int dCol = col + directions[direction][1];

            if (dRow < 1 || dRow > size || dCol < 1 || dCol > size) {
                continue;
            }

            if (isOpen(dRow, dCol)) {
                int dIndex = getIndex(dRow, dCol);
                int index = getIndex(row, col);
                union(dIndex, index);
                unionForCheckIsFull(dIndex, index);
            }
        }
    }

    private int getIndex(int row, int col) {
        return (row - 1) * size + (col - 1);
    }

    private void validateRowCol(int row, int col) {
        if (row < 1 || row > size) {
            throw new IllegalArgumentException("Row " + row + " out of range " + size);
        }

        if (col < 1 || col > size) {
            throw new IllegalArgumentException("Col " + col + " out of range " + size);
        }
    }

    private int root(int i) {
        if (idWithVirtualSite[i] == i) {
            return i;
        }

        while (i != idWithVirtualSite[i]) {
            idWithVirtualSite[i] = idWithVirtualSite[idWithVirtualSite[i]];
            i = idWithVirtualSite[i];
        }
        return i;
    }

    private boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    private void union(int firstId, int secondId) {
        int firstRootId = root(firstId);
        int secondRootId = root(secondId);
        if (treeDepthWithVirtualSite[firstRootId] > treeDepthWithVirtualSite[secondId]) {
            idWithVirtualSite[secondRootId] = idWithVirtualSite[firstRootId];
            treeDepthWithVirtualSite[firstId] += treeDepthWithVirtualSite[secondId];
        } else {
            idWithVirtualSite[firstRootId] = idWithVirtualSite[secondRootId];
            treeDepthWithVirtualSite[firstRootId] += treeDepthWithVirtualSite[secondRootId];
        }
    }

    private int rootForCheckIsFull(int i) {
        if (id[i] == i) {
            return i;
        }

        while (i != id[i]) {
            id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    private boolean connectedForCheckIsFull(int p, int q) {
        return rootForCheckIsFull(p) == rootForCheckIsFull(q);
    }

    private void unionForCheckIsFull(int firstId, int secondId) {
        int firstRootId = rootForCheckIsFull(firstId);
        int secondRootId = rootForCheckIsFull(secondId);
        if (treeDepth[firstRootId] > treeDepth[secondId]) {
            id[secondRootId] = id[firstRootId];
            treeDepth[firstId] += treeDepth[secondId];
        } else {
            id[firstRootId] = id[secondRootId];
            treeDepth[firstRootId] += treeDepth[secondRootId];
        }
    }

    public static void main(String[] args) {

    }

}

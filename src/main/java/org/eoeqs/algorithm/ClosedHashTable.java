package org.eoeqs.algorithm;

public class ClosedHashTable {
    private static final double LOAD_FACTOR = 0.75;
    private static final Integer DELETED = Integer.MIN_VALUE;
    private int tableSize;
    Integer[] table;
    boolean[] isOccupied;
    private int numElements;

    public ClosedHashTable() {
        this.tableSize = 10;
        this.table = new Integer[tableSize];
        this.isOccupied = new boolean[tableSize];
        this.numElements = 0;
    }

    private int hash(int key) {
        return (key % tableSize + tableSize) % tableSize;
    }

    public void insert(int key) {
        if ((double) numElements / tableSize >= LOAD_FACTOR) {
            resizeTable();
        }
        int index = hash(key);
        int originalIndex = index;
        while (isOccupied[index]) {
            if (table[index] != null && table[index].equals(key)) {
                return;
            }
            if (table[index] == null || table[index].equals(DELETED)) {
                break;
            }
            index = (index + 1) % tableSize;
            if (index == originalIndex) {
                throw new RuntimeException("Table is full");
            }
        }
        table[index] = key;
        isOccupied[index] = true;
        numElements++;
    }

    private void resizeTable() {
        int newTableSize = tableSize * 2;
        Integer[] newTable = new Integer[newTableSize];
        boolean[] newIsOccupied = new boolean[newTableSize];
        for (int i = 0; i < tableSize; i++) {
            if (isOccupied[i] && table[i] != null && !table[i].equals(DELETED)) {
                int key = table[i];
                int index = (key % newTableSize + newTableSize) % newTableSize;
                while (newIsOccupied[index]) {
                    index = (index + 1) % newTableSize;
                }
                newTable[index] = key;
                newIsOccupied[index] = true;
            }
        }
        table = newTable;
        isOccupied = newIsOccupied;
        tableSize = newTableSize;
    }

    public boolean search(int key) {
        int index = hash(key);
        int originalIndex = index;
        do {
            if (!isOccupied[index]) {
                return false;
            }
            if (table[index] != null && table[index].equals(key)) {
                return true;
            }
            index = (index + 1) % tableSize;
        } while (index != originalIndex);
        return false;
    }

    public void delete(int key) {
        int index = hash(key);
        int originalIndex = index;
        do {
            if (!isOccupied[index]) {
                return;
            }
            if (table[index] != null && table[index].equals(key)) {
                table[index] = DELETED;
                numElements--;
                return;
            }
            index = (index + 1) % tableSize;
        } while (index != originalIndex);
    }

    public int getTableSize() {
        return tableSize;
    }
}
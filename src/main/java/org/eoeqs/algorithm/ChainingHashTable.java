package org.eoeqs.algorithm;

import java.util.ArrayList;
import java.util.List;

public class ChainingHashTable {
    private static final double LOAD_FACTOR = 0.75;
    private int tableSize;
    List<Integer>[] table;
    private int numElements;

    public ChainingHashTable() {
        this.tableSize = 10;
        this.table = new List[tableSize];
        this.numElements = 0;
    }

    int hash(int key) {
        return (key % tableSize + tableSize) % tableSize;
    }

    public void insert(int key) {
        if ((double) (numElements + 1) / tableSize > LOAD_FACTOR) {
            resizeTable();
        }
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new ArrayList<>();
        }
        if (!table[index].contains(key)) {
            table[index].add(key);
            numElements++;
        }
    }

    private void resizeTable() {
        int newTableSize = tableSize * 2;
        List<Integer>[] newTable = new List[newTableSize];
        for (int i = 0; i < tableSize; i++) {
            if (table[i] != null) {
                for (int key : table[i]) {
                    int newIndex = (key % newTableSize + newTableSize) % newTableSize;
                    if (newTable[newIndex] == null) {
                        newTable[newIndex] = new ArrayList<>();
                    }
                    newTable[newIndex].add(key);
                }
            }
        }
        table = newTable;
        tableSize = newTableSize;
    }

    public boolean search(int key) {
        int index = hash(key);
        if (table[index] != null) {
            return table[index].contains(key);
        }
        return false;
    }

    public void delete(int key) {
        int index = hash(key);
        if (table[index] != null && table[index].remove(Integer.valueOf(key))) {
            numElements--;
        }
    }

    public int getTableSize() {
        return tableSize;
    }
}
package org.eoeqs.algorithm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for ClosedHashTable functionality")
public class ClosedHashTableTest {

    @ParameterizedTest(name = "Search for key {0} should return {1}")
    @DisplayName("Test insertion and search without collisions")
    @CsvSource({
            "1, true",
            "2, true",
            "3, true",
            "4, false"
    })
    public void testInsertAndSearchNoCollisions(int key, boolean expected) {
        ClosedHashTable ht = new ClosedHashTable();
        ht.insert(1);
        ht.insert(2);
        ht.insert(3);
        assertEquals(expected, ht.search(key));
    }

    @ParameterizedTest(name = "Search for key {0} with collisions should return {1}")
    @DisplayName("Test insertion and search with collisions")
    @CsvSource({
            "5, true",
            "15, true",
            "25, true",
            "35, false"
    })
    public void testInsertAndSearchWithCollisions(int key, boolean expected) {
        ClosedHashTable ht = new ClosedHashTable();
        ht.insert(5);
        ht.insert(15);
        ht.insert(25);
        assertEquals(expected, ht.search(key));
    }

    @ParameterizedTest(name = "Search for key {1} after inserting {0} should return {2}")
    @DisplayName("Test insertion with table resizing")
    @MethodSource("provideKeysForResize")
    public void testInsertWithResize(int[] keysToInsert, int keyToSearch, boolean expected) {
        ClosedHashTable ht = new ClosedHashTable();
        for (int key : keysToInsert) {
            ht.insert(key);
        }
        assertEquals(expected, ht.search(keyToSearch));
    }

    private static Stream<Object[]> provideKeysForResize() {
        return Stream.of(
                new Object[]{new int[]{0, 10, 20, 30, 40, 50, 60, 70, 80}, 70, true},
                new Object[]{new int[]{0, 10, 20, 30, 40, 50, 60, 70, 80, 90}, 90, true},
                new Object[]{new int[]{0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100}, 100, true}
        );
    }

    @ParameterizedTest(name = "Search for key {0} after deletion should return {1}")
    @DisplayName("Test deletion and subsequent search")
    @CsvSource({
            "5, false",
            "15, true",
            "25, false"
    })
    public void testDeleteAndSearch(int key, boolean expected) {
        ClosedHashTable ht = new ClosedHashTable();
        ht.insert(5);
        ht.insert(15);
        ht.delete(5);
        assertEquals(expected, ht.search(key));
        ht.delete(25);
        assertEquals(expected, ht.search(key));
    }

    @ParameterizedTest(name = "Insert duplicate key {0} should be searchable as {1}")
    @DisplayName("Test insertion of duplicate keys")
    @CsvSource({
            "5, true"
    })
    public void testInsertDuplicates(int key, boolean expected) {
        ClosedHashTable ht = new ClosedHashTable();
        ht.insert(5);
        ht.insert(5);
        assertEquals(expected, ht.search(key));
        assertEquals(1, countElements(ht));
    }

    private int countElements(ClosedHashTable ht) {
        int count = 0;
        for (int i = 0; i < ht.getTableSize(); i++) {
            if (ht.isOccupied[i] && ht.table[i] != null && !ht.table[i].equals(Integer.MIN_VALUE)) {
                count++;
            }
        }
        return count;
    }

    @ParameterizedTest(name = "Search for key {0} in empty table should return {1}")
    @DisplayName("Test search in an empty table")
    @CsvSource({
            "1, false",
            "10, false",
            "-1, false"
    })
    public void testSearchInEmptyTable(int key, boolean expected) {
        ClosedHashTable ht = new ClosedHashTable();
        assertEquals(expected, ht.search(key));
    }

    @Test
    @DisplayName("Test insertion into full table with resizing")
    public void testFullTableWithResize() {
        ClosedHashTable ht = new ClosedHashTable();
        int[] keys = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int key : keys) {
            ht.insert(key);
        }
        for (int key : keys) {
            assertTrue(ht.search(key));
        }
    }

    @ParameterizedTest(name = "Search for key {2} after inserting {0} and deleting {1} should return {3}")
    @DisplayName("Test multiple insertions and deletions")
    @MethodSource("provideKeysForMultipleOperations")
    public void testMultipleInsertDelete(int[] keysToInsert, int[] keysToDelete, int keyToSearch, boolean expected) {
        ClosedHashTable ht = new ClosedHashTable();
        for (int key : keysToInsert) {
            ht.insert(key);
        }
        for (int key : keysToDelete) {
            ht.delete(key);
        }
        assertEquals(expected, ht.search(keyToSearch));
    }

    private static Stream<Object[]> provideKeysForMultipleOperations() {
        return Stream.of(
                new Object[]{new int[]{1, 2, 3}, new int[]{2}, 2, false},
                new Object[]{new int[]{5, 15, 25}, new int[]{5, 15}, 25, true},
                new Object[]{new int[]{0, 10, 20, 30, 40}, new int[]{10, 20, 30}, 0, true}
        );
    }
}
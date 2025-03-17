package org.eoeqs.algorithm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for ChainingHashTable functionality")
public class ChainingHashTableTest {

    @ParameterizedTest(name = "Search for key {0} should return {1}")
    @DisplayName("Test insertion and search without collisions")
    @CsvSource({
            "1, true",
            "2, true",
            "3, true",
            "4, false"
    })
    public void testInsertAndSearchNoCollisions(int key, boolean expected) {
        ChainingHashTable ht = new ChainingHashTable();
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
            "25, true"
    })
    public void testInsertAndSearchWithCollisions(int key, boolean expected) {
        ChainingHashTable ht = new ChainingHashTable();
        ht.insert(5);
        ht.insert(15);
        ht.insert(25);
        assertEquals(expected, ht.search(key));
    }

    @Test
    @DisplayName("Test internal list structure with collisions")
    public void testInternalListStructureWithCollisions() {
        ChainingHashTable ht = new ChainingHashTable();
        ht.insert(5);
        ht.insert(15);
        ht.insert(25);
        int index = ht.hash(5);
        assertNotNull(ht.table[index]);
        assertEquals(3, ht.table[index].size());
        assertTrue(ht.table[index].contains(5));
        assertTrue(ht.table[index].contains(15));
        assertTrue(ht.table[index].contains(25));
    }

    @ParameterizedTest(name = "Search for key {1} after inserting {0} should return {2}")
    @DisplayName("Test insertion with table resizing")
    @MethodSource("provideKeysForResize")
    public void testInsertWithResize(int[] keysToInsert, int keyToSearch, boolean expected) {
        ChainingHashTable ht = new ChainingHashTable();
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

    @Test
    @DisplayName("Test index recalculation after resize")
    public void testIndexRecalculationAfterResize() {
        ChainingHashTable ht = new ChainingHashTable();
        ht.insert(2);
        ht.insert(12);
        ht.insert(22);
        ht.insert(32);
        ht.insert(42);
        ht.insert(52);
        int oldIndex = ht.hash(2) % 10;
        assertNotNull(ht.table[oldIndex]);
        assertEquals(6, ht.table[oldIndex].size());
        ht.insert(62);
        int newTableSize = ht.getTableSize();
        int newIndex = ht.hash(2) % newTableSize;
        assertNotNull(ht.table[newIndex]);
        assertEquals(7, ht.table[newIndex].size());
        assertTrue(ht.table[newIndex].contains(2));
        assertTrue(ht.table[newIndex].contains(12));
        assertTrue(ht.table[newIndex].contains(22));
        assertTrue(ht.table[newIndex].contains(32));
        assertTrue(ht.table[newIndex].contains(42));
        assertTrue(ht.table[newIndex].contains(52));
        assertTrue(ht.table[newIndex].contains(62));
    }

    @ParameterizedTest(name = "Search for key {0} after deletion should return {1}")
    @DisplayName("Test deletion and subsequent search")
    @CsvSource({
            "5, false",
            "15, true",
            "25, false"
    })
    public void testDeleteAndSearch(int key, boolean expected) {
        ChainingHashTable ht = new ChainingHashTable();
        ht.insert(5);
        ht.insert(15);
        ht.delete(5);
        assertEquals(expected, ht.search(key));
        ht.delete(25);
        assertEquals(expected, ht.search(key));
    }

    @Test
    @DisplayName("Test numElements update after deletion")
    public void testNumElementsUpdateAfterDeletion() {
        ChainingHashTable ht = new ChainingHashTable();
        ht.insert(5);
        ht.insert(15);
        assertEquals(2, countElements(ht));
        ht.delete(5);
        assertEquals(1, countElements(ht));
        ht.delete(15);
        assertEquals(0, countElements(ht));
    }

    @ParameterizedTest(name = "Insert duplicate key {0} should be searchable as {1}")
    @DisplayName("Test insertion of duplicate keys")
    @CsvSource({
            "5, true"
    })
    public void testInsertDuplicates(int key, boolean expected) {
        ChainingHashTable ht = new ChainingHashTable();
        ht.insert(5);
        ht.insert(5);
        assertEquals(expected, ht.search(key));
        assertEquals(1, countElements(ht));
    }

    @Test
    @DisplayName("Test deletion from empty or null list")
    public void testDeletionFromEmptyList() {
        ChainingHashTable ht = new ChainingHashTable();
        ht.delete(1);
        assertEquals(0, countElements(ht));
        ht.insert(1);
        ht.delete(2);
        assertEquals(1, countElements(ht));
    }

    @ParameterizedTest(name = "Search for key {0} in empty table should return {1}")
    @DisplayName("Test search in an empty table")
    @CsvSource({
            "1, false",
            "10, false",
            "-1, false"
    })
    public void testSearchInEmptyTable(int key, boolean expected) {
        ChainingHashTable ht = new ChainingHashTable();
        assertEquals(expected, ht.search(key));
    }

    @Test
    @DisplayName("Test insertion into full table with resizing")
    public void testFullTableWithResize() {
        ChainingHashTable ht = new ChainingHashTable();
        int[] keys = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int key : keys) {
            ht.insert(key);
        }
        for (int key : keys) {
            assertTrue(ht.search(key));
        }
    }

    @Test
    @DisplayName("Test behavior with minimal load before resize")
    public void testMinimalLoadBeforeResize() {
        ChainingHashTable ht = new ChainingHashTable();
        ht.insert(1);
        assertEquals(10, ht.getTableSize());
        ht.insert(2);
        assertEquals(10, ht.getTableSize());
        for (int i = 3; i <= 8; i++) ht.insert(i);
        assertEquals(20, ht.getTableSize());
    }

    @ParameterizedTest(name = "Search for key {2} after inserting {0} and deleting {1} should return {3}")
    @DisplayName("Test multiple insertions and deletions")
    @MethodSource("provideKeysForMultipleOperations")
    public void testMultipleInsertDelete(int[] keysToInsert, int[] keysToDelete, int keyToSearch, boolean expected) {
        ChainingHashTable ht = new ChainingHashTable();
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

    private int countElements(ChainingHashTable ht) {
        int count = 0;
        for (int i = 0; i < ht.getTableSize(); i++) {
            if (ht.table[i] != null) {
                count += ht.table[i].size();
            }
        }
        return count;
    }
}
package org.mesdag.scma.block.energy.network;


import java.util.HashSet;

class Node {
    public HashSet<Integer> table;

    public Node() {
        table = new HashSet<>();
    }
}

public class Trie01 {
    private final Node root;

    public Trie01() {
        root = new Node();
    }

    public int insert() {
        int size = root.table.size();
        for (int i = 0; i < size; i++) {
            if (!root.table.contains(i)) {
                root.table.add(i);
                return i;
            }
        }
        root.table.add(size);
        return size;
    }

    public void remove(int id) {
        root.table.remove(id);
    }

    public void clear() {
        root.table.clear();
    }
}

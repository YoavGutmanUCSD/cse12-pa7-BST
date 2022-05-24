import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

/**
 * @param <K> The type of the keys of this BST. They need to be comparable by nature of the BST
 * "K extends Comparable" means that BST will only compile with classes that implement Comparable
 * interface. This is because our BST sorts entries by key. Therefore keys must be comparable.
 * @param <V> The type of the values of this BST. 
 */
public class BST<K extends Comparable<? super K>, V> implements DefaultMap<K, V> {
    /* 
     * TODO: Add instance variables 
     * You may add any instance variables you need, but 
     * you may NOT use any class that implements java.util.SortedMap
     * or any other implementation of a binary search tree
     */

    // root of tree
    Node<K,V> root;

    // something to compare objects when needed
    Comparator<K> comparator;
	Comparator<V> comparatorValues;

    // size of tree
    int size = 0;

    // Constructors
    BST() {
        this.root = null;
    }

    BST(Node<K,V> root) { 
        this.root = root; 
    }



    // Helper method to help the "put" function.. put
    private Node<K,V> compareAdd(Node<K,V> nodeToCompare, K key, V value) {

        // make a nodeToCompare if it doesn't exist yet
        if (nodeToCompare == null) {
            nodeToCompare = new Node(key, value);
            size++;
            return nodeToCompare;
        }

        // otherwise, add the new node and compare using the comparator
        int comp = comparator.compare(nodeToCompare.key, key);

        if (comp < 0) {
            nodeToCompare.right = this.compareAdd(nodeToCompare.right, key, value);

        } else if (comp > 0) {
            nodeToCompare.left = this.compareAdd(nodeToCompare.left, key, value);
        } else {
            return this.root;
        }

        return nodeToCompare;
    }


    // Time complexity: O(n)
    @Override
    public boolean put(K key, V value) throws IllegalArgumentException {

        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        // if (value == null) 
        // 	throw new IllegalArgumentException("Value cannot be null.");

        // calling a handy dandy compare function
        Node<K,V> finalNode = compareAdd(this.root, key, value);

        if(finalNode != this.root) {
            this.root = finalNode;
            return true;
        }

        return false;
    }

    private boolean movingInTreeAndReplace(Node<K,V> root, K key, V value) {
        if (root != null) {
            int compKey = comparator.compare(root.key, key);
			if(compKey == 0) {
				root.value = value;
				return true;
			}
            movingInTreeAndReplace(root.left, key, value);
            movingInTreeAndReplace(root.right, key, value);
        }
		return false;

    }

    // Time complexity: O(n)
    @Override
    public boolean replace(K key, V newValue) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }

		return movingInTreeAndReplace(this.root, key, newValue);
		// good ol helper method that moves through the tree and replaces
    }

    @Override
    public boolean remove(K key) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void set(K key, V value) throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    @Override
    public V get(K key) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsKey(K key) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return false;
    }

    // Keys must be in ascending sorted order
    // You CANNOT use Collections.sort() or any other sorting implementations
    // You must do inorder traversal of the tree
    @Override
    public List<K> keys() {
        // TODO Auto-generated method stub
        return null;
    }

    private static class Node<K extends Comparable<? super K>, V> 
            implements DefaultMap.Entry<K, V> {
            K key;
            V value;
            Node<K,V> left = null;
            Node<K,V> right = null;

            private Node(K key, V value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public K getKey() {
                return key;
            }

            @Override
            public V getValue() {
                return value;
            }

            @Override
            public void setValue(V value) {
                this.value = value;
            }


    }

}

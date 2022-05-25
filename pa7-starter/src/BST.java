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

	// list of keys
	ArrayList<K> keyList = new ArrayList();

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
        int comp = key.compareTo(nodeToCompare.key);

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
        boolean leftReplace = false;
        boolean rightReplace = false;
        if (root != null) {
            boolean compKey = key.equals(root.key);
            if(compKey) {
                root.value = value;
                return true;
            }
            leftReplace = movingInTreeAndReplace(root.left, key, value);
            rightReplace = movingInTreeAndReplace(root.right, key, value);
        }
        return leftReplace || rightReplace;

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


    // find the smallest key in the right (used in the next method)
    private K smallestInTheRight(Node<K,V> rightTree) {
		if (rightTree == null) {
			return null;
		}
        K compareSmallestKey = rightTree.key;
        while (rightTree.left != null) {
            compareSmallestKey = rightTree.left.key;
            rightTree = rightTree.left;
        }

        return compareSmallestKey;
    }

    // moving through the tree and deleting values
    private Node<K,V> moveAndDelete(Node<K,V> nodeToCompare, K key) { 

        // empty trees
        if(nodeToCompare == null) {
            return nodeToCompare;
        }

        // moving through the tree by comparison
        // int comp = comparator.compare(nodeToCompare.key, key);
        int comp = key.compareTo(nodeToCompare.key);

        if (comp < 0) {
            nodeToCompare.right = this.moveAndDelete(nodeToCompare.right, key);

        } else if (comp > 0) {
            nodeToCompare.left = this.moveAndDelete(nodeToCompare.left, key);
        } 

        // key is the same? WE'RE HERE (time to delete)
        else {
            // so, are you a leaf node? you have a child? I don't care, say goodbye!
            if(nodeToCompare.right == null) {return nodeToCompare.left;}
            if(nodeToCompare.left == null) {return nodeToCompare.right;}

            // you're not a leaf node? you have TWO children? that complicates things...
            // since your end is near you need a successor. 
            // they're the smallest in the right tree (nodeToCompare.right)
            Node<K,V> rightTree = nodeToCompare.right;
            K newKey = smallestInTheRight(rightTree);

            // changing its key to the smallest key
            nodeToCompare.key = newKey;

            // delete the previous stuff
            nodeToCompare.right = moveAndDelete(nodeToCompare.right, nodeToCompare.key);
        }
        return nodeToCompare;

    }


    // O(n) Time complexity
    // deletes the specified key
    @Override
    public boolean remove(K key) throws IllegalArgumentException {
		if(key==null) {
			throw new IllegalArgumentException("Key cannot be null.");
		}


        if(this.root != moveAndDelete(this.root, key)) {

            this.root = moveAndDelete(this.root, key);

            return true;
        }
        return false;
    }


    // Helper method to help the "set" function.. set
    private Node<K,V> compareAddDupsToo(Node<K,V> nodeToCompare, K key, V value) {

        // make a nodeToCompare if it doesn't exist yet
        if (nodeToCompare == null) {
            nodeToCompare = new Node(key, value);
            size++;
            return nodeToCompare;
        }

        // otherwise, add the new node and compare using the comparator
        int comp = key.compareTo(nodeToCompare.key);

        if (comp < 0) {
            nodeToCompare.right = this.compareAdd(nodeToCompare.right, key, value);

        } else if (comp > 0) {
            nodeToCompare.left = this.compareAdd(nodeToCompare.left, key, value);
        } else {
            nodeToCompare.value = value;
            return nodeToCompare;
        }

        return nodeToCompare;
    }

    @Override
    public void set(K key, V value) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        // if (value == null) 
        // 	throw new IllegalArgumentException("Value cannot be null.");

        // calling a handy dandy compare function
        Node<K,V> finalNode = compareAddDupsToo(this.root, key, value);

        if(finalNode != this.root) {
            this.root = finalNode;
        }

    }

    private Node get(Node start, K key){
        if(key == null || start == null) { return null; }
        if(start.getKey().equals(key)) { return start; }
        Node leftMost = get(start.left, key);
        Node rightMost = get(start.left, key);
        if(leftMost != null) { return leftMost; }
        if(rightMost != null) { return rightMost; }
        return null;
    }

    @Override
    public V get(K key) throws IllegalArgumentException {
        if(key==null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }

        Node wantedNode = get(this.root, key);
        if (wantedNode == null){
            return null;
        }
        return (V) wantedNode.getValue();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(K key) throws IllegalArgumentException {
        if(get(key) == null) {
			return false;
		}
		return true;
    }

	// all keys in the left and right
	private void transverseRealms(Node<K,V> root) {
		if (root != null) {
			transverseRealms(root.right);
            //transverseRealms(root.left);
            keyList.add(root.key);
            //transverseRealms(root.right);
			transverseRealms(root.left);
        }
	}

    // Keys must be in ascending sorted order
    // You CANNOT use Collections.sort() or any other sorting implementations
    // You must do inorder traversal of the tree
    @Override
    public List<K> keys() {
		keyList.clear();
		transverseRealms(root);
		return keyList;
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

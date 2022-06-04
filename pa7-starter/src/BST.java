import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    // this function takes a note to compare and 
    // both compares and adds.
    // only adds if the node doesn't exist yet
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
            //return this.root;
        }

        return nodeToCompare;
    }


    // Time complexity: O(n)
	/**
	 * Adds the specified key, value pair to this DefaultMap
	 * Note: duplicate keys are not allowed
	 * 
	 * @return true if the key value pair was added to this DefaultMap
	 * @throws IllegalArgumentException if the key is null
	 */
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

    // goes through the whole tree and finds the node to replace
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
    /**
	 * Replaces the value that maps to the key if it is present
	 * @param key The key whose mapped value is being replaced
	 * @param newValue The value to replace the existing value with
	 * @return true if the key was in this DefaultMap
	 * @throws IllegalArgumentException if the key is null
	 */
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
    private Node<K,V> biggestInTheLeft(Node<K,V> leftTree) {
        if (leftTree == null) {
            return null;
        }
        K compareSmallestKey = leftTree.key;
        while (leftTree.right != null) {
            compareSmallestKey = leftTree.right.key;
            leftTree = leftTree.right;
        }

        return leftTree;
    }

    // find the smallest key in the right (used in the next method)
    private Node<K,V> smallestInTheRight(Node<K,V> rightTree) {
        if (rightTree == null) {
            return null;
        }
        K compareSmallestKey = rightTree.key;
        while (rightTree.left != null) {
            compareSmallestKey = rightTree.left.key;
            rightTree = rightTree.left;
        }

        return rightTree;
    }


    //taken from discussion section
	// For when our key has a left child
	// Replace our key's node with the smallest node in this left subtree
	public void replaceNodeWithLeftSubtreeMinimum(Node<K, V> nodeToReplace, Node<K, V> leftChild, Node<K, V> parent){
		// keep going left as much as possible
		while (leftChild.left != null){
			parent = parent.left;
			leftChild = leftChild.left;
		}

		nodeToReplace.key = leftChild.key;
		nodeToReplace.value = leftChild.value;

		if(leftChild.right != null){
			parent.left = leftChild.right;
		}
		else{
			parent.left = null;
		}
	}

	// For when our key has a right child, but no left
	// Replace our key's node with the smallest node in this right subtree
	public void replaceNodeWithRightSubtreeMinimum(Node<K, V> nodeToReplace, Node<K, V> rightChild, Node<K,V> parent){
		if(rightChild.left != null){
			Node<K, V> leftChild = rightChild.left;
			parent = rightChild;

			while (leftChild.left != null){
				parent = parent.left;
				leftChild = leftChild.left;
			}

			nodeToReplace.key = leftChild.key;
			nodeToReplace.value = leftChild.value;

			if(leftChild.right != null){
				parent.left = leftChild.right;
			}
			else {
				leftChild = null;
			}
		}
	    else {
  			nodeToReplace.key = rightChild.key;
  			nodeToReplace.value = rightChild.value;
  			parent.right = null;
	    }
		return;
	}

	// For when our key has no left nor right child (is a leaf node)
	// Replace our key's node with null
	public void removeLeafNode(Node<K,V> nodeToRemove, Node<K,V> previous){

		if(previous == null){
			this.root = null;
		}
		if(nodeToRemove.key.equals(previous.left.key)){
			previous.left = null;
		}
		else{
			previous.right.key = null;
		}
	}



    // O(n) Time complexity
    /**
	 * Remove the entry corresponding to the given key
	 * 
	 * @return true if an entry for the given key was removed
	 * @throws IllegalArgumentException if the key is null
	 */
    @Override
    public boolean remove(K key) throws IllegalArgumentException {
        K keyToRemove = key;

        if(key==null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }

		// Nodes visited by BFS
		Queue<Node<K,V>> queue = new LinkedList<>();
		queue.add(this.root);

		// Parent nodes of nodes visited by BFS
		Queue<Node<K, V>> previousNodes = new LinkedList<>();
		previousNodes.add(null);

		while(!queue.isEmpty()){
			Node<K, V> current = queue.remove();
			Node<K, V> previous = previousNodes.remove();

			// If the key is found, remove it
			if (current.key.equals(keyToRemove)) {
				this.size--;

				// if keyToRemove is the only node in the map
				if(this.size()<1){
					this.root = null;
					return true;
				}

				/* We want to replace the node that our keyToRemove belongs to
				 * with the next smallest value (smallest key that is larger than
				 * our key). There are three possible cases we may have:
				 *
				 * 	1) Our keyToRemove belongs to a node with two children
				 *  2) Our keyToRemove belongs to a node with one child
				 *  3) Out keyToRemove belongs to a node with no children (is a leaf node)
				 *
				 *  We first check if a left subtree exists. If so, find the minimum key
				 *  in this left subtree and replace our keyToRemove's node with it.
				 *
				 *  If not, then we check if a right subtree exists. If so, find the
				 *  minimum key in this right subtree and replace our keyToRemove's
				 *  node with it.
				 *
				 *  If not, our keyToRemove belongs to a leaf node, which we simply
				 *  want to remove by having its parent node have null instead of this
				 *  node as a child (left or right child, depending on which case we have).
				 */

				Node<K, V> parent = current;
				Node<K, V> leftChild = current.left;
				Node<K, V> rightChild = current.right;

				// our key to remove has a left node
				// replace our key's node with the smallest node in this left subtree
				if(leftChild != null){
					this.replaceNodeWithLeftSubtreeMinimum(current, leftChild, parent);
				}

				// our key to remove has no left child, but a right child
				// replace our key's node with the smallest node in this right subtree
				else if (rightChild != null){
					this.replaceNodeWithRightSubtreeMinimum(current, rightChild, parent);
				}

				// our key to remove is a leaf node
				// replace our key's node with null
				else{
					removeLeafNode(current, previous);
				}
				return true;
			}

			// Add left child of current node
			if(current.left != null){
				queue.add(current.left);
				previousNodes.add(current);
			}
			//Add right child of current node
			if(current.right != null){
				queue.add(current.right);
				previousNodes.add(current);
			}
		}
		// Key to remove was not found
		//throw new NoSuchElementException(keyToRemove.toString());
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
    /**
	 * Adds the key, value pair to this DefaultMap if it is not present,
	 * otherwise, replaces the value with the given value
	 * @throws IllegalArgumentException if the key is null
	 */
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
	/**
	 * @return the value corresponding to the specified key
	 * @throws IllegalArgumentException if the key is null
	 */
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
        //return -1000;
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


package textgen;

import java.util.AbstractList;


/** A class that implements a doubly linked list
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	int size;

	/** Create a new empty LinkedList */
	public MyLinkedList() {
		// TODO: Implement this method
        this.size = 0;
        this.head = new LLNode(null);
        this.tail = new LLNode(null);
        this.head.prev = this.tail;
        this.tail.next = this.head;
	}

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element ) 
	{
		// TODO: Implement this method
        if (element == null) {
            throw new NullPointerException("Null is not allowed as input");
        }

        LLNode<E> node = new LLNode<>(element);

        node.prev = this.head.prev;
        node.next = this.head;
        this.head.prev = node;
        node.prev.next = node;

        this.size++;

		return true;
	}

	/** Get the element at position index 
	 * @throws IndexOutOfBoundsException if the index is out of bounds. */
	public E get(int index) 
	{
		// TODO: Implement this method.
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        LLNode node = this.tail;
        for (int i = 0; i < index + 1; i++) {
            node = node.next;
        }

        return (E) node.data;
	}

	/**
	 * Add an element to the list at the specified index
	 * @param index The index where the element should be added
	 * @param element The element to add
	 */
	public void add(int index, E element ) 
	{
		// TODO: Implement this method
        if (element == null) {
            throw new NullPointerException("Null is not allowed as input");
        }

        if (index < 0 || size < index) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        LLNode<E> node = new LLNode<>(element);

        LLNode oldNode = this.tail;
        for (int i = 0; i < index + 1; i++) {
            oldNode = oldNode.next;
        }

        node.prev = oldNode.prev;
        node.next = oldNode;
        oldNode.prev = node;
        node.prev.next = node;

        this.size++;

	}


	/** Return the size of the list */
	public int size() 
	{
		// TODO: Implement this method
		return this.size;
	}

	/** Remove a node at the specified index and return its data element.
	 * @param index The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 * 
	 */
	public E remove(int index) 
	{
		// TODO: Implement this method
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        LLNode oldNode = this.tail;
        for (int i = 0; i < index + 1; i++) {
            oldNode = oldNode.next;
        }

        oldNode.prev.next = oldNode.next;
        oldNode.next.prev = oldNode.prev;

        this.size--;

		return (E) oldNode.data;
	}

	/**
	 * Set an index position in the list to a new element
	 * @param index The index of the element to change
	 * @param element The new element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public E set(int index, E element) 
	{
		// TODO: Implement this method
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index");
        }

        if (element == null) {
            throw new NullPointerException("Value can't be null");
        }

        LLNode oldNode = this.tail;
        for (int i = 0; i < index + 1; i++) {
            oldNode = oldNode.next;
        }

        E value = (E) oldNode.data;
        oldNode.data = element;

		return value;
	}   
}

class LLNode<E> 
{
	LLNode<E> prev;
	LLNode<E> next;
	E data;

	// TODO: Add any other methods you think are useful here
	// E.g. you might want to add another constructor



    public LLNode(E e)
	{
		this.data = e;
		this.prev = null;
		this.next = null;
	}

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LLNode{");
        sb.append("data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}

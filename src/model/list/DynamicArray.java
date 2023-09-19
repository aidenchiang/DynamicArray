//////////////// FILE HEADER //////////////////////////
//
// Title:    Dynamic Array
// Course:   Data Structures and Algorithms
//
// Author:   Aiden Chiang
//
///////////////////////////////////////

/*
 *	This class is a implementation of the Dynamic Array data structure
 * 
 *  @author Aiden Chiang
 * 
 */

package model.list;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import model.linearpub.DynamicList;

public class DynamicArray<E> implements DynamicList<E> {

    //---------------------------------
    // Instance Variables
	private E[] fixedArray;
	private int arraySize;
	private double growthFactor;
	private int capacity;

    //---------------------------------
    // Private Constructors

    /** Constructs and returns new DynamicArray (no args constructor) */
	private DynamicArray() {
		this.fixedArray = (E[]) new Object[defaultInitialCapacity()];
		this.growthFactor = defaultGrowthFactor();
		this.capacity = defaultInitialCapacity();
		this.arraySize = 0;
	}

    /** Constructs and returns new DynamicArray with "aGrowthFactor" */
	private DynamicArray(double aGrowthFactor) {
		this.fixedArray = (E[]) new Object[defaultInitialCapacity()];
		this.growthFactor = aGrowthFactor;
		this.capacity = defaultInitialCapacity();
		this.arraySize = 0;
	}

	//------------------------------------------------

    public static double defaultGrowthFactor() {
		//
    	return 2;
    }

	protected static int defaultInitialCapacity() {
		return 10;
	}

	//-------------------- List Statistics ---------------------

	/**
	 * Return number of elements in this list.
	 */
	@Override
	public int size() {
		//
		return this.arraySize;
	}

	/**
	 * Return true is this list contains no elements.
	 */
	@Override
	public boolean isEmpty() {
		//
		return this.size() == 0;
	}

	//------------------ Accessing Elements --------------------

	/**
	 * Return element at given index.
	 * Throws IndexOutOfBoundsException if passed index is invalid.
	 */
	@Override
	public E get(int index) {
		if (index >= this.size()) {
			throw new IndexOutOfBoundsException();
		}
		return this.fixedArray[index];
	}

	/**
	 * Return first element
	 * Throws RuntimeException if list is empty
	 */
	@Override
	public E first() {
		if(this.isEmpty()) {
			throw new RuntimeException();
		}
		return this.get(0);
	}

	/**
	 * Return last element
	 * Throws RuntimeException if list is empty
	 */
	@Override
	public E last() {
		if(this.isEmpty()) {
			throw new RuntimeException();
		}
		return this.get(this.size() - 1);
	}

	/**
	 * Return a new list containing the elements of this list
	 * between the given index "start" (inclusive) and
	 * the given index "stop" (exclusive).
	 * Throws IndexOutOfBoundsException if either passed index is invalid.
	 */
	@Override
	public DynamicList<E> subList(int start, int stop) {
		if (start > stop) {
			throw new IndexOutOfBoundsException();
		}
		if (start < 0 || this.isEmpty()) {
			throw new IndexOutOfBoundsException();
		}
		if (stop > this.size() - 1) {
			throw new IndexOutOfBoundsException();
		}
		DynamicList<E> newList = DynamicArray.newEmpty();
		for (int i=start; i < stop; i++) {
			newList.add(this.fixedArray[i]);
		}
		if (start == stop) {
			newList.add(this.fixedArray[start]);
		}
		return newList;
	}

	/**
	 * Return index of first matching element (where searchFct outputs true)
	 * Return -1 if no match
	 * Example usage (first list of integers, then employees):
	 *	index = list.find(eaInteger -> eaInteger == 10);
	 *  index = employeeList.find(employee -> employee .getFirstName().equals("Kofi"));
	 */
	@Override
	public int findFirst(Function<E, Boolean> searchFct) {
		for (int i=0; i < this.size(); i++) {
			if (searchFct.apply(this.get(i))) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Return index of last matching element (where searchFct outputs true)
	 * E.g., if searching for employee with name "Kofi" and there is a match
	 * at index=3 and index=8, findLast will return 8 (the last matching index).
	 * Hint: start search at end of list and work backwards through list.
	 * Return -1 if no match
	 */
	@Override
	public int findLast(Function<E, Boolean> searchFct) {
		for (int i=this.size() - 1; i >= 0; i--) {
			if (searchFct.apply(this.get(i))) {
				return i;
			}
		}
		return -1;
	}

	//------------------- Setting Elements ---------------------

	/**
	 * Insert passed arg "newElem" into position "index"
	 * Return previous (replaced) elem at "index"
	 * Valid "index" values are between 0 and "size - 1"
	 * If "index" is invalid, throws IndexOutOfBoundsException.
	*/
	@Override
	public E set(int index, E newElem) {
		if (index < 0 || index > this.size() - 1) {
			throw new IndexOutOfBoundsException();
		}
		E oldElem = this.get(index);
		this.fixedArray[index] = newElem;
		return oldElem;
	}

	//------- Inserting, Appending & Replacing Elements --------
	//------------------ (Dynamic Behaviors)  ------------------

	/**
	 * Add the passed element to start of list
	 */
	@Override
	public void addFirst(E newElem) {
		if (this.arraySize + 1 > capacity) {
			this.fixedArray = this.grow();
		}
		this.shiftToRight(0);
		fixedArray[0] = newElem;
		this.arraySize++;
	}

	/**
	 * Add the passed element to end of list
	 */
	@Override
	public void addLast(E newElem) {
		if (this.arraySize + 1 > capacity) {
			this.fixedArray = this.grow();
		}
		fixedArray[this.size()] = newElem;
		this.arraySize++;
	}

	/**
	 * Alias for "addLast" (same functionality)
	 */
	@Override
	public void add(E newElem) {
		if (this.arraySize + 1 > capacity) {
			this.fixedArray = this.grow();
		}
		this.addLast(newElem);
	}

	/**
	 * Add all elements from "otherDynList" into "this" list
	 */
	@Override
	public void addAll(DynamicList<E> otherDynList) {
		if (this.arraySize + otherDynList.size() > capacity) {
			this.fixedArray = this.grow();
		}
		for (int i=0; i < otherDynList.size(); i++) {
			if (otherDynList.get(i) == null) {
				break;
			}
			this.add(otherDynList.get(i));
		}
	}

	/**
	 * Add all elements from passed fixed array "this" list
	 */
	@Override
	public void addAll(E[] array) {
		if (this.arraySize + array.length > capacity) {
			this.fixedArray = this.grow();
		}
		for (int i=0; i < array.length; i++) {
			if (array[i] == null) {
				break;
			}
			this.add(array[i]);
		}
	}

	/**
	 * Shift to the right the element currently at "insertIndex" (if any) and all elements to the right
	 * Insert passed arg "newElem" into position "insertIndex"
	 * Valid "insertIndex" values are between 0 and "size"
	 * If index = "size" then it becomes a simple "add" operation
	 * If "insertIndex" is invalid, throws IndexOutOfBoundsException
	 */
	@Override
	public void insert(int insertIndex, E newElem) {
		if (this.arraySize + 1 > capacity) {
			this.fixedArray = this.grow();
		}
		if (insertIndex < 0 || insertIndex > this.size()) {
			throw new IndexOutOfBoundsException();
		}
		if (insertIndex == this.size()) {
			this.add(newElem);
		} else {
			this.shiftToRight(insertIndex);
			fixedArray[insertIndex] = newElem;
			this.arraySize++;
		}
	}

	//------------------- Removing Elements --------------------
	//------------------ (Dynamic Behaviors)  ------------------

	/**
	 * Remove first element
	 * Return removed element
	 * Throws RuntimeException if list is empty
	 */
	@Override
	public E removeFirst() {
		if (this.isEmpty()) {
			throw new RuntimeException();
		}
		E removedElem = this.get(0);
		this.shiftToLeft(1);
		this.arraySize--;
		return removedElem;
	}

	/**
	 * Remove last element
	 * Return removed element
	 * Throws RuntimeException if list is empty
	 */
	@Override
	public E removeLast() {
		E removedElem = this.get(this.size() - 1);
		this.arraySize--;
		return removedElem;
	}

	/**
	 * Reset the list so it is empty.
	 * If list is already empty, then do nothing
	 * No action is performed on the elements.
	 *
	 */
	@Override
	public void removeAll() {
		if (isEmpty()) {
		} else {
			this.arraySize = 0;
		}
	}

	/**
	 * Remove elem at index
	 * Return the removed element
	* Throws IndexOutOfBoundsException if passed index is invalid.
	 */
	@Override
	public E removeIndex(int index) {
		E removedElem = this.get(index);
		this.shiftToLeft(index + 1);
		this.arraySize--;
		return removedElem;
	}

	/**
	 * Remove first matching element (where searchFct outputs true)
	 * Return the removed element
	 * If no match, return null
	 */
	@Override
	public E removeFirstMatching(Function<E, Boolean> searchFct) {
		int index = this.findFirst(searchFct);
		E removedElem;
		if (index != -1) {
			removedElem = this.get(index);
			this.removeIndex(index);
			return removedElem;
		} else {
			return null;
		}
	}

	//----------------- Convenience Methods ------------------

	/** Return this list as array
	 *	This method requires imports of:
	 *		java.lang.reflect.Array;
	 *		java.util.concurrent.atomic.AtomicInteger;
	 */
	@Override
	@SuppressWarnings("unchecked")
	public E[] toArray() {
		//This method is completed (no work needed)
		if (this.isEmpty())
			return (E[]) Array.newInstance(Object.class, 0);
		StructureIterator<E> iter = this.iterator();
		E[] array = (E[]) Array.newInstance(iter.peek().getClass(), this.size());
		AtomicInteger counter = new AtomicInteger(0);
		this.forEach((each) -> array[counter.getAndIncrement()] = each);
		return array;
	}

	/**
	 * Returns one-line user-friendly message about this object
	 * Helpful method especially for debugging.
	 */
	@Override
	public String toString() {

		return "Growth Factor: " + growthFactor + ". Size: " + this.size() + ".";
	}

	/** Prints all elements to console, with newline after each */
	@Override
	public void printAll() {
		for (int i=0; i < this.size(); i++) {
			System.out.println(this.get(i));
		}
	}

	/** Iterates over elements in "this" object. For each element,
	 * performs actionFct (passing element being iterated on)
	 * The generic type "? super E" means some type that is
	 * a superclass of E (inclusive)
	 */
	@Override
	public void forEach(Consumer<? super E> actionFct) {
		for (int i=0; i < this.size(); i++) {
			actionFct.accept(this.get(i));
		}
	}

	/** Return new list that is "this" list joined
	 * 	with "otherList" list (this list's elements are
	 * 	first followed by the "otherList" list)
	 */
	@Override
	public DynamicList<E> join(DynamicList<E> otherList) {
		DynamicList<E> newList = DynamicArray.newEmpty();
		newList.addAll(this.fixedArray);
		newList.addAll(otherList);
		return newList;
	}

	//----------------- Utility Methods ------------------

	/**
	 * Returns new DynamicList with "new elements". Each new element
	 * is generated from mapFct invoked with an element from
	 * this list.
	 */
	@Override
	public <T> DynamicList<T> map(Function<E, T> mapFct) {
		DynamicList<T> newList = DynamicArray.newEmpty();
		for (int i=0; i < this.size(); i++) {
			newList.add(mapFct.apply(this.get(i)));
		}
		return newList;
	}

	/**
	 * Returns new DynamicList containing only elements that
	 * result in true when applied to selectFct
	 * Returns new DynamicList which is elements
	 * selected from this list via selectFct
	 */
	@Override
	public DynamicList<E> select(Function<E, Boolean> selectFct) {
		DynamicList<E> newList = DynamicArray.newEmpty();
		for (int i=0; i < this.size(); i++) {
			if(selectFct.apply(this.get(i))) {
				newList.add(this.get(i));
			}
		}
		return newList;
	}

	/**
	 * Returns new DynamicList which is this list
	 * with elements rejected via rejectFct
	 */
	@Override
	public DynamicList<E> reject(Function<E, Boolean> rejectFct) {
		DynamicList<E> newList = DynamicArray.newEmpty();
		for (int i=0; i < this.size(); i++) {
			if(rejectFct.apply(this.get(i))) {
				newList.add(this.get(i));
			}
		}
		return newList;
	}

	/** Accumulate a value by iterating over the collection
	  * and accumulating during iteration.
	  * E.g., accumulate a "sum", or accumulate
	  * a new collection which is the accumulation
	  * of sub-collections obtained from elements (think
	  * of accumulating all players in a league by
	  * accumulating the players from each team
	  */
	@Override
	public <T> T accumulate(BiFunction<T, E, T> fct, T initialValue) {
		T sum = initialValue;
		for (int i=0; i < this.size(); i++) {
			sum = fct.apply(sum, this.get(i));
		}
		return sum;
	}

    //---------------------------------
    // Public Constructors (Static Factory Constructor Methods)
	//---------------------------------

    /** Returns a new empty DynamicList */
    public static <T> DynamicList<T> newEmpty() {
        return new DynamicArray<>();
    }

    /** Return a new empty DynamicArray with "growthFactor" */
    public static <T> DynamicList<T> fromGrowthFactor(double growthFactor) {
        return new DynamicArray<>(growthFactor);
    }

    /** Return a new DynamicList that contains all elements from the
     *	param "aFixedArray" */
    public static <T> DynamicList<T> from(T[] aFixedArray) {
    	DynamicList<T> dynamic = new DynamicArray<>(defaultGrowthFactor());
		for (T nextNewElem: aFixedArray)
			dynamic.add(nextNewElem);
        return dynamic;
    }

	//----------------------------------------------------------

	// ---------------------- Helper Methods ----------------------

	// Shifts all elements in the array to the right, overwriting the element at startingIndex
	private void shiftToRight(int startingIndex) {
		for (int i=this.size() - 1; i >= startingIndex; i--) {
			this.fixedArray[i + 1] = this.get(i);
		}
	}

	// Shifts all elements in the array to the left, overwriting the element at startingIndex
	private void shiftToLeft(int startingIndex) {
		for (int i=startingIndex; i < this.size(); i++) {
			this.fixedArray[i - 1] = this.get(i);
		}
	}

	// Creates a new array with a capacity of the current arrays capacity multiplied by the growthFactor (a default
	// of 2). Then copies all elements in the old array to the new array.
	private E[] grow() {
		@SuppressWarnings("unchecked")
		int newSize = (int) (this.growthFactor * this.capacity);
		E[] newFixedArr = (E[]) new Object[newSize];
		for (int i=0; i < this.size(); i++) {
			newFixedArr[i] = this.get(i);
		}
		capacity = newSize;
		return newFixedArr;
	}
}

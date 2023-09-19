package model.linearpub;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DynamicList<E> {


	//-------------------- List Statistics ---------------------


	/**
	 * Return number of elements in this list.
	 */
	int size();


	/**
	 * Return true is this list contains no elements.
	 */
	boolean isEmpty();


	//------------------ Accessing Elements --------------------

	/**
	 * Return element at given index.
	 * Throws IndexOutOfBoundsException if passed index is invalid.
	 */
	E get(int index);


	/**
	 * Return first element
	 * Throws RuntimeException if list is empty
	 */
	E first();


	/**
	 * Return last element
	 * Throws RuntimeException if list is empty
	 */
	E last();

	/**
	 * Return a new list containing the elements of this list
	 * between the given index "start" (inclusive) and
	 * the given index "stop" (exclusive).
	 * Throws IndexOutOfBoundsException if either passed index is invalid.
	 */
	DynamicList<E> subList(int start, int stop);

	/**
	 * Return index of first matching element (where searchFct outputs true)
	 * Return -1 if no match
	 * Example usage (first list of integers, then employees):
	 *	index = list.find(eaInteger -> eaInteger == 10);
	 *  index = employeeList.find(employee -> employee .getFirstName().equals("Kofi"));
	 */
	int findFirst(Function<E, Boolean> searchFct);


	/**
	 * Return index of last matching element (where searchFct outputs true)
	 * E.g., if searching for employee with name "Kofi" and there is a match
	 * at index=3 and index=8, findLast will return 8 (the last matching index).
	 * Hint: start search at end of list and work backwards through list.
	 * Return -1 if no match
	 */
	int findLast(Function<E, Boolean> searchFct);


	//------------------- Setting Elements ---------------------


	/**
	 * Insert passed arg "newElem" into position "index"
	 * Return previous (replaced) elem at "index"
	 * Valid "index" values are between 0 and "size - 1"
	 * If "index" is invalid, throws IndexOutOfBoundsException.
	*/
	E set(int index, E newElem);


	//------- Inserting, Appending & Replacing Elements --------
	//------------------ (Dynamic Behaviors)  ------------------


	/**
	 * Add the passed element to start of list
	 */
	void addFirst(E newElem);


	/**
	 * Add the passed element to end of list
	 */
	void addLast(E newElem);


	/**
	 * Alias for "addLast" (same functionality)
	 */
	void add(E newElem);


	/**
	 * Add all elements from "otherDynList" into "this" list
	 */
	void addAll(DynamicList<E> otherDynList);
	
	/**
	 * Add all elements from passed fixed array "this" list
	 */
	void addAll(E[] array);	

	/**
	 * Shift to the right the element currently at "insertIndex" (if any) and all elements to the right
	 * Insert passed arg "newElem" into position "insertIndex"
	 * Valid "insertIndex" values are between 0 and "size"
	 * If index = "size" then it becomes a simple "add" operation
	 * If "insertIndex" is invalid, throws IndexOutOfBoundsException
	 */
	void insert(int insertIndex, E newElem);


	//------------------- Removing Elements --------------------
	//------------------ (Dynamic Behaviors)  ------------------


	/**
	 * Remove first element
	 * Return removed element
	 * Throws RuntimeException if list is empty
	 */
	E removeFirst();


	/**
	 * Remove last element
	 * Return removed element
	 * Throws RuntimeException if list is empty
	 */
	E removeLast();


	/**
	 * Reset the list so it is empty.
	 * If list is already empty, then do nothing
	 * No action is performed on the elements.
	 *
	 */
	void removeAll();


	/**
	 * Remove elem at index
	 * Return the removed element
	* Throws IndexOutOfBoundsException if passed index is invalid.
	 */
	E removeIndex(int index);


	/**
	 * Remove first matching element (where searchFct outputs true)
	 * Return the removed element
	 * If no match, return null
	 */
	E removeFirstMatching(Function<E, Boolean> searchFct);


	//----------------- Convenience Methods ------------------

	/**
	 * Return this list as an array (maintain same order of elements)
	 */
	E[] toArray();

	/**
	 * Returns one-line user-friendly message about this object
	 * Helpful method especially for debugging.
	 */
	String toString();


	/** Prints all elements to console, with newline after each */
	void printAll();


	/** Iterates over elements in "this" object. For each element,
	 * performs actionFct (passing element being iterated on)
	 * The generic type "? super E" means some type that is
	 * a superclass of E (inclusive)
	 */
	void forEach(Consumer<? super E> actionFct);
	
	/** Return new list that is "this" list joined
	 * 	with "otherList" list (this list's elements are
	 * 	first followed by the "otherList" list)
	 */
	DynamicList<E> join(DynamicList<E> otherList);	


	//----------------- Utility Methods ------------------


	/**
	 * Returns new DynamicList with "new elements". Each new element
	 * is generated from mapFct invoked with an element from
	 * this list.
	 */
	<T> DynamicList<T> map(Function<E, T> mapFct);


	/**
	 * Returns new DynamicList containing only elements that
	 * result in true when applied to selectFct
	 * Returns new DynamicList which is elements
	 * selected from this list via selectFct
	 */
	DynamicList<E> select(Function<E, Boolean> selectFct);

	/**
	 * Returns new DynamicList which is this list
	 * with elements rejected via rejectFct
	 */
	DynamicList<E> reject(Function<E, Boolean> rejectFct);
	
	/** Accumulate a value by iterating over the collection
	  * and accumulating during iteration.
	  * E.g., accumulate a "sum", or accumulate
	  * a new collection which is the accumulation
	  * of sub-collections obtained from elements (think
	  * of accumulating all players in a league by
	  * accumulating the players from each team
	  */
	<T> T accumulate(BiFunction<T, E, T> fct, T initialValue);
	
	//------------------- Optional Methods ---------------------
	
	/**
	 * Return iterator on this list
	 */
	default StructureIterator<E> iterator() { throw notImplemented(); }

	static RuntimeException notImplemented() {
		return new RuntimeException("Not Implemented");
	}

}
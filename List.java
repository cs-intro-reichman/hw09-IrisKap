/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }

    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the first element in the list */
    public CharData getFirst() {
        return first.cp;
    }
    public Node getFirstNode() {
        return first;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData newChar = new CharData(chr);
        Node newFirst = new Node(newChar);
        newFirst.next = first;
        first = newFirst;
        size++;
    }

    /** GIVE Textual representation of this list. */
    public String toString() {
        Node current = first;
        String str = "(";
        while(current != null)
        {
            str += current.toString() + " ";
            current = current.next;
        }
        str = str.substring(0, str.length()-1);
        str += ")";
        return str;
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        int index=0;
        Node current = first;
        while(current !=null)
        {
            if(current.cp.chr == chr)
            {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        int index = indexOf(chr);
        if(index == -1)
        {
            addFirst(chr);

        }
        else
        {
            int i =0;
            Node current = first;
            while(i<index)
            {
                current = current.next;
                i++;
            }
            current.cp.count++;

        }
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {

        int index = this.indexOf(chr);
        if(index == -1)
        {
            return false;
        }
        int i=0;
        Node current = first;
        Node previous = null;
        while(i < index && current!=null)
        {   
            previous = current;
            current = current.next;
            i++;
        }
        if(previous==null)
        {
            first = first.next;
            size--;
            return true;
        }
        previous.next = current.next;
        size--;
        return true;
    }
   

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if(index < 0 || index > size)
        {
            throw new IndexOutOfBoundsException();
        }
        return toArray()[index];
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
        CharData[] arr = new CharData[size];
        Node current = first;
        int i = 0;
        while (current != null) {
            arr[i++]  = current.cp;
            current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
        // If the list is empty, there is nothing to iterate   
        if (size == 0) return null;
        // Gets the element in position index of this list
        Node current = first;
        int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
        return new ListIterator(current);
    }
    }


public class QueueList <T>{
    
    public class Node {
        T value;
        Node next;
    }

    Node _head;
    Node _tail;
    int _size;

    public T dequeue(){
        if (_head != null) {
            T val = _head.value;
            _head = _head.next;
            _size--;
            return val;
        } else {
            return null;
        }
    }

    public void enqueue(T value){
        Node node = new Node();
        node.value = value;
        if (_head == null) {
            _head = node;
        } else {
            _tail.next = node;
        }
        _tail = node;
        _size++;
    }

    public boolean hasNext(){
        return _head != null;
    }

    public int size(){
        return _size;
    }
}

import java.util.NoSuchElementException;
import java.util.Random;

public class SkipList<K extends Comparable<K>,V> {
    private QuadNode<K, V> head = new QuadNode<K, V>(null, null, 0);
    final static Random random = new Random();

    public void insert(K key, V value) {
        int level = 0;

        while (random.nextBoolean()) {
            level++;
        }

        while (head.getLevel() < level) {
            QuadNode<K, V> newHead = new QuadNode<K, V>(null, null, head.getLevel() + 1);
            head.setUp(newHead);
            newHead.setDown(head);
            head = newHead;
        }

        head.insert(key, value, level, null);
    }

    public V find(K key) {
        return head.find(key).getValue();
    }

    public void delete(K key) {
        for (QuadNode<K,V> node = head.find(key); node != null; node = node.getDown()) {
            node.getPrevious().setNext(node.getNext());
            if (node.getNext() != null) {
                node.getNext().setPrevious(node.getPrevious());
            }
        }

        while (head.getNext() == null) {
            head = head.getDown();
            head.setUp(null);
        }
    }

    @Override
    public String toString() {
        return head.toString();
    }

    public static void main(String[] args) {
        SkipList<Integer, String> sl = new SkipList<Integer, String>();
        sl.insert(3, "tre");
        sl.insert(6, "sex");
        sl.insert(2, "två");
        sl.insert(5, "fem");
        sl.insert(1, "ett");
        try {
            sl.insert(1, "ett");
            throw new IllegalStateException("Duplicates are not allowed!");
        } catch (IllegalArgumentException e) {
            System.out.println("Yes, 1 should not be allowed again.");
        }

        System.out.println(sl);
        System.out.println("-------");
        System.out.println(sl.find(3).equals("tre"));
        System.out.println(sl.find(6).equals("sex"));
        System.out.println(sl.find(2).equals("två"));
        System.out.println(sl.find(5).equals("fem"));
        System.out.println(sl.find(1).equals("ett"));

        sl.delete(6);
        System.out.println(sl);
        System.out.println("-------");

        sl.delete(3);
        System.out.println(sl);
        System.out.println("-------");

        try {
            sl.find(3);
            throw new IllegalStateException("Nooo!");
        } catch (NoSuchElementException e) {
            System.out.println("Yes, 3 should not be found");
        }
    }
}

package com.javarush.task.task20.task2028;

import jdk.nashorn.internal.ir.IfNode;

import java.io.Serializable;
import java.util.*;

/* 
Построй дерево(1)
Итак, основа дерева создана, пора тебе поработать немного самому.
Вспомним как должно выглядеть наше дерево.

Элементы дерева должны следовать так как показано на картинке:
http://info.javarush.ru/uploads/images/00/04/89/2014/03/21/ee9a9b.jpg

Необходимо написать методы, которые бы позволили создать такую структуру дерева и проводить операции над ней.

Тебе необходимо реализовать:
1. метод add(String s) - добавляет элементы дерева, в качестве параметра принимает имя элемента (elementName),
искать место для вставки начинаем слева направо.
2. метод remove(Object o) - удаляет элемент дерева имя которого было полученного в качестве параметра.
3. метод size() - возвращает текущее количество элементов в дереве.
4. метод getParent(String s) - возвращает имя родителя элемента дерева, имя которого было полученного в качестве параметра.


Требования:
1. В классе CustomTree должны быть реализованы методы согласно условия задачи.
2. После добавления N элементов в дерево с помощью метода add, метод size должен возвращать N.
3. После удаления последнего добавленного элемента из дерева с помощью метода remove, метод size должен возвращать N-1.
4. После удаления второго элемента добавленного в дерево, метод size должен возвращать N/2 + 1
(для случаев где N > 2 и является степенью двойки), N - размер дерева до удаления.
5. Метод getParent должен возвращать имя родителя для любого элемента дерева.
*/
public class CustomTree extends AbstractList<String> implements Cloneable, Serializable{
    Entry<String> root = new Entry<>("0");

    static class Entry<T> implements Serializable{
        String elementName;
        int lineNumber;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String str){
            elementName = str;
            availableToAddLeftChildren = true;
            availableToAddRightChildren = true;
        }

        public void checkChildren() {
            if (leftChild != null) {
                availableToAddLeftChildren = false;
            }
            if (rightChild != null) {
                availableToAddRightChildren = false;
            }
        }

       public boolean isAvailableToAddChildren() {
            return availableToAddLeftChildren || availableToAddRightChildren;
        }


    }

    public static void main(String[] args) {
        List<String> list = new CustomTree();
        for (int i = 1; i < 16; i++) {
            list.add(String.valueOf(i));
        }
        System.out.println(list.size());
        ((CustomTree)list).horizontalTreeWalk(((CustomTree)list).root);
        System.out.println("Expected 3, actual is " + ((CustomTree) list).getParent("8"));
        list.remove("1");
        System.out.println(list.size());
        ((CustomTree)list).horizontalTreeWalk(((CustomTree)list).root);
         list.add("16");
        System.out.println(list.size());
        ((CustomTree)list).horizontalTreeWalk(((CustomTree)list).root);
        list.remove("6");
        System.out.println(list.size());
        ((CustomTree)list).horizontalTreeWalk(((CustomTree)list).root);
        System.out.println("Expected null, actual is " + ((CustomTree) list).getParent("6"));
        System.out.println("Expected 5, actual is " + ((CustomTree) list).getParent("11"));
        System.out.println("Expected 11, actual is " + ((CustomTree) list).getParent("16"));

    }

    public void horizontalTreeWalk(Entry<String> top) {

        LinkedList<Entry> queue=new LinkedList<> ();
        Entry<String> noMoreEntriesFlag = new Entry<>("noMoreEntriesFlag");//this flag is necessary for the
        //situation when tree consists only of one (right or left) branch
        //flag is added to the end of the queue, other elements are positioned before it
        queue.add(noMoreEntriesFlag);
        do{
            if (top.leftChild != null) {
                queue.add(queue.size() - 1, top.leftChild);//element is added before noMoreEntriesFlag
            }
            if (top.rightChild != null) {
                queue.add(queue.size() - 1, top.rightChild);//element is added before noMoreEntriesFlag
            }
            if (!queue.isEmpty()) {
                top = queue.poll();//if not use noMoreEntriesFlag in one branch tree while loop will be terminated after
                //root child is polled of the queue, so root child's children won't be iterated.
                System.out.println(top.getClass().getSimpleName()+"@"+top.hashCode() + " : " + top.elementName);
            }
        }while (!queue.isEmpty());
    }


    @Override
    public boolean add(String s) {
        Entry<String> current = root;
        Entry<String> nodeToAdd = new Entry<>(s);

        LinkedList<Entry> queue=new LinkedList<> ();

        Entry<String> noMoreEntriesFlag = new Entry<>("noMoreEntriesFlag");//this flag is necessary for the
        //situation when tree consists only of one (right or left) branch
        //flag is added to the end of the queue, other elements are positioned before it
        queue.add(noMoreEntriesFlag);

        do {

                if (current.availableToAddLeftChildren) {
                    current.leftChild = nodeToAdd;
                    nodeToAdd.parent = current;
                    current.checkChildren();
                    return true;
                } else {
                    if (current.leftChild != null) {
                        queue.add(queue.size() - 1,current.leftChild);//element is added before noMoreEntriesFlag
                    }
                }

                if (current.availableToAddRightChildren) {
                    current.rightChild = nodeToAdd;
                    nodeToAdd.parent = current;
                    current.checkChildren();
                    return true;
                } else {
                    if (current.rightChild != null) {
                        queue.add(queue.size() - 1, current.rightChild);//element is added before noMoreEntriesFlag
                    }
                }

                if (!queue.isEmpty()) current = queue.poll();//if not use noMoreEntriesFlag in one branch tree while loop will be terminated after
            //root child is polled of the queue, so root child's children won't be iterated.

        }while (!queue.isEmpty());

        return false;
    }

    public boolean remove(Object o) {
        Entry<String> current = root;

        LinkedList<Entry> queue = new LinkedList<>();

        Entry<String> noMoreEntriesFlag = new Entry<>("noMoreEntriesFlag");//this flag is necessary for the
        //situation when tree consists only of one (right or left) branch
        //flag is added to the end of the queue, other elements are positioned before it
        queue.add(noMoreEntriesFlag);

        do {

            if (current.leftChild != null) {
                queue.add(queue.size() - 1, current.leftChild);//element is added before noMoreEntriesFlag
            }

            if (current.rightChild != null) {
                queue.add(queue.size() - 1, current.rightChild);//element is added before noMoreEntriesFlag
            }

            if (!queue.isEmpty()) {
                current = queue.poll();//if not use noMoreEntriesFlag in one branch tree while loop will be terminated after
                //root child is polled of the queue, so root child's children won't be iterated.

                if(current.equals(noMoreEntriesFlag)) break;

                if (current.elementName.equals(o.toString())) {
                    Entry<String> parent = current.parent;
                    if (current.equals(parent.leftChild)) {
                        parent.leftChild = null;
                        return true;
                    }
                    if (current.equals(parent.rightChild)) {
                        parent.rightChild = null;
                        return true;
                    }
                }
            }

        }while (!queue.isEmpty());


        return false;
    }

    public String getParent(String s) {
        Entry<String> current = root;

        LinkedList<Entry> queue=new LinkedList<> ();

        Entry<String> noMoreEntriesFlag = new Entry<>("noMoreEntriesFlag");//this flag is necessary for the
        //situation when tree consists only of one (right or left) branch
        //flag is added to the end of the queue, other elements are positioned before it
        queue.add(noMoreEntriesFlag);

        String parentName = null;

        do {

            if (current.leftChild != null) {
                queue.add(queue.size() - 1, current.leftChild);//element is added before noMoreEntriesFlag
            }

            if (current.rightChild != null) {
                queue.add(queue.size() - 1, current.rightChild);//element is added before noMoreEntriesFlag
            }

            if (!queue.isEmpty()) {
                current = queue.poll();//if not use noMoreEntriesFlag in one branch tree while loop will be terminated after
                //root child is polled of the queue, so root child's children won't be iterated.

                if(current.equals(noMoreEntriesFlag)) break;

                if (current.elementName.equals(s)) {
                    parentName = current.parent.elementName;
                }
            }

        }while (!queue.isEmpty());


        return parentName;
    }

    @Override
    public int size() {

        Entry<String> current = root;
        int counter = 0;

        LinkedList<Entry> queue=new LinkedList<> ();

        Entry<String> noMoreEntriesFlag = new Entry<>("noMoreEntriesFlag");//this flag is necessary for the
        //situation when tree consists only of one (right or left) branch
        //flag is added to the end of the queue, other elements are positioned before it
        queue.add(noMoreEntriesFlag);

        do {

            if (current.leftChild != null) {

                queue.add(queue.size() - 1, current.leftChild);//element is added before noMoreEntriesFlag
            }

            if (current.rightChild != null) {
                queue.add(queue.size() - 1, current.rightChild);//element is added before noMoreEntriesFlag
            }

                if (!queue.isEmpty()) {
                    current = queue.poll();//if not use noMoreEntriesFlag in one branch tree while loop will be terminated after
                    //root child is polled of the queue, so root child's children won't be iterated.
                    if(current.equals(noMoreEntriesFlag)) break;
                    counter++;
                }

        }while (!queue.isEmpty());

        return counter;
    }



    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
}

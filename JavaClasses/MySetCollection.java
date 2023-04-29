package com.javarush.task.task37.task3707;

import java.io.*;
import java.util.*;

public class MySetCollection<E> extends AbstractSet<E> implements Serializable, Cloneable, Set<E> {
    private static final Object PRESENT=new Object();
    private transient HashMap<E, Object> map;



    public MySetCollection(){
        map=new HashMap<>();
    }
    public MySetCollection(Collection<? extends E> collection){
        map=new HashMap<>((int)Math.max((collection.size()/.75f)+1, 16));
        collection.forEach(s-> map.put(s, PRESENT));
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> resIterator=map.keySet().iterator();
        return resIterator;
    }
    @Override
    public boolean add(E e){
        if (map.containsKey(e)) return false;
        map.put(e, PRESENT);
        return true;
    }
    public int size(){
        return map.size();
    }
    public boolean isEmpty(){
        return map.isEmpty();
    }
    public void clear(){
        map.clear();
    }
    public boolean remove(Object o){
        return super.remove(o);
    }

    public Object clone(){
        try {
            MySetCollection<E> newSet = (MySetCollection<E>) super.clone();
            newSet.map = (HashMap<E, Object>) map.clone();
            return newSet;
        } catch (Exception e) {
            throw new InternalError();
        }
    }
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        try {
            stream.writeInt(HashMapReflectionHelper.<Integer>callHiddenMethod(map, "capacity"));
            stream.writeFloat(HashMapReflectionHelper.<Float>callHiddenMethod(map, "loadFactor"));
            stream.writeInt(map.size());
            for (E e : map.keySet()) stream.writeObject(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();

        int capacity = s.readInt();
        float loadFactor = s.readFloat();
        map = new HashMap<>(capacity, loadFactor);

        int size = s.readInt();

        for (int i = 0; i < size; i++) {
            E e = (E) s.readObject();
            map.put(e, PRESENT);
        }
    }


}

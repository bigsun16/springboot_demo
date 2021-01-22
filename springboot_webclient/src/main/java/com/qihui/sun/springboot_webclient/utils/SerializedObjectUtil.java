package com.qihui.sun.springboot_webclient.utils;

import java.io.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SerializedObjectUtil {
    private SerializedObjectUtil(){}
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public static void serializedObject(Object obj,String fileName){
        writeLock.lock();
        try (ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(fileName))){
            ous.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public static Object deSerializedObject(String fileName){
        readLock.lock();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return null;
    }
}

package io.game;

import java.util.*;

public class InstructionRegister {
    private Queue<Process> processQueue;
    private int maxSize;

    public InstructionRegister(int maxSize) {
        this.maxSize = maxSize;
        this.processQueue = new LinkedList<>();
    }

    public boolean addProcess(Process p) {
        if (processQueue.size() < maxSize) {
            processQueue.add(p);
            return true;
        }
        return false;
    }

    public Process peek() {
        return processQueue.peek();
    }

    public Process remove() {
        return processQueue.poll();
    }

    public int size() {
        return processQueue.size();
    }

    public boolean isFull() {
        return processQueue.size() >= maxSize;
    }

    public boolean isEmpty() {
        return processQueue.isEmpty();
    }

    public Queue<Process> getQueue() {
        return processQueue;
    }

    public List<Process> getAllProcesses() {
        return new ArrayList<>(processQueue);
    }

    public boolean removeProcess(Process process) {
        return processQueue.remove(process);
    }
}

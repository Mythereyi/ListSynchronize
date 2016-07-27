package com.java.training.examples;

import java.util.ArrayList;
import java.util.List;

class Adder implements Runnable {

	private ListSynchronize adder;

	Adder(ListSynchronize addList) {
		this.adder = addList;
	}

	@Override
	public void run() {

		for (int increment = 0; increment <= 10; increment++) {
			adder.add(increment);
		}
	}

}

class Remover implements Runnable {

	private ListSynchronize removal;

	Remover(ListSynchronize removeList) {
		this.removal = removeList;
	}

	@Override
	public void run() {
		while (true) {
		
			if (Thread.currentThread().isInterrupted())
				break;
			removal.remove();
		}
	}
}

public class ListSynchronize {

	static List<Integer> myList = new ArrayList<>();

	public synchronized void add(int item) {
		myList.add(item);
		notify();
		System.out.println("Added: " + item);

	}

	public synchronized int remove() {
		
		int item = -1;
		try {
			if (myList.size() == 0)
				wait();
			item = myList.remove(0);
			System.out.println("Removed: " + item);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return item;
	}

	public static void main(String[] args) throws InterruptedException {
		ListSynchronize myListSync = new ListSynchronize();
		Thread threadAdder = new Thread(new Adder(myListSync));
		Thread threadremover = new Thread(new Remover(myListSync));

		threadremover.start();
		threadAdder.start();

		try {
			threadAdder.join();
			threadremover.join(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
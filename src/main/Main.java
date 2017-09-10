package main;

import java.util.ArrayList;
import java.util.Arrays;

import model.Controller;

public class Main {

	public static void main(String[] args) {        
		new Controller(new ArrayList<>(Arrays.asList(args)));
	}
}
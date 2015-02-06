/* 
 * NOTICE
 * This software was produced for the office of the Director of National Intelligence (ODNI)
 * Intelligence Advanced Research Projects Activity (IARPA) ICArUS program, 
 * BAA number IARPA-BAA-10-04, under contract 2009-0917826-016, and is subject 
 * to the Rights in Data-General Clause 52.227-14, Alt. IV (DEC 2007).
 * 
 * This software and data is provided strictly to support demonstrations of ICArUS challenge problems
 * and to assist in the development of cognitive-neuroscience architectures. It is not intended to be used
 * in operational systems or environments.
 * 
 * Copyright (C) 2015 The MITRE Corporation. All Rights Reserved.
 * 
 */
package org.mitre.icarus.cps.app.util;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import org.mitre.icarus.cps.app.window.IApplicationWindow.WindowAlignment;

/**
 * @author CBONACETO
 *
 */
public class CPSUtils {	
	
	/**
	 * @param arr
	 * @return
	 */
	public static int[] nextPermutation(int[] arr) {
		int[] a = (int[]) arr.clone();
		int n = a.length-1;
		int j = n - 1;
		// "123, 132, 213, 231, 312, 321"
		while (a[j] > a[j+1]) {
			if (j==0) {
				// last permutation - reset
				int[] error = new int[] { 0 } ;
				return error;
			}
			j--;
		}

		// j is the largest subscript with a[j] < a[j+1]
		int k = n;
		while (a[j] > a[k]) {
			// sanity checking here
			k--;
		}
		// a[k] is the smallest term greater than a[j] to the
		// right of a[j] -- swap a[j] and a[k]
		int tmp = a[j]; a[j] = a[k]; a[k] = tmp;
		int r = n;
		int s = j + 1;

		while (r > s) {
			// swap a[r] and a[s]
			tmp = a[r];
			a[r] = a[s];
			a[s] = tmp;
			r--;
			s++;
		}
		
		return a;
	}	

	/**
	 * @param n
	 * @return
	 */
	public static int factorial(int n) {
		int f = 1;
		for (int i=1; i <= n; i++) {
			f *= i;
		}
		return f;		
	}
	
	/** Center a frame on the screen */
	public static void centerFrameOnScreen(Frame frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		Dimension frameSize = frame.getSize();
		frame.setLocation((screenSize.width - frameSize.width)/2, 
				(screenSize.height - frameSize.height)/2);
	}
	
	/**
	 * Aling a frame to the center, left, or right of the screen.
	 * 
	 * @param frame
	 * @param alignment
	 */
	public static void alignFrameOnScreen(Frame frame, WindowAlignment alignment) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		Dimension frameSize = frame.getSize();
		switch(alignment) {
		case CENTER:
			frame.setLocation((screenSize.width - frameSize.width)/2, 
					(screenSize.height - frameSize.height)/2);
			break;
		case LEFT:
			frame.setLocation(10, (screenSize.height - frameSize.height)/2);
			break;
		case RIGHT:
			int x = screenSize.width - frameSize.width - 10;
			frame.setLocation(x < 0 ? 0 : x, (screenSize.height - frameSize.height)/2);
			break;
		default:
			break;
		
		}
		
	}
	
	/** Center a frame over another frame */
	public static void centerFrameOnFrame(Frame frame, Frame centeringFrame) {
		Dimension centeringFrameSize = centeringFrame.getSize();
		Dimension frameSize = frame.getSize();		
		frame.setLocation((centeringFrameSize.width - frameSize.width)/2 + centeringFrame.getLocation().x, 
				(centeringFrameSize.height - centeringFrameSize.height)/2 + centeringFrame.getLocation().y);
	}	
}
package Moka7Demo;

import javax.swing.JTextArea;

public interface PLCListener {			
	abstract public void PLCBitChanged(int address, int pos, boolean val, String plcName);
	abstract public void PLCBitChanged(int address, int pos, boolean val, String plcName, JTextArea monitor);
	
}

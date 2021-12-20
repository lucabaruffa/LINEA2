package Moka7Demo;

import javax.swing.JTextArea;

public class PLCListenerImplementation implements PLCListener {
	
		
    @Override
    public void PLCBitChanged(int address, int pos, boolean val, String plcName) {
        switch (address) {
        case 0:
            switch (pos) {
            case 1:
                System.out.println("Bit at address 0.1 of PLC " + plcName + " changed to: " + val);
            }
        }
    }
    
    
    
    @Override
    public void PLCBitChanged(int address, int pos, boolean val, String plcName, JTextArea monit) {
        switch (address) {
        case 0:
            switch (pos) {
            case 1:
                monit.append("Bit at address 0.1 of PLC " + plcName + " changed to: " + val);
            }
        }
    }
    
}
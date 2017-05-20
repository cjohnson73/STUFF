import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener
{
	private boolean keys[];
	private boolean keysR[];
	public KeyManager()
	{
		keys = new boolean[256];
		keysR = new boolean[256];
	}
	public boolean keyPressed(int a)
	{ 
		if(keys[KeyEvent.VK_UP] && a==0)
			return true;
		if(keys[KeyEvent.VK_DOWN] && a==1)
			return  true;
		if(keys[KeyEvent.VK_RIGHT] && a==2)
			return  true;
		if(keys[KeyEvent.VK_LEFT] && a==3) 
			return  true;
		if((keys[KeyEvent.VK_SPACE]) && a==4)
			return  true;
		if(keys[KeyEvent.VK_G] && a==5)
			return  true;
		if(keys[KeyEvent.VK_M] && a==6)
			return  true;
		if(keys[KeyEvent.VK_ENTER] && a==7)
			return true;
		if(keys[KeyEvent.VK_ESCAPE] && a==8)
			return true;
		return false;
	}
	public boolean keyR(int a)
	{ 
		if(keysR[KeyEvent.VK_UP] && a==0)
			return true;
		if(keysR[KeyEvent.VK_DOWN] && a==1)
			return  true;
		if(keysR[KeyEvent.VK_RIGHT] && a==2)
			return  true;
		if(keysR[KeyEvent.VK_LEFT] && a==3) 
			return  true;
		if((keysR[KeyEvent.VK_SPACE]) && a==4)
			return  true;
		if(keysR[KeyEvent.VK_G] && a==5)
			return  true;
		if(keysR[KeyEvent.VK_M] && a==6)
			return  true;
		if(keysR[KeyEvent.VK_ENTER] && a==7)
			return true;
		if(keysR[KeyEvent.VK_ESCAPE] && a==8)
			return true;
		return false;
	}
	@Override
	public void keyPressed(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;
		keysR[e.getKeyCode()] = false;
	}
	@Override
	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
		keysR[e.getKeyCode()] = true;
	}
	@Override
	public void keyTyped(KeyEvent e)
	{
	}
}

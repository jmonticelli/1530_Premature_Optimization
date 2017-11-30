package sweets;
import java.awt.Color;
import java.io.Serializable;

public class Player implements Serializable
{
	private Color playerColor;
	private String name;
	private int position;
	private int boomerangCount;
	
	public Player() {
		playerColor = null;
		name = "";
		position = 0;
	}
	
	public Player(Color c, String n, int p, int bCount) {
		playerColor = c;
		name = n;
		position = p;
		boomerangCount = bCount;
	}
	
	public void setName(String n)
	{
		name = n;
	}

	public String getName()
	{
		return name;
	}
	
	public void setColor(Color colr)
	{
		playerColor = colr;
	}

	public Color getColor()
	{
		return playerColor;
	}
	
	public int getPos() {
		return position;
	}
	
	public void setPos(int pos) {
		position = pos;
	}	
	
	// Return 1 if a boomerang is thrown; if no boomerangs are left, return 0
	public int throwBoomerang() {
		if (boomerangCount > 0) {
			boomerangCount--;
			return 1;
		}
		
		return 0;
	}
	
	public int getBoomerangCount() {
		return boomerangCount;
	}
}

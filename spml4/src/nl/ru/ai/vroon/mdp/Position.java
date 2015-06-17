package nl.ru.ai.vroon.mdp;

public class Position {
	private int x;
	private int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	

	public int getY() {
		return y;
	}
	
	@Override
	public int hashCode() {
		return x * 31 + 5381 * y; 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Position)) {
			return false;
		}
		
		Position b = (Position) obj;
		return b.getX() == this.getX() && b.getY() == this.getY();
	}
	
	
	

}

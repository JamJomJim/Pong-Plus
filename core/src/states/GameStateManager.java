package states;

import java.util.Stack;

public class GameStateManager {

	private Stack<State> states;
	
	public GameStateManager() {
		states = new Stack<State>();
	}

	public void push(State state) {
		states.push(state);
	}
	
	public State pop() {
		return states.pop();
	}
	
	public State peek() {
		return states.peek();
	}
	
	public void set(State state) {
		states.pop();
		states.push(state);
	}
	
	public void render() {
		states.peek().render();
	}
	
	public void dispose() {
		while (!states.isEmpty()) {
			states.pop().dispose();
		}
	}
}

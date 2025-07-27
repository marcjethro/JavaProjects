import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class MazeSolve {
	public static void main(String[] args) {
		int[][] maze_grid = MazeGen.generateMazeGrid(10, 10);
		solveMaze(maze_grid);
	}

	public static void solveMaze(int[][] maze_grid) {
		int[][] solved_maze = new int[maze_grid.length][maze_grid[0].length];
		Stack<MazeGen.coord> stack = new Stack<>();
		stack.push(new MazeGen.coord(0, 0));
		while (!stack.empty()) {
			List<MazeGen.coord> possible = new ArrayList<>();
			MazeGen.coord cur = stack.peek();

			if (cur.x == maze_grid.length - 1 && cur.y == maze_grid[0].length - 1) break;

			solved_maze[cur.x][cur.y] = 2;

			if (cur.x - 1 >= 0 && solved_maze[cur.x - 1][cur.y] != 2 && maze_grid[cur.x - 1][cur.y] != 0) {
				possible.add(new MazeGen.coord(cur.x - 1, cur.y));
			}

			if (cur.x + 1 < maze_grid.length && solved_maze[cur.x + 1][cur.y] != 2 && maze_grid[cur.x + 1][cur.y] != 0) {
				possible.add(new MazeGen.coord(cur.x + 1, cur.y));
			}

			if (cur.y - 1 >= 0 && solved_maze[cur.x][cur.y - 1] != 2 && maze_grid[cur.x][cur.y - 1] != 0) {
				possible.add(new MazeGen.coord(cur.x, cur.y - 1));
			}

			if (cur.y + 1 < maze_grid[0].length && solved_maze[cur.x][cur.y + 1] != 2 && maze_grid[cur.x][cur.y + 1] != 0) {
				possible.add(new MazeGen.coord(cur.x, cur.y + 1));
			}

			if (possible.size() == 0) {
				stack.pop();
			} else {
				stack.push(possible.get(0));
			}
		}

		while (!stack.empty()) {
			MazeGen.coord cur = stack.pop();
			maze_grid[cur.x][cur.y] = 2;
		}

	}
}

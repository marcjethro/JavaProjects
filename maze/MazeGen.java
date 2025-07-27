import java.util.Stack;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeGen {
	public static int[][] generateMazeGrid(int X, int Y) {
		if (X % 2 == 0) {
			X += 1;
		}

		if (Y % 2 == 0) {
			Y += 1;
		}

		int[][] maze_grid = new int[X][Y];

		Stack<coord> coords = new Stack<>();
		coords.push(new coord(0, 0));

		while (!coords.empty()) {
			List<coord> possible = new ArrayList<>();
			coord cur = coords.peek();

			maze_grid[cur.x()][cur.y()] = 1;

			if (cur.x() - 2 >= 0 && maze_grid[cur.x() - 2][cur.y()] != 1) {
				possible.add(new coord(cur.x() - 2, cur.y()));
			}

			if (cur.x() + 2 < X && maze_grid[cur.x() + 2][cur.y()] != 1) {
				possible.add(new coord(cur.x() + 2, cur.y()));
			}

			if (cur.y() - 2 >= 0 && maze_grid[cur.x()][cur.y() - 2] != 1) {
				possible.add(new coord(cur.x(), cur.y() - 2));
			}

			if (cur.y() + 2 < Y && maze_grid[cur.x()][cur.y() + 2] != 1) {
				possible.add(new coord(cur.x(), cur.y() + 2));
			}

			if (possible.size() == 0) {
				coords.pop();
			} else {
				Random random = new Random();
				int randomIndex = random.nextInt(possible.size());
				coord randomCoord = possible.get(randomIndex);
				coord between = cur.getBetween(randomCoord);
				maze_grid[between.x()][between.y()] = 1;
				coords.push(randomCoord);
			}
		}
		return maze_grid;
	}


	public static record coord (int x, int y) {
		coord getBetween(coord next) {
			int xDif = next.x() - this.x();
			int yDif = next.y() - this.y();
			if (xDif == 0) {
				return new coord(this.x, this.y + yDif/2);
			} else {
				return new coord(this.x + xDif/2, this.y);
			}
		}
	}
}

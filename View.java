public class View {
	
	private char[][] content;
	private int height;
	private int width;
	
	public static final int FILL_BELOW  = 1;
	public static final int SPACE_TRANS = 2;
	
	private static char space = '　';
	
	public View() {
		height = width = 0;
		content = new char[1][1];
	}
	
	public View(String str) {
		this(str.split("\n"));
	}
	
	public View(String[] strs) {
		height = strs.length;
		for (String line : strs)
			width = Math.max(width, line.length());
		content = new char[height][width];
		for (int h = 0; h < height; h++) {
			int w = 0;
			int len = strs[w].length();
			for (; w < len; w++)
				content[h][w] = strs[h].charAt(w);
			for (; w < width; w++)
				content[h][w] = space;
		}
	}
	
	public int height() {
		return height;
	}
	
	public int width() {
		return width;
	}
	
	public View fill(int h, int w, String str) {
		return fill(h, w, str, 0);
	}
	
	public View fill(int h, int w, String str, int flag) {
		return fill(h, w, new View(str), flag);
	}
	
	public View fill(int h, int w, String[] strs) {
		return fill(h, w, strs, 0);
	}
	
	public View fill(int h, int w, String[] strs, int flag) {
		return fill(h, w, new View(strs), flag);
	}
	
	public View fill(int h, int w, View view) {
		return fill(h, w, view, 0);
	}
	
	public View fill(int h, int w, View view, int flag) {
		int oldH, oldW, newH, newW;
		if (h >= 0 && w >= 0) {
			// h >= 0 and w >= 0
			oldH = 0; oldW = 0; newH = h; newW = w;
			height = Math.max(height, h + view.height);
			width = Math.max(width, w + view.width);
		} else if (h >= 0) {
			// h >= 0 and w < 0
			oldH = 0; oldW = -w; newH = h; newW = 0;
			height = Math.max(height, h + view.height);
			width = Math.max(-w + width, view.width);
		} else if (w >= 0) {
			// h < 0 and w >= 0
			oldH = -h; oldW = 0; newH = 0; newW = w;
			height = Math.max(-h + height, view.height);
			width = Math.max(width, w + view.width);
		} else {
			// h < 0 and w < 0
			oldH = -h; oldW = -w; newH = 0; newW = 0;
			height = Math.max(-h + height, view.height);
			width = Math.max(-w + width, view.width);
		}
		// backup old content
		char[][] old = new char[content.length][content[0].length];
		for (h = 0; h < content.length; h++)
			for (w = 0; w < content[0].length; w++)
				old[h][w] = content[h][w];
		// create new content
		content = new char[height][width];
		for (h = 0; h < height; h++)
			for (w = 0; w < width; w++)
				content[h][w] = space;
		boolean spaceCover = (flag & SPACE_TRANS) == 0;
		if ((flag & FILL_BELOW) != 0) {
			fill(newH, newW, view.content, spaceCover);
			fill(oldH, oldW, old, spaceCover);
		} else {
			fill(oldH, oldW, old, spaceCover);
			fill(newH, newW, view.content, spaceCover);
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++)
				sb.append(content[h][w]);
			sb.append("\n");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	// fill above with target from (h,w)
	private void fill(int h, int w, char[][] target, boolean spaceCover) {
		for (int hh = 0; hh < target.length; hh++)
			for (int ww = 0; ww < target[0].length; ww++)
				if (spaceCover || target[hh][ww] != space)
					content[h + hh][w + ww] = target[hh][ww];
	}
}


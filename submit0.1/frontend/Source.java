package frontend;

import java.io.*;
import java.util.ArrayList;

public class Source {
    private ArrayList<String> lines;
    private int lineIdx;
    private int colIdx;

    public Source(InputStream inputStream) {
        lines = new ArrayList<>();
        lineIdx = colIdx = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String curLine = null;
            while ((curLine = reader.readLine()) != null) {
                lines.add(curLine);
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public int getColIdx() { return colIdx; }

    public int getLineIdx() { return lineIdx; }

    public String getLine() { return isEndOfFile()? "" : lines.get(lineIdx); }

    public String getRestLine() { return getLine().substring(colIdx); }

    public boolean isEndOfFile() { return lineIdx >= lines.size(); }

    public boolean isEndOfLine() { return colIdx >= getLine().length(); }

    public boolean isWhiteSpace() { return isEndOfLine() || Character.isWhitespace(getLine().charAt(colIdx)); }

    public String getPostStr(int stride) {
        if (isEndOfFile()) return "";
        if (colIdx + stride >= getLine().length()) return getRestLine();
        else return getLine().substring(colIdx, colIdx + stride);
    }

    public void step(int stride) {
        int curStride = 0;
        while (curStride < stride && !isEndOfFile()) {
            if (getLine().length() - colIdx < stride - curStride) {
                curStride += (getLine().length() - colIdx + 1);
                this.stepLine();
            } else {
                colIdx += (stride - curStride);
                curStride = stride;
            }
        }
    }

    public void stepLine() {
        if (!isEndOfFile()) {
            lineIdx += 1;
            colIdx = 0;
        }
    }
}

package frontend.tokenize;

import driver.Config;
import frontend.Source;
import frontend.exceptions.TokenTypeNotFoundException;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private Source source;
    private ArrayList<Token> tokens;

    public Tokenizer(Source source) {
        this.source = source;
    }

    public ArrayList<Token> tokenize() throws TokenTypeNotFoundException{
        tokens = new ArrayList<>();
        while (!source.isEndOfFile()) {
            this.skip();
            if (source.isEndOfFile()) {
                break;
            }
            Token matchedToken = null;
            for (Token.TokenType tokenType : Token.TokenType.values()) {
                Pattern pattern = tokenType.getPattern();
                Matcher matcher = pattern.matcher(source.getRestLine());
                if (matcher.find()) {
                    matchedToken = Token.getInstance(
                            source.getLineIdx(), source.getColIdx(),
                            source.getColIdx() + matcher.end(), matcher.group(0), tokenType);
                    tokens.add(matchedToken);
                    source.step(matcher.group(0).length());
                    break;
                }
            }
            if (!source.isEndOfFile() && matchedToken == null) {
                throw new TokenTypeNotFoundException(
                        source.getLineIdx(), source.getColIdx(), source.getRestLine());
            }
            System.out.println(matchedToken.toString());
        }
        return tokens;
    }

    public void skip() {
        while (!source.isEndOfFile()) {
            while (!source.isEndOfFile() && source.isWhiteSpace()) {
                source.step(1);
            }
            if (source.getPostStr(2).equals("//")) {
                source.stepLine();
                continue;
            }
            if (source.getPostStr(2).equals("/*")) {
                source.step(2);
                while (!source.isEndOfFile() && !source.getPostStr(2).equals("*/")) {
                    source.step(1);
                }
                if (source.getPostStr(2).equals("*/")) {
                    source.step(2);
                    continue;
                }
            }
            break;
        }
    }

    public void output(PrintStream printStream) {
        for (Token u : tokens) {
            u.output(printStream);
        }
    }
}

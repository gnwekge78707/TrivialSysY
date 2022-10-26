package frontend.tokenize;

import driver.Config;
import driver.Output;
import frontend.Source;
import frontend.exceptions.TokenTypeNotFoundException;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private Source source;
    private ArrayList<Token> tokens;

    private int tokenIdx;
    private int preTokenIdx;
    private LinkedList<Token> tokenStack;

    public Tokenizer(Source source) {
        this.source = source;
        this.tokenIdx = 0;
        this.preTokenIdx = 0;
        this.tokenStack = new LinkedList<>();
    }

    public Token consumeToken() {
        if (tokenIdx == tokens.size()) {
            return null;
        } else {
            tokenStack.add(tokens.get(tokenIdx));
            return tokens.get(tokenIdx++);
        }
    }

    public Token reverseToken() {
        return tokens.get(tokenIdx);
    }

    public void step() {
        preTokenIdx = tokenIdx;
        tokenStack.forEach(u -> {
            Output.getInstance().updateBuffer(Config.OutputLevel.SYNTAX, u.getOutputString());
        });
        tokenStack.clear();
    }

    public void reverse() {
        tokenIdx = preTokenIdx;
        tokenStack.clear();
    }

    public Token getPrevToken() {
        for (int i = tokenIdx - 1; i >= 0; i--) {
            if (tokens.get(i) != null) {
                return tokens.get(i);
            }
        }
        return null;
    }

    // -------------- support for checkpoints -----------------
    LinkedList<Integer> checkpointStack = new LinkedList<>();
    LinkedList<LinkedList<String>> outputBufferCheckpoints = new LinkedList<>();

    public void putCheckpoint() {
        // putting a checkpoint at current token
        // you must step before putting checkpoint
        assert tokenIdx == preTokenIdx && tokenStack.isEmpty();
        checkpointStack.add(tokenIdx);
        outputBufferCheckpoints.add(Output.getInstance().getBuffer(Config.OutputLevel.SYNTAX));
    }

    public void restoreCheckpoint() {
        assert !checkpointStack.isEmpty();
        tokenIdx = checkpointStack.pollLast();
        preTokenIdx = tokenIdx;
        tokenStack.clear();
        Output.getInstance().replaceBuffer(Config.OutputLevel.SYNTAX, outputBufferCheckpoints.pollLast());
    }
    // -------------- support for checkpoints -----------------


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
            // System.out.println(matchedToken.toString());
        }
        for (Token u : tokens) {
            Output.getInstance().updateBuffer(Config.OutputLevel.TOKENIZE, u.getOutputString());
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

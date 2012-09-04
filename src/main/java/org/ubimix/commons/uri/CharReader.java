package org.ubimix.commons.uri;

public class CharReader {

    private static char NULL = '\0';

    private char fChar = NULL;

    private int fPos;

    private CharSequence fSequence;

    public CharReader(CharSequence sequence) {
        fSequence = sequence;
    }

    public char getChar() {
        if (fChar == NULL) {
            fChar = fSequence.charAt(fPos);
        }
        return fChar;
    }

    private int getHexCode(char ch) {
        if (Character.isDigit(ch)) {
            return ch - '0';
        }
        if (ch >= 'a' && ch <= 'f') {
            return 10 + (ch - 'a');
        }
        if (ch >= 'A' && ch <= 'F') {
            return 10 + (ch - 'A');
        }
        return -1;
    }

    public int getPos() {
        return fPos;
    }

    public boolean incPos() {
        fChar = NULL;
        if (fPos >= fSequence.length()) {
            return false;
        }
        fPos++;
        return true;
    }

    // http://tools.ietf.org/html/rfc3986#appendix-A
    // unreserved / pct-encoded / sub-delims / ":" / "@"
    protected char readPchar() {
        char ch = readUnreserved();
        if (ch != NULL) {
            return ch;
        }
        ch = readPctEncoded();
        if (ch != NULL) {
            return ch;
        }
        ch = readSubDelims();
        if (ch != NULL) {
            return ch;
        }
        ch = getChar();
        if (ch == ':' || ch == '@') {
            incPos();
            return ch;
        }
        return NULL;
    }

    public char readPctEncoded() {
        char ch = getChar();
        if (ch != '%') {
            return NULL;
        }
        int a = 0;
        int b = 0;
        int pos = getPos();
        boolean restore = true;
        if (incPos()) {
            ch = getChar();
            a = getHexCode(ch);
            if (a >= 0) {
                if (incPos()) {
                    ch = getChar();
                    b = getHexCode(ch);
                    if (b >= 0) {
                        restore = false;
                    }
                }
            }
        }
        if (restore) {
            setPos(pos);
            ch = NULL;
        } else {
            int code = ((a << 8) | (b << 0)) & 0xFF;
            ch = (char) code;
        }
        return ch;
    }

    protected char readSubDelims() {
        char ch = getChar();
        switch (ch) {
            case '!':
            case '$':
            case '&':
            case '\'':
            case '(':
            case ')':
            case '*':
            case '+':
            case ',':
            case ';':
            case '=':
                incPos();
                break;
            default:
                ch = NULL;
                break;
        }
        return ch;
    }

    // http://tools.ietf.org/html/rfc3986#appendix-A
    // ALPHA / DIGIT / "-" / "." / "_" / "~"
    public char readUnreserved() {
        char ch = getChar();
        boolean result = Character.isLetter(ch)
            || Character.isDigit(ch)
            || ch == '-'
            || ch == '.'
            || ch == '_'
            || ch == '~';
        if (result) {
            incPos();
            return ch;
        } else {
            return NULL;
        }
    }

    public void setPos(int pos) {
        fChar = NULL;
        fPos = pos;
    }

}
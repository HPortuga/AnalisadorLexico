public class Scanner {
   private String input;
   private int pos;

   public Scanner(String input) {
      this.input = input;
      this.pos = 0;
   }

   public String getInput() {
      return input;
   }

   public void setInput(String input) {
      this.input = input;
   }

   public int getPos() {
      return pos;
   }

   public void setPos(int pos) {
      this.pos = pos;
   }

   public Token nextToken() {
      Token token;
      int state;
      char chr;
      String lexema;

      if (pos >= input.length())
         return new Token(Names.EOF);

      state = 0;
      chr = input.charAt(pos);
      while(true) {
         if (getPos() > getInput().length())
            return new Token(Names.EOF);

         switch (state) {
            case 0:
               if (getPos() >= getInput().length())
                  return new Token(Names.EOF);

               else if (Character.isLetter(chr) || isUnderscore(chr))
                  state = 1;
               else if (chr == ' ' || chr == '\n')
                  state = 2;
               else if (Character.isDigit(chr))
                  state = 3;
               else if (chr == '<' || chr == '>' || chr == '!')
                  state = 4;
               else if (chr == '=')
                  state = 5;
               else if (chr == '+' || chr == '-')
                  state = 6;
               else if (chr == '*' || chr == '/' || chr == '%')
                  state = 7;
               else if (isSeparator(chr))
                  state = 8;
               else if (chr == '"')
                  state = 9;
               else
                  lexicalError();

               incrementPos();
               break;

            case 1:  // ID
               lexema = "";
               lexema += String.valueOf(chr);

               while (getPos() < getInput().length()) {
                  chr = input.charAt(getPos());

                  if (Character.isLetterOrDigit(chr) || isUnderscore(chr)) {
                     incrementPos();
                     lexema += String.valueOf(chr);
                  } else break;
               }

               state = 0;
               return new Token(Names.ID, lexema);

            case 2:  // Espaço em branco
               while (getPos() < getInput().length()) {
                  chr = input.charAt(getPos());
                  if (chr == ' ' || chr == '\n')
                     incrementPos();
                  else
                     break;
               }

               state = 0;
               break;

            case 3:  // Int
               lexema = "";
               lexema += String.valueOf(chr);

               while (getPos() < getInput().length()) {
                  chr = input.charAt(getPos());

                  if (Character.isDigit(chr)) {
                     lexema += String.valueOf(chr);
                     incrementPos();
                  } else break;
               }

               state = 0;
               return new Token(Names.INTEGER_LITERAL, lexema);

            case 4:  // RELOP
               lexema = "";
               lexema += String.valueOf(chr);

               if (getPos() < getInput().length()) {
                  char nextChar = input.charAt(getPos());
                  if (chr == '!' && nextChar != '=')
                     lexicalError();

                  chr = input.charAt(getPos());
                  if (chr == '=') {
                     lexema += String.valueOf(chr);
                     state = 0;
                     incrementPos();
                     return new Token(Names.RELOP, lexema);
                  }
               }

               state = 0;
               return new Token(Names.RELOP, lexema);

            case 5:  // ATTR
               lexema = "";
               lexema += String.valueOf(chr);

               if (getPos() < getInput().length()) {
                  chr = input.charAt(getPos());
                  if (chr == '=') {
                     state = 4;
                     break;
                  }
               }

               state = 0;
               return new Token(Names.ATTR, lexema);

            case 6:  // OPNUM
               lexema = "";
               lexema += String.valueOf(chr);

               state = 0;
               return new Token(Names.OPNUM, lexema);

            case 7:  // OPUN
               lexema = "";
               lexema += String.valueOf(chr);

               if (getPos() < getInput().length()) {
                  chr = input.charAt(getPos());
                  if (chr == '/') {
                     state = 10;
                     break;
                  }
                  else if (chr == '*') {
                     state = 11;
                     break;
                  }
               }

               state = 0;
               return new Token(Names.OPUN, lexema);

            case 8:  // Separador
               lexema = "";
               lexema += String.valueOf(chr);

               state = 0;
               if (chr == '(')
                  return new Token(Names.LPAREN, lexema);
               else if (chr == ')')
                  return new Token(Names.RPAREN, lexema);
               else if (chr == '[')
                  return new Token(Names.LSBR, lexema);
               else if (chr == ']')
                  return new Token(Names.RSBR, lexema);
               else if (chr == '{')
                  return new Token(Names.LCBR, lexema);
               else if (chr == '}')
                  return new Token(Names.RCBR, lexema);
               else if (chr == ',')
                  return new Token(Names.COMMA, lexema);
               else if (chr == '.')
                  return new Token(Names.DOT, lexema);
               else
                  return new Token(Names.SEMICOLON, lexema);

            case 9:  // String
               lexema = "";
               lexema += String.valueOf(chr);
               boolean finalizouString = false;

               while (getPos() < getInput().length()) {
                  chr = input.charAt(getPos());
                  lexema += String.valueOf(chr);
                  incrementPos();
                  if (chr == '"') {
                     finalizouString = true;
                     break;
                  }
               }

               state = 0;
               if (finalizouString)
                  return new Token(Names.STRING, lexema);
               else
                  lexicalError();

            case 10:    // Comentário em linha
               while (getPos() < getInput().length()) {
                  chr = input.charAt(getPos());
                  incrementPos();
                  if (chr == '\n') {
                     chr = input.charAt(getPos());
                     break;
                  }
               }

               state = 0;
               break;

            case 11:    // Comentário em bloco
               while (getPos() < getInput().length()) {
                  chr = input.charAt(getPos());
                  incrementPos();
                  if (chr == '*') {
                     chr = input.charAt(getPos());
                     if (chr == '/') {
                        incrementPos();
                        chr = input.charAt(getPos());
                        break;
                     }
                  }
               }

               state = 0;
               break;

            default:
               lexicalError();
         }
      }
   }

   private boolean isSeparator(char chr) {
      return chr == '(' || chr == ')' || chr == '[' || chr == ']'
      || chr == '{' || chr == '}' || chr == ';' || chr == '.' || chr == ',';
   }

   private void incrementPos() { setPos(getPos() + 1); }

   private void lexicalError() {
      System.out.println("Token mal formado");
      System.exit(1);
   }

   private boolean isUnderscore(char chr) {
      return (chr == '_');
   }
}

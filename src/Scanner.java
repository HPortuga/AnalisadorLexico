import javax.naming.Name;

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

      if (pos == input.length())
         return new Token(Names.EOF);

      state = 0;
      chr = input.charAt(pos);
      while(true) {
         if (getPos() > getInput().length())
            return new Token(Names.EOF);

         switch (state) {
            case 0:
               if (getPos() == getInput().length())
                  return new Token(Names.EOF);

               else if (Character.isLetter(chr) || isUnderscore(chr))
                  state = 1;
               else if (chr == ' ')
                  state = 2;
               else if (Character.isDigit(chr))
                  state = 3;
               else if (isSeparator(chr))
                  state = 4;
               else if (isOperadorSimples(chr))
                  state = 5;
               else if (chr == '<' || chr == '>' || chr == '=')
                  state = 6;
               else if (chr == '!')
                  state = 7;
               else
                  lexicalError();
               pos ++;
               break;

            case 1:  // ID
               lexema = "";
               lexema += String.valueOf(chr);

               while (getPos() < getInput().length()) {
                  chr = input.charAt(getPos());

                  if (Character.isLetterOrDigit(chr) || isUnderscore(chr)) {
                     lexema += String.valueOf(chr);
                     incrementPos();
                  } else break;
               }
               state = 0;
               return new Token(Names.ID, lexema);

            case 2:  // Espaço em branco
               while (getPos() < getInput().length()) {
                  chr = input.charAt(getPos());
                  if (chr == ' ')
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

            case 4:  // Separador
               lexema = "";
               lexema += String.valueOf(chr);

               state = 0;
               return new Token(Names.SEP, lexema);

            case 5:  // Operadores Simples
               lexema = "";
               lexema += String.valueOf(chr);

               state = 0;
               return new Token(Names.OP, lexema);

            case 6:  // Operadores <, >, =, <=, >=, ==
               lexema = "";
               lexema += String.valueOf(chr);

               chr = input.charAt(getPos());
               if (chr == '=') {
                  lexema += String.valueOf(chr);
                  incrementPos();
                  state = 0;
                  return new Token(Names.OP, lexema);
               } else {
                  incrementPos();
                  state = 0;
                  return new Token(Names.OP, lexema);
               }

            case 7:  // !=
               lexema = "";
               lexema += String.valueOf(chr);
               char next = input.charAt(getPos());
               if (next != '=')
                  break;
               else {
                  lexema += String.valueOf(next);
                  state = 0;
                  return new Token(Names.OP, lexema);
               }
            default:
               lexicalError();
         }
      }
   }

   private boolean isSeparator(char chr) {
      return chr == '(' || chr == ')' || chr == '[' || chr == ']'
      || chr == '{' || chr == '}' || chr == ';' || chr == '.' || chr == ',';
   }

   private boolean isOperadorSimples(char chr) {
      return chr == '+' || chr == '-' || chr == '*' || chr == '/' || chr == '%';
   }

   private void incrementPos() {
      setPos(getPos() + 1);
   }

   private void lexicalError() {
      System.out.println("Token mal formado");
      System.exit(1);
   }

   private boolean isUnderscore(char chr) {
      return (chr == 95);
   }
}
